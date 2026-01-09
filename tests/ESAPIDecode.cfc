component extends="org.lucee.cfml.test.LuceeTestCase" labels="esapi" {
	function run( testResults , testBox ) {
		describe( "test case for guardDecode", function() {
			it(title = "Checking guardDecode with decodeFrom='html'", body = function( currentSpec ) {
				var inputString = "<script>";
				enc = guardEncode('html', inputString);
				dec = guardDecode("html", enc);
				assertEquals(dec, inputString);
			});
			it(title = "Checking guardDecode with decodeFrom='url'", body = function( currentSpec ) {
				var inputUrl = "https://download.lucee.org";
				enc = guardEncode('url', inputUrl);
				dec = guardDecode("url", enc);
				assertEquals(dec, inputUrl);
			});
		});
	}
}