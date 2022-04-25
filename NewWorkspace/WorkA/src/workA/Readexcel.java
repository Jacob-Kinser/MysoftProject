package workA;

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
	String buildingCode;
	
	String closetCol;
	int closetNum;
	
	String jackCol;
	int jackNum;
	
	String roomCol;
	int roomNum;
	
	String noteCol;
	int noteNum;
	
	String sourcePortCol;
	int sourcePortNum;
	
	String desPortCol;
	int desPortNum;
	
	String startDate;
	String endDate;
	
	
	int start;
	int stop;
	
	String accNumCol;
	int accNum;
	
	String ignoreCol;
	int ignoreNum;
	ArrayList<String> Ignore = new ArrayList<>();
	
	Boolean PortCol = false;
	String PortNbrCol;
	int PortNbrNum;
	
	int sleepTime;
	String path;
	FileInputStream ss;
	XSSFWorkbook wb;
	Sheet s;
	Scanner scanner;
	public Readexcel(String text) {
		System.out.println("Make sure file is closed before finishing entering cells");
		scanner = new Scanner(System.in);
		if(text.equals("folder3update")) Folder3update();
		else masterRead(text); 

		
	}
	

	/*
		if flag == 0 then reading
		if flag == 1 then writing
	*/
	public String RWcell(int col1, int row1,String text, int flag) {
		//path = "U://work/BUILDING.xlsx";
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
	        		if(cell == null || cell.toString().equals("") || cell.toString().equals(" ") || cell.toString().equals("  ")) {
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
		                //Cell cell = row.createCell(noteNum);
	            	 Cell cell = row.createCell(col1);
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
	public String getBuildingCode(String jack) {
		return jack.substring(0, 3);
	}
	
	/*
	  	createcable CreateCableMenu(); - closet jack room notes
		"read" CreateReadTableMenu(); - jack pauseTime
		"Jack" CreateJackMenu(); - closet jack room notes
		"fullcreate" createFullCreateUpdateMenu(false); - closet jack room notes
		"fullcreateAC" createFullCreateUpdateMenu(true); - closet jack room start date AccNum notes
		"Zout" zOut(false); - jack room notes
		"FullZupdate" createFullZupdate(false); - closet jack room enddate notes
		"ZoutAC" zOut(true);- jack room notes enddate
		"FullZupdateAC" createFullZupdate(true); - closet jack room enddate notes accNum start date
		"folder3update" Folder3update(); - keep sep method
		"CA" CreateAccount(); - jack accNum notes start date
		"Verify" Verify(); - closet jack pausetime notes
	 */
	
	public void masterRead(String s) {
		String valid = "n";
		System.out.println("Avoid whitespace when entering responses");
		System.out.println("It is assumed that the first row is dedicated to the column name");
		System.out.println("Read README.txt for more details");
		while(valid.equals("n") || valid.equals("no")){
			System.out.println("Enter the absolute file path to the excel spreadsheet ex. U://work/BUILDING.xlsx");
			path = scanner.next();
			
			System.out.println("Please enter the sheet name");
			sheetName = scanner.next();
			
			if(s.equals("createcable") || s.equals("Jack") || s.equals("fullcreate") || s.equals("fullcreateAC") 
					|| s.equals("FullZupdate") || s.equals("FullZupdateAC") || s.equals("Verify")) {
				System.out.println("Please enter the column (Letter) that contains the closet, column format ex. J40-XX");
				closetCol = scanner.next();
				closetNum = Convert(closetCol);
			}
			
			if(s.equals("createcable") || s.equals("read") || s.equals("Jack") || s.equals("fullcreate") 
					|| s.equals("fullcreateAC") || s.equals("Zout") || s.equals("FullZupdate")
					|| s.equals("ZoutAC") || s.equals("FullZupdateAC") || s.equals("CA")
					|| s.equals("Verify") || s.equals("oldjacks") || s.equals("WorkTagUpdate")) {
				System.out.println("Please enter the column (Letter) that contains the complete jackid (including -D)");
				jackCol = scanner.next();
				jackNum = Convert(jackCol);
			}
			
			if(s.equals("createcable") || s.equals("Jack") || s.equals("fullcreate") || s.equals("fullcreateAC") 
					|| s.equals("FullZupdate") || s.equals("FullZupdateAC") || s.equals("Zout")
					|| s.equals("ZoutAC") || s.equals("Verify")) {
				System.out.println("Please enter the column (Letter) that contains the complete Roomid (distribution id)");
				roomCol = scanner.next();
				roomNum = Convert(roomCol);
			}
			
			if(s.equals("fullcreateAC") || s.equals("FullZupdateAC") || s.equals("CA")) {
				System.out.println("Please enter the column (Letter) that contains the account number for UserI");
				accNumCol = scanner.next();
				accNum = Convert(accNumCol);
				
				System.out.println("Please enter a start date for user accounts that need an start date (format: 2/15/2021)");
				startDate = scanner.next();
				checkDate(startDate);
			}
			
			if(s.equals("FullZupdate") || s.equals("ZoutAC") || s.equals("FullZupdateAC")) {
				System.out.println("Please enter a end date for user accounts that need an end date (format: 2/16/2021)");
				endDate = scanner.next();
				checkDate(endDate);
			}
			
			if(s.equals("createcable") || s.equals("Jack") || s.equals("fullcreate") || s.equals("fullcreateAC") 
					|| s.equals("FullZupdate") || s.equals("FullZupdateAC") || s.equals("Zout")
					|| s.equals("ZoutAC") || s.equals("CA") || s.equals("Verify") || s.equals("WorkTagUpdate")) {
				System.out.println("Please enter an empty column (Letter) where notes can be written");
				noteCol = scanner.next();
				noteNum = Convert(noteCol);
			}
			
			if(s.equals("read") || s.equals("Verify")) {
				System.out.println("Please enter the amount of time (seconds) to pause in between searches");
				sleepTime = scanner.nextInt();
			}
			
			if(s.equals("Zout") || s.equals("FullZupdate") || s.equals("ZoutAC") || s.equals("FullZupdateAC")) {
				System.out.println("Are there any Jacks that need to be ignored when Z'ing out");
				System.out.println("These are jacks that could not be apart of the patch out plan\n"
						+ "but need to remain on mysoft\n"
						+ "Please specify a column that contains these jackids\n"
						+ "if there are no jackids that need to be ignored, "
						+ "enter none\n"
						+ "NOTE: These jacks only need to be ignored "
						+ "if they share a room with other jacks on "
						+ "the patch out plan");
				ignoreCol = scanner.next();
				ignoreNum = Convert(ignoreCol);
				
			}
			
			System.out.println("Ensure file is closed at this time");
			System.out.println("\nDoes this information look correct?");
			System.out.println("File path: " + path);
			System.out.println("Sheet name: " + sheetName);
			
			if(s.equals("createcable") || s.equals("read") || s.equals("Jack") || s.equals("fullcreate") 
					|| s.equals("fullcreateAC") || s.equals("Zout") || s.equals("FullZupdate")
					|| s.equals("ZoutAC") || s.equals("FullZupdateAC") || s.equals("CA")
					|| s.equals("Verify") || s.equals("oldjacks") || s.equals("WorkTagUpdate")) {
				String jack = RWcell(jackNum,1,"", 0);
				buildingCode = getBuildingCode(jack);
				System.out.println("Jackid column: " + jackCol +  ", first row value: " + jack);
				System.out.println("Building Code: " + buildingCode);
			}
			
			if(s.equals("createcable") || s.equals("Jack") || s.equals("fullcreate") || s.equals("fullcreateAC") 
					|| s.equals("FullZupdate") || s.equals("FullZupdateAC") || s.equals("Verify")) {
				String closetval =  RWcell(closetNum,1,"", 0);
				System.out.println("Closet column: " + closetCol + ", first row value: " + closetval);
			}
			
			if(s.equals("createcable") || s.equals("Jack") || s.equals("fullcreate") || s.equals("fullcreateAC") 
					|| s.equals("FullZupdate") || s.equals("FullZupdateAC") || s.equals("Zout")
					|| s.equals("ZoutAC") || s.equals("Verify")) {
				String roomval = RWcell(roomNum,1,"", 0);
				System.out.println("Distribution column: " + roomCol + ", first row value: " + roomval);
			}
			
			if(s.equals("fullcreateAC") || s.equals("FullZupdateAC") || s.equals("CA")) {
				String accVal =  RWcell(accNum,1,"", 0);
				System.out.println("AccNum column: " + accNumCol + ", first row value: " + accVal);
				System.out.println("Start Date: " + startDate);
			}
			
			if(s.equals("FullZupdate") || s.equals("ZoutAC") || s.equals("FullZupdateAC")) {
				System.out.println("End Date: " + endDate);
			}
			
			if(s.equals("createcable") || s.equals("Jack") || s.equals("fullcreate") || s.equals("fullcreateAC") 
					|| s.equals("FullZupdate") || s.equals("FullZupdateAC") || s.equals("Zout")
					|| s.equals("ZoutAC") || s.equals("CA") || s.equals("Verify") || s.equals("WorkTagUpdate")) {
				String noteval =  RWcell(noteNum,1,"", 0);
				System.out.println("Note column: " + noteCol + ", first row value should be empty: " + noteval);
			}
				
			if(s.equals("read") || s.equals("Verify")) {
				System.out.println("Pause time: " + sleepTime);
			}
			
			if(s.equals("Zout") || s.equals("FullZupdate") || s.equals("ZoutAC") || s.equals("FullZupdateAC")) {
				if(!ignoreCol.equals("none")) {
					System.out.println("You entered column for jacks to ignore: " + ignoreCol);
				}
			}
			
			System.out.println("(y/n)");
			valid = scanner.next();
		} 
		if(s.equals("Zout") || s.equals("FullZupdate") || s.equals("ZoutAC") || s.equals("FullZupdateAC")) {
			if(!ignoreCol.equals("none")) {
				System.out.println("Creating a list of jacks to ignore");
				int row = 1;
				int NullinaRow = 0;
				while(NullinaRow != 4) {
					String jackid = RWcell(ignoreNum, row, null, 0);
					if(jackid.equals("empty")) {
						NullinaRow++;
					}
					else {
						Ignore.add(jackid);
						row++;
						NullinaRow = 0;
					}
				}
			}
		}
	}
	
	
	public void Folder3update() {
		String valid = "n";
		System.out.println("Avoid whitespace when entering responses");
		System.out.println("It is assumed that the first row is dedicated to the column name");
		System.out.println("Read README.txt for more details");
		while(valid.equals("n") || valid.equals("no")){
			System.out.println("Enter the absolute file path to the excel spreadsheet ex. U://work/BUILDING.xlsx");
			path = scanner.next();
			
			System.out.println("Please enter the sheet name");
			sheetName = scanner.next();
			
			System.out.println("Please enter the column (Letter) that contains the complete jackid (including -D)");
			jackCol = scanner.next();
			jackNum = Convert(jackCol);
			
		
			System.out.println("Please enter an empty column (Letter) where notes can be written");
			noteCol = scanner.next();
			noteNum = Convert(noteCol);
			
			System.out.println("Would you like to specify a column (Letter) for the portNbr");
			System.out.println("If yes, it will enter port from specified column");
			System.out.println("If no, it will search *jackid for port nbr");
			System.out.println("y/n");
			valid = scanner.next();
			if(valid.equals("y") || valid.equals("yes")) {
				System.out.println("Enter Col");
				PortNbrCol = scanner.next();
				PortNbrNum = Convert(PortNbrCol);
				PortCol = true;
			}
			valid = "n";
			
			
			
			String jack = RWcell(jackNum,1,"", 0);
			buildingCode = getBuildingCode(jack);
			
			
			System.out.println("\nDoes this information look correct?");
			System.out.println("File path: " + path);
			System.out.println("Sheet name: " + sheetName);
			System.out.println("Jackid column: " + jackCol +  ", first row value: " + jack);
			if(PortCol) {
				String port = RWcell(PortNbrNum,1,"", 0);
				System.out.println("PortNbr column: " + jackCol +  ", first row value: " + port);
			}
			
			System.out.println("(y/n)");
			valid = scanner.next();
		} 
	}
	
	public void checkDate(String date) {
		
	}
	

}
