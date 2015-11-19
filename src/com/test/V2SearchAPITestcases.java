package com.test;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.lib.APIBase;

import com.genericlib.GenericLib;

public class V2SearchAPITestcases extends GenericLib {

	public String FastapiServerurl;
	private static Logger logger = Logger.getLogger(V2SearchAPITestcases.class);
	HashMap<String, String> hm = new HashMap<String, String>();		


	public static Logger getLogger() {
		return logger;
	}

	@BeforeClass
	public void setUp() throws IOException, InterruptedException 
	{	 	
		com.lib.APIBase.failTestStatus.clear();
		FastapiServerurl=getPropertyFromConfigFile("FAST_API_SEARCH_SERVER")+getPropertyFromConfigFile("V2SEARCH_Endpoint");
		getLogger().info("FastapiServerurl is "+FastapiServerurl);
		
	}

	//https://fastapi-v3.stage.quixey.com/v2/search?stores=[%22yelp%22]&log=0&q=apps&partner_secret=49stzavvrpqtevp7g9wd951e5f49h6mb&partner_id=47602789&disable_cache=1
	
	@Test (dataProvider = "stores", groups = { "V2Searchapi", "smoke", "abstract"})
	public void InvalidStoresTest(String storevalue, int expectedResult) throws Exception {

		hm.put("q", getPropertyFromConfigFile("query"));
		hm.put("partner_secret",getPropertyFromConfigFile("partner_secret"));
		hm.put("partner_id", getPropertyFromConfigFile("partner_id"));
		hm.put("filter_empty_results",getPropertyFromConfigFile("filter_empty_results"));
		hm.put("geoloc", getPropertyFromConfigFile("geoloc"));					
		hm.put("result_sources_static",getPropertyFromConfigFile("result_sources_dynamic"));						
		hm.put("dv_criteria", getPropertyFromConfigFile("dv_criteria"));	
		hm.put("stores",storevalue );	

		HttpResponse httpResponse = ConnectHTTPClient().execute(HTTPGetObj(hm, FastapiServerurl));
		JSONObject jo=	this.getJsonObject(httpResponse);

		assertEqual(300, jo.getInt("status"),"Verify response code is coming as 400 for wrong store value");
		analyzeTestResult(Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Test Case 16: platform_ids fail cases
    https://fastapi-v3.stage.quixey.com/v2/search?q=apps&partner_secret=74bt1vgb3xn239h62rb8s4vbddh845fa&adtrack=2005&partner_id=2409447768&platform_ids=['g']
    The cases in platform_id_fail_case should return the following response (or something close to it)
 	    platform_id_fail_case = ['["g"]', "string", "2005", "['itune']", "000000000000000000000", "0.12345", "Ã¦Â¸Â¸Ã¦Ë†ï¿½", "ÃŽÂµÃŽÂºÃŽÂ±Ã�â€žÃ�Å’", "mÃ¡Â»â„¢t trÃ„Æ’m"]

	 */
	@Test (dataProvider = "InValidPlatform", groups = { "V2Searchapi", "smoke", "abstract"})
	public void testInvalidPlatformIDWithInValidFormat(String platform_ids, int expectedResult) throws Exception {

		hm.put("q", getPropertyFromConfigFile("query"));
		hm.put("partner_secret",getPropertyFromConfigFile("partner_secret"));
		hm.put("partner_id", getPropertyFromConfigFile("partner_id"));
		hm.put("filter_empty_results",getPropertyFromConfigFile("filter_empty_results"));
		hm.put("geoloc", getPropertyFromConfigFile("geoloc"));					
		hm.put("result_sources_static", getPropertyFromConfigFile("result_sources_dynamic"));						
		hm.put("dv_criteria", getPropertyFromConfigFile("dv_criteria"));	
		hm.put("platform_ids",platform_ids );	

		HttpResponse httpResponse = ConnectHTTPClient().execute(HTTPGetObj(hm, FastapiServerurl));
		JSONObject jo=	this.getJsonObject(httpResponse);

		assertEqual(expectedResult, jo.getInt("status"),"Verify response code is coming as 400 for wrong platform");
		analyzeTestResult(Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 *  Test Case 17:  platform_id_no_results_case contains cases in a valid format but invalid ids so there should be no results
	 */
	@Test (dataProvider = "InValidWithValidFormat", groups = { "V2Searchapi", "smoke", "abstract"})
	public void testInvalidPlatformIDWithValidFormat(String platform_ids, int expectedResult) throws Exception {

		hm.put("q", getPropertyFromConfigFile("query"));
		hm.put("partner_secret",getPropertyFromConfigFile("partner_secret"));
		hm.put("partner_id", getPropertyFromConfigFile("partner_id"));
		hm.put("filter_empty_results",getPropertyFromConfigFile("filter_empty_results"));
		hm.put("geoloc", getPropertyFromConfigFile("geoloc"));					
		hm.put("result_sources_static", getPropertyFromConfigFile("result_sources_dynamic"));						
		hm.put("dv_criteria", getPropertyFromConfigFile("dv_criteria"));	
		hm.put("platform_ids",platform_ids );	

		HttpResponse httpResponse = ConnectHTTPClient().execute(HTTPGetObj(hm, FastapiServerurl));
		JSONObject jo=	this.getJsonObject(httpResponse);

		assertEqual(expectedResult,jo.getInt("status"),"Verify response code is coming as 200 for valid platform value");

		Assert.assertTrue(jo.isNull("funcUrls"),"Verify funcUrl does not exists in the Json.");
		analyzeTestResult(Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	@Test (dataProvider = "InValidWithValidFormat", groups = { "fastapi", "smoke", "abstract"})
	public void testInvalidStoreIDWithValidFormat(String storevalue, int expectedResult) throws Exception {

		hm.put("q", getPropertyFromConfigFile("query"));
		hm.put("partner_secret",getPropertyFromConfigFile("partner_secret"));
		hm.put("partner_id", getPropertyFromConfigFile("partner_id"));
		hm.put("filter_empty_results",getPropertyFromConfigFile("filter_empty_results"));
		hm.put("geoloc", getPropertyFromConfigFile("geoloc"));					
		hm.put("result_sources_static", getPropertyFromConfigFile("result_sources_dynamic"));						
		hm.put("dv_criteria", getPropertyFromConfigFile("dv_criteria"));	
		hm.put("stores",storevalue );	

		HttpResponse httpResponse = ConnectHTTPClient().execute(HTTPGetObj(hm, FastapiServerurl));
		JSONObject jo=	this.getJsonObject(httpResponse);

		assertEqual(expectedResult, jo.getInt("status"),"Verify response code is coming as 200 for invalid store id with valid JSON format value");

		Assert.assertTrue(jo.isNull("funcUrls"),"Verify funcUrl does not exists in the Json.");
		analyzeTestResult(Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	The q parameter contains the API's query text. It is required.
    Test Case 28: q Fail Cases:
    https://fastapi-v3.stage.quixey.com/v2/search?q=&partner_id=47602789&partner_secret=rhu1n9wvw69ekz7atwr6nf9rbgu6drpb
    Cases with query set to q_fail_case should return a fail case..
	 */
	@Test
	public void testVerifyMissingQueryParameter() throws Exception {

		hm.put("partner_secret",getPropertyFromConfigFile("partner_secret"));
		hm.put("partner_id", getPropertyFromConfigFile("partner_id"));

		HttpResponse httpResponse = ConnectHTTPClient().execute(HTTPGetObj(hm, FastapiServerurl));

		JSONObject jo=	this.getJsonObject(httpResponse);

		String ActualMsg = com.lib.JSONUtil.extractString(jo, "msg");

		// Verify that HTTP status msg  and status 400 are returned after hitting api with Missing param: q
		assertEqual("Missing param: q",	ActualMsg,
				"Verify HTTP status msg is returned after hitting api with incorrect Geo location "); 

		assertEqual(400, jo.getInt("status"),"Verify HTTP status 400 is returned after hitting api with Missing param: q");
		analyzeTestResult(Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 *  Test Case 29: q Valid Cases:
    The following queries for q should return results.
	 * @param queryValue
	 */
	@Test (dataProvider = "query")
	public void testVerifyValidQueryParameter(String queryValue) throws Exception {

		hm.put("partner_secret",getPropertyFromConfigFile("partner_secret"));
		hm.put("partner_id", getPropertyFromConfigFile("partner_id"));
		hm.put("q", queryValue);

		int httpresponsecode=gethttpresponsecode(hm, FastapiServerurl);
		//Assert.assertEquals(200, httpresponsecode,"Verify HTTP status 200 is returned after hitting api with valid param: q");
		assertEqual(200, httpresponsecode,"Verify HTTP status 200 is returned after hitting api with valid param: q");
		analyzeTestResult(Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	@DataProvider(name = "query")
	public static Object[][] testQuerydata() {
		return new Object[][] {{"thai food"}, {"indian delivery food"}, {"mexican"}};
	}


	@DataProvider(name = "stores")
	public static Object[][] testdata() {
		return new Object[][] {{"test", 400}, {"2005", 400}, {"itune", 400}};
	}

	@DataProvider(name = "InValidPlatform")
	public static Object[][] testInValidPlatformData() {
		return new Object[][] {{"['g']", 400}, {"2005", 400}, {"0.12345", 400}};
	}
	@DataProvider(name = "InValidWithValidFormat")
	public static Object[][] testInValidPlatformForValidData() {
		return new Object[][] {{"[22222]", 200}, {"[12345]", 200}};

	}
}




