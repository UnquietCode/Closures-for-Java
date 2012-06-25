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

import static org.junit.Assert.assertEquals;


/**
 * @author Benjamin Fagin
 * @version 03-02-2011
 */
public class ClosurePassing_T {
	@Test
	public void basic() {
		// create a multiplier through nested closures
		Closure1<Integer, Integer> squared = new AbstractClosure1<Integer, Integer>() {
			public @Override
			Integer run(Integer p1) {
				if (p1 == null)
					return null;
				else
					return p1 * p1;
			}
		};

		Closure1<Integer, Integer> copycat = new AbstractClosure1<Integer, Integer>(squared) {
			Closure1 a1 = a1();

			@SuppressWarnings("unchecked")
			public @Override
			Integer run(Integer p1) {
				return (Integer) a1.run(p1);
			}
		};

		assertEquals((Integer) 16, copycat.run(4));
	}
}
