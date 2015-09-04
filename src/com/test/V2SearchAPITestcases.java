package com.test;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.lib.HTTPClientLib;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import com.quixey.db.Utils;
import com.lib.APIBase;

public class V2SearchAPITestcases  {

	public String FastapiServerurl;
	private static Logger logger = Logger.getLogger(V2SearchAPITestcases.class);
	
	public static Logger getLogger() {
        return logger;
    }
	
	@BeforeClass
	public void setUp() throws IOException, InterruptedException 
	{	 				
		FastapiServerurl=com.genericlib.GenericLib.getPropertyFromConfigFile("FAST_API_SEARCH_SERVER")+com.genericlib.GenericLib.getPropertyFromConfigFile("V2SEARCH_Endpoint");
		getLogger().info("FastapiServerurl is "+FastapiServerurl);
	 }

	
	@Test (dataProvider = "stores")
	public void InvalidStoresTest(String storevalue, int expectedResult) throws Exception {
		
				
		HashMap<String, String> hm = new HashMap<String, String>();		

		hm.put("q", com.genericlib.GenericLib.getPropertyFromConfigFile("query"));
		hm.put("partner_secret",com.genericlib.GenericLib.getPropertyFromConfigFile("partner_secret"));
		hm.put("partner_id", com.genericlib.GenericLib.getPropertyFromConfigFile("partner_id"));
		hm.put("filter_empty_results",com.genericlib.GenericLib.getPropertyFromConfigFile("filter_empty_results"));
		hm.put("geoloc", com.genericlib.GenericLib.getPropertyFromConfigFile("geoloc"));					
		hm.put("result_sources_static", com.genericlib.GenericLib.getPropertyFromConfigFile("result_sources_dynamic"));						
		hm.put("dv_criteria", com.genericlib.GenericLib.getPropertyFromConfigFile("dv_criteria"));	
		hm.put("stores",storevalue );	
								
		int httpresponsecode=com.lib.HTTPClientLib.gethttpresponsecode(hm, FastapiServerurl);
		
		com.lib.APIBase.assertEqual(expectedResult, httpresponsecode,"Verify response code is coming as 400 for wrong store value");
		
		}
	

	@DataProvider(name = "stores")
    public static Object[][] testdata() {
	     return new Object[][] {{"test", 400}, {"2005", 400}, {"itune", 400}};
	  }
        
	}

	


