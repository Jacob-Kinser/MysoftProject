package workA; //a
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.StaleElementReferenceException;
public class Folder3Update {
	WebDriver driver;//row 340 last one maybe not checked
	ArrayList<WebElement> returnUsers = new ArrayList<>();
	ArrayList<String> returnUsersX = new ArrayList<>();
	ArrayList<Integer> returnUsersNum = new ArrayList<>();
	ArrayList<String> returnUsersIN = new ArrayList<>();
	public Folder3Update(WebDriver d) {
		driver = d;
	}
	
	public void update() throws InterruptedException {
		Readexcel ES = new Readexcel("folder3update");
		
		int row = 1;
		int NullinaRow = 0;
		switchToEqupServices();
		setSearchUserID();
		Boolean hasPort = ES.PortCol;
//		String jackidtest = "J402200B1-D";
//		String newJackidtest = jackidtest.substring(0, jackidtest.length() - 3);
//		Search(newJackidtest);
//		System.out.println(returnUsers.size());
//		for(int i = 0; i < returnUsers.size(); ++i) {
//			WebElement e =  driver.findElement(By.xpath("//*[@id=\"dgListView_" + returnUsersNum.get(i) +"_1\"]"));
//			returnUsersIN.add(e.getAttribute("title"));
//			System.out.println(e.getAttribute("title"));
//		}
		while(NullinaRow != 4) {
			String jackid = ES.RWcell(ES.jackNum, row, null, 0);
			String PortNbr = "";
			if(hasPort) {
				PortNbr = ES.RWcell(ES.PortNbrNum, row, null, 0);
			}
			if(jackid.equals("empty") || PortNbr.equals("empty")) {
				ES.RWcell(ES.noteNum, row, "One or more field empty", 1);
				NullinaRow++;
			}
			else {
				System.out.println("jackid: " + jackid + " row: " + row);
				Boolean failed = true;
				while(failed) {
					try {
						Search(jackid);
						failed = false;
					}catch(NoSuchElementException ex) {
						new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")));
						driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
						failed = true;
					}
				}
				String itemNbr = "";
				for(int i = 0; i < returnUsers.size(); ++i) {
					String titlePath = "//*[@id=\"dgListView_" + returnUsersNum.get(i) +"_1\"]";
					int titleSize = driver.findElements(By.xpath(titlePath)).size();
					if(titleSize>0) {
						WebElement e =  driver.findElement(By.xpath(titlePath));
						returnUsersIN.add(e.getAttribute("title"));
					}
				//System.out.println(e.getAttribute("title"));
				}
				
				if(returnUsers.size() == 0) {
					/*
					 * SEARCHED WITH NORMAL JACK AND NONE FOUND
					 * SEARCH WITH TRIMMED
					 * STILL NONE = NONE FOUND
					 * SOME FOUND = MANUAL UPDATE NEEDED
					 */
					String trimmedjackid = "";
					if(jackid.contains("-D")) {
						trimmedjackid = jackid.substring(0, jackid.length() - 2);
					}
					else {
						trimmedjackid = jackid;
					}
					Search(trimmedjackid);
					if(returnUsers.size() == 0) {
						ES.RWcell(ES.noteNum, row, "none found with no end date", 1);
					}
					else {
						ES.RWcell(ES.noteNum, row, "Manual update needed", 1);
					}
				}
				else {
					itemNbr = returnUsersIN.get(0);
					int AcceptedCount = 0;
					int AcceptedIndex = 0;
					if(returnUsers.size() > 1) { 
						Boolean phone = false;
						for(int i = 0; i < returnUsersIN.size(); ++i) {
							if(returnUsersIN.get(i).contains("CISCO")) {
								ES.RWcell(ES.noteNum, row, "Phone", 1);
								phone  = true;
							}
							if(returnUsersIN.get(i).equals("CENET") || returnUsersIN.get(i).equals("DENET") ||returnUsersIN.get(i).equals("ENETALL")
									|| returnUsersIN.get(i).equals("FENET") || returnUsersIN.get(i).equals("ICENET") || returnUsersIN.get(i).equals("SYSFENET")
									|| returnUsersIN.get(i).equals("SYSCENET")) {
								AcceptedCount++;
								AcceptedIndex = i;
							}
						}
						if(phone) {
							ES.RWcell(ES.noteNum, row, "Phone", 1);
						}
						else if(AcceptedCount == 1) {
							System.out.println("More than one returned, updating: " + driver.findElement(By.xpath((returnUsersX.get(AcceptedIndex)))));
							String updateMessage = "failed";
							while(updateMessage.equals("failed")) {
								updateMessage = updatePort(returnUsersX.get(AcceptedIndex), jackid, hasPort, PortNbr);	
							}
							ES.RWcell(ES.noteNum, row, updateMessage, 1);
						}
						else {
							ES.RWcell(ES.noteNum, row, "More than one field returned", 1);
						}
						
					}
					else if(itemNbr.equals("CENET") || itemNbr.equals("DENET") || itemNbr.equals("ENETALL")
							|| itemNbr.equals("FENET") || itemNbr.equals("ICENET") || itemNbr.equals("SYSFENET")
							|| itemNbr.equals("SYSCENET")) {
							String updateMessage = "failed";
							while(updateMessage.equals("failed")) {
								updateMessage = updatePort(returnUsersX.get(0), jackid, hasPort, PortNbr);	
							}
							ES.RWcell(ES.noteNum, row, updateMessage, 1);
					}
					else if (itemNbr.contains("CISCO")){ 
						System.out.println();
						ES.RWcell(ES.noteNum, row, "Phone", 1);
					}
					else {//do not update
						ES.RWcell(ES.noteNum, row, "Do not update", 1);
					}
				
				}
				NullinaRow = 0;
			}
				row++;
		}
		System.out.println(row);
	}
	
