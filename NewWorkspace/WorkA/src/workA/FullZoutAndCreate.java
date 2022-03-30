package workA;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.StaleElementReferenceException;

public class FullZoutAndCreate {
	WebDriver driver;
	Boolean AccountCreate;
	Readexcel ES;
	ArrayList<String> IgnoreWrongRoom = new ArrayList<>();
	fullZout Z;
	
	public FullZoutAndCreate(WebDriver d, Boolean AC) {
		driver = d;
		AccountCreate = AC;
	}
	
	public void Full() throws InterruptedException {
		
		if(AccountCreate)
			ES = new Readexcel("FullZupdateAC");
		else
			ES = new Readexcel("FullZupdate");
		
		 Z = new fullZout(driver,AccountCreate);
//		fullCreate fullCreate = new fullCreate(driver);
		ArrayList<String> Bvariants = new ArrayList<>();
		for(int j = 1; j < 35; j++) {
			String k = "B" + j + "-D";
			String m = "B" + j + "-D*";
			String l = "B" + j + "W";
			Bvariants.add(k);
			Bvariants.add(m);
			Bvariants.add(l);
		}
		
		//System.out.println(Bvariants);
		
		CreateCable cable = new CreateCable(driver);
		CreateJack jack = new CreateJack(driver);
		CreateAccount account = new CreateAccount(driver);
		
//		UpdateCable uCable = new UpdateCable(driver);
//		UpdateJack uJack = new UpdateJack(driver);
		UpdateAccount uAccount = new UpdateAccount(driver);
		System.out.println("Obtaining all JackIDs");
		Z.getAllJacks(ES);
		System.out.println(Z.JackIDs);
		//Thread.sleep(100000);
		//Z.zOut(ES, true);
//		System.out.println("jackid " +Z.JackIDs);
//		System.out.println("two left " +Z.twoLeft);
//		System.out.println("Zd out " +Z.ZdOut);

		int row = 1;
		int NullinaRow = 0;
		while(NullinaRow != 4) {
			IgnoreWrongRoom.clear();
			String writeString = "";
			Boolean write = false; 
			String jackid = ES.RWcell(ES.jackNum, row, null, 0);
			String closet = ES.RWcell(ES.closetNum, row, null, 0);
			String room = ES.RWcell(ES.roomNum, row, null, 0);
			String building = ES.buildingCode;
			String startdate = "";
			String enddate = "";
			String accNum = "";
			enddate = ES.endDate;
			String jackCreated = "";
			Boolean canBeUpdated = false;
			if(AccountCreate) {
				startdate = ES.startDate;
				accNum = ES.RWcell(ES.accNum, row, null, 0);
			}
			if(jackid.equals("empty") || closet.equals("empty") || room.equals("empty")) {
				ES.RWcell(ES.noteNum, row, "One or more field empty", 1);
				NullinaRow++;
			}
			else {
				canBeUpdated = Z.canBeUpdatedCheck(cable, jack, account, jackid, room);
				System.out.println(jackid + " " + room + " " + closet + " row: " + row + " in update, can be updated:  " + canBeUpdated);
				Z.cableSwitch(cable);
				cable.Searchcable(jackid);
				/*
				 * Take cable, take distribution for every cable.
				 * make another list of cables to ignore so you dont Z out
				 * cables in different rooms
				 */

				//if(Z.twoLeft.contains(row)) {
//				if(canBeUpdated) {
//					cable.Searchcable(room);
//					UpdateVDCable(jackid, closet,room,cable);
//					System.out.println("Updating cable");
//				}
				if(cable.returnCables.size() == 0){
					cable.Searchcable(room);
					int updateCount = 0;
					if(cable.returnCables.size() != 0) {
						updateCount = updateTrimCable(cable, room);
					}
					if(updateCount == 0) {
						cable.fullCreate(jackid, building, closet, room);
						write = true;
						writeString += "cable";
						System.out.println("Creating cable");
					}
					else {
						System.out.print("Updating cable");
						UpdateVDCable(jackid, closet, room, cable);
					}
				}
				else {
					System.out.println("cable created, size: " + cable.returnCables.size());
					cable.Searchcable(room);
					if(cable.returnCables.size() > 1) {
						int trimCount = updateTrimCable(cable, room) - 1;
						while(trimCount > -1) {
							System.out.print("Zing Cable");
							Z.zCable(cable.returnCablesX.get(trimCount));
							trimCount--;
						}
					}
				}
				Z.jackSwitch(jack);
				jack.Searchjack(jackid);
				//if(Z.twoLeft.contains(row)) {
//				if(canBeUpdated) {
//					jack.Searchjack(room);
//					UpdateVD(jackid,room,jack);
//					System.out.println("Updating Jack");
//				}
				if(jack.returnJacks.size() == 0){
					jack.Searchjack(room);
					int updateCount = 0;;
					if(jack.returnJacks.size() != 0) {
						updateCount = updateTrimJack(jack);
					}
					if(updateCount == 0) {
						jackCreated = jack.fullCreate(jackid, room, building);
						writeString += ", jack";
						System.out.println("Creating jack");
					}
					else {
						System.out.print("Updating jack");
						UpdateVD( jackid, room, jack);
					}
				}
				else {
					System.out.println("jack created, size: " + jack.returnJacks.size());
					jack.Searchjack(room);
					if(jack.returnJacks.size() > 1) {
						int trimCount = updateTrimJack(jack) - 1;
						while(trimCount > -1) {
							System.out.print("Zing jack");
							Z.zJack(jack.returnJacks.get(trimCount));
							trimCount--;
						}
					}
				}
				Z.accountSwitch(account);
				account.searchAccount(jackid);
				if(account.returnUsers.size() == 0) {
					System.out.println("no account");
				}
				
				if(AccountCreate || canBeUpdated) { //Z.twoLeft.contains(row)) {
					
					//if(Z.twoLeft.contains(row)) {
//					if(canBeUpdated) {
//						account.searchAccount(room);
//						updateAccount(uAccount, account, account.returnUsers.size() - 1, jackid, enddate);
//						System.out.println("Updating account");
//					}
					if(account.returnUsers.size() == 0){
						if(accNum.equals("empty")) {
							
						}
						else {
							account.searchAccount(room);
							int updateCount = 0;
							if(account.returnUsers.size() != 0) {
								updateCount = updateTrimAccount(account);
							}
							if(updateCount == 0) {
								if(AccountCreate) {
									account.fullCreate(jackid, startdate, accNum);
									writeString += ", account";
									System.out.println("Creating account");
								}
							}
							else {
								System.out.print("Updating account");
								updateAccount(uAccount, account, account.returnUsers.size() - 1, jackid, enddate);
							}
						}
					}
				}
				else if(account.returnUsers.size() >= 1) {
					System.out.println("account created");
					account.searchAccount(room);
					if(account.returnUsers.size() > 1){
						int trimCount = updateTrimAccount(account) - 1;
						while(trimCount > -1) {
							System.out.print("Zing account");
							Z.endDate(account.returnUsersX.get(trimCount), enddate);
							trimCount--;
						}
					}
				}
				Z.Reset();
				NullinaRow = 0;
				if(jackCreated.equals("manual update needed")) {
					ES.RWcell(ES.noteNum, row, jackCreated, 1);
				}
				else if(write) {
					ES.RWcell(ES.noteNum, row, writeString + "created", 1); 
				}
				else if(cable.returnCables.size() >= 1 && account.returnUsers.size() >= 1 && jack.returnJacks.size() >= 1) {
					ES.RWcell(ES.noteNum, row, "Already fully Created", 1);
				}
				else {
					System.out.println("check this row");
				}
			}
			row++;
		}
		System.out.println(row);
	}
	
