package org.lucee.extension.esapi.functions;

import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;

public class EncodeForSQL extends FunctionSupport {

	private static final long serialVersionUID = 1533424366864717468L;

	public static String call(PageContext pc, String item, String dialect, boolean canonicalize) throws PageException {

		return ESAPIEncode.encode(item, ESAPIEncode.ENC_SQL, canonicalize, ESAPIEncode.toCodec(dialect));
	}

	public static String call(PageContext pc, String item, String dialect) throws PageException {
		return call(pc, item, dialect, false);
	}

	@Override
	public Object invoke(PageContext pc, Object[] args) throws PageException {
		if (args.length == 2) return call(pc, cast.toString(args[0]), cast.toString(args[1]));
		if (args.length == 3) return call(pc, cast.toString(args[0]), cast.toString(args[1]), cast.toBooleanValue(args[2]));
		throw exp.createFunctionException(pc, "EncodeForSQL", 2, 3, args.length);
	}
}