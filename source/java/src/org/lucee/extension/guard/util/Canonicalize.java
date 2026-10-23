package org.lucee.extension.guard.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

import lucee.loader.engine.CFMLEngineFactory;
import lucee.runtime.exp.PageException;
import lucee.runtime.util.Strings;

public class Canonicalize {

	private static final int MAX_ITERATIONS = 5;

	/**
	 * Simplified canonicalization that handles URL and HTML encoding
	 * 
	 * @param input The input to canonicalize
	 * @param throwOnMultiple Throw exception if multiple encoding detected
	 * @return Canonicalized string
	 * @throws UnsupportedEncodingException
	 * @throws RuntimeException
	 * @throws PageException
	 */
	public static String canonicalize(String input, boolean throwOnMultiple) throws PageException {
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

			// Decode URL encoding, a "%" that is not a valid escape is left as is (LDEV-4743)
			String urlDecoded = decodePercent(working);
			if (!urlDecoded.equals(working)) {
				if (iterations > 1) {
					multipleEncodingDetected = true;
				}
				working = urlDecoded;
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
				throw new IllegalArgumentException("Too many encoding layers detected (>" + MAX_ITERATIONS + "). Possible attack.");
			}

		}
		while (!working.equals(previous));

		// Check if multiple encoding was detected
		if (throwOnMultiple && multipleEncodingDetected) {
			throw CFMLEngineFactory.getInstance().getExceptionUtil().createSecurityException("Multiple encoding layers detected in input: " + input);
		}
		return util.replace(working, placeholder, "+", false, true);
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
						}
						else {
							// Decimal
							codepoint = Integer.parseInt(numStr);
						}

						// Basic validation
						if (codepoint >= 0 && codepoint <= 0x10FFFF) {
							result.append((char) codepoint);
							i = endIdx + 1;
							continue;
						}
					}
					catch (NumberFormatException e) {
						// Not a valid number, treat as regular text
					}
				}
			}

			result.append(input.charAt(i));
			i++;
		}

		return result.toString();
	}

	/**
	 * Lenient percent decoding as used by canonicalize(), following the ESAPI PercentCodec semantics: every
	 * valid escape (% followed by two hex digits) is decoded, a "%" that does not start a valid escape is kept
	 * as a literal character instead of failing (LDEV-4743). Consecutive escapes are decoded as UTF-8; when they
	 * do not form valid UTF-8 they are kept as they are. A "+" is not touched (it is not an escape here).
	 *
	 * @param str the string to decode
	 * @return the decoded string, the same instance when nothing was decoded
	 */
	public static String decodePercent(String str) {
		if (str == null || str.indexOf('%') == -1) {
			return str;
		}
		int len = str.length();
		StringBuilder sb = new StringBuilder(len);
		boolean changed = false;
		int i = 0;
		while (i < len) {
			char c = str.charAt(i);
			if (c != '%' || !isEscape(str, i)) {
				sb.append(c);
				i++;
				continue;
			}
			// collect a run of consecutive valid escapes
			int start = i;
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			while (i < len && str.charAt(i) == '%' && isEscape(str, i)) {
				bytes.write((hex(str.charAt(i + 1)) << 4) | hex(str.charAt(i + 2)));
				i += 3;
			}
			try {
				CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT)
						.onUnmappableCharacter(CodingErrorAction.REPORT);
				sb.append(decoder.decode(ByteBuffer.wrap(bytes.toByteArray())));
				changed = true;
			}
			catch (CharacterCodingException cce) {
				// not valid UTF-8, keep the escapes as they are
				sb.append(str, start, i);
			}
		}
		return changed ? sb.toString() : str;
	}

	private static boolean isEscape(String str, int index) {
		return index + 2 < str.length() && hex(str.charAt(index + 1)) != -1 && hex(str.charAt(index + 2)) != -1;
	}

	private static int hex(char c) {
		if (c >= '0' && c <= '9') return c - '0';
		if (c >= 'a' && c <= 'f') return c - 'a' + 10;
		if (c >= 'A' && c <= 'F') return c - 'A' + 10;
		return -1;
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

		for (String test: tests) {
			String result = canonicalize(test, false);
			System.out.println("Input:  " + test);
			System.out.println("Output: " + result);
			System.out.println();

		}

	}
}
