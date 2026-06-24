component extends="org.lucee.cfml.test.LuceeTestCase" labels="guard" {
	function run( testResults , testBox ) {
		describe( "test case for decodeFromURL", function() {
			it(title = "Checking with decodeFromURL", body = function( currentSpec ) {
				var enc=EncodeForURL('<script>');
				assertEquals('%3Cscript%3E',enc);
				assertEquals('<script>',decodeFromURL(enc));
			});
		});	
	}
}