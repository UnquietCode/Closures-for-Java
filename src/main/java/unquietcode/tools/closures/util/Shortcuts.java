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

package unquietcode.tools.closures.util;


/**
 * @author Benjamin Fagin
 * @version Nov 28, 2010
 *
 * From UQCLib 0.2
 */
public final class Shortcuts {

	/**
	 * Alias for System.exit(code), reminding us all of php.
	 * Remember: your program will end here rather than the line where this method is invoked.
	 *
	 * @param code  exit code
	 */
	public static void die(int code) {
		System.exit(code);
	}

	/**
	 * Prints to System.out using println.
	 *
	 * @param object    object to print
	 */
	public static <T> void out(T object) {
		System.out.println(object);
	}

	/**
	 * Prints a blank line to System.out
	 */
	public static void out() {
		System.out.println();
	}

	/**
	 * Prints to System.out using print
	 *
	 * @param object    object to print
	 */
	public static <T> void outN(T object) {
		System.out.print(object);
	}

	/**
	 * Prints to System.err using println.
	 *
	 * @param object    object to print
	 */
	public static <T> void err(T object) {
		System.err.println(object);
	}

	/**
	 * Prints a blank line to System.err
	 */
	public static void err() {
		System.err.println();
	}

	/**
	 * Prints to System.err using print
	 *
	 * @param object    object to print
	 */
	public static <T> void errN(T object) {
		System.err.print(object);
	}

	/**
	 * Takes "string" and returns "'string'". Does not escape the string.
	 *
	 * @param string    String to be wrapped
	 * @return          'string'
	 */
	public static String sqt(String string) {
		return "'" + string + "'";
	}

	/**
	 * Takes "string" and returns ""string"". Does not escape the string.
	 *
	 * @param string    String to be wrapped
	 * @return          "string"
	 */
	public static String dqt(String string) {
		return "\"" + string + "\"";
	}
}