	public int updateTrimCable(CreateCable cable, String room) {
		int updateCount = cable.returnCables.size();
		int i = cable.returnCables.size() - 1;
		while(i > -1) {
			String s = driver.findElement(By.xpath("//*[@id=\"dgListView_" + cable.returnCablesNum.get(i) + "_0\"]")).getAttribute("title");
			String SearchedRoom = driver.findElement(By.xpath("//*[@id=\"dgListView_" + cable.returnCablesNum.get(i) + "_5\"]")).getAttribute("title");
			if(!SearchedRoom.equals(room)) {
				String s1 = s + "*";
				String s2 = s.substring(0, s.length() - 2);
				String V = s.substring(0, s.length() - 1);
				IgnoreWrongRoom.add(V + "V");
				IgnoreWrongRoom.add(s);
				IgnoreWrongRoom.add(s1);
				IgnoreWrongRoom.add(s2 + "W");
				cable.returnCables.remove(i);
				cable.returnCablesNum.remove(i);
				cable.returnCablesX.remove(i);
				updateCount--;
			}
			else if(Z.JackIDs.contains(s) || ES.Ignore.contains(s)) {
//				Boolean updateJack = true;
//				for(int j = 0; j < Bvariants.size(); ++i) {
//					if(s.contains(Bvariants.get(j))) {
//						updateJack = false;
//					}
//				}	
				updateCount--;
				cable.returnCables.remove(i);
				cable.returnCablesNum.remove(i);
				cable.returnCablesX.remove(i);
				
			}
			i--;
		}
		if(IgnoreWrongRoom.size()!=0) {
			System.out.println("Wrong room ignore list " + IgnoreWrongRoom);
		}
		return updateCount;
	}
	
