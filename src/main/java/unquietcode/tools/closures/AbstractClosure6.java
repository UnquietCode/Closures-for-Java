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

import unquietcode.tools.closures.view.Closure6View;
import unquietcode.tools.closures.view.ClosureView;

/**
 * @author  Benjamin Fagin
 * @version Dec 10, 2010
 */
public abstract class AbstractClosure6<Z, A,B,C,D,E,F> extends ClosureBase<Z> implements Closure6<Z, A,B,C,D,E,F> {
	public abstract Z run(A p1, B p2, C p3, D p4, E p5, F p6);

	public AbstractClosure6(Object...args) {
		super(args);
	}

	public final Class[] getArgumentTypes() {
		return super.getArgumentTypes(AbstractClosure6.class);
	}

	@SuppressWarnings("unchecked")
	public final Closure6View<Z, A,B,C,D,E,F> getView() {
		final Closure6 base = this;

		return new Closure6View() {
			public Object run(Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
				return base.run(p1, p2, p3, p4, p5, p6);
			}

			public Class[] getArgumentTypes() {
				return base.getArgumentTypes();
			}
		};
	}

	@SuppressWarnings("unchecked")
	public final ClosureView<Z> toClosure() {
		final Closure6 base = this;

		return new ClosureView<Z>() {
			public Z run(Object...args) {
				return (Z) base.run(args[0], args[1], args[2], args[3], args[4], args[5]);
			}

			public int getExpectedArgs() {
				return 6;
			}

			public Class[] getArgumentTypes() {
				return base.getArgumentTypes();
			}
		};
	}
}
