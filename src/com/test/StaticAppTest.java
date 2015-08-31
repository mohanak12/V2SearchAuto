package com.test;

import java.util.HashMap;

import org.eclipse.jetty.util.log.Log;
import org.testng.annotations.Test;

import com.genericlib.GenericLib;
import com.genericlib.GlobalSetup;
import com.lib.HTTPClientLib;

public class StaticAppTest {

	@Test
	public void validQueryTest() throws Exception {

		String FastapiServerurl;
		
		FastapiServerurl=com.genericlib.GenericLib.getPropertyFromConfigFile("FAST_API_SEARCH_SERVER")+com.genericlib.GenericLib.getPropertyFromConfigFile("V2SEARCH_Endpoint");

		GenericLib.setPropertyAPItoTestinConfigFile("FastApi_StaticApps");

		GlobalSetup.setGlobalData();

		HashMap<String, String> hm = new HashMap<String, String>();
		
		System.out.println("Rowcount" + GlobalSetup.rowCount);
		
		for (int i = 1; i < GlobalSetup.rowCount; i++) {

			hm.put("q", com.genericlib.ExcelUtility.readDataFromExcel(
					GlobalSetup.excelFilePath, GlobalSetup.excelFileName,
					GlobalSetup.excelSheetNo, i, 2));
			hm.put("partner_secret", com.genericlib.GenericLib
					.getPropertyFromConfigFile("partner_secret"));
			hm.put("partner_id", com.genericlib.GenericLib
					.getPropertyFromConfigFile("partner_id"));
			hm.put("filter_empty_results", com.genericlib.GenericLib
					.getPropertyFromConfigFile("filter_empty_results"));
			hm.put("geoloc", com.genericlib.ExcelUtility.readDataFromExcel(
					GlobalSetup.excelFilePath, GlobalSetup.excelFileName,
					GlobalSetup.excelSheetNo, i, 3));			
					
			hm.put("result_sources_static", com.genericlib.GenericLib
					.getPropertyFromConfigFile("result_sources_static"));		
						
			hm.put("dv_criteria", com.genericlib.GenericLib
					.getPropertyFromConfigFile("dv_criteria"));		
			
					
			int status = HTTPClientLib.verifyStaticJSONResponse(hm,FastapiServerurl, i);
			
			Log.info("status response ::" + status);

		}

	}

}
