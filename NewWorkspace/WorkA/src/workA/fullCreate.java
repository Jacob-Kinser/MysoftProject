package workA;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class fullCreate {
	WebDriver driver;
	Boolean AccountCreate;
	
	public fullCreate(WebDriver d, Boolean AC) {
		driver = d;
		AccountCreate = AC;
	}
	
	public void create() throws InterruptedException {
		CreateCable cable = new CreateCable(driver);
		CreateJack jack = new CreateJack(driver);
		CreateAccount account = new CreateAccount(driver);
		Readexcel ES;
		if(AccountCreate) {
			ES = new Readexcel("fullcreateAC");
		}
		else {
			ES = new Readexcel("fullcreate");
		}
		int row = 1;
		int NullinaRow = 0;
		while(NullinaRow != 7) {
			String writeString = "";
			Boolean comment = false;
			String jackid = ES.RWcell(ES.jackNum, row, null, 0);
			String closet = ES.RWcell(ES.closetNum, row, null, 0);
			String room = ES.RWcell(ES.roomNum, row, null, 0);
			String building = ES.buildingCode;
			String accNum = "";
			String startdate = "";
			System.out.println(row + " " + jackid);
			if(AccountCreate) {
				accNum = ES.RWcell(ES.accNum, row, null, 0);
				startdate = ES.startDate;
			}
			/*
			 * update this
			 */
			if(jackid.equals("empty") || closet.equals("empty") || room.equals("empty")) {
				ES.RWcell(ES.noteNum, row, "One or more field empty", 1);
				NullinaRow++;
			}
			else {
				cable.Switchtocable();
				driver.switchTo().parentFrame();
				driver.switchTo().frame(driver.findElement(By.name("main")));
				int sizeC = cable.Searchcable(jackid);
				if(sizeC > 0) {
					//System.out.println("cable exists");
					writeString = "cable exists, ";
					comment = true;
				}
				else {
					//System.out.println("Creating cable");
					cable.fullCreate(jackid, building, closet, room);
				}
				
				driver.switchTo().parentFrame();
				driver.switchTo().frame(driver.findElement(By.name("contents")));
				driver.findElement(By.xpath("//*[@id=\"td1\"]")).click();
				driver.switchTo().parentFrame();
				driver.switchTo().parentFrame();
				jack.SwitchTojack();
				driver.switchTo().parentFrame();
				driver.switchTo().frame(driver.findElement(By.name("main")));
				int numJacks = jack.Searchjack(jackid);
				if(numJacks == 0) {
					jack.fullCreate(jackid, room, building);
				}
				else {//already created, maybe modify search
					writeString += "jack already exists, ";
					comment = true;
				}
				if(AccountCreate) {
					driver.switchTo().parentFrame();
					driver.switchTo().parentFrame();
					account.switchToAccount();
					driver.switchTo().parentFrame();
					driver.switchTo().frame(driver.findElement(By.name("main")));
					account.searchAccount(jackid);
					int numAccounts = account.returnUsers.size();
					if(numAccounts == 0) {
						account.fullCreate(jackid, startdate, accNum);
					}
					else {
						writeString += "User account created ";
						comment = true;
					}
				}
				
				driver.switchTo().parentFrame();
				driver.switchTo().frame(driver.findElement(By.name("contents")));
				driver.findElement(By.xpath("//*[@id=\"td1\"]")).click();
				driver.findElement(By.xpath("//*[@id=\"td15\"]")).click();
				driver.switchTo().parentFrame();
				driver.switchTo().parentFrame();
				
				if(comment) {
					String s = "cable exists, jack already exists, User account created ";
					if(writeString.equals(s)) {
						ES.RWcell(ES.noteNum, row, "Already Created", 1);
					}
					else {
						ES.RWcell(ES.noteNum, row, writeString, 1);
					}
					
				}
				NullinaRow = 0;
			}
			row++;
		}
	}
	
	
}
