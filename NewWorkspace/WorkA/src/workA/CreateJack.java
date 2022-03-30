package workA;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;

public class CreateJack {
	WebDriver driver;
	ArrayList<String> returnJacks = new ArrayList<>();
	ArrayList<Integer> returnJacksNum = new ArrayList<>();
	public CreateJack(WebDriver d) {
		driver = d;
	}
	
	public void update() throws InterruptedException {
		SwitchTojack();
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
		Readexcel ES = new Readexcel("jack");
		String building = ES.buildingCode;
		int Nullinarow = 0;
		int row = 1;
		while(Nullinarow != 4) {
			returnJacks.clear();
			String jackid = ES.RWcell(ES.jackNum, row, null, 0);
			String room = ES.RWcell(ES.roomNum, row, null, 0);
			int numResults = -1;
			if(jackid.equals("empty") || room.equals("empty")) {
				Nullinarow++;
			}
			else{
				numResults = Searchjack(jackid); 
			}
			
			if(numResults == 0) {
				fullCreate(jackid,room,building);
			}
			else if(numResults == 1) {
				
			}
			else if(numResults != -1){
				
			}
			
			row++;
		}
	}
	
	public String fullCreate(String jackid, String room, String building) throws InterruptedException {
		addNew();
		setJackID(jackid);
		setStatus();
		setBuilding(building);
		setLineType();
		String retStr = CableTracking(room,jackid);
		saveExit();
		return retStr;
	}
	
	public void addNew() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdAdd\"]")));
		driver.findElement(By.xpath("//*[@id=\"cmdAdd\"]")).click();
	}
	
	public void setJackID(String jackid) {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"JACKID\"]")));
		driver.findElement(By.xpath("//*[@id=\"JACKID\"]")).sendKeys(jackid);
	}
	
	public void setStatus() {
		
	}
	
	public void setBuilding(String Building) {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"LOCATIONSEQ_BUILDINGID\"]")));
		driver.findElement(By.xpath("//*[@id=\"LOCATIONSEQ_BUILDINGID\"]")).sendKeys(Building);
	}
	
	public void setLineType() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"LINETYPESEQ_DESCRIPTION\"]")));
		Select cableid = new Select(driver.findElement(By.xpath("//*[@id=\"LINETYPESEQ_DESCRIPTION\"]")));
		cableid.selectByValue("2");
	}
	
	public void setPairs() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"NBRCABLEPAIRS\"]")));
		Select cableid = new Select(driver.findElement(By.xpath("//*[@id=\"NBRCABLEPAIRS\"]")));
		cableid.selectByValue("4");
	}
	
	public String CableTracking(String room, String jackid) throws InterruptedException {//finish this
		String retStr = "";
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_4\"]")));
		driver.findElement(By.xpath("//*[@id=\"BUTTON_4\"]")).click();
		String parentWindow = driver.getWindowHandle();
		 String cableWindow = null;
		 Set<String> handles = driver.getWindowHandles(); // get all window handles
		 Iterator<String> iterator = handles.iterator();
		 while (iterator.hasNext()){
			 cableWindow = iterator.next();
		 }
		 driver.switchTo().window(cableWindow); 
		 
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdAddOrDelete\"]")));
		driver.findElement(By.xpath("//*[@id=\"cmdAddOrDelete\"]")).click();
		
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtDistributionId\"]")));
		WebElement disID = driver.findElement(By.xpath("//*[@id=\"txtDistributionId\"]"));
		disID.sendKeys(room);
		disID.sendKeys(Keys.TAB);
		Thread.sleep(3000);
		if(isAlertPresent()){
			disID.sendKeys("");
			disID.sendKeys(Keys.TAB);
			System.out.println("LOOK AT JACK AT THIS ROW");
			retStr = "manual update needed";
		}
		else {
			Select cableid = new Select(driver.findElement(By.xpath("//*[@id=\"selCableId\"]")));
			try {
			 cableid.selectByValue(jackid);
			}catch(NoSuchElementException ex) {
				System.out.println("LOOK AT JACK AT THIS ROW");
				System.out.println(ex);
			}
			 
			 if(isAlertPresent()) {
					System.out.println("LOOK AT JACK AT THIS ROW");
					retStr = "manual update needed";
			}
		}
		 for(int i = 0; i < 10; ++i) {
				try {
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdSaveAndExit\"]")));
					driver.findElement(By.xpath("//*[@id=\"cmdSaveAndExit\"]")).click();
					break;
				}catch(StaleElementReferenceException | NoSuchElementException ex ){
					System.out.println(ex);
				}
			}
		
		if(isAlertPresent()) {
			System.out.println("LOOK AT JACK AT THIS ROW");
		}
		for(int i = 0; i < 10; ++i) {
			try {
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdSaveAndExit\"]")));
				driver.findElement(By.xpath("//*[@id=\"cmdSaveAndExit\"]")).click();
				break;
			}catch(StaleElementReferenceException | NoSuchElementException ex ){
				System.out.println(ex);
			}
		}
		 
		 driver.switchTo().window(parentWindow);
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("mainFrame")));
			driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("main")));
			driver.switchTo().frame(driver.findElement(By.name("main")));
		 return retStr;
	}
	
	public void saveExit() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_3\"]")));
		driver.findElement(By.xpath("//*[@id=\"BUTTON_3\"]")).click();
	}
	
	public void SwitchTojack() throws InterruptedException{
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
			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td8\"]")));
			 new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"td8\"]")));
			WebElement element = driver.findElement(By.xpath("//*[@id=\"td8\"]"));
			element.click();
			switched = true;
			}catch(TimeoutException ex) {
				System.out.println(ex);
				driver.navigate().refresh();
				Thread.sleep(4000);
			}
	}
	}
	
	public int Searchjack(String cable) throws InterruptedException {
		int j = 0;
		returnJacks.clear();
		returnJacksNum.clear();
		while(returnJacks.size() == 0 && j != 3) {
			//if(j == 1) System.out.println("caught one");
			returnJacks.clear();
			returnJacksNum.clear();
			Boolean Ready = false;
			while(!Ready) {
				try {
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter1\"]")));
					driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]")).sendKeys(cable);
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdGet\"]")));
					new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"cmdGet\"]"))); //.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdGet\"]")));
					driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
					Ready = true;
				}catch(StaleElementReferenceException | NoSuchElementException | NullPointerException ex ){
					driver.navigate().refresh();
					Thread.sleep(5000);
					SwitchTojack();
					driver.switchTo().parentFrame();
					driver.switchTo().frame(driver.findElement(By.name("main")));
					System.out.println("Caught stale element or no element: " + ex.getMessage());
				}
			}
			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter1\"]")));
			WebElement element1 = driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]"));
			element1.sendKeys(cable);
			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdGet\"]")));
			driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
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
						returnJacks.add(path);
						returnJacksNum.add(digit);
					}
				}
				digit++;
				path = "//*[@id=\"dgListView_" + digit + "\"]";
				path2 = "//*[@id=\"dgListView_" + digit + "_0\"]";;
			}
			j++;
		}
		return returnJacks.size();

	}
	
	public boolean isAlertPresent() 
	{ 
		Boolean ret = false;
		 int i=0;
		 while(i++<5){
		    try 
		    { 
		        driver.switchTo().alert().accept(); 
		        ret = true;
		    }   // try 
		    catch (NoAlertPresentException Ex) 
		    { 
		        ret = false;
		    }   // catch 
		    i++;
		}   // isAlertPresent()
		 return ret;
	}
}
