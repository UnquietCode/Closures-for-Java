/*
 * Copyright 2012 Benjamin Fagin
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *
 *     Read the included LICENSE.TXT for more information.
 */

package unquietcode.tools.closures;

import org.junit.Test;
import unquietcode.tools.closures.view.ClosureView;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static unquietcode.tools.closures.util.Shortcuts.out;

/**
 * @author  Mike Sullivan
 * @version 02-27-2011
 */
public class MSChain_T {

    final Random gen = new Random();    // (shared generator)

    ClosureView<String> closure1 = (new AbstractClosure2<String, Object, Object>() {
        String nums[] = {"1", "2", "3", "4"};
        public @Override
        String run(Object s, Object t) {
            StringBuilder sb = new StringBuilder();
            for(String str : nums)    {
                sb.append(str);
                out("Before: s.getClass(): " + s.getClass() + " instance of check: " + (s instanceof ClosureView) + " - sb: " + sb.toString());
                if(s instanceof ClosureView)    {
                    sb.append(((ClosureView) s).run(t));
                } else if(s instanceof String) {
                    sb.append(s);
                }
                out("After: s.getClass(): " + s.getClass() + " instance of check: " + (s instanceof ClosureView) + " - sb: " + sb.toString());
            }
            return sb.toString();
        }
    }).toClosure();
        
    ClosureView<String> closure2 = (new AbstractClosure1<String, Object>() {
        String nums[] = {"A", "B", "C", "D"};
        public @Override
        String run(Object s) {
            StringBuilder sb = new StringBuilder();
            for(String str : nums)    {
                sb.append(str);
                if(s instanceof String) {
                    sb.append(s);
                }
            }
            return sb.toString();
        }
    }).toClosure();

    ClosureView<String> closure3 = (new AbstractClosure1<String, Object>() {
        String nums[] = {"5", "6", "7", "8"};
        public @Override
        String run(Object s) {
            StringBuilder sb = new StringBuilder();
            for(String str : nums)    {
                sb.append(str);
                if(s instanceof String) {
                    sb.append(s);
                }
            }
            return sb.toString();
        }
    }).toClosure();

    ClosureView<ClosureView<String>> closure4 = (new AbstractClosure1<ClosureView<String>, String>() {
        public @Override
        ClosureView<String> run(String s) {
            StringBuilder sb = new StringBuilder();

            return (new AbstractClosure1<String, String>() {
                        public @Override
                        String run(String s) {
                            StringBuilder sb = new StringBuilder();
                            sb.append('|').append(s).append('|');
                            return sb.toString();
                        }
                    }).toClosure();
        }
    }).toClosure();

	@Test
	public void basic() {
		// Chains allow multiple closures to be linked together into one execution sequence.
		// They are a convenient way to create different strategies from smaller components.


        assertEquals(closure1.run("", ""),"1234"); 
        assertEquals(closure1.run(" ", " "),"1 2 3 4 "); 

        assertEquals(closure2.run(""),"ABCD"); 
        assertEquals(closure2.run(" "),"A B C D "); 
    }
    
    @Test
    public void embed() {
        assertEquals(closure1.run(closure2, " "),"1A B C D 2A B C D 3A B C D 4A B C D ");
	    assertEquals(closure1.run("X", " "),"1X2X3X4X");
    }
    
    @Test
    public void chain() {
        Chain<String> chain1 = new Chain<String>(closure2, closure3);
        assertEquals(chain1.run(" "), "5A B C D 6A B C D 7A B C D 8A B C D ");

        Chain<String> chain2 = new Chain<String>(closure2, closure2);
        assertEquals(chain2.run(" "), "AA B C D BA B C D CA B C D DA B C D ");

        Chain<String> chain3 = new Chain<String>(closure3, closure3);
        assertEquals(chain3.run(" "), "55 6 7 8 65 6 7 8 75 6 7 8 85 6 7 8 ");
    }

    @Test
    public void chain2() {
        Chain<String> chain1 = new Chain<String>(closure1, closure2);
        assertEquals(chain1.run(" ", " "), "A1 2 3 4 B1 2 3 4 C1 2 3 4 D1 2 3 4 ");

    }

    @Test
    public void embed2() {
       assertEquals(closure4.run(" ").run(" "),"| |"); 
        assertEquals(closure4.run(" ").run("A"),"|A|"); 
        assertEquals(closure4.run("A").run(" "),"| |"); 
    }
    

    @Test
    public void chain3() {
        Chain<String> chain1 = new Chain<String>(closure4.run(" "), closure2);
        assertEquals(chain1.run(" "), "A| |B| |C| |D| |");

        Chain<String> chain2 = new Chain<String>(closure4.run("a"), closure2);
        assertEquals(chain1.run(" "), "A| |B| |C| |D| |");
    }

}
