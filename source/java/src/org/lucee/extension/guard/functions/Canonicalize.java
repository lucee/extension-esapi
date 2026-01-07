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
import java.net.URLDecoder;

import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;

public class Canonicalize extends FunctionSupport {

	private static final long serialVersionUID = -4248746351014698481L;

	public static String call(PageContext pc, String input, boolean restrictMultiple, boolean restrictMixed) throws PageException {
		return GuardEncode.canonicalize(input, restrictMultiple, restrictMixed, false);
	}

	public static String call(PageContext pc, String input, boolean restrictMultiple, boolean restrictMixed, boolean throwonError) throws PageException {

		String str = input;
		String decodeUrl = " ";
		String strFind = "%";
		int count = 0, fromIndex = 0;

		while ((fromIndex = str.indexOf(strFind, fromIndex)) != -1) {
			count++;
			fromIndex++;
		}

		try {
			decodeUrl = URLDecoder.decode(input, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw cast.toPageException(e);
		}

		if (decodeUrl == input) return GuardEncode.canonicalize(input, restrictMultiple, restrictMixed, throwonError);

		else {
			if (throwonError == false && (restrictMultiple == true || restrictMixed == true)) {
				if (count > 0) return " ";
				else return GuardEncode.canonicalize(input, restrictMultiple, restrictMixed, throwonError);
			}
			else {
				return GuardEncode.canonicalize(input, restrictMultiple, restrictMixed, throwonError);
			}
		}
	}

	@Override
	public Object invoke(PageContext pc, Object[] args) throws PageException {
		if (args.length == 4) return call(pc, cast.toString(args[0]), cast.toBooleanValue(args[1]), cast.toBooleanValue(args[2]), cast.toBooleanValue(args[3]));
		if (args.length == 3) return call(pc, cast.toString(args[0]), cast.toBooleanValue(args[1]), cast.toBooleanValue(args[2]), false);
		if (args.length == 2) return call(pc, cast.toString(args[0]), cast.toBooleanValue(args[1]), false, false);
		if (args.length == 1) return call(pc, cast.toString(args[0]), false, false, false);
		throw exp.createFunctionException(pc, "Canonicalize", 3, 4, args.length);
	}
}
