package com.lib;


import java.util.PriorityQueue;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.apache.log4j.Logger;
import com.quixey.db.Utils;

public class APIBase {
	
	public static Logger logger = Logger.getLogger(APIBase.class.getName());
	public static PriorityQueue<String> failTestStatus = new PriorityQueue<String>();
	public static boolean debugTurnOn =false;
	public String errorMessages=null;
	public int errorCount=0;
	
	@BeforeTest(groups = { "abstract"})
	public void initFramework() throws Exception {
		logger.info("Invoked Method ");
		logger.info("before test empty error queue");
		failTestStatus.clear();
		
	}


    public static boolean assertEqual(Boolean bExp, Boolean bFound, String sMsg) throws Exception{
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
    
    public static boolean assertEqual(Boolean bExp, Boolean bFound) throws Exception {
    	return assertEqual(bExp, bFound, null);
    }  
    
    public static boolean assertEqual(String sExp, String sFound, String sMsg) throws Exception {
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
     public static boolean assertEqual(int sExp, int sFound, String sMsg) throws Exception {
            if (sExp==sFound) {
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
    
    public static boolean assertEqual(String sExp, String sFound) throws Exception {
    	return assertEqual(sExp, sFound, null);
    }
    
    public static void analyzeTestResult(String TestCaseName)
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
    public static String truncateString (String str, int length)
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
	
	
	
}
