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
import unquietcode.tools.closures.view.Closure1View;
import unquietcode.tools.closures.view.Closure2View;
import unquietcode.tools.closures.view.Closure3View;
import unquietcode.tools.closures.view.ClosureView;

import static unquietcode.tools.closures.util.Shortcuts.out;


/**
 * @author  Benjamin Fagin
 * @version Dec 11, 2010
 */
@SuppressWarnings("unchecked")
public class MultiClosureCasting_T {
	@Test
	public void multiclosureBasics() {
		// Multiclosures are like an abstract collection of other closures. They can support 0-6 arguments
		// and vararg. Any method not implemented by you will throw an exception.
		// The vararg method functions as a fallback for any unimplemented versions, if it is itself defined.
		// They support views just like the other classes, and can even offer individual views for each
		// of the closure types 0-6 and the unified ClosureView.

		// Note that here the Overrides are optional, as the class does not require explicitly
		// implementing the methods. It's pick and choose.

		MultiClosure<String> helloMaker = new AbstractMultiClosure() {
			String greeting = "Hello";

			public Object run(Object p1) {
				return greeting + " " + p1;
			}

			public @Override
			Object run(Object p1, Object p2) {
				return greeting + " " + p1 + " and " + p2;
			}

			public @Override
			Object run(Object...args) {
				StringBuilder sb = new StringBuilder();
				sb.append(greeting).append(" to");

				int lcv = 0;
				for (Object o : args) {
					String s = (String) o;
					sb.append(" ").append(s);

					if (lcv == args.length-2)
						sb.append(", and");
					else if (lcv < args.length-1)
						sb.append(",");

					++lcv;
				}

				return sb.toString();
			}
		};

		out(helloMaker.run("Bob"));
		out(helloMaker.run("Allan", "Susan"));
		out(helloMaker.run("Jennifer", "Alexander", "Ryan", "Chris"));
		out();


		// We can try to get a more constrained versions of our helloMaker
		Closure1View<String, String> singles = helloMaker.toClosure1();
		out(singles.run("Steven"));

		// Of course, you don't have to type them if you don't want to.
		Closure2View doubles = helloMaker.toClosure2();
		out(doubles.run("Tabitha", "Aragorn"));

		// We didn't define a 3 argument version, but we did define a vararg, so it will work.
		Closure3View<String, String, String, String> fallback = helloMaker.toClosure3();
		out(fallback.run("Arnold", "Julie", "Marissa"));

		// However, if we did not define a vararg version, we would have gotten an exception at runtime!

		// We can curry the MultiClosure too.
		out();
		helloMaker.curry(1, "Goodbye");
		out(helloMaker.run("Bob", "Allan", "Susan", "Jennifer", "Alexander", "Ryan", "Chris"));

		// Remember though that the views are affected.
		out();
		ClosureView closure = helloMaker.toClosure();
		helloMaker.curry(1, "Welcome back");
		out(closure.run("Bob", "Allan", "Susan", "Jennifer", "Alexander", "Ryan", "Chris"));
	}

	@Test
	public void mcTest() {
		// MultiClosures will let you know which of their methods have been implemented.
		// They must be implemented using Object types in the parameters.
		// Use @Override annotations if that helps.

		MultiClosure<String> helloMaker = new AbstractMultiClosure<String>() {

			public @Override
			String run(Object p1) {
				return "hello " + p1;
			}

			public @Override
			String run(Object p1, Object p2) {
				return "hello " + p1 + " and " + p2;
			}

			public String run(String p1) {  // This does NOT override any base methods!
				return "d'oh " + p1;
			}
		};

		// We can figure out what's implemented.
		for (int i = -1; i < 8; ++i) {
			out(i + " : " + helloMaker.isImplemented(i));
		}

		out();
		out(helloMaker.run(1));         // ok
		out(helloMaker.run(1,2));       // fine
		//out(helloMaker.run(1,2,3));   // This will throw an exception (NotImplementedException).
	}

	@Test
	public void combinatorTest() {
		// MultiClosure provides a static method for combining closures into views
	}


}
