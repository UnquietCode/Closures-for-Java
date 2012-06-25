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
import unquietcode.tools.closures.view.Closure0View;
import unquietcode.tools.closures.view.Closure1View;
import unquietcode.tools.closures.view.ClosureView;

import java.util.HashMap;
import java.util.Map;

import static unquietcode.tools.closures.util.Shortcuts.out;
import static unquietcode.tools.closures.util.Shortcuts.outN;

/**
 * @author Benjamin Fagin
 * @version 01-23-2011
 */
public class ClosureViews_T {
	@Test
	public void basicViews() {
		// Views are closures shielded by an interface. They are linked to their parent closure,
		// however they cannot modify it nor retrieve it. Being views, if the original closure
		// is modified, the view will be modified. This goes for currying as well as mutable
		// arguments.

		Closure1<Integer, Integer> magnify = new AbstractClosure1<Integer, Integer>() {
			public Integer run(Integer val) {
				return 10 * val;
			}
		};

		// get a view
		Closure1View<Integer, Integer> view1 = magnify.getView();
		out(view1.run(12));  // 12 * 10 = 120

		// of course, typing is not required
		Closure1View view2 = magnify.getView();
		out(view2.run(15));  // 12 * 10 = 120

		// All closure classes can also return a ClosureView for unification purposes.
		ClosureView view3 = magnify.toClosure();
		out("\nExpected arguments: " + view3.getExpectedArgs());
		out(view3.run(4));              // 4 * 10 = 40
		out(view3.run(4, 8, 12, 20));   // additional arguments are ignored
	}

	@Test
	public void instaView() {
		// If the closure isn't needed, just leverage Java's syntax to ditch it.
		// Remember though, the reference to the original closure is lost. No modifications
		// can be made (except inside). This is also a good way to ensure that the view
		// will never be adversely affected by the parent closure being mutated.
		// This is NOT a defense against mutable arguments!

		Closure1View<Integer, Integer> view1 = (new AbstractClosure1<Integer, Integer>() {
			public Integer run(Integer val) {
				return 10 * val;
			}
		}).getView();

		out(view1.run(12));  // 12 * 10 = 120
	}

	@Test
	public void fibonacciGenerator() {
		// Just to clarify, while currying can be done from within, it is slow
		// and definitely the wrong way to do it.

		Closure0View<Long> fibonacci = (new AbstractClosure0<Long>() {
			long a = 0;
			long b = 1;
			long c;

			public Long run() {
				long temp = a;
				c = a + b;
				a = b;              // The correct way.
				this.curry(2, c);   // Why oh why would you do this?!

				return temp;
			}
		}).getView();

		for (int i=0; i < 20; ++i) {
			outN(fibonacci.run());
			outN(", ");
		}
	}

	@Test
	public void curryViewExample() {
		// Views are tied to specific closures. If the original parent closure is modified, the
		// view will respond accordingly (or perhaps the better term is 'unknowingly').
		// Therefore it is best not to consider a view as immutable.

		// Views will also reflect changes to mutable arguments in the original closure.

		Map<String, Integer> names = new HashMap<String, Integer>();

		Closure0<String> helloBot = new AbstractClosure0<String>(names, 5) {
			String greeting = "Hello";

			public String run() {
				StringBuilder sb = new StringBuilder();
				Map<String, Integer> names = a1();

				for (Map.Entry<String, Integer> entry : names.entrySet()) {
					for (int i=0; i < entry.getValue(); ++i) {
						sb.append(greeting).append(" ").append(entry.getKey()).append(".\n");
					}
					sb.append("\n");
				}

				return sb.toString();
			}
		};

		// using a view
		Closure0View<String> view = helloBot.getView();
		names.put("Alice", 2);
		names.put("Bob", 3);
		out(view.run());

		// curried
		names.clear();
		names.put("Martin", 3);
		names.put("Akheel", 2);
		helloBot.curry(1, "Goodbye");
		out(view.run());    // Same view, different output.
	}


	@Test
	/*  //TODO
		The java compiler warns about theset hings tsinsdf
		Think about it like this: passing null is passing in one Object which is null.
		It therefore goes to argument 1.
	 */
	public void varargArrays() {
		// Keep in mind the way varargs works in Java, becaus this will affect argument passing.
		Integer numbers[] = {1,2,3};

		Closure0 c1 = new AbstractClosure0(numbers) {
			//Integer nums[] = (Integer[]) a1();  // This will fail! All of the numbers have been split up.
			Integer n1 = (Integer) a1();          // This is ok.

			public Object run() {
				return n1;
			}
		};
		out(c1.run());  // outputs 1

		//////////////

		// An array passed with another argument will not combine.
		Integer num = 14;
		Closure0 c2 = new AbstractClosure0(numbers, num) {
			Integer nums[] = (Integer[]) a1();  // This is ok now.
			Integer num = (Integer) a2();

			public Object run() {
				return nums[0] + num;
			}
		};
		out(c2.run());  // outputs 15

		//////////////

		// The null value is stored in argument 1, similar to normal Java varargs.
		Closure0 c3 = new AbstractClosure0(null) {

			public Object run() {
				return a1();
			}
		};
		out(c3.run());  // outputs null

		/////////////

		// normal Java
		out("\n");
		out(regularJava(1));        // 1
		out(regularJava(1, 2));     // 2
		out(regularJava(numbers));  // 3
		out(regularJava(null));     // null
		out(regularJava());		    // 0
	}

	private static String regularJava(Integer...args) {
		return args == null ? "null" : args.length + "";
	}
}
