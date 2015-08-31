package com.genericlib;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.genericlib.ExcelUtility;
import com.genericlib.GenericLib;
import com.lib.Log;

public class GlobalSetup {

	public static String Mainexcelfilepath;
	public static String excelFilePath;
	public static String excelFileName;
	public static int excelSheetNo;
	public static int rowCount;

	public static void setGlobalData() throws IOException {

		try {
			String filePath = System.getProperty("user.dir");

			Mainexcelfilepath = filePath
					+ GenericLib.getPropertyFromConfigFile("Mainexcelfilepath");

			excelFilePath = filePath
					+ GenericLib.getPropertyFromConfigFile("excelfilepath");
			excelFileName = GenericLib
					.getPropertyFromConfigFile("CommonExcelfilename");

			File src = new File(Mainexcelfilepath + excelFileName);
			File target = new File(excelFilePath + excelFileName);

			Files.copy(src.toPath(), target.toPath(),
					StandardCopyOption.REPLACE_EXISTING);

			switch (GenericLib.getPropertyFromConfigFile("APItoTest")) {

			
			case "FastApi_StaticApps":
				excelSheetNo = 0;
				break;
			case "FastApi_DynamicApps":
				excelSheetNo = 1;
				break;	
			
			}

			rowCount = ExcelUtility.getRowCount(GlobalSetup.excelFilePath,
					GlobalSetup.excelFileName, GlobalSetup.excelSheetNo);

		} catch (Exception e) {

			Log.error("Error while executing setGlobalData " + e.getMessage());
		}
	}

}