	/*
	 * Filter with end date (use account create setup)
	 * filter item nbrs
	 */
	public void Search(String jackid) throws InterruptedException {
		int j = 0;
		returnUsers.clear();
		returnUsersX.clear();
		returnUsersNum.clear();
		returnUsersIN.clear();
		while(returnUsers.size() == 0 && j!=3) {
			returnUsers.clear();
			returnUsersX.clear();
			returnUsersNum.clear();
			returnUsersIN.clear();
			Boolean Ready = false;
			while(!Ready) {
				try {
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter1\"]")));
					driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]")).sendKeys(jackid);
					new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdGet\"]")));
					new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"cmdGet\"]")));
					driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
					Ready = true;
				}catch(StaleElementReferenceException | NoSuchElementException | NullPointerException ex){
					driver.navigate().refresh();
					Thread.sleep(5000);
					switchToEqupServices();
					driver.switchTo().parentFrame();
					driver.switchTo().frame(driver.findElement(By.name("main")));
					System.out.println("Caught stale element or no element: " + ex.getMessage());
				}
			}
			 
				int digit = 0;
				String path = "//*[@id=\"dgListView_" + digit + "\"]";
				String path2 = "//*[@id=\"dgListView_" + digit + "_4\"]";
				for(int i = 0; i < 10; i++) {
					int size0 = driver.findElements(By.xpath(path2)).size();
					if(size0 == 0) {
						size0 = driver.findElements(By.xpath(path)).size();
						String path3 = "//*[@id=\"dgListView_" + digit + "_3\"]";
						int size1 = driver.findElements(By.xpath(path3)).size();
						if(size0 == 1 && size1 == 1) {
							returnUsersX.add(path);
							returnUsers.add(driver.findElement(By.xpath(path)));
							returnUsersNum.add(digit);
						}
					}
					digit++;
					path = "//*[@id=\"dgListView_" + digit + "\"]";
					path2 = "//*[@id=\"dgListView_" + digit + "_4\"]";
				}
				j++;
		}
	}
	
	public String updatePort(String s, String jackid, Boolean hasPort, String PortNbrStr) throws InterruptedException {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));
		WebElement a = driver.findElement(By.xpath(s));
		Actions act = new Actions(driver);
		act.moveToElement(a).click().click().perform();
		
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"head5\"]")));
		driver.findElement(By.xpath("//*[@id=\"head5\"]")).click();
		

		
		String parentWindow = driver.getWindowHandle();
		//System.out.println("Initial window: " + parentWindow + " " + driver.findElements(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).size());
		 String roomWindow = null;
		 //Thread.sleep(2000);
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdPORTLENSEQ_LENPORTNBR\"]")));
		 driver.findElement(By.xpath("//*[@id=\"cmdPORTLENSEQ_LENPORTNBR\"]")).click();
		 //Thread.sleep(2000);
		 Set<String> handles = driver.getWindowHandles(); // get all window handles
		 Iterator<String> iterator = handles.iterator();
		 while (iterator.hasNext()){
			 roomWindow = iterator.next();
		 }
		 driver.switchTo().window(roomWindow); // switch to popup window
		 //System.out.println("first switch: " + roomWindow);
		 try {
			 new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"selSearchBy\"]")));
			 Select lineType = new Select(driver.findElement(By.xpath("//*[@id=\"selSearchBy\"]")));
			 lineType.selectByValue("299");
		 }catch(TimeoutException ex) {
			 System.out.println(waitForAlert());
			 System.out.println(ex);
//			 driver.switchTo().window(parentWindow);
//			 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")));
//			 driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
			 driver.navigate().refresh();
			 driver.navigate().refresh();
			 System.out.println(waitForAlert());
			 switchToEqupServices();
			 Search(jackid);
			 return "failed";
		 }
		 
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter1\"]")));
		 WebElement datafield = driver.findElement(By.xpath("//*[@id=\"txtFilter1\"]"));
		 datafield.clear();
		 datafield.sendKeys("DATA");
		 
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter2\"]")));
		 WebElement portfield = driver.findElement(By.xpath("//*[@id=\"txtFilter2\"]"));
		 
		 int size1 =  0;
		 int search = 0;
		 while(search < 2 && size1 == 0) {
			 Boolean findField = false;
			 while(!findField) {
				 try {
					 portfield.clear();
					 portfield.sendKeys("*" + jackid);
					 findField = true;
				 }catch(StaleElementReferenceException ex) {
					 System.out.println(ex);
					 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter2\"]")));
					 portfield = driver.findElement(By.xpath("//*[@id=\"txtFilter2\"]"));
	//				 driver.close();
	//				 driver.switchTo().window(parentWindow);
	//				 driver.navigate().refresh();
	//				 switchToEqupServices();
	//				 Search(jackid);
	//				 return "failed";
				 }
			 }
			 
			 new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdGet\"]")));
			 driver.findElement(By.xpath("//*[@id=\"cmdGet\"]")).click();
			 
			 size1 =  driver.findElements(By.xpath("//*[@id=\"dgListView_" + 0 + "_1\"]")).size();
			 search++;
		 }
		 
		
		 
		 ArrayList<String> PortNbr = new ArrayList<>();
		 ArrayList<String> PortPath = new ArrayList<>();
		 String returnMessage = "";
		 for(int i = 0; i < 10; ++i) {
			 String path = "//*[@id=\"dgListView_" + i + "_1\"]";
			 int size =  driver.findElements(By.xpath(path)).size();
			 if(size == 1) {
				WebElement e =  driver.findElement(By.xpath(path));
				String title = e.getAttribute("title");
				System.out.println(title);
				if(hasPort) {
					if(title.equals(PortNbrStr)) {
						PortNbr.add(e.getAttribute("title"));
						System.out.println("first if");
						PortPath.add(path);
					}
				}
				else if(title.charAt(0) != 'z' || title.charAt(0) != 'Z'){
					PortNbr.add(e.getAttribute("title"));
					System.out.println("second if");
					PortPath.add(path);
				}
					
			 }
		 }
		 if(PortNbr.size() == 0) {
			 if(hasPort) {
				 CreatePortNbr(PortNbrStr);
				 returnMessage = "Created Port";
			 }
			 else {
				 returnMessage = "No Ports found";
				 driver.close();
			 }
		 }
		 else if(PortNbr.size() == 1) {
			 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PortPath.get(0))));
			 WebElement port = driver.findElement(By.xpath(PortPath.get(0)));
			 Actions act2 = new Actions(driver);
			 act2.moveToElement(port).click().click().perform();
			 returnMessage = "Updated";
		 }
		 else {
			 int PortIndex = -1;
			 int found = 0;
			 for(int i = 0; i < PortNbr.size(); ++i){
				 String temp = PortNbr.get(i);
				 if(temp.contains("M01P") || temp.contains("M02P") || temp.contains("M03P")
						 || temp.contains("M04P") || temp.contains("M05P") || temp.contains("M06P")
						 || temp.contains("M07P") || temp.contains("M08P") || temp.contains("M09P")
						 || temp.contains("M010P")) {
					 PortIndex = i;
					 found++;
				 }
				 else if(temp.contains("X01P") || temp.contains("X02P") || temp.contains("X03P")
						 || temp.contains("X04P") || temp.contains("X05P") || temp.contains("X06P")
						 || temp.contains("X07P") || temp.contains("X08P") || temp.contains("X09P")
						 || temp.contains("X010P")) {
					 PortIndex = i;
					 found++;
				 }
			 }
			 if(found > 1) {
				 driver.close();
				 returnMessage = "Duplicate ports";
			 }
			 else if(found == 1) {
				 String portPath = PortPath.get(PortIndex);
				 WebElement port = driver.findElement(By.xpath(portPath));
				 Actions act2 = new Actions(driver);
				 act2.moveToElement(port).click().click().perform();
				 returnMessage = "Updated";
			 }
			 else {
				 driver.close();
				 returnMessage = "No Port found, Manual Update needed";
			 }
		 }
		 driver.switchTo().window(parentWindow);  // switch back to parent window
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("mainFrame")));
		 driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("main")));
		 driver.switchTo().frame(driver.findElement(By.name("main")));
