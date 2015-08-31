package com.genericlib;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {
	
	private static FileInputStream fs;
	private static XSSFWorkbook wb;

	
public static void Addcolumninexcel(String excelfilepath,String excelfilename,int sheetNum) throws IOException
{
	
	fs = new FileInputStream(excelfilepath + excelfilename);
	wb = new XSSFWorkbook(fs);	
	
	try 
	{
		
		XSSFSheet sheet = wb.getSheetAt(sheetNum);
		Cell cell = null;
		cell = sheet.getRow(0).createCell(2, Cell.CELL_TYPE_STRING);
		
		Cell cell1 = null;
		cell1 = sheet.getRow(0).createCell(3, Cell.CELL_TYPE_STRING);
		
		cell.setCellValue("Actual Result");	
		cell1.setCellValue("Status");
				
		FileOutputStream fileOut = new FileOutputStream(excelfilepath + excelfilename);

		wb.write(fileOut);
		fileOut.close();
		
	} catch (Exception e) {		
		e.printStackTrace();
	}
}

public static int GetSheetNumFromexcel(String excelfilepath,String excelfilename,String SheetName) throws IOException
{
	
	fs = new FileInputStream(excelfilepath + excelfilename);
	wb = new XSSFWorkbook(fs);	
	
	int sheetNum = 0;
	String SheetName1;
	
	try 
	{
		
		for (int i = 0; i < wb.getNumberOfSheets(); i++) 
		{		

			System.out.println("Sheet at " + "[" + i + "]"
					+ wb.getSheetAt(i).getSheetName());
			
			SheetName1=wb.getSheetAt(i).getSheetName();
			if(SheetName1==SheetName)
			{
				sheetNum=i;
				break;
			}
		}	
		
	} catch (Exception e) {		
		e.printStackTrace();
	}
	
	return sheetNum;
}

public static void writeDataInExcel(String excelfilepath,String excelfilename,int SheetNum,int rownum,int colnum,String responsedata) throws Exception
{
	
	fs = new FileInputStream(excelfilepath + excelfilename);
	wb = new XSSFWorkbook(fs);

	XSSFSheet sheet = wb.getSheetAt(SheetNum);		
		
	Cell cell= null;
	cell = sheet.getRow(rownum).createCell(colnum, Cell.CELL_TYPE_STRING);
		
	cell.setCellValue(responsedata);
	
	FileOutputStream fileOut = new FileOutputStream(excelfilepath + excelfilename);
	wb.write(fileOut);
	fs.close();
	fileOut.close();
}

public static void WritedatainExcel1(String excelfilepath,String excelfilename,int SheetNum,int rownum,int colnum,int responsedata) throws Exception
{
	
	fs = new FileInputStream(excelfilepath + excelfilename);
	wb = new XSSFWorkbook(fs);

	XSSFSheet sheet = wb.getSheetAt(SheetNum);		
		
	Cell cell= null;
	cell = sheet.getRow(rownum).createCell(colnum, Cell.CELL_TYPE_NUMERIC);
		
	cell.setCellValue(responsedata);
	
	FileOutputStream fileOut = new FileOutputStream(excelfilepath + excelfilename);

	wb.write(fileOut);
	fileOut.close();
}		
	

public static String readDataFromExcel(String excelfilepath,String excelfilename,int SheetNum,int rownum,int colno) throws IOException{
	
	fs = new FileInputStream(excelfilepath + excelfilename);

	wb = new XSSFWorkbook(fs);
	String cellValue =  "";
	
	XSSFSheet sheet = wb.getSheetAt(SheetNum);
	Cell cell = null;
	cell = sheet.getRow(rownum).getCell(colno);
	 try{
		// System.out.print(cell.getStringCellValue());
		 cellValue=cell.getStringCellValue();
	 }
	 catch (Exception e) {		
		e.printStackTrace();
	}
	 
     fs.close();
	return cellValue;
    	
		
	}
public static double ReaddatafromExcelbyRowCol(String excelfilepath,String excelfilename,int SheetNum,int rownum,int colnum) throws IOException{
	
	fs = new FileInputStream(excelfilepath + excelfilename);

	wb = new XSSFWorkbook(fs);
	double cellValue = 0;
	
	XSSFSheet sheet = wb.getSheetAt(SheetNum);
	Cell cell = null;
	cell = sheet.getRow(rownum).getCell(colnum);
	 try{
		 System.out.print(cell.getNumericCellValue());
		 cellValue=(double) cell.getNumericCellValue();
	 }
	 catch (Exception e) {		
		e.printStackTrace();
	}
	 
     fs.close();
	return cellValue;
    	
		
	}

public static int getRowCount(String excelfilepath,String excelfilename,int SheetNum) throws IOException{
	int rowcnt = 0;
	
	fs = new FileInputStream(excelfilepath + excelfilename);

	wb = new XSSFWorkbook(fs);
		
	XSSFSheet sheet = wb.getSheetAt(SheetNum);
	rowcnt= sheet.getLastRowNum();    
    
    fs.close();
    return rowcnt+1;	
	 
	}
}





/*Iterator<Row> rowIterator = sheet.iterator();
while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
     
    //For each row, iterate through each columns
    Iterator<Cell> cellIterator = row.cellIterator();
            
   	 Cell cell = cellIterator.next();              
        switch(cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                System.out.print(cell.getBooleanCellValue() + "\t\t");
                break;
            case Cell.CELL_TYPE_NUMERIC:
                System.out.print(cell.getNumericCellValue() + "\t\t");
                break;
            case Cell.CELL_TYPE_STRING:
                System.out.print(cell.getStringCellValue());
                break;
        
    }
    System.out.println("");
}*/



