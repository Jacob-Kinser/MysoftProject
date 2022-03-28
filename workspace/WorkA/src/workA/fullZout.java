package workA;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
/*
 * 1st get all rooms and jacks on spreadsheet
 * 2nd search all rooms on mysoft and compare then with jacks on spreadsheet
 */
public class fullZout {
	WebDriver driver;
	ArrayList<String> JackIDs = new ArrayList<>();
	ArrayList<String> allRooms = new ArrayList<>();
	Boolean AccountCreate;
	
	/*
	 * might not need this?
	 * maybe when creating ?
	 * 
	 */
	ArrayList<Boolean> ZdOut = new ArrayList<>();
	
	/*
	 * Returns the row where there are two elements left -v and -d 
	 * that can be updated to b1
	 */
	ArrayList<Integer> twoLeft = new ArrayList<>();
	
	public fullZout(WebDriver d, Boolean AC) {
		driver = d;
		AccountCreate = AC;
	}
	
	public void execute() throws InterruptedException {
		Readexcel ES;
		if(AccountCreate)
			ES = new Readexcel("ZoutAC");
		else
			ES = new Readexcel("Zout");
		getAllJacks(ES);
		zOut(ES, false);
//		for(int i = 0; i < 2; ++i) {
//			String a = "J402200";

//		}
		System.out.println("jacks: " + JackIDs);
		System.out.println("Zd out: " + ZdOut);
		System.out.println("ones left to be updated: " + twoLeft);
		
	}
	