//		 System.out.println("last switch back: " + driver.getWindowHandle() + " " + driver.findElements(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).size());
//		 System.out.println("frame size " + driver.findElements(By.name("mainFrame")).size());
//		 System.out.println("frame size " + driver.findElements(By.name("main")).size());
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")));
		 while(driver.findElements(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).size() == 1){
			 driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
		 }
		if(waitForAlert()) {
			returnMessage = "invalid PortNbr";
			 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_CANCEL\"]")));
			 driver.findElement(By.xpath("//*[@id=\"BUTTON_CANCEL\"]")).click();
			 waitForAlert();
		}
		return returnMessage;
		 
		
	}
	
	public void CreatePortNbr(String port) {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdAdd\"]")));
		new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"cmdAdd\"]")));
		driver.findElement(By.xpath("//*[@id=\"cmdAdd\"]")).click();
		
//		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"MOCKSEQ_SWITCHNODEID\"]")));
//		 Select SwitchNode = new Select(driver.findElement(By.xpath("//*[@id=\"MOCKSEQ_SWITCHNODEID\"]")));
//		 SwitchNode.selectByValue("2");
		String parentWindow = driver.getWindowHandle();
		//System.out.println("window before switch: " + parentWindow);
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdLENPORTMASTERSEQ_LENPORTNBR\"]")));
		new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"cmdLENPORTMASTERSEQ_LENPORTNBR\"]")));
		driver.findElement(By.xpath("//*[@id=\"cmdLENPORTMASTERSEQ_LENPORTNBR\"]")).click();
		
		PortWindow(parentWindow, port);
		driver.switchTo().window(parentWindow);
		//System.out.println("switch back before SE: " + parentWindow);
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_3\"]")));
		driver.findElement(By.xpath("//*[@id=\"BUTTON_3\"]")).click();
	}
	
	public void PortWindow(String parentWindow, String port) {
		
		 String portWindow = null;
		 Set<String> handles = driver.getWindowHandles(); // get all window handles
		 Iterator<String> iterator = handles.iterator();
		 while (iterator.hasNext()){
			 portWindow = iterator.next();
		 }
		 driver.switchTo().window(portWindow); // switch to popup window
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cmdAdd\"]")));
		 new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"cmdAdd\"]")));
		 driver.findElement(By.xpath("//*[@id=\"cmdAdd\"]")).click();
		 
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"SWITCHNODESEQ_SWITCHNODEID\"]")));
		 Select SwitchNode = new Select(driver.findElement(By.xpath("//*[@id=\"SWITCHNODESEQ_SWITCHNODEID\"]")));
		 SwitchNode.selectByValue("2");
		 
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"LENPORTNBR\"]")));
		 WebElement datafield = driver.findElement(By.xpath("//*[@id=\"LENPORTNBR\"]"));
		 datafield.sendKeys(port);
		 
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"PHONECARDTYPESEQ_CARDTYPE\"]")));
			Select searchBy = new Select(driver.findElement(By.xpath("//*[@id=\"PHONECARDTYPESEQ_CARDTYPE\"]")));
			if(port.contains("M0")) {
				searchBy.selectByValue("136");
			}
			else {
				searchBy.selectByValue("137");
			}
		 
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"STATUS\"]")));
		 Select status = new Select(driver.findElement(By.xpath("//*[@id=\"STATUS\"]")));
		 status.selectByValue("A");
		 
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")));
		 driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")).click();
		 
		 //dont need to search. automatically selects