	public int updateTrimJack(CreateJack jack) {
		int updateCount = jack.returnJacks.size();
		int i = jack.returnJacks.size() - 1;
		while(i > -1) {
			String s = driver.findElement(By.xpath("//*[@id=\"dgListView_" + jack.returnJacksNum.get(i) + "_0\"]")).getAttribute("title");
			if(Z.JackIDs.contains(s) || ES.Ignore.contains(s) || IgnoreWrongRoom.contains(s)) {
				updateCount--;
				jack.returnJacks.remove(i);
				jack.returnJacksNum.remove(i);
				
			}
			i--;
		}
		return updateCount;
	}
	
	public int updateTrimAccount(CreateAccount account) {
		int updateCount = account.returnUsers.size();
		int i = account.returnUsers.size() - 1;
		while(i > -1) {
			String s = driver.findElement(By.xpath("//*[@id=\"dgListView_" + account.returnUsersNum.get(i) + "_0\"]")).getAttribute("title");
			if(Z.JackIDs.contains(s) || ES.Ignore.contains(s) || IgnoreWrongRoom.contains(s)) {
				updateCount--;
				account.returnUsers.remove(i);
				account.returnUsersNum.remove(i);
				account.returnUsersX.remove(i);
			}
			i--;
		}
		return updateCount;
	}
	
	public void updateAccount(UpdateAccount uAccount, CreateAccount account, int i, String jackid, String date) throws InterruptedException {
		while(i>=0) {
			//String path = "//*[@id=\"dgListView_" + account.returnUsersNum.get(i) +"_0\"]";
			//WebElement item = driver.findElement(By.xpath(path));
			//String itemID = item.getAttribute("title");
			//if(!itemID.equals(jackid)) {
				if(i == 0) {
					String x = "//*[@id=\"dgListView_0\"]";
					findElement(x);
					Thread.sleep(2000);
					uAccount.UpdateInformation(x, jackid);
				}
				else {
					String x = "//*[@id=\"dgListView_" + account.returnUsersNum.get(i) + "\"]";
					findElement(x);
					uAccount.SetEndDate(x,date);
				}
			//}
			i--;
		}
	}
	
	public void UpdateVD(String jackid, String room, CreateJack jack) throws InterruptedException {
		jack.Searchjack(room);
		int i = jack.returnJacks.size() - 1;
		while(i > -1) {
			if(i == 0) {
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(jack.returnJacks.get(i))));
				WebElement a = driver.findElement(By.xpath(jack.returnJacks.get(i)));
				Actions act = new Actions(driver);
				act.moveToElement(a).click().click().perform();
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"JACKID\"]")));
				WebElement jackfield = driver.findElement(By.xpath("//*[@id=\"JACKID\"]")); 
				jackfield.clear();
				jackfield.sendKeys(jackid);
				driver.findElement(By.xpath("//*[@id=\"BUTTON_3\"]")).click();

			}
			else{
				//String path = "//*[@id=\"dgListView_" + jack.returnJacksNum.get(i) + "\"]";
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(jack.returnJacks.get(i))));
				WebElement a = driver.findElement(By.xpath(jack.returnJacks.get(i)));
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
	
	
	public void UpdateVDCable(String jackid, String closet, String room, CreateCable cable) throws InterruptedException {
		//cable.Searchcable(room);
		int i = cable.returnCables.size() - 1;
		 while(i > -1) {
			 //String trimmed = returnCables.get(i).trim();
			 if(i == 0) {
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cable.returnCablesX.get(i))));
				WebElement a = driver.findElement(By.xpath(cable.returnCablesX.get(i)));
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
				
				waitForAlert();
//				String parentWindow = driver.getWindowHandle();
//				driver.switchTo().alert().accept();
//				driver.switchTo().window(parentWindow);
				/*
				 * write x as done
				 */
			 }
			 else{
				 new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cable.returnCablesX.get(i))));
				WebElement a = driver.findElement(By.xpath(cable.returnCablesX.get(i)));
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
				waitForAlert();
//				Thread.sleep(5000);
//				String parentWindow = driver.getWindowHandle();
//				driver.switchTo().alert().accept();
//				driver.switchTo().window(parentWindow);
//				Thread.sleep(5000);
//				
			 }
			 
			 i--;
		 }
	}
	
	public boolean findElement(String x) {
	    boolean result = false;
	    
	    while(!result) {
	    //	System.out.println("1");
	        try {
	        	new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(x)));
	            result = true;
	            break;
	        } catch(StaleElementReferenceException e) {
	        	System.out.println("finding element");
	        }
	        
	    }
	    return result;
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
