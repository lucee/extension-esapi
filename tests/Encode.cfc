component extends="org.lucee.cfml.test.LuceeTestCase" labels="guard" {

    function run( testResults , testBox ) {
        describe( title = "Guard: Contextual Encoding Suite", body = function() {
            
            // --- OWASP Java Encoder Targets ---

            it( "encodes for HTML", function() {
                var raw = '<b> "Test" & ''Check'' </b>';
                var expected = '&lt;b&gt; &quot;Test&quot; &amp; &#39;Check&#39; &lt;/b&gt;';
                expect( guardEncode(raw, "html") ).toBe( expected );
            });

            it( "encodes for HTML Attributes", function() {
                var raw = ' "><script>alert(1)</script>';
                // Attributes are encoded more aggressively than body HTML
                expect( guardEncode(raw, "html_attr") ).toInclude( "&quot;" );
                expect( guardEncode(raw, "html_attr") ).notToInclude( ">" );
            });

            it( "encodes for JavaScript", function() {
                var raw = "'; alert(1); var x='";
                // Should use hex/unicode escapes for quotes and semicolons
                var res = guardEncode(raw, "javascript");
                expect( res ).toInclude( "\x27" ); 
                expect( res ).notToInclude( "'" );
            });

            it( "encodes for CSS", function() {
                var raw = "background: url('javascript:alert(1)')";
                // CSS encoder escapes non-alphanumerics with backslashes/hex
                expect( guardEncode(raw, "css") ).toInclude( "\3a " ); 
            });

            it( "encodes for URL (URI Component)", function() {
                var raw = "John Doe & Sons/Company";
                expect( guardEncode(raw, "url") ).toBe( "John%20Doe%20%26%20Sons%2FCompany" );
            });

            it( "encodes for XML and XML Attributes", function() {
                var raw = '<test value="5"> & ';
                expect( guardEncode(raw, "xml") ).toInclude( "&lt;" );
                expect( guardEncode(raw, "xml_attr") ).toInclude( "&quot;" );
            });

            // --- CustomEncoder Targets ---

            it( "encodes for LDAP DN (Distinguished Name)", function() {
                var raw = "Doe, John ##123";
                // Should escape the leading # and the comma
                expect( guardEncode(raw, "dn") ).toBe( "\#Doe\, John \#123" );
            });

            it( "encodes for LDAP Search Filter", function() {
                var raw = "admin* (test)";
                // Asterisks and parens must be hex-escaped in filters
                expect( guardEncode(raw, "ldap") ).toBe( "admin\2a  \28test\29" );
            });

            it( "encodes for XPath", function() {
                var raw = " ' or 1=1 ";
                expect( guardEncode(raw, "xpath") ).toBe( " &#39; or 1=1 &#39; " );
            });

            it( "encodes for VBScript", function() {
                var raw = "alert!";
                // Custom VBScript encoder hex-escapes punctuation
                expect( guardEncode(raw, "vbscript") ).toInclude( "hex(21)" );
            });

            it( "encodes for SQL (Multi-Dialect)", function() {
                var raw = "O'Reilly";
                // Test Oracle/Standard (double quote)
                expect( guardEncode(raw, "sql", false, "oracle") ).toBe( "O''Reilly" );
                // Test MySQL (backslash)
                expect( guardEncode(raw, "sql", false, "mysql") ).toBe( "O\'Reilly" );
            });

            // --- Utility Flags ---

            it( "canonicalizes before encoding when requested", function() {
                // %253c is double encoded '<'
                var input = "%253cscript%253e";
                // If canonicalize=true, it should resolve to <script> then encode for HTML
                var res = guardEncode(input, "html", true);
                expect( res ).toBe( "&lt;script&gt;" );
            });

        });
    }
}