package workA;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.ArrayList;

public class Verify {
	WebDriver driver;
	
	

	public Verify(WebDriver d) {
		driver = d;

	}
	
	public void fullVerify() throws InterruptedException {
		Readexcel ES = new Readexcel("Verify");
		fullZout Zout = new fullZout(driver,false);
		CreateCable cable = new CreateCable(driver);
		CreateJack jack = new CreateJack(driver);
		CreateAccount account = new CreateAccount(driver);
		ArrayList<String> check = new ArrayList<>();
		ArrayList<String> needUserI = new ArrayList<>();
		ArrayList<String> closetRoomCheck = new ArrayList<>();
		
		int row = 1;
		int NullinaRow = 0;
		while(NullinaRow != 7) {
			String jackid = ES.RWcell(ES.jackNum, row, null, 0);
			String room = ES.RWcell(ES.roomNum, row, null, 0);
			String closet = ES.RWcell(ES.closetNum, row, null, 0);
			if(jackid.equals("empty") || room.equals("empty") || closet.equals("empty")) {
				ES.RWcell(ES.noteNum, row, "One or more field empty", 1);
				NullinaRow++;
			}
			else {
				System.out.println("Searching: " + jackid + " " + room + " on row: " + row);
				Boolean cableF = false;
				Boolean jackF = false;
				Boolean accountF = false;
				Zout.cableSwitch(cable);
				cable.Searchcable(jackid);
				int cSize = cable.returnCables.size();
				if(cSize == 0) {
					System.out.println("No cable");
				}
				else if(cSize >= 1) {
					System.out.println("Cable size: " + cSize);
					cableF = true;
					if(driver.findElements(By.xpath("//*[@id=\"dgListView_0_2\"]")).size() == 1
							&& driver.findElements(By.xpath("//*[@id=\"dgListView_0_5\"]")).size() == 1){
						String closetVal = driver.findElement(By.xpath("//*[@id=\"dgListView_0_2\"]")).getAttribute("title");
						String roomVal = driver.findElement(By.xpath("//*[@id=\"dgListView_0_5\"]")).getAttribute("title");
						if(!roomVal.equals(room) || !closetVal.equals(closet)) {
							closetRoomCheck.add(jackid);
						}
					}
					else {
						closetRoomCheck.add(jackid);
					}
				}
//				cable.Searchcable(room);
//				if(cable.returnCables.size() > 1) {
//					System.out.println("TEMP CHECK HERE CABLE");
//				}
				Zout.jackSwitch(jack);
				jack.Searchjack(jackid);
				int jSize = jack.returnJacks.size();
				if(jSize == 0) {
					System.out.println("No jack");
				}
				else if(jSize >= 1) {
					System.out.println("jack size: " + jSize);
					jackF = true;
				}
//				jack.Searchjack(room);
//				if(jack.returnJacks.size() > 1) {
//					System.out.println("TEMP CHECK HERE");
//				}
				Zout.accountSwitch(account);
				account.searchAccount(jackid);
				int aSize = account.returnUsers.size();
				if(aSize == 0) {
					System.out.println("No account");
				}
				else if(aSize >= 1) {
					System.out.println("account size: " + aSize);
					accountF = true;
				}
				Zout.Reset();
				NullinaRow = 0;
				if(cableF && jackF && accountF) {
					System.out.println("Fully Created");
					ES.RWcell(ES.noteNum, row, "Fully Created", 1);
				}
				else if(cableF && jackF && !accountF) {
					needUserI.add(jackid);
					ES.RWcell(ES.noteNum, row, "need UserI", 1);
				}
				else if(!cableF && !jackF && !accountF) {
					ES.RWcell(ES.noteNum, row, "Nothing created", 1);
				}
				else if(cableF && !jackF && accountF) {
					ES.RWcell(ES.noteNum, row, "No jack", 1);
				}
				else if(!cableF && jackF && accountF) {
					ES.RWcell(ES.noteNum, row, "No Cable", 1);
				}
				else {
					check.add(jackid);
					ES.RWcell(ES.noteNum, row, "Manual Check needed", 1);
				}
			}
			row++;
		}
		System.out.println("Need UserI: " + needUserI);
		System.out.print("\n\nCheck These: " + check);
		System.out.print("\n\nCheck room and/or closet under cable: " + closetRoomCheck);
	}
}
