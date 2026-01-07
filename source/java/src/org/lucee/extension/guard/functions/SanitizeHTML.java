package org.lucee.extension.guard.functions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;

public final class SanitizeHTML extends FunctionSupport {

	public static final Safelist POLICY_ALL_BUILTIN = createAllBuiltin();

	// Create a static settings object to reuse
	private static final Document.OutputSettings NO_PRETTY_PRINT = new Document.OutputSettings().prettyPrint(false);

	private static Safelist createAllBuiltin() {
		Safelist sl = Safelist.relaxed();
		sl.addTags("table", "thead", "tbody", "tfoot", "tr", "th", "td");
		sl.addTags("style");
		return sl;
	}

	public static String sanitize(PageContext pc, String unsafeHtml, Safelist policy) throws PageException {
		return Jsoup.clean(unsafeHtml, "", policy, NO_PRETTY_PRINT);
	}

	public static String sanitize(PageContext pc, String unsafeHtml, String policies) throws PageException {
		policies = policies.replace(" ", "").toUpperCase();
		Safelist sl = new Safelist();

		if (policies.contains("FORMATTING")) {
			sl.addTags("b", "em", "i", "strong", "u", "strike", "sub", "sup");
		}
		if (policies.contains("BLOCKS")) {
			sl.addTags("p", "div", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "li", "blockquote", "br", "hr");
		}
		if (policies.contains("STYLES")) {
			sl.addTags("style");
			sl.addAttributes(":all", "style");
		}
		if (policies.contains("LINKS")) {
			sl.addTags("a");
			sl.addAttributes("a", "href", "title", "rel");
			sl.addProtocols("a", "href", "http", "https", "mailto", "ftp");
		}
		if (policies.contains("TABLES")) {
			sl.addTags("table", "thead", "tbody", "tfoot", "tr", "th", "td", "caption", "colgroup", "col");
		}
		if (policies.contains("IMAGES")) {
			sl.addTags("img");
			sl.addAttributes("img", "src", "alt", "title", "width", "height");
			sl.addProtocols("img", "src", "http", "https");
		}

		return sanitize(pc, unsafeHtml, sl);
	}

	@Override
	public Object invoke(PageContext pc, Object[] args) throws PageException {
		if (args.length == 1) {
			return sanitize(pc, cast.toString(args[0]), POLICY_ALL_BUILTIN);
		}
		if (args.length == 2) {
			if (args[1] instanceof Safelist) {
				return sanitize(pc, cast.toString(args[0]), (Safelist) args[1]);
			}
			return sanitize(pc, cast.toString(args[0]), cast.toString(args[1]));
		}
		throw exp.createFunctionException(pc, "SanitizeHTML", 1, 1, args.length);
	}
}