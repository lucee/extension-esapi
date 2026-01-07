package org.lucee.extension.guard.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import lucee.loader.engine.CFMLEngine;
import lucee.loader.engine.CFMLEngineFactory;
import lucee.runtime.exp.PageException;
import lucee.runtime.util.Strings;

public class Canonicalize {

	private static final int MAX_ITERATIONS = 5;

	/**
	 * Removes "unsafe" characters that are commonly used in injection attacks but
	 * are not part of the standard alphanumeric set. * @param input The string to
	 * clean
	 * 
	 * @return A string containing only "safe" characters
	 */
	public static String simplify(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}

		StringBuilder sb = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);

			// Define "Safe" characters: Alphanumeric, space, and very basic punctuation
			// This effectively "strips" ^, \, and others not in this list.
			if (isSafe(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private static boolean isSafe(char c) {
		// Allow A-Z, a-z, 0-9
		if (Character.isLetterOrDigit(c)) {
			return true;
		}

		// Allow common "inert" punctuation
		switch (c) {
		case ' ':
		case '#':
		case '-':
		case '_':
		case '.':
		case '@':
		case ':':
		case ',':
		case '!':
		case '?':
		case '$':
		case '&':
		case '*':
		case '(':
		case ')':
		case '+':
		case '{':
		case '}':
		case '[':
		case ']':
		case '<':
		case '>':
		case '"':
		case ';':
		case '~':
		case '`':
		case '|':
		case '/':
		case '\'':
			return true;
		default:
			return false;
		}
	}

	/**
	 * Simplified canonicalization that handles URL and HTML encoding
	 * 
	 * @param input           The input to canonicalize
	 * @param throwOnMultiple Throw exception if multiple encoding detected
	 * @return Canonicalized string
	 * @throws UnsupportedEncodingException
	 */
	public static String canonicalize(String input, boolean throwOnMultiple) throws UnsupportedEncodingException {
		if (input == null || input.isEmpty()) {
			return input;
		}
		String placeholder = "djkilbmop";
		Strings util = CFMLEngineFactory.getInstance().getStringUtil();
		String working = util.replace(input, "+", placeholder, false, true);

		String previous;
		int iterations = 0;
		boolean multipleEncodingDetected = false;

		do {
			previous = working;
			iterations++;

			// Decode URL encoding
			try {
				String urlDecoded = decode(working);
				if (!urlDecoded.equals(working)) {
					if (iterations > 1) {
						multipleEncodingDetected = true;
					}
					working = urlDecoded;
				}
			} catch (PageException pe) {
				// Invalid URL encoding, continue with HTML
			}

			// Decode HTML entities
			String htmlDecoded = decodeHtmlEntities(working);
			if (!htmlDecoded.equals(working)) {
				if (iterations > 1 && !working.equals(previous)) {
					multipleEncodingDetected = true;
				}
				working = htmlDecoded;
			}

			// Safety check - prevent infinite loops
			if (iterations > MAX_ITERATIONS) {
				throw new IllegalArgumentException(
						"Too many encoding layers detected (>" + MAX_ITERATIONS + "). Possible attack.");
			}

		} while (!working.equals(previous));

		// Check if multiple encoding was detected
		if (throwOnMultiple && multipleEncodingDetected) {
			throw new SecurityException("Multiple encoding layers detected in input: " + input);
		}
		return simplify(util.replace(working, placeholder, "+", false, true));
	}

	private static String decodeHtmlEntities(String input) {
		if (input == null) {
			return null;
		}

		String result = input;

		// Common HTML entities
		result = result.replace("&lt;", "<");
		result = result.replace("&gt;", ">");
		result = result.replace("&quot;", "\"");
		result = result.replace("&#34;", "\"");
		result = result.replace("&#39;", "'");
		result = result.replace("&#x27;", "'");
		result = result.replace("&apos;", "'");
		result = result.replace("&#x2F;", "/");
		result = result.replace("&#47;", "/");
		result = result.replace("&amp;", "&"); // Must be last!

		// Numeric entities (simple cases)
		result = decodeNumericEntities(result);

		return result;
	}

	/**
	 * Decode numeric HTML entities like &#60; or &#x3C;
	 */
	private static String decodeNumericEntities(String input) {
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

						if (numStr.charAt(0) == 'x' || numStr.charAt(0) == 'X') {
							// Hexadecimal
							codepoint = Integer.parseInt(numStr.substring(1), 16);
						} else {
							// Decimal
							codepoint = Integer.parseInt(numStr);
						}

						// Basic validation
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

	public static String decode(String str) throws PageException {
		CFMLEngine eng = CFMLEngineFactory.getInstance();
		try {
			Class<?> clazz = eng.getClassUtil().loadClass("lucee.commons.net.URLDecoder");

			// decode(String str, boolean force)
			return eng.getCastUtil().toString(eng.getClassUtil().callMethod(clazz,
					eng.getCreationUtil().createKey("decode"), new Object[] { str, false }));
		} catch (Exception e) {
			try {
				return URLDecoder.decode(str, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				throw eng.getCastUtil().toPageException(e);
			}
		}

	}

	/**
	 * Example usage
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Test cases
		String[] tests = { "%253Cscript%253E", // Double URL encoded
				"&lt;script&gt;", // HTML encoded
				"%3Cscript%3E", // URL encoded
				"&#60;script&#62;", // Numeric HTML entities
				"normal text", // No encoding
				"%2522hello%2520world%2522" // Multiple URL encoding
		};

		System.out.println("Testing SimpleCanonicalize:\n");

		for (String test : tests) {
			String result = canonicalize(test, false);
			System.out.println("Input:  " + test);
			System.out.println("Output: " + result);
			System.out.println();

		}

	}
}
