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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

/**
 * @author  Benjamin Fagin
 * @version Dec 10, 2010
 */
public abstract class AbstractMultiClosure<Z> extends ClosureBase<Z> implements MultiClosure<Z> {
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Original { }

	private static final int MAX_PARAMS = 6;                // for now, it's set at 6
	boolean isImplemented[] = new boolean[MAX_PARAMS+2];    // 0-6, and then 7 is vararg


	public AbstractMultiClosure(Object...args) {
		super(args);

		//set up an implemented map
		Class c = this.getClass();
		Method methods[] = c.getMethods();

		for (Method method : methods) {
			if (method.getName().equals("run")) {
				if (method.isVarArgs()) {
					isImplemented[MAX_PARAMS+1] = !method.isAnnotationPresent(Original.class);
				} else {
					//find out how many param
					Class classes[] = method.getParameterTypes();
					int number = classes == null ? -1 : classes.length;
					
					if (number < 0 || number > MAX_PARAMS) {
						continue; //not one of our methods
					}

					isImplemented[number] = !method.isAnnotationPresent(Original.class);
				}
			}
		}
	}

	public final boolean isImplemented(int x) {
		if (x < 0)
			return false;

		if (x > MAX_PARAMS+1)
			x = MAX_PARAMS+1;

		return isImplemented[MAX_PARAMS+1] || isImplemented[x];
	}


	@Original
	public Z run() {
		return run(new Object[]{});
	}
	@Original
	public Z run(Object p1) {
		return run(new Object[]{p1});
	}
	@Original
	public Z run(Object p1, Object p2) {
		return run(new Object[]{p1, p2});
	}
	@Original
	public Z run(Object p1, Object p2, Object p3) {
		return run(new Object[]{p1, p2, p3});
	}
	@Original
	public Z run(Object p1, Object p2, Object p3, Object p4) {
		return run(new Object[]{p1, p2, p3 ,p4});
	}
	@Original
	public Z run(Object p1, Object p2, Object p3, Object p4, Object p5) {
		return run(new Object[]{p1, p2, p3 ,p4, p5});
	}
	@Original
	public Z run(Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		return run(new Object[]{p1, p2, p3 ,p4, p5, p6});
	}
	@Original
	public Z run(Object...args) {
		throw new NotImplementedException();
	}

//TODO this is indicative of a larger problem in the class design
	public final MultiClosureView<Z> getView() {
		final MultiClosure<Z> base = this;

		return new MultiClosureView<Z>() {
			public Z run() {
				return base.run();
			}

			public Z run(Object p1) {
				return base.run(p1);
			}

			public Z run(Object p1, Object p2) {
				return base.run(p1,p2);
			}

			public Z run(Object p1, Object p2, Object p3) {
				return base.run(p1,p2,p3);
			}

			public Z run(Object p1, Object p2, Object p3, Object p4) {
				return base.run(p1,p2,p3,p4);
			}

			public Z run(Object p1, Object p2, Object p3, Object p4, Object p5) {
				return base.run(p1,p2,p3,p4,p5);
			}

			public Z run(Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
				return base.run(p1,p2,p3,p4,p5,p6);
			}

			public Z run(Object...args) {
				return base.run(args);
			}

			public Closure0View<Z> toClosure0() {
				return base.toClosure0();
			}

			public Closure1View toClosure1() {
				return base.toClosure1();
			}

			public Closure2View toClosure2() {
				return base.toClosure2();
			}

			public Closure3View toClosure3() {
				return base.toClosure3();
			}

			public Closure4View toClosure4() {
				return base.toClosure4();
			}

			public Closure5View toClosure5() {
				return base.toClosure5();
			}

			public Closure6View toClosure6() {
				return base.toClosure6();
			}

			public ClosureView<Z> toClosure() {
				return base.toClosure();
			}

			public boolean isImplemented(int x) {
				return base.isImplemented(x);
			}
		};
	}

	public final Closure0View<Z> toClosure0() {
		if (!isImplemented(0))
			return null;

		final MultiClosure<Z> base = this;

		return new Closure0View<Z>() {
			public Z run() {
				return base.run();
			}
		};
	}

	public final Closure1View toClosure1() {
		if (!isImplemented(1))
			return null;

		final MultiClosure base = this;

		return new Closure1View() {
			public Object run(Object p1) {
				return base.run(p1);
			}

			public Class[] getArgumentTypes() {
				return new Class[]{Object.class};
			}
		};
	}

	public final Closure2View toClosure2() {
		if (!isImplemented(2))
			return null;

		final MultiClosure base = this;

		return new Closure2View() {
			public Object run(Object p1, Object p2) {
				return base.run(p1, p2);
			}

			public Class[] getArgumentTypes() {
				return new Class[]{Object.class, Object.class};
			}
		};
	}

	public final Closure3View toClosure3() {
		if (!isImplemented(3))
			return null;

		final MultiClosure base = this;

		return new Closure3View() {
			public Object run(Object p1, Object p2, Object p3) {
				return base.run(p1, p2, p3);
			}

			public Class[] getArgumentTypes() {
				return new Class[]{Object.class, Object.class, Object.class};
			}
		};
	}

