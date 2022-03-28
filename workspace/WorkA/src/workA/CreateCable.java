package workA;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

public class CreateCable {
	WebDriver driver;
	ArrayList<WebElement> returnCables = new ArrayList<>();
	ArrayList<String> returnCablesX = new ArrayList<>();
	ArrayList<Integer> returnCablesNum = new ArrayList<>();
	
	public CreateCable(WebDriver d) {
		driver = d;
	}
	public int Searchcable(String cable) throws InterruptedException {
		int j = 0;
		returnCables.clear();
		returnCablesNum.clear();
		returnCablesX.clear();
		while(returnCables.size() == 0 && j != 3) {
			//if(j == 1) System.out.println("caught one");
			Boolean Ready = false;
			while(!Ready) {
				try {
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter1\"]")));
					driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]")).sendKeys(cable);
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdGet\"]")));
					new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"cmdGet\"]")));
					driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
					Ready = true;
				}catch(StaleElementReferenceException | NoSuchElementException | NullPointerException ex){
					driver.navigate().refresh();
					Thread.sleep(5000);
					Switchtocable();
					driver.switchTo().parentFrame();
					driver.switchTo().frame(driver.findElement(By.name("main")));
					System.out.println("Caught stale element or no element: " + ex.getMessage());
				}
			}
			returnCables.clear();
			returnCablesNum.clear();
			returnCablesX.clear();
			int digit = 0;
			String path = "//*[@id=\"dgListView_" + digit + "\"]";
			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(path)));
			//String path2 = "//*[@id=\"tdDiv_0_dgListViewCol0\"]";
			String path2 = "//*[@id=\"dgListView_" + digit + "_0\"]";
			//*[@id="dgListView_0_0"]
			for(int i = 0; i < 10; i++) {
				//System.out.println(path2);
				int size0 = driver.findElements(By.xpath(path)).size();
				if(size0 == 1) {
					size0 = driver.findElements(By.xpath(path2)).size();
					if(size0 == 1) {
						returnCablesX.add(path);
						returnCables.add(driver.findElement(By.xpath(path)));
						returnCablesNum.add(digit);
					}
				}
				digit++;
				path = "//*[@id=\"dgListView_" + digit + "\"]";
				path2 = "//*[@id=\"dgListView_" + digit + "_0\"]";;
			}
			j++;
		//	System.out.println(j + " " + returnCables.size());
		}
		return returnCables.size();
		
	}
	
	public void create() throws InterruptedException {
		Switchtocable();
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
		System.out.println(driver.findElements(By.name("main")).size());
		Readexcel ES = new Readexcel("createcable");
		int row = 1;
		int NullinaRow = 0;
		Thread.sleep(300);
		while(NullinaRow != 4) {
			String jackid = ES.RWcell(ES.jackNum, row, null, 0);
			String closet = ES.RWcell(ES.closetNum, row, null, 0);
			String room = ES.RWcell(ES.roomNum, row, null, 0);
			String building = ES.buildingCode;
			if(room.equals("empty") || closet.equals("empty") || jackid.equals("empty")) {
				ES.RWcell(ES.noteNum, row, "One or more field empty", 1);
				NullinaRow++;
			}
			else {
				int sizeC = Searchcable(jackid);
				if(sizeC > 0) {
					ES.RWcell(ES.noteNum, row, "Already Created", 1);
				}
				else {
					fullCreate(jackid,building,closet,room);
				 
				}
				 
				 NullinaRow = 0;
				 
			}
			row++;
		}
	}
	
	public void fullCreate(String jackid, String building, String closet, String room) throws InterruptedException {
		addNew();
		 
		addJackID(jackid);
		 
		setCableType();
		 
		setOrigConnectionType();
		//System.out.println(driver.getWindowHandle());
		setCloset(building, closet);
		
		setTermConnectionType();
		 
		setRoom(room, building);
 
		Thread.sleep(500);
		 
		GenerateCablePairs();
		 
		SaveandExit();
	}
	
	
	public void addNew() {
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdAdd\"]")));
		 driver.findElement(By.xpath("//*[@id=\"cmdAdd\"]")).click();
	}
	
	public void addJackID(String jackid) {
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"DESCRIPTION\"]")));
		 WebElement cableid = driver.findElement(By.xpath("//*[@id=\"DESCRIPTION\"]"));
		 cableid.sendKeys(jackid);
	}
	
	public void setCableType() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"CABLETYPESEQ_DESCRIPTION\"]")));
		 Select cableType = new Select(driver.findElement(By.xpath("//*[@id=\"CABLETYPESEQ_DESCRIPTION\"]")));
		 cableType.selectByValue("1488");
	}
	
	public void setOrigConnectionType() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ORIGCONNECTTYPESEQ_DESCRIPTION\"]")));
		 Select connectionType = new Select(driver.findElement(By.xpath("//*[@id=\"ORIGCONNECTTYPESEQ_DESCRIPTION\"]")));
		 connectionType.selectByValue("35");
	}
	
	public void setCloset(String building, String closet) throws InterruptedException {
		String parentWindow = driver.getWindowHandle();
		 String closetWindow = null;
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdORIGDISTRIBUTIONSEQ_DISTRIBUTIONID\"]")));
		 WebElement closetButton = driver.findElement(By.xpath("//*[@id=\"cmdORIGDISTRIBUTIONSEQ_DISTRIBUTIONID\"]"));
		 closetButton.click();
		 Set<String> handles = driver.getWindowHandles(); // get all window handles
		 Iterator<String> iterator = handles.iterator();
		 while (iterator.hasNext()){
			 closetWindow = iterator.next();
		 }
		 driver.switchTo().window(closetWindow); 
		 
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter1\"]")));
		 driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]")).sendKeys(closet);
		 driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
		 String closetText = driver.findElement(By.xpath("//*[@id=\"dgListView_0\"]")).getText();
		 if(!closet.equals(closetText.subSequence(0, closet.length()))) {
			 driver.findElement(By.xpath("//*[@id=\"cmdAdd\"]")).click();
			 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"DISTRIBUTIONID\"]")));
			 driver.findElement(By.xpath("//*[@id=\"DISTRIBUTIONID\"]")).sendKeys(closet);
			 
			 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"DISTRIBUTIONTYPESEQ_DISTRIBUTIONTYPE\"]")));
			 Select ClosetType = new Select(driver.findElement(By.xpath("//*[@id=\"DISTRIBUTIONTYPESEQ_DISTRIBUTIONTYPE\"]")));
			 ClosetType.selectByValue("5");
			 driver.findElement(By.xpath("//*[@id=\"LOCATIONSEQ_BUILDINGID\"]")).sendKeys(building);
			 driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
		 }
		 else {
			 WebElement a = driver.findElement(By.xpath("//*[@id=\"dgListView_0\"]"));
			 Actions act = new Actions(driver);
			act.moveToElement(a).click().click().perform();
		 }
	
		 driver.switchTo().window(parentWindow); 
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("mainFrame")));
		driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("main")));
		driver.switchTo().frame(driver.findElement(By.name("main")));
		
	}
	
	public void setTermConnectionType() throws InterruptedException {
		// WebDriverWait wait = new WebDriverWait(driver, 15);
//		 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"TERMCONNECTTYPESEQ_DESCRIPTION\"]")));
		 Thread.sleep(1000);
		 Boolean ready = false;
		 new WebDriverWait(driver, 20).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
		 while(!ready) {
			 try {
				 new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"TERMCONNECTTYPESEQ_DESCRIPTION\"]")));
				 ready = true;
			 }catch(TimeoutException ex) {
				System.out.println(driver.getWindowHandle());
				System.out.println(ex); 
			 }
		 }
		 Select connectionType = new Select(driver.findElement(By.xpath("//*[@id=\"TERMCONNECTTYPESEQ_DESCRIPTION\"]")));
		 connectionType.selectByValue("35");
	}
	
	
	public void setRoom(String room, String building) {
		String parentWindow = driver.getWindowHandle();
		 String roomWindow = null;
		 WebElement roomButton = driver.findElement(By.xpath("//*[@id=\"cmdTERMDISTRIBUTIONSEQ_DISTRIBUTIONID\"]"));
		 roomButton.click();
		 Set<String> handles = driver.getWindowHandles(); // get all window handles
		 Iterator<String> iterator = handles.iterator();
		 while (iterator.hasNext()){
			 roomWindow = iterator.next();
		 }
		 driver.switchTo().window(roomWindow); // switch to popup window
		 WebDriverWait wait = new WebDriverWait(driver, 15);
		 wait = new WebDriverWait(driver, 45);
		 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdAdd\"]")));
		 driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]")).sendKeys(room);
		 driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
		 int size0 = driver.findElements(By.xpath("//*[@id=\"dgListView_0\"]")).size();
		 int size1 = driver.findElements(By.xpath("//*[@id=\"dgListView_0_0\"]")).size();
		// String roomText = driver.findElement(By.xpath("//*[@id=\"dgListView_0\"]")).getText();
		 String roomText = "";
		 if(size1 == 1) {
			 WebElement e = driver.findElement(By.xpath("//*[@id=\"dgListView_0_0\"]"));
			 roomText = e.getAttribute("title");
		 }
		 if(!room.equals(roomText)){ //.subSequence(0, room.length()))) {
			 driver.findElement(By.xpath("//*[@id=\"cmdAdd\"]")).click();
			 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"DISTRIBUTIONID\"]")));
			 driver.findElement(By.xpath("//*[@id=\"DISTRIBUTIONID\"]")).sendKeys(room);
			 
			 Select distributionType = new Select(driver.findElement(By.xpath("//*[@id=\"DISTRIBUTIONTYPESEQ_DISTRIBUTIONTYPE\"]")));
			 distributionType.selectByValue("6");
			 
			 driver.findElement(By.xpath("//*[@id=\"LOCATIONSEQ_BUILDINGID\"]")).sendKeys(building);
			 
			 driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