	public void zOut(Readexcel ES, Boolean update) throws InterruptedException {
		CreateCable cable = new CreateCable(driver);
		CreateCable cable2 = new CreateCable(driver);
		CreateJack jack = new CreateJack(driver);
		CreateJack jack2 = new CreateJack(driver);
		CreateAccount account = new CreateAccount(driver);
		CreateAccount account2 = new CreateAccount(driver);
		int row = 1;
		int NullinaRow = 0;
		while(NullinaRow != 4) {
			Boolean CableZd = false;
			Boolean JackZd = false;
			Boolean AccountZd = false;
			Boolean canBeUpdated = false;
			String jackID = ES.RWcell(ES.jackNum, row, null, 0);
			String room = ES.RWcell(ES.roomNum, row, null, 0);
			String date = "";
			date = ES.endDate;
			
			if(jackID.equals("empty") || room.equals("empty")) {
				ES.RWcell(ES.noteNum, row, "One or more field empty", 1);
				NullinaRow++;
			}
			else {
				if(update)
				canBeUpdated = canBeUpdatedCheck(cable,jack,account,jackID,room);
				//System.out.println(canBeUpdated + " for row " + row);
				System.out.println(jackID + " " + room + " row: " + row + " in z out, update: " + canBeUpdated);
				//Thread.sleep(4000);
				cableSwitch(cable);
				cable.Searchcable(jackID);
				//Thread.sleep(4000);
				cable2.Searchcable(room);
				//Thread.sleep(4000);
				int i = cable.returnCables.size();
				int j = cable2.returnCables.size() - 1;
				/*
				 * returned two results when searching room
				 * returned none when searching for jack
				 */
				if(i != 1) {
					if(canBeUpdated && update) {
						//System.out.println("In cable if how am  i here"+ jackID + " " + j );
						//canBeUpdated = checkVD();
						if(!canBeUpdated) {
							//make note needs to be updated manually
							System.out.println("can't be updated, Z out as normal");
						}
					}
					if(!canBeUpdated) {
						//System.out.println("In cable else"+ jackID + " " + j + " " + cable2.returnCables);
						while(j > -1) {
							String s = "//*[@id=\"dgListView_" + cable2.returnCablesNum.get(j) + "_0\"]";
							new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));
							WebElement jackfield = driver.findElement(By.xpath(s));
							String title = jackfield.getAttribute("title");
							//System.out.println("title and j: " + title + " "+ j);
							if(!JackIDs.contains(title)) {
								zCable(cable2.returnCablesX.get(j));
								CableZd = true;
							}
							else {
								//System.out.println("jackid contained" + title);
							}
							j--;
						}
					}
				}
				jackSwitch(jack);
				jack.Searchjack(jackID);
				//Thread.sleep(4000);
				jack2.Searchjack(room);
				//Thread.sleep(4000);
				i = jack.returnJacks.size();
				j = jack2.returnJacks.size() - 1;
				if(i != 1) {
					if(update && canBeUpdated) {
						//System.out.println("In jack if"+ i + j);
					//	canBeUpdated = checkVD();
						if(!canBeUpdated) {//manual update needed
							ES.RWcell(ES.noteNum, row, "Manual update needed", 1);
						}
					}
					else { 
						//System.out.println("In jack else"+ i + j);
						while(j > - 1) {
							String s = "//*[@id=\"dgListView_" + jack2.returnJacksNum.get(j) + "_0\"]";
							new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));
							WebElement jackfield = driver.findElement(By.xpath(s));
							String title = jackfield.getAttribute("title");
							if(!JackIDs.contains(title)) {
								zJack(jack2.returnJacks.get(j));
								JackZd = true;
							}
							j--;
						}
					}
				}
				accountSwitch(account);
				//if(AccountCreate) {
					account.searchAccount(jackID);
					//Thread.sleep(4000);
					account2.searchAccount(room);
					//Thread.sleep(4000);
					i = account.returnUsers.size();
					j = account2.returnUsers.size() - 1;
					
					if(i == 0) {
						if(update && canBeUpdated) {
				
						}
						else {
							//System.out.println("In account else"+ i + j);
							while(j > -1) {
								String s = "//*[@id=\"dgListView_" + account2.returnUsersNum.get(j) + "_0\"]";
								new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));
								WebElement jackfield = driver.findElement(By.xpath(s));
								String title = jackfield.getAttribute("title");
								if(!JackIDs.contains(title)) {
									System.out.println("setting enddate");
									endDate(account2.returnUsersX.get(j),date);
									AccountZd = true;
								}
								j--;
							}
						}
					}
					Reset();
					if((CableZd && JackZd && AccountZd) || (CableZd && JackZd && !update)) {
						/*
						 * Maybe?
						 * or maybe remove from list
						 * then when you create you can just see if that jack is on the list
						 * and create if it is there? maybe idk bye
						 */
						System.out.println("zd em out");
						ZdOut.set(row - 1, true);
					}
					else if (canBeUpdated) {
						System.out.println("They can be updated");
						twoLeft.add(row);
					}
					else if(CableZd || AccountZd || CableZd) {
						System.out.println("CHECK HERE: zD out something but not everything");
					}
					else {
						System.out.println("Already existed or does not exist");
					}
				//}
				/*else {
					if(CableZd && JackZd) {
						ZdOut.set(row - 1, true);
					}
					else if (canBeUpdated) {
						twoLeft.add(row);
					}
					Reset();
				}*/
				NullinaRow = 0;
			}
			row++;
			//if(row == 64) Thread.sleep(600000);
		}
		System.out.println(row);
	}
	
	public void getAllJacks(Readexcel ES) {
		int row = 1;
		int NullinaRow = 0;
		while(NullinaRow != 4) {
			String jack = ES.RWcell(ES.jackNum, row, null, 0);
			String room = ES.RWcell(ES.roomNum, row, null, 0);
			if(room.equals("empty")) {
				NullinaRow++;
			}
			else {
				JackIDs.add(jack);
				allRooms.add(room);
				ZdOut.add(false);
				if(jack.contains("AA-D") || jack.contains("AC-D")) {
					String V = jack.substring(0, jack.length() - 1);
					JackIDs.add(V + "V");
					ZdOut.add(false);
					
				}
				if(jack.contains("B1-D") || jack.contains("B2-D") || jack.contains("B3-D") || jack.contains("B4-D") || jack.contains("B5-D") ||
						jack.contains("B6-D") ||jack.contains("B7-D") ||jack.contains("B8-D") || jack.contains("B9-D") ||jack.contains("B10-D") ||
						jack.contains("B11-D") || jack.contains("B12-D") || jack.contains("B13-D") || jack.contains("B14-D") || jack.contains("B15-D") ||
						jack.contains("B16-D") || jack.contains("B17-D") || jack.contains("B18-D") || jack.contains("B19-D") || jack.contains("B20-D") ||
						jack.contains("B21-D") || jack.contains("B22-D") || jack.contains("B23-D") || jack.contains("B24-D") || jack.contains("B25-D") ||
						jack.contains("B26-D") || jack.contains("B27-D") || jack.contains("B28-D") ||jack.contains("B29-D") || jack.contains("B30-D") ||
						jack.contains("B31-D") || jack.contains("B32-D") || jack.contains("B33-D") || jack.contains("B34-D") || jack.contains("B35-D") ||
						jack.contains("AA-D") || jack.contains("AB-D") || jack.contains("AC-D") || jack.contains("AD-D") || jack.contains("AE-D")) {
					String newS = jack + "*";
					String V = jack.substring(0, jack.length() - 2);
					JackIDs.add(newS);
					JackIDs.add(V+"W");
					
				}
				NullinaRow = 0;
			}
			row++;
		}
	}
	
	public void zCable(String a) throws InterruptedException {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(a)));
		WebElement field = driver.findElement(By.xpath(a));
		Actions act = new Actions(driver);
		act.moveToElement(field).click().click().perform();
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
		Thread.sleep(2000);
		//String parentWindow = driver.getWindowHandle();
		waitForAlert();
		//driver.switchTo().alert().accept();
		//driver.switchTo().window(parentWindow);
		Thread.sleep(2000);
	}
	
	public void zJack(String s) {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));
		WebElement a = driver.findElement(By.xpath(s));
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
	
	public void endDate(String a, String date) throws InterruptedException {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(a)));
		WebElement element = driver.findElement(By.xpath(a));
		Actions act = new Actions(driver);
		act.moveToElement(element).click().click().perform();
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"DATECLOSED\"]")));
		driver.findElement(By.xpath("//*[@id=\"DATECLOSED\"]")).sendKeys(date);
		driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
		Thread.sleep(1000);
		Boolean isTherealert = isAlertPresent();
		if(isTherealert) {
			System.out.println("Updated userI");
			//waitForAlert();
		}
	}
	
	public Boolean checkVD() {
		String R1 = "//*[@id=\"dgListView_" + 0 + "_0\"]";
		String R2 = "//*[@id=\"dgListView_" + 1 + "_0\"]";
		
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(R1)));
		WebElement jackfield = driver.findElement(By.xpath(R1));
		String titleR1 = jackfield.getAttribute("title");
		
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(R2)));
		jackfield = driver.findElement(By.xpath(R2));
		String titleR2 = jackfield.getAttribute("title");
		//System.out.println("R1 AND R2: " +titleR1 + " " + titleR2);
		if(titleR1.contains("AA-D") && titleR2.contains("AA-V")) {
			String NewR1 = titleR1.substring(0, titleR1.length() - 4);
			String R1B1 = NewR1 + "B1-D";
			//System.out.println("New R1: " + NewR1 + " R1B1: " + R1B1);
			if(JackIDs.contains(R1B1)) {
				return true;
			}
		}
		return false;
	}
	
	public void cableSwitch(CreateCable cable) throws InterruptedException {
		cable.Switchtocable();
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
	}
	
	public void jackSwitch(CreateJack jack) throws InterruptedException {
		driver.switchTo().parentFrame();
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("contents")));
		driver.switchTo().frame(driver.findElement(By.name("contents")));
		driver.findElement(By.xpath("//*[@id=\"td1\"]")).click();
		driver.switchTo().parentFrame();
		driver.switchTo().parentFrame();
		jack.SwitchTojack();
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
	}
	
	public void accountSwitch(CreateAccount account) throws InterruptedException {
		driver.switchTo().parentFrame();
		driver.switchTo().parentFrame();
		account.switchToAccount();
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
	}
	
	public void Reset() {
		driver.switchTo().parentFrame();
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("contents")));
		driver.switchTo().frame(driver.findElement(By.name("contents")));
		driver.findElement(By.xpath("//*[@id=\"td1\"]")).click();
		driver.findElement(By.xpath("//*[@id=\"td15\"]")).click();
		driver.switchTo().parentFrame();
		driver.switchTo().parentFrame();
	}
	
	/*
	 * Can be updated logic
	 * if there is two cables, two jacks, and at least 1 account - it can be updated
	 * 
	 * if there is more than two cables and jacks, search jack list and see if the room is listed on there more than once
	 * 
	 */
	public boolean canBeUpdatedCheck(CreateCable cable, CreateJack jack, CreateAccount account, String jackid, String room) throws InterruptedException {
		cableSwitch(cable);
		int cSize = cable.Searchcable(room);
	//	ArrayList<WebElement> cRoom = cable.returnCables;
		int cSize2 = cable.Searchcable(jackid);
	//	ArrayList<WebElement> cJack = cable.returnCables;
		jackSwitch(jack);
		int jSize = jack.Searchjack(room);
		//ArrayList<WebElement> jRoom = cable.returnCables;
		int jSize2 = jack.Searchjack(jackid);
		//ArrayList<WebElement> jJack = cable.returnCables;
		accountSwitch(account);
		account.searchAccount(room);
		int aSize = account.returnUsers.size();
		//ArrayList<WebElement> aRoom = cable.returnCables;
		account.searchAccount(jackid);
		int aSize2 = account.returnUsers.size();
		//ArrayList<WebElement> aJack = cable.returnCables;
		Reset();
		Boolean TwoLeft =  ((cSize == 2 && cSize2 == 0) && (jSize == 2 && jSize2 == 0) && (aSize > 0 && aSize2 == 0));
		if(TwoLeft) return true;
		
		if(cSize2 == 0 && jSize2 == 0 && aSize2 == 0) { //room was not returned
			if(cSize > 0 && jSize > 0 && aSize > 0) { //there is a jack to update
//				int found = 0;
//				for(int i = 0; i < JackIDs.size(); ++i) {
//					if(JackIDs.get(i).contains(room)) {
//						found++;
//					}
//				}
				//if(found == 3) {
					return true;
				//}
			}
		}
		
		return false;
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
