package workA;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;


public class UpdateAccount {
	WebDriver driver;
	ArrayList<WebElement> returnUsers = new ArrayList<>();
	ArrayList<Integer> returnUsersNum = new ArrayList<>();
	public UpdateAccount(WebDriver d) {
		driver = d;
	}
	
	public void update() {
		switchToAccount();
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
		String room = "E6322";
		String jackid = "";
		String date ="";
		SearchUserID(room);
		if(returnUsers.size() == 0) {// create one
			
		}
		else {
			int i = returnUsers.size() - 1;//check if 0
			System.out.println(i);
			while(i>=0) {
				String path = "//*[@id=\"dgListView_" + returnUsersNum.get(i) +"_0\"]";
				WebElement item = driver.findElement(By.xpath(path));
				String itemID = item.getAttribute("title");
				if(!itemID.equals(jackid)) {
					if(i == 0) {
						//UpdateInformation(returnUsers.get(i), jackid);
					}
					else {
						//SetEndDate(returnUsers.get(i), date);
					}
				}
				i--;
			}
		}
		
		
	}
	
	/*
	 * Returns users IDs without an end date
	 * 
	 */
	public void SearchUserID(String room) {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter1\"]")));
		driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]")).sendKeys(room);
		driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
		int digit = 0;
		String path = "//*[@id=\"dgListView_" + digit + "\"]";
		String path2 = "//*[@id=\"dgListView_" + digit + "_6\"]";
		for(int i = 0; i < 10; i++) {
			//System.out.println(path2);
			int size0 = driver.findElements(By.xpath(path2)).size();
			if(size0 == 0) {
				size0 = driver.findElements(By.xpath(path)).size();
				if(size0 == 1) {
					returnUsers.add(driver.findElement(By.xpath(path)));
					returnUsersNum.add(digit);
				}
			}
			digit++;
			path = "//*[@id=\"dgListView_" + digit + "\"]";
			path2 = "//*[@id=\"dgListView_" + digit + "_6\"]";
		}
	}
	
	public void SetEndDate(String x, String date) {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(x)));
		WebElement element = driver.findElement(By.xpath(x));
		Actions act = new Actions(driver);
		act.moveToElement(element).click().click().perform();
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"DATECLOSED\"]")));
		driver.findElement(By.xpath("//*[@id=\"DATECLOSED\"]")).sendKeys(date);
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")));
		driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
		
		isAlertPresent();
		
	}
	
	public void UpdateInformation(String x, String jack) {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(x)));
		WebElement element = driver.findElement(By.xpath(x));
		Actions act = new Actions(driver);
		act.moveToElement(element).click().click().perform();
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"USERID\"]")));
		WebElement a = driver.findElement(By.xpath("//*[@id=\"USERID\"]"));
		a.clear();
		a.sendKeys(jack);
		
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"LASTNAME\"]")));
		a = driver.findElement(By.xpath("//*[@id=\"LASTNAME\"]"));
		a.clear();
		a.sendKeys(jack);
		
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"NTID\"]")));
		a = driver.findElement(By.xpath("//*[@id=\"NTID\"]"));
		a.clear();
		a.sendKeys(jack);
		
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")));
		driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
		
		isAlertPresent();
	}
	
	
	public void switchToAccount() {
		driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
	    driver.switchTo().frame(driver.findElement(By.name("contents")));
		driver.findElement(By.xpath("//*[@id=\"td15\"]")).click();
		WebElement element = driver.findElement(By.xpath("//*[@id=\"td17\"]"));
		element.click();
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
		        break;
		    }  
		    catch (NoAlertPresentException Ex) 
		    { 
		        ret = false;
		    } 
		    i++;
		}  
		 return ret;
	}
}
