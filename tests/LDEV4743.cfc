component extends="org.lucee.cfml.test.LuceeTestCase" labels="guard" {
	function run( testResults , testBox ) {
		describe( title='Testcase for LDEV-4743' , body=function() {

			it( title='Checking simple canonicalize()', body=function() {
				expect( canonicalize( "Lucee", false, false ) ).toBe( "Lucee" );
			});

			it( title='Checking canonicalize() function with complex string', body=function() {
				expect( 
					canonicalize( "!@##$^&*()_+{}[]:"";''<>, .?/|\~`.", false, false ) )
					       .toBe( "!@##$^&*()_+{}[]:"";''<>, .?/|\~`." );
			});

			it( title='Checking canonicalize() function with % symbol', body=function() {
				var str = '(80 - 100%).pdf'; // Only failed with the '%' symbol
				expect( canonicalize( str, false, false ) ).toBe( str );
			});

			it( title='a trailing % is kept as is', body=function() {
				expect( canonicalize( "100%", false, false ) ).toBe( "100%" );
				expect( canonicalize( "100%", false, false, false ) ).toBe( "100%" );
				expect( canonicalize( "100%", false, false, true ) ).toBe( "100%" );
				expect( canonicalize( "100%", true, true, false ) ).toBe( "100%" );
			});

			it( title='a % followed by non hex characters is kept as is', body=function() {
				expect( canonicalize( "50% off", false, false ) ).toBe( "50% off" );
				expect( canonicalize( "a%zzb", false, false ) ).toBe( "a%zzb" );
				expect( canonicalize( "a%4", false, false ) ).toBe( "a%4" );
			});

			it( title='the old workaround (% replaced with %25) still works', body=function() {
				expect( canonicalize( "(80 - 100%25).pdf", false, false ) ).toBe( "(80 - 100%).pdf" );
			});

			it( title='valid escapes next to a bare % are still decoded', body=function() {
				expect( canonicalize( "%3Cb%3E 100%", false, false ) ).toBe( "<b> 100%" );
				expect( canonicalize( "%E2%82%AC 5%", false, false ) ).toBe( "#chr( 8364 )# 5%" );
			});

			it( title='escapes that are not valid UTF-8 are kept as is', body=function() {
				expect( canonicalize( "a%FFb", false, false ) ).toBe( "a%FFb" );
			});

			it( title='multiple encoding is still detected with a bare % in the input', body=function() {
				expect( function(){
					canonicalize( "%2525 100%", true, false, true );
				}).toThrow();
			});
		});
	}
}
