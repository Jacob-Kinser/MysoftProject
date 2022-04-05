package workA;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Auto {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Starting...");
		String current;
		File file2 = new File("");
		try {
			current = new java.io.File( "." ).getCanonicalPath();
			//System.out.println("Current dir:"+current);
			File file = new File(current);
			File file1 = new File(file.getParent());
			file2 = new File(file1.getParent());
			file2 = new File(file2.getPath() +"/" +"geckodriver-v0.29.1-win64/geckodriver.exe");
	        String currentDir = System.getProperty("user.dir");
	       // System.out.println("Current dir using System:" +currentDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    // System.out.println(file2.getPath());
	       System.setProperty("webdriver.gecko.driver", file2.getPath());
	       FirefoxOptions options = new FirefoxOptions()
        	       .setLogLevel(Level.OFF);
        WebDriver driver = new FirefoxDriver(options);
        driver.get("https://mysoft.tele.iastate.edu/");
//        System.setProperty("webdriver.Firefox.silentOutput", "true");
//        System.setProperty("webdriver.gecko.driver","src/main/resources/drivers/geckodriver.exe");
//        System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
//        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
        
        try {
            WebDriverWait wait = new WebDriverWait(driver,5);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            System.out.println("There is no alert");
        }
        System.out.println("A popup to mysoft should have appeared");
        System.out.println("Input your credentials");
        System.out.println("Enter 1 here to continue, Enter 2 if the pop up did not appear");
        Scanner scanner = new Scanner(System.in);
        int myInt = scanner.nextInt();
        if(myInt == 1) {
        	System.out.println("Welcome to Mysoft");
        	//FirefoxProfile FO = new FirefoxProfile();
        }
        else if(myInt == 2) {
        	System.out.println("Try running the program again");
        }
        else {
        	System.out.println("Unexpected input, exiting");
        }
        
//        CreateAccount ca = new CreateAccount(driver);
//        ca.switchToAccount();
//        driver.switchTo().parentFrame();
//		driver.switchTo().frame(driver.findElement(By.name("main")));
//        ca.fullCreate("X991012B1-D", "5/17/2021", "4444");
        
//        CreateJack cj = new CreateJack(driver);
//        cj.SwitchTojack();
//        driver.switchTo().parentFrame();
//		driver.switchTo().frame(driver.findElement(By.name("main")));
//		System.out.println(cj.Searchjack("X991012"));
//		System.out.println(cj.Searchjack("X99101212121"));
//        CreateCable cable = new CreateCable(driver);
//        cable.Switchtocable();
//		driver.switchTo().parentFrame();
//		driver.switchTo().frame(driver.findElement(By.name("main")));
//		cable.Searchcable("X991012");
//		for(int i = 0; i < cable.returnCables.size();++i){
//			String s = "//*[@id=\"dgListView_" + i + "_0\"]";
//			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));
//			WebElement jackfield = driver.findElement(By.xpath(s));
//			String title = jackfield.getAttribute("title");
//			System.out.println("title " + i + ": " + title);
//		}
		
//        	CreateJack cable = new CreateJack(driver);
//        	cable.SwitchTojack();
//			driver.switchTo().parentFrame();
//			driver.switchTo().frame(driver.findElement(By.name("main")));
//			cable.Searchjack("X991012");
//			for(int i = 0; i < cable.returnJacks.size();++i){
//				String s = "//*[@id=\"dgListView_" + i + "_0\"]";
//				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));
//				WebElement jackfield = driver.findElement(By.xpath(s));
//				String title = jackfield.getAttribute("title");
//				System.out.println("title " + i + ": " + title);
//			}
        
//    	CreateAccount cable = new CreateAccount(driver);
//    	cable.switchToAccount();
//		driver.switchTo().parentFrame();
//		driver.switchTo().frame(driver.findElement(By.name("main")));
//		cable.searchAccount("j402220b");
//		System.out.println("return size: " + cable.returnUsers.size());
//		for(int i = 0; i < cable.returnUsers.size();++i){
//			String s = "//*[@id=\"dgListView_" + cable.returnUsersNum.get(i) + "_0\"]";
//			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));
//			WebElement jackfield = driver.findElement(By.xpath(s));
//			String title = jackfield.getAttribute("title");
//			System.out.println("title " + cable.returnUsersNum.get(i) + ": " + title);
//		}
         fullZout z = new fullZout(driver,false);
    	CreateCable cable = new CreateCable(driver);
		CreateJack jack = new CreateJack(driver);
		CreateAccount account = new CreateAccount(driver);
		
//		cable.fullCreate("E6302B1-D", "E63", "E63-BC", "E6302");
//		cable.Searchcable("X99");
//		//z.zCable("//*[@id=\"dgListView_0\"]");
////		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cable.returnCablesX.get(0))));
////		WebElement field = driver.findElement(By.xpath(cable.returnCablesX.get(0)));
////		Actions act = new Actions(driver);
////		act.moveToElement(field).click().click().perform();
////		Thread.sleep(5000);
////		driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
//		z.jackSwitch(jack);
//		jack.fullCreate("E6302B1-D", "E6302",  "E63");
//		jack.Searchjack("x99");
//		//z.accountSwitch(account);
//		//account.searchAccount("x99");
//		z.Reset();
//		
//		z.cableSwitch(cable);
//		cable.Searchcable("X991010");
//		z.jackSwitch(jack);
//		jack.Searchjack("x991010");
//		driver.navigate().refresh();
//		Thread.sleep(1500);
//		z.accountSwitch(account);
//		account.searchAccount("x99");
//		z.Reset();
//		
//		z.cableSwitch(cable);
//		cable.Searchcable("X991011");
//		z.jackSwitch(jack);
//		jack.Searchjack("x991011");
//		z.Reset();
		
//		for(int i = 0; i < 1000;++i) {
//			z.cableSwitch(cable);
//			cable.Searchcable("E63G70EB1-D");
//			z.jackSwitch(jack);
//			jack.Searchjack("E63G70EB1-D");
//			z.accountSwitch(account);
//			account.searchAccount("E63G70EB1-D");
//			z.Reset();
//			if(cable.returnCables.size() != 1 || jack.returnJacks.size() != 1 || account.returnUsers.size() != 1)
//			System.out.println("i: " + i + " cable size: " + cable.returnCables.size() + " jack size: " + jack.returnJacks.size() + " account size: " + account.returnUsers.size());
//			
//		}
			
		
        System.out.println("Select option");
        System.out.println("1. Folder 3 -> 4");
        System.out.println("2. Worktag Collection");
        System.out.println("3. ");
        System.out.println("4. ");
        System.out.println("6. View Jacks");
        System.out.println("7. Full Create");
        System.out.println("8. Cable and Jack Create");
        System.out.println("9. Full update");
        System.out.println("10. Fully update cable and jack");
        System.out.println("11. Full Z out");
        System.out.println("12. Fully Z out Cable and Jack");
        System.out.println("13. Full account create"); 
        System.out.println("14. Verify");
        System.out.println("15. Update card type TEMP");
        myInt = scanner.nextInt();
        if(myInt == 1) {
        	System.out.println("Folder 3 update");
        	Folder3Update a = new Folder3Update(driver);
        	a.update();
        }
        else if(myInt == 2) {
        	onlyApplication(driver);
        	System.out.println("Worktag Collection");
        	WorktagUpdate a = new WorktagUpdate(driver);
        	a.Update();
		}
        else if(myInt == 3) {
			
		}
        else if(myInt == 4) {
			
		}
        else if(myInt == 5) {
			
		}
        else if(myInt == 6) {
        	System.out.println("Read Jacks Selected");
			ReadJacks a = new ReadJacks(driver);
			a.read();
		}
        else if(myInt == 7) {
			System.out.println("Full Create selected");
			fullCreate a = new fullCreate(driver,true);
			a.create();
		}
        else if(myInt == 8) {
     		System.out.println("Cable and Jack Create selected");
     		fullCreate a = new fullCreate(driver, false);
     		a.create();
     	}
        else if(myInt == 9) {
        	System.out.println("Full update selected");
        	FullZoutAndCreate a = new FullZoutAndCreate(driver,true);
        	a.Full();
		}
        else if(myInt == 10) {
        	System.out.println("Fully update cable and jack");
        	FullZoutAndCreate a = new FullZoutAndCreate(driver,false);
        	a.Full();
		}
        else if(myInt == 11) {
//			System.out.println("Full Z out selected");
//			fullZout zOut = new fullZout(driver,true);
//			zOut.execute();
		}
        else if(myInt == 12) {
//			System.out.println("Fully Z out Cable and Jack selected");
//			fullZout zOut = new fullZout(driver,false);
//			zOut.execute();
		}
        else if(myInt == 13) {
			System.out.println("Full account create selected");
			CreateAccount a = new CreateAccount(driver);
			a.create();
		}
        else if(myInt == 14) {
			System.out.println("Verify selected");
//			CheckAll a = new CheckAll(driver);
//			a.checkAll();
			Verify a = new Verify(driver);
			a.fullVerify();
		}
        else if(myInt == 15) {
        	OldJacksLocater a = new OldJacksLocater(driver,"cable");
        	a.Findall(false);
//			System.out.println("temp selected");
//			UpdateCardType a = new UpdateCardType(driver);
//			a.update();
		}
        scanner.close();
	}
	
	public static void onlyApplication(WebDriver driver) throws InterruptedException {
		Boolean switched = false;
		while(!switched) {
			try {
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("mainFrame")));
				driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("contents")));
			    driver.switchTo().frame(driver.findElement(By.name("contents")));
			    new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tdApplication\"]")));
			    new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"tdApplication\"]")));
				driver.findElement(By.xpath("//*[@id=\"tdApplication\"]")).click();
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"NewTask\"]")));
				new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"NewTask\"]")));
				WebElement element = driver.findElement(By.xpath("//*[@id=\"NewTask\"]"));
				
				//String parentWindow = driver.getWindowHandle();
				 String closetWindow = null;
				 
				element.click();
				
				 Set<String> handles = driver.getWindowHandles(); // get all window handles
				 Iterator<String> iterator = handles.iterator();
				 while (iterator.hasNext()){
					 closetWindow = iterator.next();
				 }
				 driver.switchTo().window(closetWindow); 
				 

			
				// driver.switchTo().window(parentWindow); 
				
				
				
				switched = true;
			}catch(TimeoutException ex) {
					System.out.println(ex);
					driver.navigate().refresh();
					Thread.sleep(4000);
			}
		}
	}

}



//CreateCable a = new CreateCable(driver);
// a.Searchcable();
// a.create();
// a.Switchtocable();
// driver.switchTo().parentFrame();
// driver.switchTo().frame(driver.findElement(By.name("main")));
//	WebElement element1 = driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]"));
//	element1.sendKeys("E63201");
//	driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
//	element1= driver.findElement(By.xpath("//*[@id=\"dgListView_0\"]"));
//	System.out.println(driver.findElement(By.xpath("//*[@id=\"dgListView_0_0\"]")).getText());
//	Actions act = new Actions(driver);
//	act.moveToElement(element1).click().click().perform();
