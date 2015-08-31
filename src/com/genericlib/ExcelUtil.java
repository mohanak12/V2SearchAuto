package com.genericlib;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExcelUtil {

	private static XSSFSheet sh;

	public static XSSFSheet getSh() {
		return sh;
	}

	public static void setSh(XSSFSheet sh) {
		ExcelUtil.sh = sh;
	}

	private static XSSFCell cell;
	private static XSSFRow Row;

	@SuppressWarnings("static-access")
	public static void setCellData(String Result, int RowNum, int ColNum)
			throws Exception {

		try {

			Row = sh.getRow(RowNum);

			cell = Row.getCell(ColNum, Row.RETURN_BLANK_AS_NULL);

			if (cell == null) {

				cell = Row.createCell(ColNum);

				cell.setCellValue(Result);
			}

			else {

				cell.setCellValue(Result);

			}

			// Constant variables Test Data path and Test Data file name

		} catch (Exception e) {

			throw (e);

		}

	}

}
