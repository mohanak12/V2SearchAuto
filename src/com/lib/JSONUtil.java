package com.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.genericlib.GenericLib;

/**
 * JSON object utility class
 * 
 */
public class JSONUtil extends APIBase{

	private static Logger logger = Logger.getLogger(JSONUtil.class.getName());
	
	/**
	 * Converts string to a JSONObject
	 * 
	 * @param value (String) : value to be converted
	 * @return JSONObject
	 * @throws JSONException 
	 */
	public static JSONObject toJSONObject(String value) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		if(isJSONObjectFormat(value)) {
			jsonObject = new JSONObject(value);
		} else {
			logger.error("String [" + value + "] is not in a JSONObject format.");
		}
		
		return jsonObject;
	}
	
	/**
	 * Converts a string to a JSONArray
	 * 
	 * @param value (String) : value to be converted
	 * @return JSONArray
	 * @throws JSONException 
	 */
	public static JSONArray toJSONArray(String value) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		
		if(isJSONArrayFormat(value)) {
			jsonArray = new JSONArray(value);
		} else {
			logger.error("String [" + value + "] is not in a JSONArray format.");
		}

		return jsonArray;
	}

	/**
	 * Extracts a JSON Object from the given JSON Object at the key specified.  The key can
	 * also contain ':' within it to indicate the hierarchy where the JSON Object can be found.
	 * 
	 * @param jo (JSONObject) : the JSON Object to extract the JSON Array from
	 * @param key (String) : the key or keys where to look for the JSON Array
	 * @return JSONObject : the extracted JSON Object or an empty one if the key(s) do not exist
	 * @throws JSONException 
	 */
	public static JSONObject extractJSONObject(JSONObject jo, String key) throws JSONException {
		JSONObject jo2 = new JSONObject();
		
		try {
			if(jo != null && key != null && key.contains(":")) {
				String[] keys = key.split(":");
				for(int i=0; i<keys.length; i++) {
					if(jo.has(keys[i])) {
						if(i == (keys.length - 1) && isJSONObjectFormat(jo.getString(keys[i]))) {
							jo2 = jo.getJSONObject(keys[i]);
						} else if(isJSONObjectFormat(jo.getString(keys[i]))) {
							jo = jo.getJSONObject(keys[i]);
						} else if(isJSONArrayFormat(jo.getString(keys[i]))) {
							logger.error("Key [" + keys[i] + "] points to a JSON Array.");
						} else {
							logger.error("Key [" + keys[i] + "] points to a non JSON Object.");
						}
					} else {
						logger.error("JSON Object does not contain key [" + keys[i] + "]");
					}
				}
			} else if(jo != null && key != null) {
				if(jo.has(key) && isJSONObjectFormat(jo.getString(key))) {
					jo2 = jo.getJSONObject(key);
				} else if(jo.has(key)) {
					logger.error("Key [" + key + "] points to a non-JSON Object.");
				} else {
					logger.error("JSON Object does not contain key [" + key + "]");
				}
			}
		} catch(NoSuchElementException e) {
			logger.warn(e.getMessage());
		}
		return jo2;
	}
	
	/**
	 * Extracts a JSON Object from the given JSON Array at the index specified.  If a JSON Object
	 * is not found at the index given, an empty JSON Object is returned.
	 * 
	 * @param ja (JSONArray) : the JSON Array to extract the JSON Object from
	 * @param index (int) : the index of the JSONArray where the JSONObject lies
	 * @return JSONObject : the extracted JSON Object or an empty one if the key(s) does not exist
	 * @throws JSONException 
	 */
	public static JSONObject extractJSONObject(JSONArray ja, int index) throws JSONException {
		JSONObject jo = new JSONObject();

		try {
			if(ja.optJSONObject(index) != null) {
				jo = ja.getJSONObject(index);
			}
			
		} catch(NoSuchElementException e) {
			logger.warn(e.getMessage());
		}
		
		return jo;
	}
	
	/**
	 * Extracts a JSON Array from the given JSON Object at the key specified.  The key can
	 * also contain ':' within it to indicate the hierarchy where the JSON Array can be found.
	 * 
	 * @param jo (JSONObject) : the JSON Object to extract the JSON Array from
	 * @param key (String) : the key or keys where to look for the JSON Array
	 * @return JSONArray : the extracted JSON Array or an empty one if the key(s) do not exist
	 * @throws JSONException 
	 */
	public static JSONArray extractJSONArray(JSONObject jo, String key) throws JSONException {
		JSONArray ja = new JSONArray();
		
		try {
			if(jo != null && key != null && key.contains(":")) {
				String[] keys = key.split(":");
				for(int i=0; i<keys.length; i++) {
					if(jo.has(keys[i])) {
						if(isJSONObjectFormat(jo.getString(keys[i]))) {
							jo = jo.getJSONObject(keys[i]);
						} else if(i == (keys.length - 1) && isJSONArrayFormat(jo.getString(keys[i]))) {
							ja = jo.getJSONArray(keys[i]);
						} else if(isJSONArrayFormat(jo.getString(keys[i]))) {
							logger.error("Key [" + keys[i] + "] points to a JSON Array, but it is not at the end of the key list.");
						} else {
							logger.error("Key [" + keys[i] + "] is not the last key and does not point to a JSON Object.");
						}
					} else {
						logger.error("JSON Object does not contain key [" + keys[i] + "]");
					}
				}
			} else if(jo != null && key != null) {
				if(jo.has(key) && isJSONArrayFormat(jo.getString(key))) {
					ja = jo.getJSONArray(key);
				} else if(jo.has(key)) {
					logger.error("Key [" + key + "] points to a non-JSON Array.");
				} else {
					logger.error("JSON Object does not contain key [" + key + "]");
				}
			}

		} catch (NoSuchElementException e) {
			logger.warn(e.getMessage());
		}
		return ja;
	}
	
	/**
	 * Extracts a String from the given JSON Object at the key specified.  The key can
	 * also contain ':' within it to indicate the hierarchy where the String can be found.
	 * 
	 * @param jo (JSONObject) : the JSON Object to extract the String from
	 * @param key (String) : the key or keys where to look for the String
	 * @return String : the extracted String or an empty one if the key(s) do not exist
	 * @throws JSONException 
	 */
	public static String extractString(JSONObject jo, String key) throws JSONException {
		
		String val = "";
		try {
			if(jo != null && key != null && key.contains(":")) {
				String[] keys = key.split(":");
				for(int i=0; i<keys.length; i++) {
					if(jo.has(keys[i])) {
						if(isJSONObjectFormat(jo.getString(keys[i]))) {
							jo = jo.getJSONObject(keys[i]);
						} else if(i == (keys.length - 1)) {
							val = jo.optString(keys[i]);
						}
					} else {
						logger.error("JSON Object does not contain key [" + keys[i] + "]");
					}
				}
			} else if(jo != null && key != null) {
				if(jo.has(key)) {
					val = jo.optString(key);
				} else {
					logger.error("JSON Object does not contain key [" + key + "]");
				}
			}		

		} catch(NoSuchElementException e) {
			logger.warn(e.getMessage());
		}
		
		return val;
	}
	
	
	/**
	 * Extracts a String from the given JSON Array at the index specified.
	 * 
	 * @param ja (JSONArray) : the JSON Array to extract the String from
	 * @param index (int) : the index where to look for the String
	 * @return String : the extracted String or an empty one if the index does not exist
	 * @throws JSONException 
	 */
	public static String extractString(JSONArray ja, int index) throws JSONException {
		
		String val = "";
		try {
			if(ja != null && index >= 0) {
				val = ja.getString(index);
			} else if(index < 0) {
				logger.error("Cannot extract string from JSONArray, index is less than zero. [" + index + "]");
			} else {
				logger.error("Cannot extract string from JSONArray, JSONObject is null.");
			}

		} catch(NoSuchElementException e) {
			logger.warn(e.getMessage());
		}
		
		return val;
	}
	
	/**
	 * Checks if the String given is in a JSONObject format and can be converted
	 *  
	 * @param val (String) : value to check
	 * @return boolean
	 */
	public static boolean isJSONObjectFormat(String val) {
		return (val != null && val.trim().startsWith("{") && val.trim().endsWith("}") ? true : false);
	}

	/**
	 * Checks if the BufferedReader starts with {
	 * 
	 * @param br (BufferedReader) : Buffered Reader to check
	 * @return boolean
	 */
	public static boolean isJSONObjectFormat(BufferedReader br) {
		boolean answer = false;
		String line;
		try {
			if( (line = br.readLine()) != null ) {
				answer = (line.startsWith("{") ? true : false);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		return answer;
	}

	/**
	 * Checks if the InputStream starts with a {
	 * 
	 * @param is (InputStream) : Input Stream to check
	 * @return boolean
	 */
	public static boolean isJSONObjectFormat(InputStream is) {
		boolean answer = false;
		try {
			answer = isJSONObjectFormat(new BufferedReader(new InputStreamReader(is, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
		}
		return answer;
	}	
	/**
	 * Checks if the String given is in a JSONArray format and can be converted
	 * 
	 * @param val (String) : value to check
	 * @return boolean
	 */
	public static boolean isJSONArrayFormat(String val) {
		return (val != null && val.trim().startsWith("[") && val.trim().endsWith("]") ? true : false);
	}
	
	/**
	 * Checks if the BufferedReader starts with [
	 * 
	 * @param br (BufferedReader) : Buffered Reader to check
	 * @return boolean
	 */
	public static boolean isJSONArrayFormat(BufferedReader br) {
		boolean answer = false;
		String line;
		try {
			if( (line = br.readLine()) != null ) {
				answer = (line.startsWith("[") ? true : false);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		return answer;
	}

	/**
	 * Checks if the InputStream starts with a [
	 * 
	 * @param is (InputStream) : Input Stream to check
	 * @return boolean
	 */
	public static boolean isJSONArrayFormat(InputStream is) {
		boolean answer = false;
		try {
			answer = isJSONArrayFormat(new BufferedReader(new InputStreamReader(is, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
		}
		return answer;
	}
	/**
	 * This method checks the JSONObject and returns false if any keys are not present in the object;
	 * true otherwise
	 *  
	 * @param keys (String[]) : Keys to look for
	 * @param jo (JSONObject) : JSONObject to search
	 * @return boolean : true/false if all keys are present
	 */
	public static boolean areKeysPresent(String[] keys, JSONObject jo) {
		boolean present = true;

		if(jo != null && jo.length() > 0 && keys != null && keys.length > 0) {
			for (String key : keys) {
				if (!jo.has(key)) {
					present = false;
					break;
				}
			}
		} else {
			present = false;
		}
		
		return present;
	}
	
	/**
	 * This method checks the JSONObject and returns false if any keys are not present in the object or
	 * if any of the keys' values are null/empty; true otherwise
	 *  
	 * @param keys (String[]) : Keys to look for
	 * @param jo (JSONObject) : JSONObject to search
	 * @return boolean : true/false if all keys are present
	 * @throws JSONException 
	 */
	public static boolean areKeysPopulated(String[] keys, JSONObject jo) throws JSONException {
		boolean present = true;

		try {
			if(jo != null && jo.length() > 0 && keys != null && keys.length > 0) {
				for (String key : keys) {
					if (!jo.has(key) || jo.isNull(key) || String.valueOf(jo.get(key)).isEmpty()) {
						if(!jo.has(key)) {
							logger.info("Failed => areKeysPopulated : Could not find key [" + key + "]");
							//failTestStatus.add("Failed => areKeysPopulated : Could not find key [" + key + "]");
						} else if(jo.isNull(key)) {
							logger.info("Failed => areKeysPopulated : Key [" + key + "] is null");
							//failTestStatus.add("Failed => areKeysPopulated : Key [" + key + "] is null");
						} else if(String.valueOf(jo.get(key)).isEmpty()) {
							logger.info("Failed => areKeysPopulated : Key [" + key + "] is empty");
							//failTestStatus.add("Failed => areKeysPopulated : Key [" + key + "] is empty");
						}
						present = false;
					}
				}
			} else {
				logger.error("Either key set or JSON Object is null or empty.");
				//failTestStatus.add("Failed => Either key set or JSON Object is null or empty");
				present = false;
			}

		} catch(NoSuchElementException e) {
			logger.warn(e.getMessage());
			//failTestStatus.add("Failed => Try Catch Exception : " + e.toString());
			present = false;
		}
		
		return present;
	}
	
	/**	 This method checks the HttpResponse and returns JSONObject; 
	 * @param httpResponse
	 */
	public JSONObject getJsonObject(HttpResponse httpResponse) throws JSONException, IOException{
		String resp = "";
		JSONObject jo = null;
		BufferedReader br = new BufferedReader(
				new InputStreamReader((httpResponse.getEntity().getContent())));
		while ((resp = br.readLine()) != null) {

			jo = new JSONObject(resp);
		}	
		return jo;
	}
}