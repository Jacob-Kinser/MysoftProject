package workA;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckAll {
	WebDriver driver;
	
	
	public CheckAll(WebDriver d) {
		driver = d;
	}
	
	public void checkAll() throws InterruptedException {
		fullZout zOut = new fullZout(driver,false);
		FullZoutAndCreate zOutC = new FullZoutAndCreate(driver,false);
		CreateCable cable = new CreateCable(driver);
		CreateJack jack = new CreateJack(driver);
		CreateAccount account = new CreateAccount(driver);
		Readexcel ES = new Readexcel("Verify");
		ArrayList<String> Check = new ArrayList<>();
		int row = 1;
		int NullinaRow = 0;
		System.out.println("Obtaining all Jacks");
		zOut.getAllJacks(ES);
		
		while(NullinaRow != 4) {
			Boolean cableF = true;
			Boolean jackF = true;
			Boolean accountF = true;
			String jackid = ES.RWcell(ES.jackNum, row, null, 0);
			String closet = ES.RWcell(ES.closetNum,row,null,0);
			int pauseTime = ES.sleepTime;
			if(jackid.equals("empty") || closet.equals("empty")) {
				ES.RWcell(ES.noteNum, row, "One or more field empty", 1);
				NullinaRow++;
			}
			else {
				System.out.println("Checking: " + jackid + " " + closet + " on row: " + row);
				Boolean add = false;
				zOut.cableSwitch(cable);
				cable.Searchcable(jackid);
				Thread.sleep(pauseTime * 1000);
				int j = cable.returnCables.size();
				Boolean wrongCloset = false;
				if(j>=1) {
					String closetResult = driver.findElement(By.xpath("//*[@id=\"dgListView_0_2\"]")).getAttribute("title");
					System.out.println(closetResult);
					if(!closetResult.equals(closet)) wrongCloset = true;
				}
				if(j != 1) {
					cableF = false;
					add = true;
				}
				
				zOut.jackSwitch(jack);
				jack.Searchjack(jackid);
				Thread.sleep(pauseTime * 1000);
				j = jack.returnJacks.size();
				if(j != 1) {
					jackF = false;
					add = true;
				}
				
				zOut.accountSwitch(account);
				account.searchAccount(jackid);
				Thread.sleep(pauseTime * 1000);
				j = account.returnUsers.size();
				if(j == 0) {
					accountF = false;
					add = true;
				}
				
				zOut.Reset();
				NullinaRow = 0;
				
				if(add) Check.add(jackid);
				if(wrongCloset) {
					System.out.println("Manual Updated needed: WRONG CLOSET");
					ES.RWcell(ES.noteNum, row, "WRONG CLOSET", 1);
				}
				else if(!cableF || !jackF) {
					System.out.println("Manual Updated needed");
					ES.RWcell(ES.noteNum, row, "Manual Updated needed", 1);
				}
				else if(cableF && jackF && !accountF) {
					System.out.println("Account needs to be created");
					ES.RWcell(ES.noteNum, row, "Account needs to be created", 1);
				}
				else if(cableF && jackF && accountF) {
					System.out.println("FullCreated");
					ES.RWcell(ES.noteNum, row, "FullCreated", 1);
				}
				else {
					System.out.println("Manual Check needed");
					ES.RWcell(ES.noteNum, row, "Manual Check needed", 1);
				}
			}
			row++;
		}
		System.out.println("Check these");
		for(String s: Check) {
			System.out.println(s);
		}
		
	}
}


//while(j > -1) {
//	String s = "//*[@id=\"dgListView_" + cable.returnCablesNum.get(j) + "_0\"]";
//	new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));
//	WebElement jackfield = driver.findElement(By.xpath(s));
//	String title = jackfield.getAttribute("title");
//	if(!zOut.JackIDs.contains(title)) {
//		Check.add(jackid);
//	}
//	else {
//		//System.out.println("jackid contained" + title);
//	}
//	j--;
//}
