package com.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.genericlib.ExcelUtility;
import com.genericlib.GlobalSetup;

public class HTTPClientLib extends JSONUtil{

	public HttpClient HttpClient = null;
	public HttpGet HttpGet = null;

	public static HttpClient ConnectHTTPClient() {

		HttpClient httpClient = new DefaultHttpClient();
		return httpClient;
	}

	public static HttpGet HTTPGetObj(HashMap<String, String> hashmap, String url) {

		HttpGet httpGetRequest = new HttpGet(addQueryParamstoUrl(hashmap,url));
		return httpGetRequest;
	}

	public static String addQueryParamstoUrl(HashMap<String, String> hashmap,
			String url) {

		List<NameValuePair> params = new LinkedList<NameValuePair>();
		Set<Entry<String, String>> set = hashmap.entrySet();
		Iterator<Entry<String, String>> i = set.iterator();

		if (!url.endsWith("?"))
			url += "?";

		// Display elements
		while (i.hasNext()) {
			Entry<String, String> me = i.next();
			switch (me.getKey().toString()) {

			case "q":
				params.add(new BasicNameValuePair("q", me.getValue().toString()));
				break;

			case "partner_id":
				params.add(new BasicNameValuePair("partner_id", me.getValue()
						.toString()));
				break;

			case "partner_secret":
				params.add(new BasicNameValuePair("partner_secret", me
						.getValue().toString()));
				break;

			case "custom_id":
				params.add(new BasicNameValuePair("custom_id", me.getValue()
						.toString()));
				break;

			case "limit":
				params.add(new BasicNameValuePair("limit", me.getValue()
						.toString()));
				break;

			case "include_platforms":
				params.add(new BasicNameValuePair("include_platforms", me
						.getValue().toString()));
				break;

			case "ais":
				params.add(new BasicNameValuePair("ais", me.getValue()
						.toString()));
				break;

			case "include_screenshots":
				params.add(new BasicNameValuePair("include_screenshots", me
						.getValue().toString()));
				break;

			case "include_descriptions":
				params.add(new BasicNameValuePair("include_descriptions", me
						.getValue().toString()));
				break;

			case "include_snippets":
				params.add(new BasicNameValuePair("include_snippets", me
						.getValue().toString()));
				break;

			case "edition_limit":
				params.add(new BasicNameValuePair("edition_limit", me
						.getValue().toString()));
				break;

			case "locale":
				params.add(new BasicNameValuePair("locale", me.getValue()
						.toString()));
				break;

			case "geoloc":
				params.add(new BasicNameValuePair("geoloc", me.getValue()
						.toString()));
				break;

			case "user_agent":
				params.add(new BasicNameValuePair("user_agent", me.getValue()
						.toString()));
				break;

			case "result_sources_dynamic":
				params.add(new BasicNameValuePair("result_sources_dynamic", me
						.getValue().toString()));
				break;

			case "result_sources_static":
				params.add(new BasicNameValuePair("result_sources_static", me
						.getValue().toString()));
				break;

			case "filter_empty_results":
				params.add(new BasicNameValuePair("filter_empty_results", me
						.getValue().toString()));
				break;

			case "dv_criteria":
				params.add(new BasicNameValuePair("dv_criteria", me.getValue()
						.toString()));
				break;
				
			case "stores":
				params.add(new BasicNameValuePair("stores", me.getValue()
						.toString()));
				break;	
			}

		}

		String paramString = URLEncodedUtils.format(params, "utf-8");
		url += paramString;

		Log.info("url :: " + url);

		return url;
	}

	public static int gethttpresponse(HashMap<String, String> hashmap,
			String url, boolean isarray, String ElementName) throws Exception {

		int statuscode = 0;

		try {

			HttpResponse httpResponse = ConnectHTTPClient().execute(
					HTTPGetObj(hashmap, url));

			System.out.println("All Header::"
					+ httpResponse.getAllHeaders().toString());
			System.out.println("Value ::"
					+ httpResponse.getEntity().getContentType().getValue());

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(httpResponse.getEntity().getContent())));

			int httpstatuscode = httpResponse.getStatusLine().getStatusCode();

			System.out.println("HTTP Status code" + httpstatuscode);

			String output;
			System.out.println("Output from Server .... \n");

