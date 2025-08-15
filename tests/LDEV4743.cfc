component extends="org.lucee.cfml.test.LuceeTestCase" labels="esapi" {
	function run( testResults , testBox ) {
		describe( title='Testcase for LDEV-4743' , body=function() {

			it( title='Checking simple canonicalize()', body=function() {
				expect( canonicalize( "Lucee", false, false ) ).toBe( "Lucee" );
			});

			it( title='Checking canonicalize() function with complex string', body=function() {
				expect( canonicalize( "!@##$^&*()_+{}[]:"";''<>, .?/|\~`.", false, false ) ).toBe( "!@##$&*()_+{}[]:"";''<>, .?/|\~`." );
			});

			it( title='Checking canonicalize() function with % symbol', skip=true, body=function() {
				var str = '(80 - 100%).pdf'; // Only failed with the '%' symbol
				expect( canonicalize( str, false, false ) ).toBe( str );
			});
		});
	}
}
