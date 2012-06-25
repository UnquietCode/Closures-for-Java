
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


import unquietcode.tools.closures.view.ClosureView;

/**
 * @author  Benjamin Fagin
 * @version 12/12/10
 */
public abstract class AbstractClosure<Z> extends ClosureBase<Z> implements Closure<Z> {
	private int expectedArgs = -1;

	public final void setExpectedArgs(int args) {
		if (args >= 0)
			expectedArgs = args;
		else
			expectedArgs = -1;
}

	public final int getExpectedArgs() {
		return expectedArgs;
	}

	public AbstractClosure(Object...args) {
		super(args);
	}

	public final ClosureView<Z> getView() {
		return this.toClosure();
	}

	@SuppressWarnings("unchecked")
	public final ClosureView<Z> toClosure() {
		final Closure base = this;

		return new ClosureView<Z>() {
			public Z run(Object...args) {
				return (Z) base.run(args);
			}

			public int getExpectedArgs() {
				return base.getExpectedArgs();
			}
		};
	}

	public abstract Z run(Object...args);
}
