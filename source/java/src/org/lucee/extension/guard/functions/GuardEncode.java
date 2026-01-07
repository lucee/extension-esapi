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

import java.io.UnsupportedEncodingException;

import org.lucee.extension.guard.util.Canonicalize;
import org.owasp.encoder.Encode;

import lucee.loader.engine.CFMLEngineFactory;
import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;

public class GuardEncode extends FunctionSupport {

	private static final long serialVersionUID = -6432679747287827759L;

	// this constants are also defined in Lucee core, do not change
	public static final short ENC_BASE64 = 1;
	public static final short ENC_CSS = 2;
	public static final short ENC_DN = 3;
	public static final short ENC_HTML = 4;
	public static final short ENC_HTML_ATTR = 5;
	public static final short ENC_JAVA_SCRIPT = 6;
	public static final short ENC_LDAP = 7;
	public static final short ENC_OS = 8;
	public static final short ENC_SQL = 9;
	public static final short ENC_URL = 10;
	public static final short ENC_VB_SCRIPT = 11;
	public static final short ENC_XML = 12;
	public static final short ENC_XML_ATTR = 13;
	public static final short ENC_XPATH = 14;
	public static final short ENC_NONE = 15;

	public static String encode(String item, short encFor, boolean canonicalize) throws PageException {
		return encode(item, encFor, canonicalize, null);
	}

	public static String encode(String item, short encFor, boolean canonicalize, String sqlDialect)
			throws PageException {
		if (eng.getStringUtil().isEmpty(item))
			return item;

		try {
			if (canonicalize)
				item = org.lucee.extension.guard.util.Canonicalize.canonicalize(item, false);

			switch (encFor) {
			case ENC_CSS:
				return Encode.forCssString(item);
			case ENC_HTML:
				return Encode.forHtml(item);
			case ENC_HTML_ATTR:
				return Encode.forHtmlAttribute(item);
			case ENC_JAVA_SCRIPT:
				return Encode.forJavaScript(item);
			case ENC_NONE:
				return item;
			case ENC_URL:
				return Encode.forUriComponent(item);
			case ENC_XML:
				return Encode.forXml(item);
			case ENC_XML_ATTR:
				return Encode.forXmlAttribute(item);

			// These are not supported by OWASP Java Encoder
			case ENC_DN:
				throw exp.createApplicationException(
						"DN encoding is not supported by the guard extension, install the ESAPI extension for this.");
			case ENC_LDAP:
				throw exp.createApplicationException(
						"LDAP encoding is not supported by the guard extension, install the ESAPI extension for this.");
			case ENC_VB_SCRIPT:
				throw exp.createApplicationException(
						"VBScript encoding is not supported by the guard extension, install the ESAPI extension for this.");
			case ENC_XPATH:
				throw exp.createApplicationException(
						"XPath encoding is not supported by the guard extension, install the ESAPI extension for this.");
			case ENC_SQL:
				throw exp.createApplicationException(
						"SQL encoding should not be used. Use parameterized queries/prepared statements instead.");
			}
			throw exp.createApplicationException("invalid target encoding definition");
		} catch (Exception e) {
			throw cast.toPageException(e);
		}
	}

	public static String call(PageContext pc, String strEncodeFor, String value) throws PageException {
		return call(pc, strEncodeFor, value, false, null);
	}

	public static String call(PageContext pc, String strEncodeFor, String value, boolean canonicalize)
			throws PageException {
		return call(pc, strEncodeFor, value, canonicalize, null);
	}

	public static String call(PageContext pc, String strEncodeFor, String value, boolean canonicalize, String dialect)
			throws PageException {
		short type = toEncodeType(pc, strEncodeFor);
		return encode(value, type, canonicalize, dialect);
	}

