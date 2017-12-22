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
package org.lucee.extension.esapi.functions;

import java.io.PrintStream;

import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;

import org.lucee.extension.esapi.util.DevNullOutputStream;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.errors.EncodingException;

public class ESAPIDecode extends FunctionSupport {
	
	private static final long serialVersionUID = 7054200748398531363L;
	
	public static final short DEC_BASE64=1;
	public static final short DEC_URL=2;
	public static final short DEC_HTML=3;
	
	public static String decode(String item, short decFrom) throws PageException  {
		
		PrintStream out = System.out;
		try {
			 System.setOut(new PrintStream(DevNullOutputStream.DEV_NULL_OUTPUT_STREAM));
			 Encoder encoder = ESAPI.encoder();
			 switch(decFrom){
			 case DEC_URL:return encoder.decodeFromURL(item);
			 //case DEC_BASE64:return encoder.decodeFromBase64(item);
			 case DEC_HTML:return encoder.decodeForHTML(item);
			 }
			 throw exp.createApplicationException("invalid target decoding defintion");
		}
		catch(EncodingException ee){
			throw cast.toPageException(ee);
		}
		finally {
			 System.setOut(out);
		}
	}
	
	public static String call(PageContext pc , String strDecodeFrom, String value) throws PageException{
		short decFrom;
		strDecodeFrom=eng.getStringUtil().emptyIfNull(strDecodeFrom).trim().toLowerCase();
		if("url".equals(strDecodeFrom)) decFrom=DEC_URL;
		else if("html".equals(strDecodeFrom)) decFrom=DEC_HTML;
		else 
			throw exp.createFunctionException(pc, "ESAPIDecode", 1, "decodeFrom", "value ["+strDecodeFrom+"] is invalid, valid values are " +"[url,html]","");
		return decode(value, decFrom);
	}

	@Override
	public Object invoke(PageContext pc, Object[] args) throws PageException {
		if(args.length==2) return call(pc,cast.toString(args[0]),cast.toString(args[1]));
		throw exp.createFunctionException(pc, "ESAPIDecode", 2, 2, args.length);
	}
	
}