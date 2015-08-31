/*package com.quixey.common;


import java.util.PriorityQueue;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.apache.log4j.Logger;

import com.quixey.project.phoenix.fastapi.tests.FastApi_AS;



public class APIBase {
	
    public static Properties properties = PropertyLoader.getInstance().getProperties();
    public static PriorityQueue<String> failTestStatus = new PriorityQueue<String>();
    public static Logger logger = Logger.getLogger(APIBase.class.getName());
    public boolean debugTurnOn =false;
    public String errorMessages=null;
    public int errorCount=0;
    
    @BeforeTest(groups = { "abstract"})
	public void initFramework() throws Exception {
		logger.info("Invoked Method initFramework");
		logger.info("before test empty error queue");
		failTestStatus.clear();
	}
    
   // @AfterTest(groups = { "abstract"})
	public void analysis() throws Exception{
		Integer icount=0;
    	try {
    		logger.info("****************");
			logger.info("Analysis Results");
			logger.info("****************");
			logger.info("Total Failures:" + failTestStatus.size());
		if (!failTestStatus.isEmpty()) {  	
		     while (failTestStatus.size() != 0)
				{   
		    	 icount = icount+1;
		    	 errorMessages += "Verification:" + icount + " " + truncateString(failTestStatus.remove(),200) + "\n"; 
				}
		        logger.info(errorMessages);    
		     
		        org.testng.Assert.fail();
		  }
				
		} catch (Exception e) {
			//logger().warn(e.getMessage());
			e.printStackTrace();
		}
	}
    
    public static String sourceEditionAdd(String url, String sourceEditionJson) throws Exception {
		
		HttpClient cli = new HttpClient();
		String resp = cli.postJson(url,sourceEditionJson);
		//System.out.println("Source Edition Add Response: "+resp);
		
		return resp;
	}
    
    public String truncateString (String str, int length)
	{
		//this function allow you to limit how long the string could be, if debug is turn on
		if (!debugTurnOn)
		{
			if (str.length() > length)
			{
				str= str.substring(0, length);
			}	
		}

		return str;
	}
    
    public boolean assertEqual(Boolean bExp, Boolean bFound, String sMsg) throws Exception{
        if (bExp == bFound) {
        	if (sMsg != null) logger.info("Assert Passed => "+sMsg);
        	return true;
        } else {
        	if (sMsg != null) {
        		logger.info("Assert Failed => "+sMsg);
        	}else{
        		logger.info("Assert Failed => Error: NoMatch");
        	}
        	//logger.info("Expected: "+bExp);
        	//logger.info("Found:    "+bFound);
        	failTestStatus.add("Assert Failed => " + sMsg + " Expected: "+bExp + " But " + "Found:"+bFound);
      	 
      	  	return false;
        }
    }    
    
    public boolean assertEqual(Boolean bExp, Boolean bFound) throws Exception {
    	return assertEqual(bExp, bFound, null);
    }  
    
    public boolean assertEqual(String sExp, String sFound, String sMsg) throws Exception {
        if (sExp.equalsIgnoreCase(sFound)) {
        	if (sMsg != null) logger.info("Passed => "+sMsg);
        	return true;
        } else {
        	if (sMsg != null){
        		logger.info("*** Error: "+sMsg);
        	}else{
        		logger.info("*** Error: NoMatch");
        	}
        	//logger.info("Expected: "+sExp);
        	//logger.info("Found:    "+sFound);
        	failTestStatus.add("Assert Expected: "+sExp + " But " + "Found:"+sFound);
        	
        	return false;
        }
    }
    
    public boolean assertEqual(String sExp, String sFound) throws Exception {
    	return assertEqual(sExp, sFound, null);
    }
    
    public void analyzeTestResult(String TestCaseName)
	{   //this function is call to analyze if the test case's steps in execution and verification passed or not
		String errorMessages="";
		
		logger.info("****************");
		logger.info("Analysis Results");
		logger.info("****************");
		logger.info("Total Failures:" + failTestStatus.size());
		
		if (failTestStatus.size() == 0)
        {
			logger.info("TC: "+ TestCaseName + " PASSED.");
			Assert.assertTrue(true, "TC: "+ TestCaseName + " PASSED.");
        	
        }else if (!failTestStatus.isEmpty())
        {
        	logger.info ("TC: "+ TestCaseName + " FAILED");
        	 while (failTestStatus.size() != 0)
		        {   
        		 errorMessages += truncateString(failTestStatus.remove(),200) + "\n"; 
		        }
        	 logger.info(errorMessages);
        	 Assert.fail(TestCaseName + " FAILED");
        	 //note: the native assert function will hang Junit if you don't pass in resolvable object
        	 //org.junit.Assert.assertFalse("TC: "+ TestCaseName + " FAILED due to Exceptions OR bad locators:" + errorMessages, true);
        }
	   
	}
	
	
	
}
*/