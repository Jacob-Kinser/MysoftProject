package workA;

import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UpdateCable {
	WebDriver driver;
	ArrayList<String> returnCables = new ArrayList<>();
	public UpdateCable(WebDriver d) {
		driver = d;
	}
	public void updateCable() throws InterruptedException {
		 Switchtocable();
		 driver.switchTo().parentFrame();
		 driver.switchTo().frame(driver.findElement(By.name("main")));
		 returnCables.clear();
		 Readexcel ES = new Readexcel("createcable");
		int row = 1;
		int NullinaRow = 0;
		Thread.sleep(300);
		while(NullinaRow!=4) {	
			String jackid = ES.RWcell(ES.jackNum, row, null, 0);
			String closet = ES.RWcell(ES.closetNum, row, null, 0);
			String room = ES.RWcell(ES.roomNum, row, null, 0);
			returnCables.clear();
			int cablesFound = Searchcable(room);
			if(room.equals("empty") || closet.equals("empty") || jackid.equals("empty")) {
				ES.RWcell(ES.noteNum, row, "One or more field empty", 1);
				NullinaRow++;
			}
			 else if(cablesFound == 2) { //search distribution
				UpdateVD(jackid,closet);
			 }
			 else if(cablesFound == 1){
				 String trimmed = returnCables.get(0).trim();
				 if(trimmed.equals(jackid)) {
					 ES.RWcell(ES.noteNum, row, "Already Created", 1);
				 }
				 else {
					 
				 }
			 }
			 row++;
		}
		 
	}
	
	public void UpdateVD(String jackid, String closet) throws InterruptedException {
		int i = returnCables.size() - 1;
		 while(i > -1) {
			 String trimmed = returnCables.get(i).trim();
			 if(trimmed.charAt(trimmed.length() - 1) == 'D') {
				WebElement a = driver.findElement(By.xpath("//*[@id=\"dgListView_0\"]"));
				Actions act = new Actions(driver);
				act.moveToElement(a).click().click().perform();
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"DESCRIPTION\"]")));
				WebElement jackfield = driver.findElement(By.xpath("//*[@id=\"DESCRIPTION\"]"));
				jackfield.clear();
				jackfield.sendKeys(jackid);
				
				
				WebElement closetfield = driver.findElement(By.xpath("//*[@id=\"ORIGDISTRIBUTIONSEQ_DISTRIBUTIONID\"]"));
				closetfield.clear();
				closetfield.sendKeys(closet);
				
				driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
				String parentWindow = driver.getWindowHandle();
				driver.switchTo().alert().accept();
				driver.switchTo().window(parentWindow);
				/*
				 * write x as done
				 */
			 }
			 else if(trimmed.charAt(trimmed.length() - 1) == 'V'){
				WebElement a = driver.findElement(By.xpath("//*[@id=\"dgListView_1\"]"));
				Actions act = new Actions(driver);
				act.moveToElement(a).click().click().perform();
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"DESCRIPTION\"]")));
				WebElement jackfield = driver.findElement(By.xpath("//*[@id=\"DESCRIPTION\"]"));
				String jack = jackfield.getAttribute("value"); 
				jackfield.clear();
				//System.out.println(jack);
				String zjack = "z"+jack;
				jackfield.sendKeys(zjack);
				WebElement closetfield = driver.findElement(By.xpath("//*[@id=\"ORIGDISTRIBUTIONSEQ_DISTRIBUTIONID\"]"));
				closetfield.clear();
				closetfield.sendKeys("z");
				WebElement roomfield = driver.findElement(By.xpath("//*[@id=\"TERMDISTRIBUTIONSEQ_DISTRIBUTIONID\"]"));
				roomfield.clear();
				roomfield.sendKeys("z");
				driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
				Thread.sleep(5000);
				String parentWindow = driver.getWindowHandle();
				driver.switchTo().alert().accept();
				driver.switchTo().window(parentWindow);
				Thread.sleep(5000);
				
			 }
			 else{
				 
			 }
			 
			 i--;
		 }
	}
	public int Searchcable(String cable) {
//        Switchtocable();
//		driver.switchTo().parentFrame();
//	    driver.switchTo().frame(driver.findElement(By.name("main")));
		returnCables.clear();
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter1\"]")));
		WebElement element1 = driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]"));
		element1.sendKeys(cable);
		driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
		int i = 0;
		while(i<15) {
			String path = "//*[@id=\"tdDiv_" + i + "_dgListViewCol0\"]";
			int size = driver.findElements(By.xpath(path)).size();
			if(size == 1) {
				returnCables.add(driver.findElement(By.xpath(path)).getText());
			}
			i++;
		}
		return returnCables.size();
	}
	public void Switchtocable() {
		driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
	    driver.switchTo().frame(driver.findElement(By.name("contents")));
		driver.findElement(By.xpath("//*[@id=\"td1\"]")).click();
		WebElement element = driver.findElement(By.xpath("//*[@id=\"td2\"]"));
		element.click();
	}
}
