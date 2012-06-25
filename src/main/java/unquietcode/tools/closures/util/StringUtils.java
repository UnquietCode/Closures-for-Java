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
 * @author Ben Fagin
 * @version Nov 28, 2010
 *
 * From UQCLib 0.2
 */
public class StringUtils {
	private StringUtils() { }

	public static String blanks(int total) {
		StringBuilder sb = new StringBuilder();

		for (int i=0; i < total; ++i) {
			sb.append(" ");
		}

		return sb.toString();
	}

	public static String reverse(String string) {
		StringBuilder sb = new StringBuilder(string);
		sb = sb.reverse();
		return sb.toString();
	}

	public static boolean startsWithAny(String string, String...prefixes) {
		for (String prefix : prefixes)
			if (string.startsWith(prefix))
				return true;

		return false;
	}

	public static boolean startsWithAnyCI(String string, String...prefixes) {
		string = string.toLowerCase();

		for (String prefix : prefixes)
			if (string.startsWith(prefix.toLowerCase()))
				return true;

		return false;
	}

	public static boolean endsWithAny(String string, String...suffixes) {
		for (String suffix : suffixes)
			if (string.endsWith(suffix))
				return true;

		return false;
	}

	public static boolean endsWithAnyCI(String string, String...suffixes) {
		string = string.toLowerCase();

		for (String suffix : suffixes)
			if (string.endsWith(suffix.toLowerCase()))
				return true;

		return false;
	}

	public static boolean equalsAny(String string, String...others) {
		for (String other : others)
			if (string.equals(other))
				return true;

		return false;
	}

	public static boolean equalsAnyCI(String string, String...others) {
		string = string.toLowerCase();

		for (String other : others)
			if (string.equals(other.toLowerCase()))
				return true;

		return false;
	}

	public static boolean hasText(String string) {
		return string != null  &&  !string.trim().isEmpty();
	}
}

