/**
 * 
 */
package com.lib;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.genericlib.GenericLib;

/**
 * @author thirupathi_maram
 *
 */
public class Log {

	
	private static Logger log = Logger.getLogger(Log.class);
	
	private static String dirPath = System.getProperty("user.dir");
    private static String configFilename = dirPath + GenericLib
			.getPropertyFromConfigFile("Logfilepath")+File.separator+"Logging.properties";
    
    static {
    	 PropertyConfigurator.configure(configFilename);
         
         System.out.println("loaded \t" + configFilename);
    }
    public Log() {
    	
                   
    }
    
    public static void info(String message)
    {
                    log.info(message);
    }
    public static void warn(String message)
    {
                    log.warn(message);
    }
    public static void error(String message)
    {
                    log.error(message);
    }
    public static void debug(String message)
    {
                    log.debug(message);
    }
    public static void fatal(String message)
    {
                    log.fatal(message);
    }

}