//		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"selSearchBy\"]")));
//		 Select searchBy = new Select(driver.findElement(By.xpath("//*[@id=\"selSearchBy\"]")));
//		 searchBy.selectByValue("296");
//	
//		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"txtFilter2\"]")));
//		 WebElement textfield = driver.findElement(By.xpath("//*[@id=\"txtFilter2\"]"));
//		 textfield.clear();
//		 textfield.sendKeys(port);
//		 
//		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"dgListView_0\"]")));
//			WebElement a = driver.findElement(By.xpath("//*[@id=\"dgListView_0\"]"));
//			Actions act = new Actions(driver);
//			act.moveToElement(a).click().click().perform();
			
		 //driver.switchTo().window(parentWindow);
	}
	
	public void switchToEqupServices() {
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("mainFrame")));
		driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("contents")));
	    driver.switchTo().frame(driver.findElement(By.name("contents")));
	    new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td62\"]")));
		driver.findElement(By.xpath("//*[@id=\"td62\"]")).click();
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td63\"]")));
		WebElement element = driver.findElement(By.xpath("//*[@id=\"td63\"]"));
		element.click();
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
	}
	
	public void setSearchUserID() { 
		 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"selSearchBy\"]")));
		 Select lineType = new Select(driver.findElement(By.xpath("//*[@id=\"selSearchBy\"]")));
		 lineType.selectByValue("1343");
	}
	
	public boolean waitForAlert() throws InterruptedException {
	   int i=0;
	   while(i++<5){
	        try {
	        	driver.switchTo().alert().accept();
	            return true;
	        }
	        catch(NoAlertPresentException e) {
	          //Thread.sleep(1000);
	          continue;
	        }
	   }
	   return false;
	}
}
