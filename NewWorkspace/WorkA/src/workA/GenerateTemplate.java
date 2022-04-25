package workA;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileOutputStream;
import java.util.Scanner;

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
		Readexcel ES = new Readexcel("WorkTagUpdate"); 
		promptUser();
	}
	
	public GenerateTemplate(WebDriver d, Readexcel f) {
		driver = d;
		ES = f;
		promptUser();
	}
	
	public void generate() throws InterruptedException {
		String buildingCode = GrabBuildingCode(ES.RWcell(ES.jackNum, 1, null, 0));
		System.out.println("Grabbing Building code with jack: " + " Got: " + buildingCode);
		int row = 1;
	    int NullinaRow = 0;
	    while(NullinaRow !=20) {
		    System.out.println("row: " + row);
		    String jackid = ES.RWcell(ES.jackNum, row, null, 0); 
		    String Tag = ES.RWcell(ES.noteNum, row, null, 0); 
		    String closet = ES.RWcell(ES.closetNum, row, null, 0); // grab from excel //need closet in excel sheet, since not all cables exist
		    String SourcePort = ES.RWcell(ES.sourcePortNum, row, null, 0); // grab from excel
		    String TargetPort = ES.RWcell(ES.desPortNum, row, null, 0); // grab from excel
		    String hostname = "MDF-SW-" + buildingCode + closet + "01.TELE.IASTATE.EDU"; //mdf vs idf?
		    closet = buildingCode + "-" + closet;
		    if(jackid.equals("empty") || closet.equals("empty") || Tag.equals("empty") || SourcePort.equals("empty") || TargetPort.equals("empty")) {
		    	NullinaRow++;
		    }
		    else {	   
			    HSSFRow rowVals = sheet.createRow((short)row);
			    rowVals.createCell(0).setCellValue(hostname);
			    rowVals.createCell(1).setCellValue(SourcePort);
			    rowVals.createCell(2).setCellValue(buildingCode + "-" + closet + "-COPPERPATCH");
			    rowVals.createCell(3).setCellValue(TargetPort);
			    rowVals.createCell(4).setCellValue("");
			    rowVals.createCell(5).setCellValue("");
			    rowVals.createCell(6).setCellValue(jackid);
			    rowVals.createCell(7).setCellValue(Tag);
			    NullinaRow = 0;
		    }
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
		int folderIndex = fullpath.lastIndexOf("/");
		String oldFilename = fullpath.substring(folderIndex + 1);
		fullpath = fullpath.substring(folderIndex, folderIndex);
		int extentionsIndex = oldFilename.lastIndexOf(".");
		String newFilename = oldFilename.substring(extentionsIndex) + "ImportTemplate.xlsx";
		fullpath += newFilename;
		System.out.println(fullpath);
		return fullpath;
	}
	
	public String GrabBuildingCode(String sampleJackid) throws InterruptedException {
		String title = "";
		driver.navigate().refresh();
		Thread.sleep(4000);
		CreateCable cable = new CreateCable(driver);
		cable.Switchtocable();
		cable.Searchcable(sampleJackid.substring(0, 2));
		if(cable.returnCables.size() > 0) {
			title = driver.findElement(By.xpath("//*[@id=\"dgListView_0_2\"]")).getAttribute("title");
			int indexDash = title.indexOf("-");
			title = title.substring(0, indexDash);
		}
		else {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Please enter the building code ");
			title = scanner.next();
			scanner.close();
		}
		driver.navigate().refresh();
		Thread.sleep(4000);
		return title;
	}
	
	public void promptUser() {
		filepath = generateFilepath(ES.path);
		Scanner scanner = new Scanner(System.in);
		 
		System.out.println("Please enter the column (Letter) that contains the closet, column format ex. J40-XX");
		ES.closetCol = scanner.next();
		ES.closetNum = ES.Convert(ES.closetCol);
		
		System.out.println("Please enter the column (Letter) ");
		ES.sourcePortCol = scanner.next();
		ES.sourcePortNum = ES.Convert(ES.sourcePortCol);
		
		System.out.println("Please enter the column (Letter) ");
		ES.desPortCol = scanner.next();
		ES.desPortNum = ES.Convert(ES.desPortCol);
		
		
		scanner.close();
	}
	

}
