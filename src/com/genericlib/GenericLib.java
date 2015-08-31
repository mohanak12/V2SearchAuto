package com.genericlib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.lib.Log;

public class GenericLib {

	private static Properties props = new Properties();
	private static String fileName = "config.properties";
	private static String propertyFilePath = System.getProperty("user.dir")
			+ File.separator + "src" + File.separator + "configurations"
			+ File.separator + "config.properties";

	public static String getPropertyFromConfigFile(String property) {

		try {

			props.load(new FileInputStream(propertyFilePath));
			// Log.info(fileName + " loaded successfully.");
			return props.getProperty(property);

		} catch (Exception e) {
			Log.error("Error while loading" + fileName + "from"
					+ propertyFilePath);
		}

		return null;
	}

	public static void setPropertyAPItoTestinConfigFile(String value) {
		try {
			props.load(new FileInputStream(propertyFilePath));
			props.setProperty("APItoTest", value);
			OutputStream out = new FileOutputStream(propertyFilePath);
			props.store(out, "updated APIToTest param value");

		} catch (Exception e) {
			Log.error("Error executing setPropertyAPItoTestinConfigFile");
		}
	}

}