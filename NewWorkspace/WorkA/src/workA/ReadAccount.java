package workA;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ReadAccount {
	WebDriver driver;
	
	public ReadAccount(WebDriver d) {
		driver = d;
	}
	
	public void Read() throws InterruptedException {
		switchToAccount();
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
		int row = 1;
	    Readexcel ES = new Readexcel("read");
	    int NullinaRow = 0;
	    while(NullinaRow != 4) {
	    	String jackid = ES.RWcell(ES.jackNum, row, null, 0);
			if(jackid.equals("empty")) {
				//ES.RWcell(ES.noteNum, row, "Missing jackid", 1);
				NullinaRow++;
			}
			else {
				WebElement element1 = driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]"));
				element1.sendKeys(jackid);
				driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
				//check if it is created
				Thread.sleep(ES.sleepTime * 1000);
				NullinaRow = 0;
			}
			row++;
	    }
	}
	
	public void switchToAccount() {
		driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
	    driver.switchTo().frame(driver.findElement(By.name("contents")));
		driver.findElement(By.xpath("//*[@id=\"td15\"]")).click();
		WebElement element = driver.findElement(By.xpath("//*[@id=\"td17\"]"));
		element.click();
	}
}
