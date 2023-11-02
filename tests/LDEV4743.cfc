component extends="org.lucee.cfml.test.LuceeTestCase" labels="esapi" skip=true {
	function run( testResults , testBox ) {
		describe( title='Testcase for LDEV-4743' , body=function() {
			it( title='Checking canonicalize() function with % symbol' , body=function() {
				expect( canonicalize("Lucee", false, false) ).toBe( "Lucee" );
				expect( canonicalize("!@##$^&*()_+{}[]:"";''<>, .?/|\~`.", false, false) ).toBe( "!@##$^&*()_+{}[]:"";''<>, .?/|\~`." );
				expect( function() {
					var str = '(80 - 100%).pdf'; // Only failed with the '%' symbol
					canonicalize(str, false, false);
				}).notToThrow();
			});
		});
	}
}
