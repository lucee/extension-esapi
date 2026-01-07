/**
 *
 * Copyright (c) 2014, the Railo Company Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 **/
package org.lucee.extension.guard.functions;

import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;

/**
 * IMPORTANT: Decoding user input is generally NOT recommended for security
 * purposes. Modern security practices focus on encoding OUTPUT, not decoding
 * input.
 * 
 * This class is provided for legitimate use cases like processing
 * already-encoded data, but should NOT be used as a security measure to
 * "sanitize" user input.
 */
public class GuardDecode extends FunctionSupport {

	private static final long serialVersionUID = 7054200748398531363L;

	public static final short DEC_BASE64 = 1;
	public static final short DEC_URL = 2;
	public static final short DEC_HTML = 3;

	public static String decode(String item, short decFrom) throws PageException {
		if (eng.getStringUtil().isEmpty(item))
			return item;

		switch (decFrom) {
		case DEC_URL:
			return decodeURL(item);
		case DEC_HTML:
			return decodeHTML(item);
		}
		throw exp.createApplicationException("invalid target decoding definition");

	}

	private static String decodeURL(String input) throws PageException {
		return org.lucee.extension.guard.util.Canonicalize.decode(input);
	}

	/**
	 * Decode common HTML entities
	 * 
	 */
	private static String decodeHTML(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}

		String result = input;

		// Common named HTML entities
		result = result.replace("&lt;", "<");
		result = result.replace("&gt;", ">");
		result = result.replace("&quot;", "\"");
		result = result.replace("&#34;", "\"");
		result = result.replace("&#39;", "'");
		result = result.replace("&#x27;", "'");
		result = result.replace("&apos;", "'");
		result = result.replace("&#x2F;", "/");
		result = result.replace("&#47;", "/");
		result = result.replace("&nbsp;", " ");
		result = result.replace("&amp;", "&"); // Must be last!

		// Decode numeric HTML entities (&#60; or &#x3C;)
		result = decodeNumericEntities(result);

		return result;
	}

	/**
	 * Decode numeric HTML entities like &#60; (decimal) or &#x3C; (hexadecimal)
	 */
	private static String decodeNumericEntities(String input) {
		if (input == null || input.indexOf("&#") == -1) {
			return input;
		}

		StringBuilder result = new StringBuilder();
		int i = 0;

		while (i < input.length()) {
			if (input.charAt(i) == '&' && i + 2 < input.length() && input.charAt(i + 1) == '#') {
				// Found potential numeric entity
				int endIdx = input.indexOf(';', i + 2);

				if (endIdx > i + 2 && endIdx < i + 10) { // Reasonable length
					try {
						String numStr = input.substring(i + 2, endIdx);
						int codepoint;

						if (numStr.length() > 0 && (numStr.charAt(0) == 'x' || numStr.charAt(0) == 'X')) {
							// Hexadecimal (&#x3C;)
							codepoint = Integer.parseInt(numStr.substring(1), 16);
						} else {
							// Decimal (&#60;)
							codepoint = Integer.parseInt(numStr);
						}

						// Validate codepoint is in valid Unicode range
						if (codepoint >= 0 && codepoint <= 0x10FFFF) {
							result.append((char) codepoint);
							i = endIdx + 1;
							continue;
						}
					} catch (NumberFormatException e) {
						// Not a valid number, treat as regular text
					}
				}
			}

			result.append(input.charAt(i));
			i++;
		}

		return result.toString();
	}

	public static String call(PageContext pc, String strDecodeFrom, String value) throws PageException {
		short decFrom;
		strDecodeFrom = eng.getStringUtil().emptyIfNull(strDecodeFrom).trim().toLowerCase();

		if ("url".equals(strDecodeFrom)) {
			decFrom = DEC_URL;
		} else if ("html".equals(strDecodeFrom)) {
			decFrom = DEC_HTML;
		} else {
			throw exp.createFunctionException(pc, "guardDecode", 1, "decodeFrom",
					"value [" + strDecodeFrom + "] is invalid, valid values are [url,html]", "");
		}

		return decode(value, decFrom);
	}

	@Override
	public Object invoke(PageContext pc, Object[] args) throws PageException {
		if (args.length == 2)
			return call(pc, cast.toString(args[0]), cast.toString(args[1]));
		throw exp.createFunctionException(pc, "guardDecode", 2, 2, args.length);
	}
}