	public static short toEncodeType(String strEncodeFor, short defaultValue) {
		strEncodeFor = eng.getStringUtil().emptyIfNull(strEncodeFor).trim().toLowerCase();

		if ("css".equals(strEncodeFor))
			return ENC_CSS;
		else if ("dn".equals(strEncodeFor))
			return ENC_DN;
		else if ("html".equals(strEncodeFor))
			return ENC_HTML;
		else if ("html_attr".equals(strEncodeFor))
			return ENC_HTML_ATTR;
		else if ("htmlattr".equals(strEncodeFor))
			return ENC_HTML_ATTR;
		else if ("html-attr".equals(strEncodeFor))
			return ENC_HTML_ATTR;
		else if ("html attr".equals(strEncodeFor))
			return ENC_HTML_ATTR;
		else if ("htmlattribute".equals(strEncodeFor))
			return ENC_HTML_ATTR;
		else if ("html_attributes".equals(strEncodeFor))
			return ENC_HTML_ATTR;
		else if ("htmlattributes".equals(strEncodeFor))
			return ENC_HTML_ATTR;
		else if ("html-attributes".equals(strEncodeFor))
			return ENC_HTML_ATTR;
		else if ("html attributes".equals(strEncodeFor))
			return ENC_HTML_ATTR;
		else if ("js".equals(strEncodeFor))
			return ENC_JAVA_SCRIPT;
		else if ("javascript".equals(strEncodeFor))
			return ENC_JAVA_SCRIPT;
		else if ("java_script".equals(strEncodeFor))
			return ENC_JAVA_SCRIPT;
		else if ("java script".equals(strEncodeFor))
			return ENC_JAVA_SCRIPT;
		else if ("java-script".equals(strEncodeFor))
			return ENC_JAVA_SCRIPT;
		else if ("ldap".equals(strEncodeFor))
			return ENC_LDAP;
		else if ("".equals(strEncodeFor) || "none".equals(strEncodeFor))
			return ENC_NONE;
		else if ("sql".equals(strEncodeFor))
			return ENC_SQL;
		else if ("url".equals(strEncodeFor))
			return ENC_URL;
		else if ("vbs".equals(strEncodeFor))
			return ENC_VB_SCRIPT;
		else if ("vbscript".equals(strEncodeFor))
			return ENC_VB_SCRIPT;
		else if ("vb-script".equals(strEncodeFor))
			return ENC_VB_SCRIPT;
		else if ("vb_script".equals(strEncodeFor))
			return ENC_VB_SCRIPT;
		else if ("vb script".equals(strEncodeFor))
			return ENC_VB_SCRIPT;
		else if ("xml".equals(strEncodeFor))
			return ENC_XML;
		else if ("xmlattr".equals(strEncodeFor))
			return ENC_XML_ATTR;
		else if ("xml attr".equals(strEncodeFor))
			return ENC_XML_ATTR;
		else if ("xml-attr".equals(strEncodeFor))
			return ENC_XML_ATTR;
		else if ("xml_attr".equals(strEncodeFor))
			return ENC_XML_ATTR;
		else if ("xmlattribute".equals(strEncodeFor))
			return ENC_XML_ATTR;
		else if ("xmlattributes".equals(strEncodeFor))
			return ENC_XML_ATTR;
		else if ("xml attributes".equals(strEncodeFor))
			return ENC_XML_ATTR;
		else if ("xml-attributes".equals(strEncodeFor))
			return ENC_XML_ATTR;
		else if ("xml_attributes".equals(strEncodeFor))
			return ENC_XML_ATTR;
		else if ("xpath".equals(strEncodeFor))
			return ENC_XPATH;
		else
			return defaultValue;
	}

	public static short toEncodeType(PageContext pc, String strEncodeFor) throws PageException {
		short df = (short) -1;
		short encFor = toEncodeType(strEncodeFor, df);
		if (encFor != df)
			return encFor;

		String msg = "value [" + strEncodeFor + "] is invalid, valid values are "
				+ "[css,html,html_attr,javascript,url,xml,xml_attr]";
		throw exp.createApplicationException(msg);
	}

	public static String canonicalize(String input, boolean restrictMultiple, boolean restrictMixed,
			boolean throwOnError) throws PageException {
		if (eng.getStringUtil().isEmpty(input))
			return input;
		try {
			return Canonicalize.canonicalize(input, throwOnError);
		} catch (UnsupportedEncodingException e) {
			throw CFMLEngineFactory.getInstance().getCastUtil().toPageException(e);
		}

	}

	@Override
	public Object invoke(PageContext pc, Object[] args) throws PageException {
		if (args.length == 2)
			return call(pc, cast.toString(args[0]), cast.toString(args[1]));
		if (args.length == 3)
			return call(pc, cast.toString(args[0]), cast.toString(args[1]), cast.toBooleanValue(args[2]));
		if (args.length == 4)
			return call(pc, cast.toString(args[0]), cast.toString(args[1]), cast.toBooleanValue(args[2]),
					cast.toString(args[3]));
		throw exp.createFunctionException(pc, "guardEncode", 2, 4, args.length);
	}
}