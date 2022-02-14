/**
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
 **/
package org.lucee.extension.esapi.functions;

import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;


public final class SanitizeHTML extends FunctionSupport {

    // FORMATTING, BLOCKS, STYLES, LINKS, TABLES, IMAGES
    public static final PolicyFactory POLICY_ALL_BUILTIN = Sanitizers.FORMATTING
        .and(Sanitizers.BLOCKS)
        .and(Sanitizers.STYLES)
        .and(Sanitizers.LINKS)
        .and(Sanitizers.TABLES)
        .and(Sanitizers.IMAGES);


	public static String sanitize(PageContext pc, String unsafeHtml, PolicyFactory policy) throws PageException {

        return policy.sanitize(unsafeHtml);
	}


	public static String sanitize(PageContext pc, String unsafeHtml, String policies) throws PageException {

        policies = policies.replace(" ", "").toUpperCase();

        PolicyFactory policy = new HtmlPolicyBuilder().toFactory();
        if (policies.contains("FORMATTING")) policy = policy.and(Sanitizers.FORMATTING);
        if (policies.contains("BLOCKS"))     policy = policy.and(Sanitizers.BLOCKS);
        if (policies.contains("STYLES"))     policy = policy.and(Sanitizers.STYLES);
        if (policies.contains("LINKS"))      policy = policy.and(Sanitizers.LINKS);
        if (policies.contains("TABLES"))     policy = policy.and(Sanitizers.TABLES);
        if (policies.contains("IMAGES"))     policy = policy.and(Sanitizers.IMAGES);

        return sanitize(pc, unsafeHtml, policy);
	}


    @Override
	public Object invoke(PageContext pc, Object[] args) throws PageException {

        if (args.length == 1) {
            return sanitize(pc, cast.toString(args[0]), POLICY_ALL_BUILTIN);
        }

        if (args.length == 2) {
            if (args[1] instanceof PolicyFactory) {
                return sanitize(pc, cast.toString(args[0]), (PolicyFactory)args[1]);
            }

            return sanitize(pc, cast.toString(args[0]), cast.toString(args[1]));
        }

		throw exp.createFunctionException(pc, "SanitizeHTML", 1, 1, args.length);
	}
}