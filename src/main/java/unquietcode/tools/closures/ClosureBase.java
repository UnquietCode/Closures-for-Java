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

import com.googlecode.gentyref.GenericTypeReflector;
import unquietcode.tools.closures.view.ClosureView;
import unquietcode.tools.closures.view.ClosureViewBase;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @author  Benjamin Fagin
 * @version Dec 7, 2010
 */
abstract class ClosureBase<Z> {
	private Object arguments[];

	protected ClosureBase(Object...args) {
		if (args == null)
			arguments = new Object[] {null};
		else
			arguments = args;
	}

	// to get passed in variables
	@SuppressWarnings("unchecked")
	protected final <A> A a1() {
		return (A) arg(1);
	}

	@SuppressWarnings("unchecked")
	protected final <A> A a2() {
		return (A) arg(2);
	}

	@SuppressWarnings("unchecked")
	protected final <A> A a3() {
		return (A) arg(3);
	}

	@SuppressWarnings("unchecked")
	protected final <A> A a4() {
		return (A) arg(4);
	}

	@SuppressWarnings("unchecked")
	protected final <A> A arg(int var) {
		try {
			return (A) arguments[--var];
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new ClosureException("invalid argument index: " + var);
		}
	}

	protected final Object[] getArguments() {
		return Arrays.copyOf(arguments, arguments.length);
	}

	@SuppressWarnings("unchecked")
	public final void curry(int var, Object replacement) {
		var -= 1;
		Field f[] = this.getClass().getDeclaredFields();

		if (var >= f.length) {
			throw new ClosureException("invalid field ("+ (var+1) +")");
		}

		Field field = f[var];
		String name = field.getName();

		//TODO this might be compiler dependant, so need a better way!
		if (name.replaceAll("^this\\$[0-9]+", "").equals("")) {
			throw new ClosureException("invalid field ("+ (var+1) +")");
		}

		//make accessible
		if (!field.isAccessible())
			field.setAccessible(true);

		try {
			field.set(this, replacement);
		} catch (IllegalAccessException ex) {
			throw new ClosureException("Could not access field to change.", ex);
		} catch (IllegalArgumentException ex) {
			throw new ClosureException("Invalid object type for replacement.", ex);
		}
	}

	protected final <T extends ClosureBase> Class[] getArgumentTypes(Class<T> clazz) {
		Type baseType = GenericTypeReflector.getExactSuperType((Type) this.getClass(), clazz);

		if (baseType instanceof Class<?>) {
			// raw class, type parameters not known
			return new Class[]{};
		}

		ParameterizedType pBaseType = (ParameterizedType) baseType;
		Type types[] = pBaseType.getActualTypeArguments();

		if (types.length <= 1) {
			return new Class[]{};
		}

		Class classes[] = new Class[types.length-1];
		for (int i=1; i < types.length; ++i) {
			classes[i-1] = (Class) types[i];
		}

		return classes;
	}

	public abstract ClosureView<Z> toClosure();
	public abstract <T extends ClosureViewBase<Z>> T getView();

	//TODO generate a "signature" version of the argument types array
}
