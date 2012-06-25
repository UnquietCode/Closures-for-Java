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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static unquietcode.tools.closures.util.Shortcuts.out;

/**
 * @author  Benjamin Fagin
 * @version Dec 11, 2010
 */
@SuppressWarnings("unchecked")
public class ClosureFeatures_T {
	@Test
	public void inputTypes() {
		final String string = "hello";  // a final variable is accessible inside
		int X = 10;                     // not accessible, but can still be passed in
		final int dummyX = X;           // this will provide alternative access inside

		Closure1<Integer, Integer> adder = new AbstractClosure1<Integer, Integer>(X) {  // <-passing in a variable
			int x = this.<Integer>a1();       // will have to use this somtimes for some compilers
			int y = (Integer) a1();	    // this will work all of the time
			Integer z = a1();           // how it should always work, ideally
			int base = 12;              // of course, this always works

			public Integer run(Integer p1) {
				// access the final variables
				out(string + " " + dummyX);

				// can also get that X from the passed in variable
				out(string + " " + a1());

				// could look up by argument number instead of convenience method
				out(string + " " + arg(1));

				// or use our declared variables
				out(string + " " + x);
				out(string + " " + y);
				out(string + " " + z);

				// and we also get our parameter value
				out(string + " " + p1);

				// then we return an Integer
				return base + p1 + (Integer) a1() + x + y + z;
			}
		};

		// Now we can run it!
		out(adder.run(5));



		// automated testing below //
		assertEquals((Integer) 57, adder.run(5));
	}

	@Test
	public void factoryTest() {
		// Closures can return other closures too, of course.
		// Here we create a closure that acts as a factory of adders.

		Closure1<Closure1<Integer, Integer>, Integer> adderFactory = new AbstractClosure1<Closure1<Integer, Integer>, Integer>() {

			public Closure1<Integer, Integer> run(Integer p1) {
				Closure1<Integer, Integer> generated = new AbstractClosure1<Integer, Integer>(p1) {

					Integer base = a1();

					public Integer run(Integer p1) {
						return base + p1;
					}
				};

				return generated;
			}
		};

		// get a few adders and try them out
		Closure1<Integer, Integer> add10 = adderFactory.run(10);
		Closure1<Integer, Integer> add5 = adderFactory.run(5);
		Closure1<Integer, Integer> sub1 = adderFactory.run(-1);

		int test = 50;
		out(add10.run(test));   // 60
		out(add5.run(test));    // 55
		out(sub1.run(test));    // 49



		// automated testing below //
		assertEquals((Integer) 60, add10.run(test));
		assertEquals((Integer) 55, add5.run(test));
		assertEquals((Integer) 49, sub1.run(test));
	}

	@Test
	public void untypedFactoryTest() {
		// Only the return type needs to be specified, if you're willing to keep track
		// of the necessary number of inputs and their types.

		Closure<Closure> adderFactory = new AbstractClosure<Closure>() {

			public Closure run(Object...args) {
				return new AbstractClosure<Integer>(args[0]) {
					Integer base = a1();    // this is optional, since a1() can be used directly

					public Integer run(Object...args) {
						return base + (Integer) args[0];    // we expect one argument
					}
				};
			}
		};

		// get a few adders and try them out
		Closure add10 = adderFactory.run(10);
		Closure add5 = adderFactory.run(5);
		Closure sub1 = adderFactory.run(-1);

		// you are responsible for passing in the correct number and types of arguments
		int test = 50;
		out(add10.run(test));   // 60
		out(add5.run(test));    // 55
		out(sub1.run(test));    // 49



		// automated testing below //
		assertEquals(60, add10.run(test));
		assertEquals(55, add5.run(test));
		assertEquals(49, sub1.run(test));
	}

	@Test
	public void curryTest() {
		// Currying is supported by the Closure classes.
		// As in other places, indexing is 1-based.
		// We'll again use the adderFactory example to demonstrate this feature.

		Closure1<Closure1<Integer, Integer>, Integer> adderFactory = new AbstractClosure1<Closure1<Integer, Integer>, Integer>() {
			int declaredValue1 = 1;     // factory variable #1
			int declaredValue2 = 10;    // factory variable #2

			public Closure1<Integer, Integer> run(Integer p1) {
				return new AbstractClosure1<Integer, Integer>(p1) {
					Integer declaredValue3 = a1();  // adder variable #1

					// these variables aren't automatically segregated, so we have to
					// defensively copy
					int myDeclaredValue1 = declaredValue1;
					int myDeclaredValue2 = declaredValue2;
					
					public Integer run(Integer p1) {
						// Note that if we had used a1() down here, the value would not have been curried!
						return myDeclaredValue1 + myDeclaredValue2 + declaredValue3 + p1;
					}
				};
			}
		};

		// normal
		Closure1 tarragon = adderFactory.run(100);  // will add 1 + 10 + 100 + x
		out(tarragon.run(5));                       // which gives us 116
		assertEquals(116, tarragon.run(5));

		// The returned adder has 1 field, declaredValue3, that we can modify.
		tarragon.curry(1, 110);                     // change to add 1 + 10 + 110 + x instead
		out(tarragon.run(5));                       // now we get 126!
		assertEquals(126, tarragon.run(5));

		// we can change the factory too, though
		adderFactory.curry(1, 15);
		adderFactory.curry(2, 20);
		Closure1 cardamom = adderFactory.run(100);  // will add 15 + 20 + 100 + x
		out(cardamom.run(5));                       // which gives us 140
		assertEquals(140, cardamom.run(5));

		// (tarragon will still work if you copy factory data rather than share)
		assertEquals(126, tarragon.run(5));
	}

