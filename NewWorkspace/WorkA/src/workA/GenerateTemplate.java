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
import java.io.IOException;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class GenerateTemplate {
	WebDriver driver;
	Readexcel ES;
	String filepath = "";
	HSSFWorkbook workbook;
	HSSFSheet sheet;
	
	ArrayList<String> closets = new ArrayList<>();
	ArrayList<String> closetTypes = new ArrayList<>();
	ArrayList<String> ReturnClosets = new ArrayList<>();
	
	ArrayList<String> MissedJacks = new ArrayList<>();
	
	 // This data needs to be written (Object[])
    Map<String, Object[]> ImportRows = new TreeMap<String, Object[]>();
        
	public GenerateTemplate()  {
		
	}
	public GenerateTemplate(WebDriver d) throws InterruptedException, IOException {
		driver = d;
		//ask for filepaths
		ES = new Readexcel("WorkTagUpdate"); 
		promptUser();
		generate();
	}
	
	public GenerateTemplate(WebDriver d, Readexcel f) throws InterruptedException, IOException {
		driver = d;
		driver.navigate().refresh();
		Thread.sleep(4000);
		ES = f;
		promptUser();
		generate();
	}
	
	public void generate() throws InterruptedException, IOException {
		/*
		 * Generate the headers
		 */
		ImportRows.put(
	            "1",
	            new Object[] { "SourceHostname", "SourcePort", "TargetHostname","TargetPort","MediaType","ColorCode","Notes","AP Type" });
		String buildingCode = GrabBuildingCode(ES.RWcell(ES.jackNum, 1, null, 0));
		System.out.println("Grabbing Building code with jack: " + " Got: " + buildingCode);
		int row = 1;
		int writeRow = 2;
	    int NullinaRow = 0;
	    while(NullinaRow !=20) {
		    System.out.println("row: " + row);
		    String jackid = ES.RWcell(ES.jackNum, row, null, 0); 
		    String Tag = ES.RWcell(ES.noteNum, row, null, 0);
		    if(Tag.equals("empty")) {
		    	Tag = "";
		    }
		    String closet = ES.RWcell(ES.closetNum, row, null, 0); // grab from excel //need closet in excel sheet, since not all cables exist
		    if(!closets.contains(closet) && !closet.equals("empty")) {
		    	CheckCloset(closet);
		    }
		    String SourcePort = ES.RWcell(ES.sourcePortNum, row, null, 0); // grab from excel
		    //String TargetPort = ES.RWcell(ES.desPortNum, row, null, 0); // grab from excel
		    String stack = ES.RWcell(ES.stackNum, row, null, 0); // grab from excel
		    //closet = buildingCode + "-" + closet;
		    /*
		     * check if anything is empty besides tag
		     */
		    if(stack.equals("empty") ||jackid.equals("empty") || closet.equals("empty") || SourcePort.equals("empty")) {
		    	NullinaRow++;
		    	if(!jackid.equals("empty")) {
		    		MissedJacks.add(jackid);
		    	}
		    }
		    else {	   
		    	int closetIndex = closets.indexOf(closet);
		    	if(closetIndex == -1) {
		    		System.out.println(closet);
		    		System.out.println(closets);
		    		System.out.println(closetTypes);
		    		
		    	}
				String hostname = closetTypes.get(closetIndex) + "-SW-" + closet.replace("-", "") + "-" + stack + ".TELE.IASTATE.EDU"; 
				
				//generate new row
		        ImportRows.put(String.valueOf(writeRow), new Object[] {hostname,SourcePort,closet + "-COPPERPATCH",
		        		SourcePort, "","",jackid,Tag});
		        
		        
			    NullinaRow = 0;
			    writeRow++;
		    }
		    row++;
	    }
	    createFile();
	   
	    System.out.println("Missed Jackids: " + MissedJacks.toString());
	    
	}
	
	/*
	 * https://stackoverflow.com/questions/1176080/create-excel-file-in-java
	 * create file, set headers, add each row set in the generate method
	 */
	public void createFile() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Sheet1");
        XSSFRow row;

        Set<String> keyid = ImportRows.keySet();
  
        int rowid = 0;
  
        // writing the data into the sheets...
  
        for (String key : keyid) {
  
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = ImportRows.get(key);
            int cellid = 0;
  
            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }
  
        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        FileOutputStream out = new FileOutputStream(
            new File(filepath));
  
        workbook.write(out);
        out.close();
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
				driver.switchTo().parentFrame();
				driver.switchTo().frame(driver.findElement(By.name("main")));
			}catch(TimeoutException ex) {
					System.out.println(ex);
					driver.navigate().refresh();
					Thread.sleep(4000);
			}
		}
			
		
		//System.out.println(driver.findElements(By.name("main")).size());
		
		//select drop down
//		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("//*[@id=\"selSearchBy\"]")));
//		Select searchType = new Select(driver.findElement(By.xpath("//*[@id=\"selSearchBy\"]")));
//		 searchType.selectByValue("95");
		
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
		String response = "n";
		
		while(response.equals("n")) {
		 
			System.out.println("Please enter the column (Letter) that contains the closet, column format ex. J40-XX");
			ES.closetCol = scanner.next();
			ES.closetNum = ES.Convert(ES.closetCol);
			
			//check if not full
			
			System.out.println("Please enter the column (Letter) for the source port (1/0/5, etc.)");
			ES.sourcePortCol = scanner.next();
			ES.sourcePortNum = ES.Convert(ES.sourcePortCol);
			
//			System.out.println("Please enter the column (Letter) for the destination port (port1,port2,etc.)");
//			ES.desPortCol = scanner.next();
//			ES.desPortNum = ES.Convert(ES.desPortCol);
			
			System.out.println("Please enter the column (Letter) for the stack number (01,02,etc.)");
			ES.stackCol = scanner.next();
			ES.stackNum = ES.Convert(ES.stackCol);
			
			
			System.out.println("First Closet value: "  + ES.RWcell(ES.closetNum, 1, null, 0));
			System.out.println("First source port value: " + ES.RWcell(ES.sourcePortNum, 1, null, 0));
			//System.out.println("First target port value: " + ES.RWcell(ES.desPortNum, 1, null, 0));
			System.out.println("First stack number value: " + ES.RWcell(ES.stackNum, 1, null, 0));
			System.out.println("Verify the columns above are correct (y/n)");
			response = scanner.next().toLowerCase();
			if(response.equals("y")) {
				continue;
			}
			else if(!response.equals("n")) {
				System.out.println("please enter either y for yes or n for no");
				response = "n";
			}
		
		}
		scanner.close();
		
	}
	


}
