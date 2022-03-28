package workA;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ReadJacks {
	WebDriver driver;
	public ReadJacks(WebDriver d) {
		driver = d;
	}
	public void read() throws InterruptedException {
		SwitchTojack();
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
	public void SwitchTojack(){
		driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
	    driver.switchTo().frame(driver.findElement(By.name("contents")));
		driver.findElement(By.xpath("//*[@id=\"td1\"]")).click();
		WebElement element = driver.findElement(By.xpath("//*[@id=\"td8\"]"));
		element.click();
	}
}
