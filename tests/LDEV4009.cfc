component extends="org.lucee.cfml.test.LuceeTestCase" labels="guard" {
	
	function run( testResults, testBox ) {
		describe("Testcase for LDEV-4009", function() {
			it( title="writeoutput() with encodefor = none", body=function( currentSpec ) {
				expect(trim(testfunc_writeoutput("none"))).toBe("brad <br> wood"); 
			});
			
			it( title="writeoutput() with encodefor = empty string", body=function( currentSpec ) {
				expect(trim(testfunc_writeoutput(""))).toBe("brad <br> wood");
			});

			it( title="writeoutput() with encodefor = html", body=function( currentSpec ) {
				expect(trim(testfunc_writeoutput("html"))).toBe("brad &lt;br&gt; wood");
			});

			it( title="cfoutput with encodefor = none", body=function( currentSpec ) {
				expect(trim(testfunc_cfoutput("none"))).toBe("brad <br> wood");
			});
			
			it( title="cfoutput with encodefor = empty string", body=function( currentSpec ) {
				expect(trim(testfunc_cfoutput(""))).toBe("brad <br> wood");
			});

			it( title="cfoutput with encodefor = html", body=function( currentSpec ) {
				expect(trim(testfunc_cfoutput("html"))).toBe("brad &lt;br&gt; wood");
			});
			
		});
	}
	
	private function testfunc_writeoutput(required string encodefor) {
		cfsavecontent(variable="result") {
				var name = "brad <br> wood" ;
				writeoutput(name,arguments.encodefor);
		}
		return result;
	}

	```
	<cffunction name="testfunc_cfoutput" access="private">
		<cfargument name="encodefor" type="string" required>
		<cfsavecontent variable="result">
			<cftry>
				<cfset var name = "brad <br> wood">
				<cfoutput encodeFor="#arguments.encodeFor#">
					#name#
				</cfoutput>
				<cfcatch>
					<cfoutput>#cfcatch.message#</cfoutput>
				</cfcatch>
			</cftry>
		</cfsavecontent>
		<cfreturn result>
	</cffunction>

	```
}