//			 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter1\"]")));
//			 driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]")).sendKeys(room);
//			 driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
			 
		 }
		 else {
			 WebElement a = driver.findElement(By.xpath("//*[@id=\"dgListView_0\"]"));
			 Actions act = new Actions(driver);
			act.moveToElement(a).click().click().perform();
		 }
		 

		 driver.switchTo().window(parentWindow);  // switch back to parent window
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("mainFrame")));
		 driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("main")));
		 driver.switchTo().frame(driver.findElement(By.name("main")));
	}
	
	public void GenerateCablePairs() throws InterruptedException {
		waitForAlert2();
		String parentWindow = driver.getWindowHandle();
		 String generateWindow = null;
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_DETAIL\"]")));
		 WebElement generateButton = driver.findElement(By.xpath("//*[@id=\"BUTTON_DETAIL\"]"));
		 generateButton.click();
		 Set<String> handles = driver.getWindowHandles(); // get all window handles
		 Iterator<String>  iterator = handles.iterator();
		 while (iterator.hasNext()){
			 generateWindow = iterator.next();
		 }
		 driver.switchTo().window(generateWindow); // switch to popup window
		 WebDriverWait wait = new WebDriverWait(driver, 15);
		 wait = new WebDriverWait(driver, 45);
		 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtStartingPair\"]")));
		 driver.findElement(By.xpath("//*[@id=\"txtStartingPair\"]")).sendKeys("1");
		 driver.findElement(By.xpath("//*[@id=\"txtEndingPair\"]")).sendKeys("4");
		 Select lineType = new Select(driver.findElement(By.xpath("//*[@id=\"selLineType\"]")));
		 lineType.selectByValue("2");
		 Select status = new Select(driver.findElement(By.xpath("//*[@id=\"selStatus\"]")));
		 status.selectByValue("I");
		 driver.findElement(By.xpath("//*[@id=\"cmdGenerate\"]")).click();
		 
		 waitForAlert();

		 driver.switchTo().window(parentWindow);  // switch back to parent window
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("mainFrame")));
			driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("main")));
			driver.switchTo().frame(driver.findElement(By.name("main")));
		 
	}

	public void SaveandExit() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")));
		 
		driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
	}
	
	public void Switchtocable() throws InterruptedException {
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
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td2\"]")));
				new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"td2\"]")));
				WebElement element = driver.findElement(By.xpath("//*[@id=\"td2\"]"));
				element.click();
				switched = true;
			}catch(TimeoutException ex) {
					System.out.println(ex);
					driver.navigate().refresh();
					Thread.sleep(4000);
			}
		}
	}
	
	public boolean findElement(String x) {
	    boolean result = false;
	    
	    while(!result) {
	    //	System.out.println("1");
	        try {
	        	new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(x)));
	            result = true;
	            break;
	        } catch(StaleElementReferenceException e) {
	        	System.out.println("finding element");
	        }
	        
	    }
	    return result;
	}
	
	public void waitForAlert() throws InterruptedException
	{
	   int i=0;
	   while(i++<5)
	   {
	        try
	        {
	        	driver.switchTo().alert().accept();
	            break;
	        }
	        catch(NoAlertPresentException e)
	        {
	          Thread.sleep(4000);
	          continue;
	        }
	   }
	}
	public void waitForAlert2() throws InterruptedException
	{
	   int i=0;
	   while(i++<5)
	   {
	        try
	        {
	        	driver.switchTo().alert().accept();
	            break;
	        }
	        catch(NoAlertPresentException e)
	        {
	          Thread.sleep(250);
	          continue;
	        }
	   }
	}
}
