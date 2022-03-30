package workA;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoSuchElementException;

public class CreateAccount {
	WebDriver driver;
	ArrayList<WebElement> returnUsers = new ArrayList<>();
	ArrayList<String> returnUsersX = new ArrayList<>();
	ArrayList<Integer> returnUsersNum = new ArrayList<>();
	
	public CreateAccount(WebDriver d) {
		driver = d;
	}
	
	public void create() throws InterruptedException {
		switchToAccount();
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
		Readexcel ES = new Readexcel("CA");
		int row = 1;
		int NullinaRow = 0;
		while(NullinaRow!=4) {
			String jackid = ES.RWcell(ES.jackNum, row, null, 0);
			String date = ES.startDate;
			String accNum = ES.RWcell(ES.accNum, row, null, 0);
			if(jackid.equals("empty") || accNum.equals("empty")) {
				ES.RWcell(ES.noteNum, row, "One or more field empty", 1);
				NullinaRow++;
			}
			else {
				searchAccount(jackid);
				if(returnUsers.size() == 0)
					fullCreate(jackid,date,accNum);
				else {
					ES.RWcell(ES.noteNum, row, "Already Created", 1);
				}
				NullinaRow = 0;
			}
			row++;
		}
	}
	
	public void fullCreate(String jackid, String date, String accNum) throws InterruptedException {
		addNew();
	
		setUserID(jackid);

		setAccNumber(accNum);
		Thread.sleep(1000);
		setLastname(jackid);
		
		setDirectory();
		
		setAct(date);

		setRating();
		Thread.sleep(2000);
		saveandexit();
	
	}
	
	public void addNew() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdAdd\"]")));
		driver.findElement(By.xpath("//*[@id=\"cmdAdd\"]")).click();
	}
	
	public void setUserID(String jackid) {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"USERID\"]")));
		driver.findElement(By.xpath("//*[@id=\"USERID\"]")).sendKeys(jackid);
	}
	
	public void setAccNumber(String accNum) {
			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ACCOUNTSEQ_ACCOUNTNUMBER\"]")));
			driver.findElement(By.xpath("//*[@id=\"ACCOUNTSEQ_ACCOUNTNUMBER\"]")).sendKeys(accNum);
			driver.findElement(By.xpath("//*[@id=\"ACCOUNTSEQ_ACCOUNTNUMBER\"]")).sendKeys(Keys.TAB);
			
	}
	
	public void setLastname(String jackid) {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"LASTNAME\"]")));
		driver.findElement(By.xpath("//*[@id=\"LASTNAME\"]")).sendKeys(jackid);
	}
	
	public void setDirectory() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"DIRECTORYNBRSEQ_DIRECTORYNBR\"]")));
		driver.findElement(By.xpath("//*[@id=\"DIRECTORYNBRSEQ_DIRECTORYNBR\"]")).sendKeys("DATA");
	}
	
	public void setAct(String date) {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"DATEOPEN\"]")));
		driver.findElement(By.xpath("//*[@id=\"DATEOPEN\"]")).sendKeys(date);
	}
	
	public void setLoginID(String jackid) {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"NTID\"]")));
		driver.findElement(By.xpath("//*[@id=\"NTID\"]")).sendKeys(jackid);
	}
	
	public void setRating() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"head2\"]")));
		driver.findElement(By.xpath("//*[@id=\"head2\"]")).click();
		 Select rate = new Select(driver.findElement(By.xpath("//*[@id=\"RATETYPESEQ_RATETYPEDESCRIPTION\"]")));
		 rate.selectByValue("823");
	}
	
	public void saveandexit() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")));
		driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
	}
	
	public void searchAccount(String room) throws InterruptedException {
		returnUsers.clear();
		returnUsersNum.clear();
		returnUsersX.clear();
		int j = 0;
		while(returnUsers.size() == 0 && j != 3) {
			//if(j == 1) System.out.println("caught one");
			returnUsers.clear();
			returnUsersNum.clear();
			returnUsersX.clear();
			Boolean Ready = false;
			while(!Ready) {
				try {
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter1\"]")));
					driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]")).sendKeys(room);
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdGet\"]")));
					new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"cmdGet\"]")));
					driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
					Ready = true;
				}catch(StaleElementReferenceException | NoSuchElementException | NullPointerException ex ){
					driver.navigate().refresh();
					Thread.sleep(5000);
					switchToAccount();
					driver.switchTo().parentFrame();
					driver.switchTo().frame(driver.findElement(By.name("main")));
					System.out.println("Caught stale element or no element: " + ex.getMessage());
				}
			}
			int digit = 0;
			String path = "//*[@id=\"dgListView_" + digit + "\"]";
			String path2 = "//*[@id=\"dgListView_" + digit + "_6\"]";
			//*[@id="dgListView_1_6"]
			for(int i = 0; i < 10; i++) {
				//System.out.println(path2);
				int size0 = driver.findElements(By.xpath(path2)).size();
				if(size0 == 0) {
					//System.out.println("in p 2: " + digit + " path: " + path2);
					size0 = driver.findElements(By.xpath(path)).size();
					String path3 = "//*[@id=\"dgListView_" + digit + "_3\"]";
					int size1 = driver.findElements(By.xpath(path3)).size();
					if(size0 == 1 && size1 == 1) {
					//	System.out.println("in p 1: " + digit + " path: " + path);
						returnUsersX.add(path);
						returnUsers.add(driver.findElement(By.xpath(path)));
						returnUsersNum.add(digit);
	//					String title = driver.findElement(By.xpath(path)).getAttribute("title");
	//					System.out.println("title " + ": " + title);
					}
				}
				digit++;
				path = "//*[@id=\"dgListView_" + digit + "\"]";
				path2 = "//*[@id=\"dgListView_" + digit + "_6\"]";
			}
			j++;
		}
	}
	
	public void switchToAccount() throws InterruptedException {
		Boolean switched = false;
		while(!switched) {
			try {
			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("mainFrame")));
			driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("contents")));
		    driver.switchTo().frame(driver.findElement(By.name("contents")));
		    new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td15\"]")));
		    new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"td15\"]")));
			driver.findElement(By.xpath("//*[@id=\"td15\"]")).click();
			 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td17\"]")));
			 new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"td17\"]")));
			WebElement element = driver.findElement(By.xpath("//*[@id=\"td17\"]"));
			element.click();
			switched = true;
			}catch(TimeoutException ex) {
				System.out.println(ex);
				driver.navigate().refresh();
				Thread.sleep(4000);
			}
		}
	}
}
