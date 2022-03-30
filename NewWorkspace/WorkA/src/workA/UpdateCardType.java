package workA;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

public class UpdateCardType {
	WebDriver driver;
	ArrayList<WebElement> returnCables = new ArrayList<>();
	ArrayList<String> returnCablesX = new ArrayList<>();
	ArrayList<Integer> returnCablesNum = new ArrayList<>();
	public UpdateCardType(WebDriver d) {
		driver = d;
	}
	
	public void update() throws InterruptedException {
		Readexcel ES = new Readexcel("folder3update");
		SwitchToPort();
		int row = 1;
		int NullinaRow = 0;
		while(NullinaRow != 7) {
			String PortNbr = ES.RWcell(ES.PortNbrNum, row, null, 0);
			if(PortNbr.equals("empty")) {
				ES.RWcell(ES.noteNum, row, "One or more field empty", 1);
				NullinaRow++;
			}
			else {
				System.out.println("row: " + row + " " + PortNbr);
				Searchcable(PortNbr);
				if(returnCablesX.size() == 1) {
					String s = returnCablesX.get(0);
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));
					WebElement a = driver.findElement(By.xpath(s));
					Actions act = new Actions(driver);
					act.moveToElement(a).click().click().perform();
					
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"PHONECARDTYPESEQ_CARDTYPE\"]")));
					Select searchBy = new Select(driver.findElement(By.xpath("//*[@id=\"PHONECARDTYPESEQ_CARDTYPE\"]")));
					if(PortNbr.contains("M0")) {
						searchBy.selectByValue("136");
						ES.RWcell(ES.noteNum, row, "Updated Menet", 1);
					}
					else {
						searchBy.selectByValue("137");
						ES.RWcell(ES.noteNum, row, "Updated Xenet", 1);
					}
					
					driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
				}
				else {
					ES.RWcell(ES.noteNum, row, "None Found", 1);
				}
				NullinaRow = 0;
			}
			
			row++;
		}
	}
	
	public void SwitchToPort() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("mainFrame")));
		driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("contents")));
	    driver.switchTo().frame(driver.findElement(By.name("contents")));
	    new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td113\"]")));
		driver.findElement(By.xpath("//*[@id=\"td113\"]")).click();
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td117\"]")));
		driver.findElement(By.xpath("//*[@id=\"td117\"]")).click();
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td118\"]")));
		WebElement element = driver.findElement(By.xpath("//*[@id=\"td118\"]"));
		element.click();
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
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
					SwitchToPort();
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
	

}
