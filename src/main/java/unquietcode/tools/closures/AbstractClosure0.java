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

import unquietcode.tools.closures.view.Closure0View;
import unquietcode.tools.closures.view.ClosureView;

/**
 * @author  Benjamin Fagin
 * @version Dec 10, 2010
 */
public abstract class AbstractClosure0<Z> extends ClosureBase<Z> implements Closure0<Z> {
	public abstract Z run();

	public AbstractClosure0(Object...args) {
		super(args);
	}

	@SuppressWarnings("unchecked")
	public final Closure0View<Z> getView() {
		final Closure0 base = this;

		return new Closure0View() {
			public Object run() {
				return base.run();
			}
		};
	}

	@SuppressWarnings("unchecked")
	public final ClosureView<Z> toClosure() {
		final Closure0 base = this;

		return new ClosureView<Z>() {
			public Z run(Object...args) {
				return (Z) base.run();
			}

			public int getExpectedArgs() {
				return 0;
			}
		};
	}
}