	public final Closure4View toClosure4() {
		if (!isImplemented(4))
			return null;

		final MultiClosure base = this;

		return new Closure4View() {
			public Object run(Object p1, Object p2, Object p3, Object p4) {
				return base.run(p1, p2, p3, p4);
			}

			public Class[] getArgumentTypes() {
				return new Class[]{Object.class, Object.class, Object.class, Object.class};
			}
		};
	}

	public final Closure5View toClosure5() {
		if (!isImplemented(5))
			return null;

		final MultiClosure base = this;

		return new Closure5View() {
			public Object run(Object p1, Object p2, Object p3, Object p4, Object p5) {
				return base.run(p1, p2, p3, p4, p5);
			}

			public Class[] getArgumentTypes() {
				return new Class[]{Object.class, Object.class, Object.class, Object.class, Object.class};
			}
		};
	}

	public final Closure6View toClosure6() {
		if (!isImplemented(6))
			return null;

		final MultiClosure base = this;

		return new Closure6View() {
			public Object run(Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
				return base.run(p1, p2, p3, p4, p5, p6);
			}

			public Class[] getArgumentTypes() {
				return new Class[]{Object.class, Object.class, Object.class, Object.class, Object.class, Object.class};
			}
		};
	}

	public final ClosureView<Z> toClosure() {
		if (!isImplemented(MAX_PARAMS + 1))
			return null;

		final MultiClosure<Z> base = this;

		return new ClosureView<Z>() {
			public Z run(Object...args) {
				return base.run(args);
			}

			public int getExpectedArgs() {
				return -1;
			}
		};
	}

	/**
	 * Convenient version of makeMultiClosure which accepts closures instead of views.
	 * Null parameters mean that the resulting multiclosure will have those run methods unimplemented.
	 *
	 * @return  A new MultiClosureView, with the appropriately implemented methods.
	 */
	public static MultiClosureView<?> makeMultiClosure(final Closure0 c0, final Closure1 c1, final Closure2 c2,
	                                                   final Closure3 c3, final Closure4 c4, final Closure5 c5,
	                                                   final Closure6 c6, final Closure cX) {
		return makeMultiClosure(
			c0 == null ? null : c0.getView(),
			c1 == null ? null : c1.getView(),
			c2 == null ? null : c2.getView(),
			c3 == null ? null : c3.getView(),
			c4 == null ? null : c4.getView(),
			c5 == null ? null : c5.getView(),
			c6 == null ? null : c6.getView(),
			cX == null ? null : cX.getView()
		);
	}

	/**
	 * Takes a series of ClosureView objects and combines them into one MultiClosureView.
	 * null parameters will mean that run method is unimplemented in the resulting combined view.
	 * isImplemented will continue to return the correct information about implemented methods.
	 *
	 * While best practice would dictate that all of the views should return the same type, this is left up
	 * to the user to enforce. Follow your heart.
	 *
	 * @return  A shiny new MultiClosureView with the appropriate run methods implemented.
	 */
	public static MultiClosureView<?> makeMultiClosure(final Closure0View c0, final Closure1View c1, final Closure2View c2,
	                                                   final Closure3View c3, final Closure4View c4, final Closure5View c5,
	                                                   final Closure6View c6, final ClosureView cX) {
		return new MultiClosureView() {

			public boolean isImplemented(int x) {
				if (cX != null)
					return true;

				switch (x) {
					case 0: return c0 != null;
					case 1: return c1 != null;
					case 2: return c2 != null;
					case 3: return c3 != null;
					case 4: return c4 != null;
					case 5: return c5 != null;
					case 6: return c6 != null;
					default: return false;
				}
			}

			public Object run() {
				if (c0 == null)
					throw new NotImplementedException();
				else
					return c0.run();
			}

			public Object run(Object p1) {
				if (c1 == null)
					throw new NotImplementedException();
				else
					return c1.run(p1);
			}

			public Object run(Object p1, Object p2) {
				if (c2 == null)
					throw new NotImplementedException();
				else
					return c2.run(p1, p2);
			}

			public Object run(Object p1, Object p2, Object p3) {
				if (c3 == null)
					throw new NotImplementedException();
				else
					return c3.run(p1, p2, p3);
			}

			public Object run(Object p1, Object p2, Object p3, Object p4) {
				if (c4 == null)
					throw new NotImplementedException();
				else
					return c4.run(p1, p2, p3, p4);
			}

			public Object run(Object p1, Object p2, Object p3, Object p4, Object p5) {
				if (c5 == null)
					throw new NotImplementedException();
				else
					return c5.run(p1, p2, p3, p4, p5);
			}

			public Object run(Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
				if (c6 == null)
					throw new NotImplementedException();
				else
					return c6.run(p1, p2, p3, p4, p5, p6);
			}

			public Object run(Object...args) {
				if (cX == null)
					throw new NotImplementedException();
				else
					return cX.run(args);
			}

			public Closure0View toClosure0() {
				return c0;
			}

			public Closure1View toClosure1() {
				return c1;
			}

			public Closure2View toClosure2() {
				return c2;
			}

			public Closure3View toClosure3() {
				return c3;
			}

			public Closure4View toClosure4() {
				return c4;
			}

			public Closure5View toClosure5() {
				return c5;
			}

			public Closure6View toClosure6() {
				return c6;
			}

			public ClosureView toClosure() {
				return cX;
			}
		};
	}
}
