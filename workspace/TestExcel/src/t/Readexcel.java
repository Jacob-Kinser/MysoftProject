package t;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList; 
import java.io.IOException;  
import java.util.Iterator;  
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.xssf.usermodel.XSSFSheet;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.util.Scanner;

public class Readexcel {
	String sheetName;
	
	String buildingCol;
	int buildingNum;
	
	String closetCol;
	int closetNum;
	
	String jackCol;
	int jackNum;
	
	String roomCol;
	int roomNum;
	
	String noteCol;
	int noteNum;
	
	int start;
	int stop;
	
	String path;
	FileInputStream ss;
	XSSFWorkbook wb;
	Sheet s;
	public Readexcel() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Avoid whitespace when entering responses");
		System.out.println("It is assumed that the first row is dedicated to the column name");
		System.out.println("Read README.txt for more details");
		
		System.out.println("Enter the absolute file path to the excel spreadsheet");
		path = scanner.next();
		
		System.out.println("Please enter the sheet name");
		sheetName = scanner.next();
		
		System.out.println("Please enter the column (Letter) that contains the building code");
		buildingCol = scanner.next();
		buildingNum = Convert(buildingCol);
		
		System.out.println("Please enter the column (Letter) that contains the closet");
		closetCol = scanner.next();
		closetNum = Convert(closetCol);
		
		System.out.println("Please enter the column (Letter) that contains the complete jackid");
		jackCol = scanner.next();
		jackNum = Convert(jackCol);
		
		System.out.println("Please enter the column (Letter) that contains the complete Roomid (distribution id)");
		roomCol = scanner.next();
		roomNum = Convert(roomCol);
		
		System.out.println("Please enter an empty column where notes can be written");
		noteCol = scanner.next();
		noteNum = Convert(noteCol);
		
//		System.out.println("Please enter the number of the starting row (if starting from beginning enter 1)");
//		start = scanner.nextInt();
//		
//		System.out.println("Please enter the number of the last row");
//		stop = scanner.nextInt();
		
		System.out.println("\nDoes this information look correct (y/n)");
		
	}
	

	/*
		if flag == 0 then reading
		if flag == 1 then writing
	*/
	public String RWcell(int col1, int row1,String text, int flag) {
		path = "U://work/BUILDING.xlsx";
		String value = "";
	        try {
	            FileInputStream inputStream = new FileInputStream(new File(path));
	            Workbook workbook = WorkbookFactory.create(inputStream);
	            Sheet sheet = workbook.getSheet(sheetName);
	         
	            if(flag == 0) { //reading
	            	Row row=sheet.getRow(row1); 
	            	if(row == null) {
	        			return "empty";
	        		}
	        		Cell cell=row.getCell(col1); 
	        		if(cell == null) {
	        			return "empty";
	        		}
	        		cell.setCellType(Cell.CELL_TYPE_STRING);
	        		value=cell.getStringCellValue();  
	        		//System.out.println(value);
	        		return value;
	            }
	            else { //writing
	            	 Row row = sheet.getRow(row1); 
	            	 if(row == null) {
		        			return "empty";
		        	}
		                Cell cell = row.createCell(noteNum);
		                 cell.setCellValue(text);
	            }

	            inputStream.close();
	            FileOutputStream outputStream = new FileOutputStream(path);
	            workbook.write(outputStream);
	            workbook.close();
	            outputStream.close();
	             
	        } catch (IOException | EncryptedDocumentException
	                | InvalidFormatException ex) {
	        	System.out.println("failed to read or write file");
	            ex.printStackTrace();
	        }
	        return value;
		
	}
	
	public int Convert(String convert) {
		if(convert.length() > 1) {
			//throw some error
		}
		char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k',
				'l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
		for(int i = 0; i < alphabet.length;++i) {
			if(convert.toLowerCase().equals(String.valueOf(alphabet[i]))) {
				return i;
			}
		}
		return -1;
		
	}
	
	public static void main(String[] args) {
//		Readexcel a = new Readexcel();
//		//a.RWcell(0,7,"Hello this jack was created",1);
//		System.out.println(a.RWcell(0, 1,"",0));
//		System.out.println(a.RWcell(0, 2,"",0));
//		System.out.println(a.RWcell(0, 3,"",0));
//		System.out.println(a.RWcell(0, 4,"",0));
//		System.out.println(a.RWcell(0, 15,"",0));
//		System.out.println(a.RWcell(0, 16,"",0));
		String a = "E63201AA-V    ";
		String bb = a.trim();
		String b = "E63201AA-D";
		String c = "J402200AA-V";
		String d = "J402200AA-D";
		System.out.println(a);
		System.out.println(bb);
		System.out.println(a.charAt(a.length() - 1));
		
	}
}
