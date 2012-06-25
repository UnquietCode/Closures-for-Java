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

import unquietcode.tools.closures.view.*;

/**
 * @author  Benjamin Fagin
 * @version 01-23-2011
 */
public interface MultiClosure<Z> extends ClosureInterfaceBase<Z> {
	Z run();
	Z run(Object p1);
	Z run(Object p1, Object p2);
	Z run(Object p1, Object p2, Object p3);
	Z run(Object p1, Object p2, Object p3, Object p4);
	Z run(Object p1, Object p2, Object p3, Object p4, Object p5);
	Z run(Object p1, Object p2, Object p3, Object p4, Object p5, Object p6);
	Z run(Object... args);

	Closure0View<Z> toClosure0();
	Closure1View toClosure1();
	Closure2View toClosure2();
	Closure3View toClosure3();
	Closure4View toClosure4();
	Closure5View toClosure5();
	Closure6View toClosure6();

	MultiClosureView<Z> getView();
	boolean isImplemented(int x);
}

