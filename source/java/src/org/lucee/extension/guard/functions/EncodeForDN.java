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

public class EncodeForDN  extends FunctionSupport {

	private static final long serialVersionUID = -3276058916547934830L;

	public static String call(PageContext pc , String item, boolean canonicalize) throws PageException  {
		return GuardEncode.encode(item, GuardEncode.ENC_DN,canonicalize);
	}
	
	public static String call(PageContext pc , String item) throws PageException  {
		return call(pc, item, false);
	}
	@Override
	public Object invoke(PageContext pc, Object[] args) throws PageException {
		if(args.length==1) return call(pc,cast.toString(args[0]));
		if(args.length==2) return call(pc,cast.toString(args[0]),cast.toBooleanValue(args[1]));
		throw exp.createFunctionException(pc, "EncodeForDN", 1, 2, args.length);
	}
}