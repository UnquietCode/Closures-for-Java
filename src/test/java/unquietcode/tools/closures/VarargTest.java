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

import static unquietcode.tools.closures.util.Shortcuts.out;

/**
 * @author  Benjamin Fagin
 * @version Dec 10, 2010
 */
public class VarargTest {
	@Test
	public void varargTestX() {
		out(tryit());                     // 0
		out(tryit(1));                    // 1
		out(tryit(1, 2));                 // 2
	  //out(tryit(null));                 // NullPointerException
		out(tryit(new Object[] {}));      // 0
		out(tryit(new Object[] {null}));  // 1

		out(catchNull(null));             // 1 (null)
		out(catchNull(null, null));		  // 2

		out(whoGetsIt());                 // empty
		out(whoGetsIt(null));             // varargs
		out(whoGetsIt(new Object[] {}));  // varargs
	}

	private String tryit(Object...args) {
		return args.length + "";
	}

	private String catchNull(Object...args) {
		if (args == null)
			return 1 + " (null)";
		else
			return args.length + "";
	}

	private String whoGetsIt() {
		return "empty";
	}

	private String whoGetsIt(Object...args) {
		return "varargs";
	}


	@Test
	public void arrayPassingTest() {
		String arr[] = {"Alice", "Bob"};

		out(arrayPass(arr));
		out(arrayPass(arr, "Steve"));
		out(arrayPass("Alice", "Bob"));
	}

	@Test
	public void objectArray() {
		String arr[] = {"Alice", "Bob"};
		Object x = arr;

		out(arrayPass(arr));
		out(arrayPass("work"," thing"));
		out(arrayPass(new Object[]{"Potato", "It's not a tuber!"}));
		out(arrayPass(x));
	}


	private String arrayPass(Object...args) {
		return args.length + "";
	}

}
