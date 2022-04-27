package workA;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
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
	
	ArrayList<String> closets = new ArrayList<>();
	ArrayList<String> closetTypes = new ArrayList<>();
	ArrayList<String> ReturnClosets = new ArrayList<>();
	public GenerateTemplate()  {
		
	}
	public GenerateTemplate(WebDriver d) throws InterruptedException {
		driver = d;
		//ask for filepaths
		ES = new Readexcel("WorkTagUpdate"); 
		promptUser();
		createFile();
		generate();
	}
	
	public GenerateTemplate(WebDriver d, Readexcel f) throws InterruptedException {
		driver = d;
		ES = f;
		promptUser();
		createFile();
		generate();
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
		    if(!closets.contains(closet) && !closet.equals("empty")) {
		    	CheckCloset(closet);
		    }
		    String SourcePort = ES.RWcell(ES.sourcePortNum, row, null, 0); // grab from excel
		    String TargetPort = ES.RWcell(ES.desPortNum, row, null, 0); // grab from excel
		    //closet = buildingCode + "-" + closet;
		    if(jackid.equals("empty") || closet.equals("empty") || Tag.equals("empty") || SourcePort.equals("empty") || TargetPort.equals("empty")) {
		    	NullinaRow++;
		    }
		    else {	   
		    	int closetIndex = closets.indexOf(closet);
		    	if(closetIndex == -1) {
		    		System.out.println(closet);
		    		System.out.println(closets);
		    		System.out.println(closetTypes);
		    		
		    	}
				String hostname = closetTypes.get(closetIndex) + "-SW-" + buildingCode + closet + "01.TELE.IASTATE.EDU"; 
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
	
	public void CheckCloset(String ClosetString) throws InterruptedException {
		Boolean switched = false;
		while(!switched) {
			try {
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("mainFrame")));
				driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("contents")));
			    driver.switchTo().frame(driver.findElement(By.name("contents")));
			    new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td1\"]")));
			    new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"td1\"]")));
				driver.findElement(By.xpath("//*[@id=\"td1\"]")).click();
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td6\"]")));
				new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"td6\"]")));
				WebElement element = driver.findElement(By.xpath("//*[@id=\"td6\"]"));
				element.click();
				switched = true;
			}catch(TimeoutException ex) {
					System.out.println(ex);
					driver.navigate().refresh();
					Thread.sleep(4000);
			}
		}
			
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
		System.out.println(driver.findElements(By.name("main")).size());
		
		//select drop down
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("//*[@id=\"selSearchBy\"]")));
		Select searchType = new Select(driver.findElement(By.xpath("//*[@id=\"selSearchBy\"]")));
		 searchType.selectByValue("95");
		
		int j = 0;
		ReturnClosets.clear();
		while(ReturnClosets.size() == 0 && j != 3) {
			//if(j == 1) System.out.println("caught one");
			Boolean Ready = false;
			while(!Ready) {
				try {
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter1\"]")));
					driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]")).sendKeys(ClosetString);
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdGet\"]")));
					new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"cmdGet\"]")));
					driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
					Ready = true;
				}catch(StaleElementReferenceException | NoSuchElementException | NullPointerException ex){
					driver.navigate().refresh();
					Thread.sleep(5000);
					CheckCloset(ClosetString);
					driver.switchTo().parentFrame();
					driver.switchTo().frame(driver.findElement(By.name("main")));
					System.out.println("Caught stale element or no element: " + ex.getMessage());
				}
			}
			ReturnClosets.clear();

			int digit = 0;
			String path = "//*[@id=\"dgListView_0_1\"]";
			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(path)));
			int size0 = driver.findElements(By.xpath(path)).size();
			if(size0 == 1) {
				closets.add(ClosetString);
				String des = driver.findElement(By.xpath(path)).getAttribute("title");
				if(des.toLowerCase().contains("mdf")) {
					closetTypes.add("MDF");
				}
				else if(des.toLowerCase().contains("idf")) {
					closetTypes.add("IDF");
				}
				else { //prompt user to enter
					String type;
					Scanner scanner = new Scanner(System.in);
					System.out.println("Please enter closet type for closet " + ClosetString + " (mdf or idf)");
					type = scanner.next();
					scanner.close();
					closetTypes.add(type.toUpperCase());
					//add to des
				}
				
			}
			digit++;
			path = "//*[@id=\"dgListView_" + digit + "\"]";
			j++;
		}

		
		driver.navigate().refresh();
		Thread.sleep(4000);
	}
	
	
	/*
	 * Example input C:/Users/jacob/Documents/WorkRepo/MysoftProject/BulkImport/Friley.xlsx
	 * Example output C:/Users/jacob/Documents/WorkRepo/MysoftProject/BulkImport/FrileyImport.xlsx
	 */
	public String generateFilepath(String fullpath) {
		int folderIndex = fullpath.lastIndexOf("/");
		String oldFilename = fullpath.substring(folderIndex + 1);
		//System.out.println(oldFilename);
		fullpath = fullpath.substring(0, folderIndex + 1);
		//System.out.println(fullpath);
		int extentionsIndex = oldFilename.lastIndexOf(".");
		String newFilename = oldFilename.substring(0,extentionsIndex) + "ImportTemplate.xlsx";
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
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
		System.out.println(driver.findElements(By.name("main")).size());
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
		
		//check if not full
		
		System.out.println("Please enter the column (Letter) for the source port");
		ES.sourcePortCol = scanner.next();
		ES.sourcePortNum = ES.Convert(ES.sourcePortCol);
		
		System.out.println("Please enter the column (Letter) for the destination port");
		ES.desPortCol = scanner.next();
		ES.desPortNum = ES.Convert(ES.desPortCol);
		
		
		scanner.close();
		
		//print fields out
	}
	

}
