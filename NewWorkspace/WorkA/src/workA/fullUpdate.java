package workA;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class fullUpdate {
	WebDriver driver;
	
	public fullUpdate(WebDriver d) {
		driver = d;
	}
	
	public void update() throws InterruptedException {
		UpdateCable cable = new UpdateCable(driver);
		UpdateJack jack = new UpdateJack(driver);
		UpdateAccount account = new UpdateAccount(driver);
		
		CreateCable cableC = new CreateCable(driver);
		CreateJack jackC = new CreateJack(driver);
		CreateAccount accountC = new CreateAccount(driver);
		
		Readexcel ES = new Readexcel("fullupdate");
		int row = 1;
		int NullinaRow = 0;
		while(NullinaRow != 4) {
			Boolean CreateFlag = false;
			String writeString = "";
			Boolean comment = false;
			String jackid = ES.RWcell(ES.jackNum, row, null, 0);
			String closet = ES.RWcell(ES.closetNum, row, null, 0);
			String room = ES.RWcell(ES.roomNum, row, null, 0);
			String enddate = ES.endDate;
			String startdate = ES.startDate;
			String building = ES.buildingCode;
			String accNum = ES.RWcell(ES.accNum, row, null, 0);
			if(jackid.equals("empty") || closet.equals("empty") || room.equals("empty")) {//add to this
				//make note
				NullinaRow++;
			}
			else {
				cable.Switchtocable();
				driver.switchTo().parentFrame();
				driver.switchTo().frame(driver.findElement(By.name("main")));
				int numCables = cable.Searchcable(room);
				//MODIFY SEARCH TO CHECK RESULTS
				if(numCables == 0) {
					cableC.fullCreate(jackid, building, closet, room);
				}
				else if(numCables == 1) {//check if already created
					Boolean found = false;
					for(int i = 0; i < numCables; ++i) {
						if(cable.returnCables.get(i).equals(jackid)) {
							//make note
							found = true;
						}
					}
					if(!found) {
						cableC.fullCreate(jackid, building, closet, room);
					}
				}
				else if(numCables == 2) {
					cable.UpdateVD(jackid,closet);
				}
				else {
					
				}
				
				
				driver.switchTo().parentFrame();
				driver.switchTo().frame(driver.findElement(By.name("contents")));
				driver.findElement(By.xpath("//*[@id=\"td1\"]")).click();
				driver.switchTo().parentFrame();
				driver.switchTo().parentFrame();
				jack.SwitchTojack();
				driver.switchTo().parentFrame();
				driver.switchTo().frame(driver.findElement(By.name("main")));
				int numJacks = jack.Searchjack(room);
				if(numJacks == 0) {
					jackC.fullCreate(jackid, room, building);
				}
				if(numJacks == 1) {
					int searchJackid = jack.Searchjack(jackid);
					if(searchJackid == 0) {
						
					}
					else if(searchJackid == 1) {
						comment = true;
					}
					else {
						
					}
				}
				if(numJacks == 2) {
					jack.UpdateVD(jackid);
				}
				else {
					
				}
				
				driver.switchTo().parentFrame();
				driver.switchTo().parentFrame();
				account.switchToAccount();
				driver.switchTo().parentFrame();
				driver.switchTo().frame(driver.findElement(By.name("main")));
				account.SearchUserID(room);
				/*
				 * make account if not there?
				 */
				int i = account.returnUsers.size() - 1;//check if 0
				//System.out.println(i);
				if(i == 0) {
					accountC.fullCreate(jackid, startdate, accNum);
				}
				else {
					while(i>=0) {
						String path = "//*[@id=\"dgListView_" + account.returnUsersNum.get(i) +"_0\"]";
						WebElement item = driver.findElement(By.xpath(path));
						String itemID = item.getAttribute("title");
						if(!itemID.equals(jackid)) {
							if(i == 0) {
								//account.UpdateInformation(account.returnUsers.get(i), jackid);
							}
							else {
								//account.SetEndDate(account.returnUsers.get(i), enddate);
							}
						}
						else {
							comment = true;
							i = -1;
						}
						i--;
					}
				}
				/*
				 * switch to correct frame so you can switch back to cable
				 */
				driver.switchTo().parentFrame();
				driver.switchTo().frame(driver.findElement(By.name("contents")));
				driver.findElement(By.xpath("//*[@id=\"td1\"]")).click();
				driver.findElement(By.xpath("//*[@id=\"td15\"]")).click();
				driver.switchTo().parentFrame();
				driver.switchTo().parentFrame();
				
				if(comment) {
					ES.RWcell(ES.noteNum, row, writeString, 1);
				}
				row++;
			}
			
			
			row++;
		}
	}
	
	
	
}
