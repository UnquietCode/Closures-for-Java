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

import static unquietcode.tools.closures.util.Shortcuts.*;

/**
 * @author  Ben
 * @version 0.1
 * Date:    Dec 10, 2010
 */
public class ClosureInheritanceTest {
	@Test
	public void basic() {
		AbstractClosure2<Integer, Integer, Integer> mixer = new AbstractClosure2<Integer, Integer, Integer>() {
			int x = 10;



			public Integer run(Integer v1, Integer v2) {
				return v1 + v2 - x;
			}
		};

		ClosureView c = mixer.toClosure();
		out("expects " + c.getExpectedArgs() + " arguments");
		out("types are ");
		for (Class clazz : mixer.getArgumentTypes()) {
			outN(clazz.getName());
		}
		out("\n");

		try {
			out(c.run(20, 30)); // 40
		//	out(c.run(1));      // fail!
		} catch (ClosureException ex) {
			err(ex.getMessage());
			die(10);
		}
	}
}
