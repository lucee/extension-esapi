package org.lucee.extension.guard;

public class CustomEncoder {
	public static String encodeForDN(String input) {
		if (input == null)
			return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			// Special handling for leading/trailing spaces and leading #
			if ((i == 0 && (c == ' ' || c == '#')) || (i == input.length() - 1 && c == ' ')) {
				sb.append('\\').append(c);
			} else if (",=+<>\\;\"".indexOf(c) >= 0) {
				sb.append('\\').append(c);
			} else if (c < 32 || c > 126) {
				// Hex escape non-printable/non-ascii
				sb.append('\\').append(Integer.toHexString(c | 0x100).substring(1));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String encodeForSearchFilter(String input) {
		if (input == null)
			return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			switch (c) {
			case '\\':
				sb.append("\\5c");
				break;
			case '*':
				sb.append("\\2a");
				break;
			case '(':
				sb.append("\\28");
				break;
			case ')':
				sb.append("\\29");
				break;
			case '\u0000':
				sb.append("\\00");
				break;
			default:
				if (c < 32 || c > 126) {
					sb.append('\\').append(Integer.toHexString(c | 0x100).substring(1));
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	public static String encodeForXPath(String input) {
		if (input == null)
			return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			switch (c) {
			case '\'':
				sb.append("&#39;");
				break;
			case '\"':
				sb.append("&#34;");
				break;
			case '<':
				sb.append("&lt;");
				break; // Add this
			case '>':
				sb.append("&gt;");
				break; // Add this
			default:
				if (c < 32 || c > 126) {
					sb.append('\\').append(Integer.toHexString(c | 0x100).substring(1));
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	public static String encodeForVBScript(String input) {
		if (input == null)
			return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				sb.append(c);
			} else {
				// VBScript hex escape format
				sb.append("hex(").append(Integer.toHexString(c)).append(")");
			}
		}
		return sb.toString();
	}

	public static String encodeForSQL(String input, String dialect) {
		if (input == null)
			return null;
		if (dialect == null)
			dialect = "default";
		dialect = dialect.toLowerCase();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);

			// MySQL and MySQL_ANSI handle backslashes differently
			if (dialect.contains("mysql") && !dialect.contains("ansi")) {
				switch (c) {
				case '\0':
					sb.append("\\0");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '\'':
					sb.append("\\'");
					break;
				case '"':
					sb.append("\\\"");
					break;
				case '\032':
					sb.append("\\Z");
					break;
				default:
					sb.append(c);
				}
			}
			// Standard SQL (Oracle, DB2, SQL Server, MySQL ANSI)
			else {
				if (c == '\'') {
					sb.append("''"); // Double the single quote
				} else if (c == '\0') {
					// Remove null characters to prevent truncation attacks
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}
}
