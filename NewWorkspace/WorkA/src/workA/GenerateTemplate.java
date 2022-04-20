package workA;

import org.openqa.selenium.WebDriver;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class GenerateTemplate {
	WebDriver driver;
	Readexcel ES;
	String filepath = "";
	HSSFWorkbook workbook;
	HSSFSheet sheet;
	
	public GenerateTemplate(WebDriver d) {
		driver = d;
		//ask for filepaths
	}
	
	public GenerateTemplate(WebDriver d, Readexcel f) {
		driver = d;
		ES = f;
		filepath = generateFilepath(ES.path);
	}
	
	public void generate() {
		
		String buildingCode = GrabBuildingCode(ES.RWcell(ES.jackNum, 1, null, 0));
		System.out.println("Grabbing Building code with jack: " + " Got: " + buildingCode);
		int row = 1;
	    int NullinaRow = 0;
	    while(NullinaRow !=20) {
		    System.out.println("row: " + row);
		    String jackid = ES.RWcell(ES.jackNum, row, null, 0); 
		    String Tag = ES.RWcell(ES.noteNum, row, null, 0); 
		    String closet = ""; // grab from excel
		    String SourcePort = ""; // grab from excel
		    String TargetPort = ""; // grab from excel
		    String hostname = "";
		    closet = buildingCode + "-" + closet;
		    	   
		    HSSFRow rowVals = sheet.createRow((short)row);
		    rowVals.createCell(0).setCellValue(hostname);
		    rowVals.createCell(1).setCellValue(SourcePort);
		    rowVals.createCell(2).setCellValue(closet+"-COPPERPATCH");
		    rowVals.createCell(3).setCellValue(TargetPort);
		    rowVals.createCell(4).setCellValue("");
		    rowVals.createCell(5).setCellValue("");
		    rowVals.createCell(6).setCellValue(jackid);
		    rowVals.createCell(7).setCellValue(Tag);
		    
		    row++;
	    }
	    try {
		    FileOutputStream fileOut = new FileOutputStream(filepath);
	        workbook.write(fileOut);
	        fileOut.close();
	        workbook.close();
	    }catch ( Exception ex ) { //catch file not found exception
            System.out.println(ex);
        }
	}
	
	/*
	 * https://stackoverflow.com/questions/1176080/create-excel-file-in-java
	 * create file, set headers
	 */
	public void createFile() {
		 try {
	            workbook = new HSSFWorkbook();
	            sheet = workbook.createSheet("Sheet1");  

	            HSSFRow rowhead = sheet.createRow((short)0);
	            rowhead.createCell(0).setCellValue("SourceHostname");
	            rowhead.createCell(1).setCellValue("SourcePort");
	            rowhead.createCell(2).setCellValue("TargetHostname");
	            rowhead.createCell(3).setCellValue("TargetPort");
	            rowhead.createCell(4).setCellValue("MediaType");
	            rowhead.createCell(5).setCellValue("ColorCode");
	            rowhead.createCell(6).setCellValue("Notes");
	            rowhead.createCell(7).setCellValue("AP Type");

	            

	            

	        } catch ( Exception ex ) {
	            System.out.println(ex);
	        }
	}
	
	
	/*
	 * Example input C:/Users/jacob/Documents/WorkRepo/MysoftProject/BulkImport/Friley.xlsx
	 * Example output C:/Users/jacob/Documents/WorkRepo/MysoftProject/BulkImport/FrileyImport.xlsx
	 */
	public String generateFilepath(String fullpath) {
		return "";
	}
	
	public String GrabBuildingCode(String sampleJackid) {
		return "";
	}
	

}