	@Test
	public void simpleCurryExample() {
		Closure1<String, String> c1 = new AbstractClosure1<String, String>() {
			String greeting = "Hello";
			char ending = '.';

			public String run(String p1) {
				return greeting + " " + p1 + ending;
			}
		};

		// normal
		String name = "world";
		out(c1.run(name));      // Hello world.
		assertEquals("Hello world.", c1.run(name));

		// curried
		c1.curry(1, "Goodbye");
		c1.curry(2, '!');
		out(c1.run(name));      // Goodbye world!
		assertEquals("Goodbye world!", c1.run(name));
	}

	@Test
	public void methodCallingTest() {
		// Call a method from within the closure.

		String string = "The way I used to be";
		Closure1<String, String> closure = new AbstractClosure1<String,String>(string) {

			public String run(String a1) {
				return modify(a1) + ".";    // If it's visible to the closure, it can be run.
			}
		};

		out(string);
		out("\nis not the same as\n");
		out(closure.run(string));

		assertEquals("the way I am.", closure.run(string));
	}

	  // (used by methodCallingTest)
	  private String modify(String input) {
	  	  return input.substring(0, 10).replace("T", "t") + "am";
	  }


	@Test
	@SuppressWarnings("unchecked")
	public void changingTest() {
		// Keep in mind that unless you pass in a copy, your object can be mutated.

		Map<Integer, Character> map = new HashMap<Integer, Character>();
		map.put(0, 'A');
		map.put(1, 'B');

		Closure<Integer> encoder = new AbstractClosure<Integer>(map) {

			public Integer run(Object...args) {
				char x = ((Map<Integer, Character>)a1()).get(0);
				char y = ((Map<Integer, Character>)a1()).get(1);

				return (x + y) % (Integer) args[0];
			}
		};

		int AB = encoder.run(5);    // original
		map.put(1, 'C');            // mutate
		int AC = encoder.run(5);    // new value

		out("AB code: " + AB);
		out("AC code: " + AC);
		out();
		
		// However, the initial values can be stored in the intializer section of the closure.

		char array[] = {'D', 'E'};
		Closure<Integer> encoder2 = new AbstractClosure<Integer>(array) {
			char x = ((char[])a1())[0];
			char y = ((char[])a1())[1];

			public Integer run(Object...args) {
				return (x + y) % (Integer) args[0];
			}
		};

		int DE = encoder2.run(5);   // original
		array[1] = 'F';             // mutate
		int DF = encoder2.run(5);   // doesn't affect the closure

		out("DE code: " + DE);
		out("DF code: " + DF + " (not really!)");
	}

	@Test
	public void helperMethods() {
		// Because these are anonymous classes, you can add in whatever methods you want.
		// They will not be accessible outside of the closure (without reflection, of course).

		Closure theDestroyer = new AbstractClosure() {
			Random gen = new Random();

			public Object run(Object...args) {
				String string = (String) args[0];
				StringBuilder sb = new StringBuilder();

				for (int i=0; i < string.length(); ++i) {
					if (Character.isWhitespace(string.charAt(i)))
						sb.append(string.charAt(i));
					else
						sb.append(randomLetter(string));
				}

				return sb.toString();
			}

			// This method is accessible only within the closure.
			private char randomLetter(String string) {
				char c = '.';

				while (!Character.isLetter(c)) {
					c = string.charAt(gen.nextInt(string.length()));
				}

				return c;
			}
		};

		String theString = "The original string.";
		out(theString);
		out("\n\t\t!=\n");
		out(theDestroyer.run(theString));
	}


	//TODO is the argument array useful? is there a case when it is necessary?
	@Test
	public void getArgumentTypes() {
		Integer num = 20;
		Closure closure = new AbstractClosure(1, 'a', num) {

			public Object run(Object...args) {
				return null;
			}
		};

		// Oops! I forgot what classes my parameters were.
//		for (Class c)
	}
}
