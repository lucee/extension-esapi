component extends="org.lucee.cfml.test.LuceeTestCase" labels="esapi"{
	function run( testResults , testBox ) {
		describe( "test case for esapiDecode", function() {
			it(title = "Checking esapiDecode with decodeFrom='html'", body = function( currentSpec ) {
				var inputString = "<script>";
				enc = esapiEncode('html', inputString);
				dec = esapiDecode("html", enc);
				assertEquals(dec, inputString);
			});
			it(title = "Checking esapiDecode with decodeFrom='url'", body = function( currentSpec ) {
				var inputUrl = "https://download.lucee.org";
				enc = esapiEncode('url', inputUrl);
				dec = esapiDecode("url", enc);
				assertEquals(dec, inputUrl);
			});
		});
	}
}