component extends="org.lucee.cfml.test.LuceeTestCase" labels="guard" {

    function run( testResults , testBox ) {
        describe( title = "Testcase for sanitizeHTML function", body = function() {
            it( title = "checking sanitizeHTML() function", body = function( currentSpec ) {
                var html = '<!DOCTYPE html><html><body><h2>HTML Forms</h2><form action="/action_page.cfm"><label for="fname">First name:</label><br><input type="text" id="fname" name="fname"value="Pothys"><br></body></html>';
                
                var res=SanitizeHtml(html);
                res=reReplace(res, "\s", "", "all");
                expect(res).toBe('<h2>HTML Forms</h2>First name:<br /><br />');

                var res=html.SanitizeHtml();
                res=reReplace(res, "\s", "", "all");
                expect(res).toBe('<h2>HTML Forms</h2>First name:<br /><br />'); // member function
            });
        });
    }
}