			while ((output = br.readLine()) != null) {

				System.out.println("JSON Response" + output);
				JSONObject myObject = new JSONObject(output);

				if (isarray == false) {

					System.out.println("Key:: "
							+ myObject.get(ElementName).toString());
					statuscode = (int) myObject.get(ElementName);
				} else if (isarray == true) {

					JSONArray funcURLsarray;
					funcURLsarray = myObject.getJSONArray(ElementName);

					System.out.println(funcURLsarray.length());

					if (funcURLsarray.length() == 0) {
						statuscode = 0;
						break;
					} else if (funcURLsarray.length() > 0) {

						// ***************Check funcurl contains expected
						// function name************************

						for (int j = 0; j < funcURLsarray.length(); j++) {

							System.out.println(funcURLsarray.get(j));

						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return statuscode;
	}

	/**
	 * This method is used to validate the output JSON response for the given
	 * query.
	 * 
	 * @param hashmap
	 * @param url
	 * @param queryRowNum
	 * @return
	 * @throws Exception
	 */
	public static int verifyDynamicJSONResponse(
			HashMap<String, String> hashmap, String url, int queryRowNum)
			throws Exception {

		int statusCode = 0;
		String output;

		try {

			HttpResponse httpResponse = ConnectHTTPClient().execute(
					HTTPGetObj(hashmap, url));

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(httpResponse.getEntity().getContent())));

			int httpstatuscode = httpResponse.getStatusLine().getStatusCode();

			Log.info("httpstatuscode :: " + httpstatuscode);

			if (HttpStatus.SC_OK == httpstatuscode) {

				while ((output = br.readLine()) != null) {

					JSONObject jsonResponse = new JSONObject(output);

					if (jsonResponse != null) {

						verifyDataAndWriteToExcel(jsonResponse, queryRowNum);

					}
				}
			} else {
				Log.info("Invalid httpstatuscode :: " + httpstatuscode);
				statusCode = -1;
			}
		} catch (Exception e) {

			Log.error("Error while executing verifyDynamicJSONResponse :: "
					+ e.getMessage());
		}

		return statusCode;

	}

	/**
	 * @param hashmap
	 * @param url
	 * @param queryRowNum
	 * @return
	 * @throws Exception
	 */
	public static int verifyStaticJSONResponse(HashMap<String, String> hashmap,
			String url, int queryRowNum) throws Exception {

		int statusCode = 0;
		String output;

		try {

			HttpResponse httpResponse = ConnectHTTPClient().execute(
					HTTPGetObj(hashmap, url));

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(httpResponse.getEntity().getContent())));

			int httpstatuscode = httpResponse.getStatusLine().getStatusCode();

			Log.info("httpstatuscode :: " + httpstatuscode);

			if (HttpStatus.SC_OK == httpstatuscode) {

				while ((output = br.readLine()) != null) {

					// Log.info("JSON Response :: " + output);

					JSONObject jsonResponse = new JSONObject(output);

					if (!jsonResponse.isNull("funcUrls")) {

						validateStaticFunction(queryRowNum, jsonResponse);

					} else {

						Log.info("No funcUrls found for the givey search.");

						return -1;
					}
				}
			} else {

				Log.info("Invalid response code :: " + httpstatuscode);
				return -1;
			}

		} catch (Exception e) {

			Log.error("Error while executing verifyStaticJSONResponse :: "
					+ e.getMessage());
		}
		return statusCode;

	}

	/**
	 * This method is used to validate elements of the static query response.
	 * 
	 * @param queryRowNum
	 * @param jsonResponse
	 */
	private static void validateStaticFunction(int queryRowNum,
			JSONObject jsonResponse) {
		String appId = null;
		boolean appNameFlag = false;
		int appNameCount = 0;
		boolean funcURLExecuted = false;

		try {

			JSONArray funcURLJSONArray = JSONUtil.extractJSONArray(
					jsonResponse, "funcUrls");

			if (funcURLJSONArray != null && funcURLJSONArray.length() > 0) {

				for (int i = 0; i < funcURLJSONArray.length(); i++) {

					JSONObject funcURLObject = funcURLJSONArray
							.getJSONObject(i);

					String funcURL = funcURLObject.getString("funcUrl");

					if (funcURL == null && StringUtils.isEmpty(funcURL)) {

						com.genericlib.ExcelUtility.writeDataInExcel(
								GlobalSetup.excelFilePath,
								GlobalSetup.excelFileName,
								GlobalSetup.excelSheetNo, queryRowNum, 4,
								"FAILED:FuncURL is not empty");

					} else {

						String appName = com.genericlib.ExcelUtility.readDataFromExcel(GlobalSetup.excelFilePath,GlobalSetup.excelFileName,GlobalSetup.excelSheetNo, queryRowNum,0);

						String funcName = com.genericlib.ExcelUtility.readDataFromExcel(GlobalSetup.excelFilePath,GlobalSetup.excelFileName,GlobalSetup.excelSheetNo, queryRowNum,1);

						validateStaticFuncURL(queryRowNum, funcURL,
								funcURLExecuted, appName, funcName);
						appId = funcURLJSONArray.getJSONObject(i)
								.getString("appId");

						validateStaticStates(queryRowNum, appName, funcName,
								appId, appNameFlag, appNameCount,
								funcURLJSONArray, i);
						
						validateFuncURLStateAccessMethod(queryRowNum, funcURLObject);		
						validateFuncURLStateBindingPath(queryRowNum, funcURLObject);
						
						
						
						validateStaticAppStateContent(queryRowNum, jsonResponse, appId);
					}

				}

			}
		} catch (Exception e) {

			Log.error("Error while executing validateStaticFunction :: "
					+ e.getMessage());
		}

	}

	private static void validateStaticStates(int queryRowNum, JSONObject funcURLObject) {
				
		try {
			
			JSONArray staticStatesArray = JSONUtil.extractJSONArray(
					funcURLObject, "static_states");
			if (staticStatesArray != null && staticStatesArray.length() > 0) {

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 11, "PASSED");

				/*for (int j = 0; j < staticStatesArray.length(); j++) {
					
					JSONArray stateAccessMethodsArray = JSONUtil.extractJSONArray(
							staticStatesObject, "stateAccessMethods");
				}*/
			}
			
		} catch (Exception e) {
			
		}
	}

	/**
	 * This method is used to validate validateStaticStates element of the
	 * output JSON response.
	 * 
	 * @param queryRowNum
	 * @param appName
	 * @param funcName
	 * @param appId
	 * @param appNameFlag
	 * @param appNameCount
	 * @param funcURLJSONArray
	 * @param i
	 * @return
	 */
	private static void validateStaticStates(int queryRowNum, String appName,
			String funcName, String appId, boolean appNameFlag,
			int appNameCount, JSONArray funcURLJSONArray, int i) {

		boolean staticFURLExecuted = false;
		try {

			JSONArray staticStatesArray = JSONUtil.extractJSONArray(
					funcURLJSONArray.getJSONObject(i), "static_states");

			if (staticStatesArray != null && staticStatesArray.length() > 0) {

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 11, "PASSED");

				for (int j = 0; j < staticStatesArray.length(); j++) {

					JSONObject staticStatesObject = JSONUtil.extractJSONObject(
							staticStatesArray, j);
					
					validateStaticFurl(queryRowNum, appName, funcName, appId,
							appNameFlag, appNameCount, staticStatesObject, staticFURLExecuted);

				}
			} else {
				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 11, "FAILED: static_states is empty");
			}
		} catch (Exception e) {
			Log.error("Error while executing validateStaticStates :: "
					+ e.getMessage());
		}

	}

	/**
	 * @param queryRowNum
	 * @param staticStatesObject
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	private static void validateStaticStateStateAccessMethods(int queryRowNum,
			JSONObject staticStatesObject) throws JSONException,
			Exception {
		JSONArray stateAccessMethodsArray = JSONUtil.extractJSONArray(
				staticStatesObject, "stateAccessMethods");

		if (stateAccessMethodsArray != null
				&& stateAccessMethodsArray.length() > 0) {

			ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
					GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
					queryRowNum, 14, "PASSED");

			for (int k = 0; k < stateAccessMethodsArray.length(); k++) {
				String accessURL = JSONUtil.extractJSONObject(
						stateAccessMethodsArray, k).getString("accessUrl");

				validateAccessURL(queryRowNum, accessURL);

				validateEditions(queryRowNum, stateAccessMethodsArray, k);

			}

		} else {

			ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
					GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
					queryRowNum, 14, "FAILED: stateAccessMethods are empty");
		}
	}

	/**
	 * @param queryRowNum
	 * @param staticStatesObject
	 * @throws JSONException
	 * @throws Exception
	 */
	private static void validateStaticStateStateBindingPath(int queryRowNum,
			JSONObject staticStatesObject) throws JSONException, Exception {
		String staticStatesStateBindingPath = staticStatesObject
				.getString("stateBindingPath");

		if (staticStatesStateBindingPath == null
				|| StringUtils.isEmpty(staticStatesStateBindingPath)) {

			ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
					GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
					queryRowNum, 12, "FAILED:stateBindingPath is null");
		} else {

			ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
					GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
					queryRowNum, 12, staticStatesStateBindingPath);
		}
	}

	/**
	 * @param queryRowNum
	 * @param appName
	 * @param funcName
	 * @param appId
	 * @param appNameFlag
	 * @param appNameCount
	 * @param staticStatesObject
	 * @param staticFURLExecuted2 
	 */
	private static boolean validateStaticFurl(int queryRowNum, String appName,
			String funcName, String appId, boolean appNameFlag,
			int appNameCount, JSONObject staticStatesObject, boolean staticFURLExecuted) {
		
		try {
			String staticFurl = staticStatesObject.getString("staticFurl");

			boolean staticFurlAppNameFlag = isValidAppName(appName, staticFurl);

			boolean staticFurlFuncNameFlag = isValidFuncName(funcName,
					staticFurl);

			writeStaticFURL(queryRowNum, staticFurl, staticFurlAppNameFlag,
					staticFURLExecuted, staticFurlFuncNameFlag, appId, staticStatesObject);
		} catch (Exception e) {
			Log.error("Error while executing validateStaticFurl :: "
					+ e.getMessage());
		}
		
		return staticFURLExecuted;
	}

	/**
	 * @param queryRowNum
	 * @param myObject
	 * @param appId
	 * @throws JSONException
	 * @throws Exception
	 */
	private static void validateStaticAppStateContent(int queryRowNum,
			JSONObject myObject, String appId) {

		try {

			JSONObject appStateContentJSONObject = JSONUtil.extractJSONObject(
					myObject, "appStateContent");

			JSONObject editionsObject = JSONUtil.extractJSONObject(
					appStateContentJSONObject, "editions");

			if (editionsObject.toString().contains(appId)) {
				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 13, "PASSED");
			} else {
				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 13, "FAILED");
			}
		} catch (Exception e) {
			Log.error("Error while executing validateStaticAppStateContent :: "
					+ e.getMessage());
		}
	}

	/**
	 * This method is used to validate editions element of the output JSON
	 * response.
	 * 
	 * @param queryRowNum
	 * @param stateAccessMethodsJSONArray
	 * @param k
	 */
	private static void validateEditions(int queryRowNum,
			JSONArray stateAccessMethodsJSONArray, int k) {

		try {

			String editions = JSONUtil.extractJSONObject(
					stateAccessMethodsJSONArray, k).getString("editions");

			if (editions == null || StringUtils.isEmpty(editions)) {
				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 7, "FAILED: editions is empty array");
			} else {

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 7, "PASSED");
			}
		} catch (Exception e) {

			Log.error("Error while executing validateEditions :: "
					+ e.getMessage());
		}
	}

	/**
	 * This method is used to verify and write accessUrl to the execl sheet.
	 * 
	 * @param queryRowNum
	 * @param accessURL
	 */
	private static void validateAccessURL(int queryRowNum, String accessURL) {

		try {
			if (accessURL == null || StringUtils.isEmpty(accessURL)) {

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 5, "FAILED : accessUrl is null or empty");
			} else {

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 5, accessURL);
			}
		} catch (Exception e) {
			Log.error("Error while executing validateAccessURL :: "
					+ e.getMessage());
		}
	}

	/**
	 * This method is used to validateStateContent element of the output JSON
	 * response.
	 * 
	 * @param queryRowNum
	 * @param staticStatesObject
	 */
	private static void validateStateContent(int queryRowNum,
			JSONObject staticStatesObject) {

		boolean staticStatesFailed;

		try {
			if (!JSONUtil.isJSONObjectFormat(staticStatesObject.getJSONObject(
					"stateContent").toString())) {
				staticStatesFailed = true;

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 8, "FAILED:stateContent is null or empty");
			} else {
				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 8, "PASSED");
			}
		} catch (Exception e) {
			Log.error("Error while executing validateStateContent :: "
					+ e.getMessage());
		}
	}

	/**
	 * @param queryRowNum
	 * @param funcURLObject
	 * @throws JSONException
	 * @throws Exception
	 */
	private static void validateFuncURLStateBindingPath(int queryRowNum,
			JSONObject funcURLObject) {

		try {
			String funcURLStateBindingPath = funcURLObject
					.getString("stateBindingPath");

			if (funcURLStateBindingPath == null
					|| StringUtils.isEmpty(funcURLStateBindingPath)) {
				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 10, "PASSED");
			} else {
				ExcelUtility
						.writeDataInExcel(GlobalSetup.excelFilePath,
								GlobalSetup.excelFileName,
								GlobalSetup.excelSheetNo, queryRowNum, 10,
								"FAILED:stateBindingPath is not empty");
			}
		} catch (Exception e) {
			Log.error("Error while executing validateFuncURLStateBindingPath :: "
					+ e.getMessage());
		}
	}

	/**
	 * This method is used to validate stateAccessMethods inside single func url
	 * of the output JSON response.
	 * 
	 * @param queryRowNum
	 * @param funcURLObject
	 */
	private static void validateFuncURLStateAccessMethod(int queryRowNum,
			JSONObject funcURLObject) {

		try {

			JSONArray stateAccessMethodsArray = funcURLObject
					.getJSONArray("stateAccessMethods");

			if (stateAccessMethodsArray == null
					|| stateAccessMethodsArray.length() == 0) {

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 9, "PASSED");
			} else {

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 9,
						"FAILED:stateAccessMethods is not empty");
			}
		} catch (Exception e) {
			Log.error("Error while executing validateFuncURLStateAccessMethod :: "
					+ e.getMessage());
		}
	}

	/**
	 * This method is used to write the func url from the output JSON to the
	 * excel sheet.
	 * 
	 * @param queryRowNum
	 * @param staticFurl
	 * @param appNameFlag
	 * @param funcURLExecuted
	 * @param funcNameFlag
	 */
	private static boolean writeFuncURL(int queryRowNum, String funcURL,
			boolean appNameFlag, boolean funcURLExecuted, boolean funcNameFlag) {
		try {
			if (!funcURLExecuted) {

				if (appNameFlag && funcNameFlag) {

					ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
							GlobalSetup.excelFileName,
							GlobalSetup.excelSheetNo, queryRowNum, 4, funcURL);

					funcURLExecuted = true;
				}
			}
		} catch (Exception e) {
			Log.error("Error while executing writeFuncURL :: " + e.getMessage());
		}

		return funcURLExecuted;
	}

	/**
	 * This method is used write staticFurl to the excel sheet.
	 * 
	 * @param queryRowNum
	 * @param staticFurl
	 * @param appNameFlag
	 * @param funcURLExecuted
	 * @param funcNameFlag
	 * @param appId
	 * @param staticStatesObject 
	 */
	private static void writeStaticFURL(int queryRowNum, String staticFurl,
			boolean appNameFlag, boolean staticFURLExecuted,
			boolean funcNameFlag, String appId, JSONObject staticStatesObject) {

		try {
			if (!staticFURLExecuted) {

				if (appNameFlag && funcNameFlag) {

					ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
							GlobalSetup.excelFileName,
							GlobalSetup.excelSheetNo, queryRowNum, 15,
							staticFurl);

					staticFURLExecuted = true;

					if (appId != null || StringUtils.isNotEmpty(appId)) {
						ExcelUtility
								.writeDataInExcel(GlobalSetup.excelFilePath,
										GlobalSetup.excelFileName,
										GlobalSetup.excelSheetNo, queryRowNum,
										6, appId);
					}
					
					
					//to be add code here
					
					validateStaticStateStateAccessMethods(queryRowNum, staticStatesObject);
					

					validateStaticStateStateBindingPath(queryRowNum,
							staticStatesObject);

					validateStateContent(queryRowNum, staticStatesObject);
					
					
				}
			}
		} catch (Exception e) {
			Log.error("Error while executing :: writeStaticFURL "
					+ e.getMessage());
		}
	}

	/**
	 * @param funcName
	 * @param staticFurl
	 * @return
	 */
	private static boolean isValidFuncName(String funcName, String staticFurl) {
		boolean funcNameFlag = staticFurl.contains(funcName.trim()) ? true
				: false;
		return funcNameFlag;
	}

	/**
	 * @param appName
	 * @param staticFurl
	 * @param appNameFlag
	 * @param appNameCount
	 * @return
	 */
	private static boolean isValidAppName(String appName, String staticFurl) {

		boolean appNameFlag = false;
		int appNameCount = 0;
		if (appName.trim().split(" ").length > 1) {
			String[] appNames = appName.trim().split(" ");

			for (String app : appNames) {
				if (staticFurl.contains(app)) {
					appNameCount++;
				}
			}

			if (appNames.length == appNameCount) {
				appNameFlag = true;
			}

		} else {

			if (staticFurl.contains(appName.trim())) {
				appNameFlag = true;
			}

		}
		return appNameFlag;
	}

	/**
	 * @param queryRowNum
	 * @param funcURL
	 * @param validFuncNamd
	 * @param validAppName
	 * @throws Exception
	 */
	private static boolean validateStaticFuncURL(int queryRowNum,
			String funcURL, boolean funcURLExecuted, String appName,
			String funcName) {

		try {

			boolean validAppName = isValidAppName(appName, funcURL);

			boolean validFuncName = isValidFuncName(funcName, funcURL);

			writeFuncURL(queryRowNum, funcURL, validAppName, funcURLExecuted,
					validFuncName);
		} catch (Exception e) {
			Log.error("Error while executing validateStaticFuncURL :: "
					+ e.getMessage());
		}

		return funcURLExecuted;
	}

	/**
	 * This method is used to verify the content of the output JSON as per
	 * validation criteria.
	 * 
	 * @param myObject
	 *            output json object for the FASTAPI query
	 * @param queryRowNum
	 *            rowNumber of the target Excel Sheet to be updated.
	 * @throws JSONException
	 * @throws IOException
	 * @throws Exception
	 */
	private static void verifyDataAndWriteToExcel(JSONObject jsonResponse,
			int queryRowNum) throws JSONException, IOException, Exception {

		boolean appNameFlag = false;
		boolean funcURLExecuted = false;
		int appNameCount = 0;

		String appName = com.genericlib.ExcelUtility.readDataFromExcel(
				GlobalSetup.excelFilePath, GlobalSetup.excelFileName,
				GlobalSetup.excelSheetNo, queryRowNum, 0);

		String funcName = com.genericlib.ExcelUtility.readDataFromExcel(
				GlobalSetup.excelFilePath, GlobalSetup.excelFileName,
				GlobalSetup.excelSheetNo, queryRowNum, 1);

		JSONArray funcURLJSONArray = JSONUtil.extractJSONArray(jsonResponse,
				"funcUrls");

		validateResponse(jsonResponse, queryRowNum, appNameFlag,
				funcURLExecuted, appNameCount, appName, funcName,
				funcURLJSONArray);
	}

	/**
	 * This method is used to validate the JSON response as per validation
	 * criteria.
	 * 
	 * @param jsonResponse
	 * @param queryRowNum
	 * @param appNameFlag
	 * @param funcURLExecuted
	 * @param appNameCount
	 * @param appName
	 * @param funcName
	 * @param funcURLJSONArray
	 */
	private static void validateResponse(JSONObject jsonResponse,
			int queryRowNum, boolean appNameFlag, boolean funcURLExecuted,
			int appNameCount, String appName, String funcName,
			JSONArray funcURLJSONArray) throws JSONException, Exception {

		try {
			if (funcURLJSONArray != null && funcURLJSONArray.length() > 0) {

				for (int i = 0, size = funcURLJSONArray.length(); i < size; i++) {

					JSONObject funcURLObject = (JSONObject) funcURLJSONArray
							.get(i);

					String actualFuncURL = funcURLObject.getString("funcUrl");

					Log.info("funcUrl from response :: " + actualFuncURL);

					appNameFlag = isValidAppName(appName, actualFuncURL);

					boolean funcNameFlag = isValidFuncName(funcName,
							actualFuncURL);

					if (!funcURLExecuted) {

						if (appNameFlag && funcNameFlag) {

							ExcelUtility.writeDataInExcel(
									GlobalSetup.excelFilePath,
									GlobalSetup.excelFileName,
									GlobalSetup.excelSheetNo, queryRowNum, 4,
									actualFuncURL);

							validateFuncURL(actualFuncURL, funcURLObject,
									jsonResponse, queryRowNum);
							funcURLExecuted = true;
						}
					}
				}
			} else {
				Log.info("Invalid funcUrls array from response.");
				return;
			}
		} catch (Exception e) {

			Log.error("Error while executig validateResponse " + e.getMessage());
		}
	}

	/**
	 * This method is used to validate the func url as per the requirement.
	 * 
	 * @param actualFuncURL
	 *            funcURL to be verify
	 * @param objectInArray
	 * @param myObject
	 *            output JSON object
	 * @param queryRowNum
	 *            rowNumber of the target Excel Sheet.
	 * @param appIdStr
	 *            appId of type String
	 * @param editionId
	 *            editionId of type String
	 * @throws Exception
	 */
	private static void validateFuncURL(String actualFuncURL,
			JSONObject funcURLObject, JSONObject jsonResponse, int queryRowNum)
			throws Exception {

		String editionId = null;
		String appIdStr = null;
		JSONObject editionsJSONObject = null;

		validatesStaticStates(funcURLObject, queryRowNum);

		// Validate stateAccessMethods

		boolean stateAccessMethodsEmpty = false;

		JSONArray stateAccessMethodsJSONArray = JSONUtil.extractJSONArray(
				funcURLObject, "stateAccessMethods");

		appIdStr = funcURLObject.getString("appIdStr");

		Log.info("appIdStr :: " + appIdStr);

		if (stateAccessMethodsJSONArray != null
				&& stateAccessMethodsJSONArray.length() == 0) {

			ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
					GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
					queryRowNum, 14, "FAILED: stateAccessMethods are empty");

			stateAccessMethodsEmpty = true;

			ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
					GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
					queryRowNum, 13, "FAILED");

		} else {

			for (int i = 0; i < stateAccessMethodsJSONArray.length(); i++) {

				JSONObject stateAccessMethodsObject = stateAccessMethodsJSONArray
						.getJSONObject(i);

				Log.info("stateAccessMethodsObject ::"
						+ stateAccessMethodsObject);

				editionsJSONObject = validateStateAccessMethods(queryRowNum,
						appIdStr, editionsJSONObject, stateAccessMethodsObject);
				Log.info("editionsJSONObject ::" + editionsJSONObject);
			}
		}

		validateStateBindingPath(funcURLObject, queryRowNum);

		if (!stateAccessMethodsEmpty) {

			validateAppStateContent(jsonResponse, queryRowNum, appIdStr,
					editionsJSONObject);
		}

	}

	/**
	 * This method is used to validatesStaticStates JSON element from the output
	 * JSON response for search query.
	 * 
	 * @param funcURLObject
	 * @param queryRowNum
	 * @throws JSONException
	 * @throws Exception
	 */
	private static void validatesStaticStates(JSONObject funcURLObject,
			int queryRowNum) {

		try {

			JSONArray staticStatesJSONArray = JSONUtil.extractJSONArray(
					funcURLObject, "static_states");

			Log.info("staticStatesJSONArray :: " + staticStatesJSONArray);

			if (staticStatesJSONArray != null
					&& staticStatesJSONArray.length() == 0) {

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 11, "PASSED");
			} else {
				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 11, "FAILED:static_states is not empty");
			}
		} catch (Exception e) {

			Log.error("Error while executing validatesStaticStates :: "
					+ e.getMessage());
		}
	}

	/**
	 * This method is used to validateStateAccessMethods JSON element from the
	 * output JSON response for search query.
	 * 
	 * @param queryRowNum
	 * @param appIdStr
	 * @param editionsJSONObject
	 * @param stateAccessMethodsObject
	 * @return stateAccessMethods as a JSON object.
	 */
	private static JSONObject validateStateAccessMethods(int queryRowNum,
			String appIdStr, JSONObject editionsJSONObject,
			JSONObject stateAccessMethodsObject) {

		try {

			String accessUrl = JSONUtil.extractString(stateAccessMethodsObject,
					"accessUrl");

			ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
					GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
					queryRowNum, 5, accessUrl);

			JSONArray editionsJSONArray = JSONUtil.extractJSONArray(
					stateAccessMethodsObject, "editions");

			Log.info("editions array :: " + editionsJSONArray);

			if (editionsJSONArray != null && editionsJSONArray.length() > 0) {

				editionsJSONObject = writeEditionElementsToExcel(queryRowNum,
						appIdStr, editionsJSONObject, editionsJSONArray);
			}

		} catch (Exception e) {

			Log.error("Error while executing validateStateAccessMethods :: "
					+ e.getMessage());
		}

		return editionsJSONObject;
	}

	/**
	 * This method is used to write individual elements of editions array to
	 * excel from JSON response.
	 * 
	 * @param queryRowNum
	 * @param appIdStr
	 * @param editionsJSONObject
	 * @param editionsJSONArray
	 * @return editions array as JSON
	 */
	private static JSONObject writeEditionElementsToExcel(int queryRowNum,
			String appIdStr, JSONObject editionsJSONObject,
			JSONArray editionsJSONArray) {

		try {
			for (int j = 0; j < editionsJSONArray.length(); j++) {
				editionsJSONObject = editionsJSONArray.getJSONObject(j);

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 6, appIdStr);

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 7,
						editionsJSONObject.getString("packageName"));

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 8,
						editionsJSONObject.getString("maxVersion"));

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 9,
						editionsJSONObject.getString("minVersion"));

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 10,
						editionsJSONObject.getString("editionId"));
			}

		} catch (Exception e) {
			Log.error("Error while executing writeEditionElementsToExcel "
					+ e.getMessage());
		}

		return editionsJSONObject;

	}

	/**
	 * This method is used to validateStateBindingPath JSON element from the
	 * output JSON response for search query.
	 * 
	 * @param funcURLObject
	 * @param queryRowNum
	 */
	private static void validateStateBindingPath(JSONObject funcURLObject,
			int queryRowNum) {

		try {
			String stateBindingPath = funcURLObject
					.getString("stateBindingPath");
			if (stateBindingPath != null) {
				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 12, stateBindingPath);
			} else {

				ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
						GlobalSetup.excelFileName, GlobalSetup.excelSheetNo,
						queryRowNum, 12, "FAILED: stateBindingPath is null");
			}
		} catch (Exception e) {

			Log.error("Error while executing validateStateBindingPath :: "
					+ e.getMessage());
		}
	}

	/**
	 * This method is used to validate validateAppStateContent JSON element from
	 * the output JSON response for search query.
	 * 
	 * @param jsonObject
	 * @param queryRowNum
	 * @param appIdStr
	 * @param editionsObject
	 */
	private static void validateAppStateContent(JSONObject jsonObject,
			int queryRowNum, String appIdStr, JSONObject editionJsonObject) {

		try {
			JSONObject appStateContent = JSONUtil.extractJSONObject(jsonObject,
					"appStateContent");

			Log.info("appStateContent :: " + appStateContent);

			String[] keys = JSONObject.getNames(appStateContent);

			for (String key : keys) {

				JSONObject edition = (JSONObject) appStateContent.opt(key);
				JSONObject appStateContentEditionId = (JSONObject) edition
						.getJSONObject(editionJsonObject.getString("editionId"));
				if (appStateContentEditionId.toString().contains(appIdStr)) {
					ExcelUtility
							.writeDataInExcel(GlobalSetup.excelFilePath,
									GlobalSetup.excelFileName,
									GlobalSetup.excelSheetNo, queryRowNum, 13,
									"PASSED");
					break;
				} else {
					ExcelUtility.writeDataInExcel(GlobalSetup.excelFilePath,
							GlobalSetup.excelFileName,
							GlobalSetup.excelSheetNo, queryRowNum, 13,
							"FAILED: AppId not found in AppStateContent");
					break;
				}

			}
		} catch (Exception e) {
			Log.error("Error while executing validateAppStateContent :: "
					+ e.getMessage());
		}
	}

	public static String gethttpresponse1(HashMap<String, String> hashmap,
			String url, String ElementName) throws Exception {

		String statuscode = null;

		try {

			HttpResponse httpResponse = ConnectHTTPClient().execute(
					HTTPGetObj(hashmap, url));

			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(httpResponse.getEntity().getContent())));

			int httpstatuscode = httpResponse.getStatusLine().getStatusCode();

			System.out.println("HTTP Status code" + httpstatuscode);

			String output;
			System.out.println("Output from Server .... \n");

			while ((output = br.readLine()) != null) {

				System.out.println("JSON Response" + output);
				JSONObject myObject = new JSONObject(output);

				System.out.println("Key:: "
						+ myObject.get(ElementName).toString());
				statuscode = (String) myObject.get(ElementName);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return statuscode;
	}

	public static void main(String[] args) {

		String appName = "google express";
		String funcName = "searchProduct";
		String funcURL = "func://express.google.com/searchProduct/toy";
		String[] appNames = appName.trim().split(" ");
		System.out.println(appNames.length);
		boolean appCountFlag = false;
		int appCount = 0;
		for (String app : appNames) {
			if (funcURL.contains(app)) {
				appCount++;
			}
		}

		if (appNames.length == appCount) {
			appCountFlag = true;
		}

		if (appCountFlag && funcURL.contains(funcName.trim())) {
			System.out.println("Write FuncURL");
		} else {
			System.out.println("Do not Write FuncURL");
		}

	}
	
	//**************************************************New functions*************************
	
	public static int gethttpresponsecode(HashMap<String, String> hashmap,
			String url) throws Exception {


		int httpresponsecode=0;		

		try {

			HttpResponse httpResponse = ConnectHTTPClient().execute(
					HTTPGetObj(hashmap, url));			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(httpResponse.getEntity().getContent())));
			httpresponsecode = httpResponse.getStatusLine().getStatusCode();

			System.out.println("HTTP Status code" + httpresponsecode);					

		} catch (Exception e) {
			e.printStackTrace();
		}

		return httpresponsecode;
	}
	
}
