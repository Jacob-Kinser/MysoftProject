package workA;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UpdateJack {
	WebDriver driver;
	ArrayList<String> returnJacks = new ArrayList<>();
	public UpdateJack(WebDriver d){
		driver = d;
	}

	public void update() {
		SwitchTojack();
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
		int NullinaRow = 0;
		int row = 1;
		Readexcel ES = new Readexcel("Jack");
		while(NullinaRow != 4) {
			String jackid = ES.RWcell(ES.jackNum, row, null, 0);
			String room = ES.RWcell(ES.roomNum, row, null, 0);
			returnJacks.clear();
			int numResults = Searchjack(room);
			if(jackid.equals("empty") || room.equals("empty")) {
				ES.RWcell(ES.noteNum, row, "One or more field empty", 1);
				NullinaRow++;
			}
			else if(numResults == 1) {
				//check if it is already there
			}
			else if(numResults == 2) {
				UpdateVD(jackid);
				NullinaRow = 0;
			}
			else if(numResults == 3) {
			
			}
			else if(numResults == 4) {
				
			}
			row ++;
		}
	}
	
	public void UpdateVD(String jackid) {
		int i = 1;
		while(i > -1) {
			String trimmed = returnJacks.get(i).trim();
			if(trimmed.charAt(trimmed.length() - 1) == 'D') {
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"dgListView_0\"]")));
				WebElement a = driver.findElement(By.xpath("//*[@id=\"dgListView_0\"]"));
				Actions act = new Actions(driver);
				act.moveToElement(a).click().click().perform();
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"JACKID\"]")));
				WebElement jackfield = driver.findElement(By.xpath("//*[@id=\"JACKID\"]")); 
				jackfield.clear();
				jackfield.sendKeys(jackid);
				driver.findElement(By.xpath("//*[@id=\"BUTTON_3\"]")).click();

			}
			else if(trimmed.charAt(trimmed.length() - 1) == 'V') {
				WebElement a = driver.findElement(By.xpath("//*[@id=\"dgListView_1\"]"));
				Actions act = new Actions(driver);
				act.moveToElement(a).click().click().perform();
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"JACKID\"]")));
				WebElement jackfield = driver.findElement(By.xpath("//*[@id=\"JACKID\"]"));
				String oldjack = jackfield.getAttribute("value"); 
				jackfield.clear();
				String zjack = "z"+oldjack;
				jackfield.sendKeys(zjack);
				driver.findElement(By.xpath("//*[@id=\"BUTTON_3\"]")).click();
			}
			i--;
		}
	}
	public int Searchjack(String cable) {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter1\"]")));
		WebElement element1 = driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]"));
		element1.sendKeys(cable);
		driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
		int i = 0;
		while(i<15) {
			String path = "//*[@id=\"tdDiv_" + i + "_dgListViewCol0\"]";
			int size = driver.findElements(By.xpath(path)).size();
			if(size == 1) {
				returnJacks.add(driver.findElement(By.xpath(path)).getText());
			}
			i++;
		}
		return returnJacks.size();
	}
	
	public void SwitchTojack(){
		driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
	    driver.switchTo().frame(driver.findElement(By.name("contents")));
		driver.findElement(By.xpath("//*[@id=\"td1\"]")).click();
		WebElement element = driver.findElement(By.xpath("//*[@id=\"td8\"]"));
		element.click();
	}
}
