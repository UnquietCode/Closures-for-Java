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

import unquietcode.tools.closures.view.Closure2View;
import unquietcode.tools.closures.view.ClosureView;

/**
 * @author  Benjamin Fagin
 * @version Dec 10, 2010
 */
public abstract class AbstractClosure2<Z, A,B> extends ClosureBase<Z> implements Closure2<Z, A,B> {
	public abstract Z run(A p1, B p2);

	public AbstractClosure2(Object...args) {
		super(args);
	}

	public final Class[] getArgumentTypes() {
		return super.getArgumentTypes(AbstractClosure2.class);
	}

	public Void run(String hello, String world) {
		return null;
	}

	public void curry(int i, String world) {
		//nothing
	}

	@SuppressWarnings("unchecked")
	public final Closure2View<Z, A,B> getView() {
		final Closure2 base = this;

		return new Closure2View() {
			public Object run(Object p1, Object p2) {
				return base.run(p1, p2);
			}

			public Class[] getArgumentTypes() {
				return base.getArgumentTypes();
			}
		};
	}

	@SuppressWarnings("unchecked")
	public final ClosureView<Z> toClosure() {
		final Closure2 base = this;

		return new ClosureView<Z>() {
			public Z run(Object...args) {
				return (Z) base.run(args[0], args[1]);
			}

			public int getExpectedArgs() {
				return 2;
			}

			public Class[] getArgumentTypes() {
				return base.getArgumentTypes();
			}
		};
	}
}
