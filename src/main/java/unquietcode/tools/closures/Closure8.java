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

import unquietcode.tools.closures.view.Closure8View;

/**
 * @author Benjamin Fagin
 * @version 01-23-2011
 */
public interface Closure8<Z, A,B,C,D,E,F,G,H> extends ClosureInterfaceBase<Z> {
	Z run(A p1, B p2, C p3, D p4, E p5, F p6, G p7, H p8);
	Closure8View<Z, A,B,C,D,E,F,G,H> getView();
	Class[] getArgumentTypes();
}
