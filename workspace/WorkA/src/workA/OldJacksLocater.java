package workA;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OldJacksLocater {
	WebDriver driver;
	String search;
	ArrayList<String> OldJacks = new ArrayList<>();
	
	
	public OldJacksLocater(WebDriver d, String search) {
		driver = d;
		this.search = search;
	}
	
	public void Zall() {
		
	}
	
	public void Findall(Boolean Zout) throws InterruptedException {
		Readexcel ES = new Readexcel("oldjacks");
		int totalSize = setSearch(ES.buildingCode);
		fullZout Z = new fullZout(driver,false);
		System.out.println("finding all jacks");
		Z.getAllJacks(ES);
		System.out.println("All jacks obtained");
		//System.out.println(Z.JackIDs + "\n\n\n\n");
		int next = driver.findElements(By.xpath("//*[@id=\"dgListView_0_0\"]")).size();
		int row = 0;
		int iterator = 0;
		String path = "//*[@id=\"dgListView_" + row + "_0\"]";
		while(next!=0 && iterator < totalSize) {
			String jackid = driver.findElement(By.xpath(path)).getAttribute("title");
			System.out.println(jackid);
			if(!Z.JackIDs.contains(jackid)) {
				Boolean old = true;
				if(!jackid.contains("-D")) {
					String jackidtemp =jackid+ "-D";
					if(Z.JackIDs.contains(jackidtemp)) {
						old = false;
					}
				}
				if(old) {
					OldJacks.add(jackid);
					if(Zout) {
						if(search.equals("cable")) {
							ZoutCable(row);
						}
						else if(search.equals("jack")) {
							ZoutJack(row);
						}
						else {
							ZoutAccount(row);
						}
					}
				}
			}
			iterator++;
			row++;
			if(row == 200 && iterator < totalSize) {
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdNext\"]")));
				driver.findElement(By.xpath("//*[@id=\"cmdNext\"]")).click();
				row = 0;
			}
			path = "//*[@id=\"dgListView_" + row + "_0\"]";
			next=driver.findElements(By.xpath(path)).size();
		}
		System.out.println(iterator);
		Thread.sleep(5000);
		System.out.println("\n\n\n\nOld jacks, size: " + OldJacks.size());
		for(String s:OldJacks) {
			System.out.println(s);
		}
		
	}
	
	public int setSearch(String buildingCode) throws InterruptedException {
		int size = 0;
		CreateCable cable = new CreateCable(driver);
		CreateJack jack = new CreateJack(driver);
		CreateAccount account = new CreateAccount(driver);
		if(search.equals("cable")) {
			cable.Switchtocable();
			driver.switchTo().parentFrame();
			driver.switchTo().frame(driver.findElement(By.name("main")));
			cable.Searchcable(buildingCode);
		}
		else if(search.equals("jack")) {
			jack.SwitchTojack();
			driver.switchTo().parentFrame();
			driver.switchTo().frame(driver.findElement(By.name("main")));
			jack.Searchjack(buildingCode);
		}
		else {
			account.switchToAccount();
			driver.switchTo().parentFrame();
			driver.switchTo().frame(driver.findElement(By.name("main")));
			account.searchAccount(buildingCode);
		}
		String strSize = driver.findElement(By.xpath("/html/body/form/div[1]/table[3]/tbody/tr[1]/td/table/tbody/tr/td[1]")).getText();
		System.out.println(strSize);
		String sizeReversed = "";
		for(int i = strSize.length() - 1; i > 0; i--){
			if(strSize.charAt(i) == ' ') {
				i = 0;
			}
			else {
				sizeReversed+=strSize.charAt(i);
			}
		}
		String reverse = "";
		for(int i = sizeReversed.length() - 1; i > -1; i--) {
			reverse+=sizeReversed.charAt(i);
		}
		size = Integer.parseInt(reverse);
		System.out.println(size);
		return size;
	}
	
	public void ZoutCable(int row) throws InterruptedException {
		SelectItem(row);
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"DESCRIPTION\"]")));
		WebElement jackfield = driver.findElement(By.xpath("//*[@id=\"DESCRIPTION\"]"));
		String jack = jackfield.getAttribute("value"); 
		jackfield.clear();
		String zjack = "z"+jack;
		jackfield.sendKeys(zjack);
		WebElement closetfield = driver.findElement(By.xpath("//*[@id=\"ORIGDISTRIBUTIONSEQ_DISTRIBUTIONID\"]"));
		closetfield.clear();
		closetfield.sendKeys("z");
		WebElement roomfield = driver.findElement(By.xpath("//*[@id=\"TERMDISTRIBUTIONSEQ_DISTRIBUTIONID\"]"));
		roomfield.clear();
		roomfield.sendKeys("z");
		driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
		waitForAlert();
	}
	public void ZoutJack(int row) {
		SelectItem(row);
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"JACKID\"]")));
		WebElement jackfield = driver.findElement(By.xpath("//*[@id=\"JACKID\"]"));
		String oldjack = jackfield.getAttribute("value"); 
		jackfield.clear();
		String zjack = "z"+oldjack;
		jackfield.sendKeys(zjack);
		driver.findElement(By.xpath("//*[@id=\"BUTTON_3\"]")).click();
	}
	public void ZoutAccount(int row) {
		SelectItem(row);
	}
	
	public void SelectItem(int row) {
		String s = "";
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));
		WebElement a = driver.findElement(By.xpath(s));
		Actions act = new Actions(driver);
		act.moveToElement(a).click().click().perform();
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
}
