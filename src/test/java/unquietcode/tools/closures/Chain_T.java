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
import unquietcode.tools.closures.util.StringUtils;

import java.util.Random;

import static unquietcode.tools.closures.util.Shortcuts.out;
import static unquietcode.tools.closures.util.Shortcuts.outN;

/**
 * @author  Benjamin Fagin
 * @version 01-25-2011
 */
public class Chain_T {
	@Test
	public void basic() {
		// Chains allow multiple closures to be linked together into one execution sequence.
		// They are a convenient way to create different strategies from smaller components.


		final Random gen = new Random();    // (shared generator)

		ClosureView<String> articleGen = (new AbstractClosure1<String, String>() {
			String articlesAndPronouns[] = {"a", "the", "that", "this"};
			public @Override
			String run(String s) {
				String val = articlesAndPronouns[gen.nextInt(articlesAndPronouns.length)];
				return s + val;
			}
		}).toClosure();

		ClosureView<String> subjectGen = (new AbstractClosure1<String, String>() {
			String subjects[] = {"boy", "girl", "dog", "cat"};
			public @Override
			String run(String s) {
				String val = subjects[gen.nextInt(subjects.length)];
				return s + val;
			}
		}).toClosure();

		ClosureView<String> verbGen = (new AbstractClosure1<String, String>() {
			String verbs[] = {"ate", "destroyed", "threw", "chased", "ignored"};
			public @Override
			String run(String s) {
				String val = verbs[gen.nextInt(verbs.length)];
				return s + val;
			}
		}).toClosure();

		ClosureView<String> objectGen = (new AbstractClosure1<String, String>() {
			String subjects[] = {"ball", "tomato", "car", "Rubik's cube"};
			public @Override
			String run(String s) {
				String val = subjects[gen.nextInt(subjects.length)];
				return s + val;
			}
		}).toClosure();


		// Now let's combine them into a chain which generates sentences!
		Chain<String> sentenceGen = new Chain<String>(articleGen, subjectGen, verbGen, articleGen, objectGen);
		String sentence = sentenceGen.run("");
		out(sentence);


		// Pretty gross. Let's add a spacer.
		ClosureView<String> spacer = (new AbstractClosure1<String, String>() {
			String subjects[] = {"ball", "tomato", "car", "Rubik's cube"};
			public @Override
			String run(String s) {
				return s + " ";
			}
		}).toClosure();

		for (int x : new int[]{1,3,5,7}) {
			sentenceGen.insert(x, spacer);  // Like a list, you can insert in-between existing closures.
		}

		sentence = sentenceGen.run("");
		out(sentence);


		// Let's add some punctuation to the end at least. We can easily do that with Chain.append(...).
		ClosureView<String> punctuator = (new AbstractClosure1<String, String>() {
			public @Override
			String run(String s) {
				return s + (gen.nextBoolean() ? "." : "!");
			}
		}).toClosure();

		sentenceGen.append(punctuator);
		sentence = sentenceGen.run("");

		// (Why not capitalize it too while we're at it?)
		if (sentence.length() > 0) {
			sentence = Character.toUpperCase(sentence.charAt(0)) + sentence.substring(1);
		}

		// And that's how chains work!
		out(sentence);
	}

	@Test
	public void variableArguments() {
		// The closures do not necessarily have to be of the same argument types or lengths.
		// The expectation is, however, that all closures should properly link together.
		// Without validation or generics, you are free to combine as desired.


		ClosureView<Integer> number = (new AbstractClosure0<Integer>() {
			Random gen = new Random();

			public @Override
			Integer run() {
				return gen.nextInt(11);
			}
		}).toClosure();

		ClosureView<String> type = (new AbstractClosure1<String,Integer>() {
			Random gen = new Random();
			String words[] = {"error", "warning", "failure"};

			public @Override
			String run(Integer num) {
				String val = num +" "+ words[gen.nextInt(words.length)];

				if (num == 0 || num > 1) {
					return val + "s were";
				} else if (num == 1) {
					return val + " was";
				} else {
					return null;
				}
			}
		}).toClosure();

		ClosureView<String> full = (new AbstractClosure1<String,String>() {
			public @Override
			String run(String string) {
				if (string == null)
					return "Could not evaluate.";
				else
					return string + " detected.";
			}
		}).toClosure();

		Chain chain = new Chain<String>();
		chain.append(number, type, full);

		for (int i=1; i <= 5; ++i) {
			outN("Run "+ i +": ");
			out(chain.run());
		}

		out();

		// Of course, this setup always creates values from [0, 10]. Let's short circut it.
		chain.remove(0);        // remove the initial number generator
		out(chain.run(-5));     // chain now expects an integer to start.


		//out(chain.run());     // This will produce an exception.
		// By turning on validation we can wrap all of the exceptions and get some more useful feedback.
		chain.setValidation(true);
		out();

		try {
			out(chain.run());
		} catch (ClosureChainException ex) {
			out("Caught an exception.");
			out(ex);
		}
	}

	@Test
	public void basicValidation() {
		// Validation wraps internal Chain exceptions into a ClosureChainException. Additionally, rather than
		// simply running everything and wrapping all exceptions, some basic checks are performed before doing
		// any work. This provides more helpful and less messy error messages, as well as hopefully fewer
		// unexpected results. For example, if a closure takes 3 arguments and receives 4, it will still function
		// but the results may not be as you intended.

		ClosureView<Integer> random = (new AbstractClosure0<Integer>() {
			Random gen = new Random();

			public @Override
			Integer run() {
				return gen.nextInt(11);
			}
		}).toClosure();

		Chain<Integer> chain = new Chain<Integer>(random);
		chain.setValidation(true);


		// ok
		out(chain.run());


		// expects zero arguments
		try {
			out(chain.run(10));
		} catch (ClosureChainException ex) {
			out(ex);
		}


		// expects zero arguments
		try {
			out(chain.run(null));
		} catch (ClosureChainException ex) {
			out(ex);
		}


		// Casting exceptions occur if the output of one Closure cannot be cast to the input
		// of the next closure. They can also occur if thrown from within the Closure itself.
		ClosureView<String> reverse = (new AbstractClosure1<String, String>() {
			public @Override
			String run(String string) {
				return StringUtils.reverse(string);
			}
		}).toClosure();

		Chain<String> bad = new Chain<String>((ClosureView) random, reverse);
		bad.setValidation(true);

		try {
			out(bad.run());
		} catch (ClosureChainException ex) {
			out(ex);
			out("\t" + ex.getCause());
		}


		// Null closure in the chain, occurs when the particular closure is executed.
		random = null;
		chain.append(random);

		try {
			out(chain.run());
		} catch (ClosureChainException ex) {
			out(ex);
		}
	}
}
