package workA;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

public class WorktagUpdate {
	WebDriver driver;
	ArrayList<WebElement> returnCables = new ArrayList<>();
	ArrayList<String> returnCablesX = new ArrayList<>();
	ArrayList<Integer> returnCablesNum = new ArrayList<>();
	ArrayList<String> ItemNbr = new ArrayList<>();
	ArrayList<String> jackidRet = new ArrayList<>();

	
	public WorktagUpdate(WebDriver d) {
		driver = d;
	}
	
	public void Update() throws InterruptedException {
		/*
		 * These next few lines will switch your window to equipment services
		 * will switch your driver back into the main frame 
		 * the Select searchType takes the drop down and ensures you are searching by UserID
		 */
		SwitchtoEquipServices();
		driver.switchTo().parentFrame();
		driver.switchTo().frame(driver.findElement(By.name("main")));
		 Select searchType = new Select(driver.findElement(By.xpath("//*[@id=\"selSearchBy\"]")));
		 searchType.selectByValue("1343");
		Readexcel ES = new Readexcel("WorkTagUpdate"); //This will have the user input the column for jackid
		int row = 1;
		int NullinaRow = 0;
		
		
		while(NullinaRow !=20){ //go through the jacks until there are 20 empty rows, can change this number to be whatever you want
			System.out.println("row: " + row);
			String worktag = "";
			String jackid = ES.RWcell(ES.jackNum, row, null, 0); //read the worktag from the next line
			String newjackid = jackid;
			if(jackid.equals("empty")) { //if theres no jack, add to null counter
				NullinaRow++;
			}
			else {
				boolean manUpdate = false;
				boolean noResults = false;
				String newLine = jackid; //this variable is what gets printed back into the excel sheet
				String trimmedJackid = TrimJackid(jackid); //take the -D off the end of the jack, will return none if there is no -D
				if(trimmedJackid.equals("none")) {
					//System.out.println("jackid none " + jackid + " " + jackid.contains("-d") + " " + jackid.toUpperCase().contains("-D"));
					Searchcable(jackid); //if theres no -D search normal jackid
				}
				else Searchcable(trimmedJackid); //search with trimmed jackid
				if(returnCables.size() == 1) { //only one return
					System.out.println("first return size == 1 " + jackid);
					worktag = GrabWorktag(returnCablesX.get(0));
					newjackid = addEnd(jackid, jackidRet.get(0)); //add the -D/-dv etc back to the end
					newLine = newjackid + ", " + worktag;
				}
				else if(returnCables.size() == 2) { // two returns
					if(ItemNbr.get(0).toLowerCase().contains("ap") || ItemNbr.get(1).toLowerCase().contains("ap")) { //check if one return is an AP
						//check if ap is jackid - -d + W
						if((ItemNbr.get(0).contains("AP") || ItemNbr.get(1).contains("AP"))) { //checks for AP
							System.out.println("AP " + jackid);
							worktag = GrabWorktag(returnCablesX.get(0));
							newjackid = addEnd(jackid, jackidRet.get(0));
							newLine = newjackid + "W, " + worktag;
							
						}
						
						
					}
					else {
						Searchcable(jackid);
						if(returnCables.size() == 1) { //search again with -D at the end
							System.out.println("2nd ret size = 1 " + jackid);
							worktag = GrabWorktag(returnCablesX.get(0));
							newjackid = addEnd(jackid, jackidRet.get(0));
							newLine = newjackid + ", " + worktag;
							//ES.RWcell(ES.noteNum, row, newLine, 1);
						}
						else { //2 returns but was not an AP, manual update
							manUpdate = true; //flag as a manual update for now. check it again later in the code
							//make W jackid
							//iterate through return jacks
							//check for matches
							System.out.println("manual update 1 " + jackid + " " + jackidRet.size() + " item nbrs found " + ItemNbr.size());
							newLine = newjackid + ", " + "Manual update needed, two results (no ap)";
							ES.RWcell(ES.jackNum, row, newLine, 1);
						}
						
					}
						
				}
				else if(returnCables.size() > 2) { //more than 2 returns
					Searchcable(jackid);
					if(returnCables.size() == 1) { //search again with -D at the end
						System.out.println("2nd ret size = 1 " + jackid);
						worktag = GrabWorktag(returnCablesX.get(0));
						newjackid = addEnd(jackid, jackidRet.get(0));
						newLine = newjackid + ", " + worktag;
						//ES.RWcell(ES.noteNum, row, newLine, 1);
					}
					else {//manual tag
						manUpdate = true;
						
					}
				}
				else {
					System.out.println("No results " + jackid);
					noResults = true;
				}
				NullinaRow = 0;
				/*
				 * This if block is if there are still more than 2 returns
				 * will try and eliminate returned jackids by checking them in depth
				 */
				if(manUpdate) {
					System.out.println("manual update " + jackid + " " + jackidRet.size() + " item nbrs found " + ItemNbr.size());
					System.out.println("jack ret size " + jackidRet.size());
					//newLine = newjackid + ", " + "Manual update needed";
					//ES.RWcell(ES.jackNum, row, newLine, 1);
					/*
					 * jackid = full jackid
					 * trimmed jackid (could be none)
					 */
					String Wjackid = "";
					if(trimmedJackid.equals("none")) {
						Wjackid = jackid + "W";
//						if(jackid.charAt(jackid.length() - 2) == 'b' || (jackid.charAt(jackid.length() - 3) == 'b' && 
//								jackid.charAt(jackid.length() - 2) != 'b')) {
//							trimmedJackid = jackid;
//							jackid = jackid + "-D";
//						}
					}
					else {
						Wjackid = trimmedJackid + "W";
					
					}
					for(int i = 0; i < jackidRet.size(); ++i) {
						String tempjackid = jackid;
						String temptrimmedJackid = trimmedJackid;
						if(trimmedJackid.equals("none")) {
							temptrimmedJackid = jackid;
							/*
							 * Here if your cutsheet jackid has no ending
							 * you add the ending based on what return jack 
							 * you are looking at
							 * even if a jack return has multiple endings
							 * such as, if you search A041363AA
							 * has these returns
							 * A041363AA-VD
							 * A041363AA-D
							 * both of these will get caught and it will still get tagged as a manual return
							 */
							if(jackidRet.get(i).contains("-D")) {
								tempjackid = jackid + "-D";
							}
							else if(jackidRet.get(i).contains("-DV")) {
								tempjackid = jackid + "-DV";
							}
							else if(jackidRet.get(i).contains("-VD")) {
								tempjackid = jackid + "-VD";
							}
						}
						if(jackidRet.get(i).toLowerCase().contains(Wjackid.toLowerCase())) {
							System.out.println("here 1 " + jackidRet.get(i));
						}
						else if(jackidRet.get(i).toLowerCase().contains(tempjackid.toLowerCase())) {
							System.out.println("here 2 "+ jackidRet.get(i));
						}
//						else if(jackidRet.get(i).toLowerCase().contains(temptrimmedJackid.toLowerCase())) {
//							System.out.println("here 3 "+ jackidRet.get(i));
//						}
						else if(!jackidRet.get(i).toLowerCase().contains("*") && !jackidRet.get(i).toLowerCase().endsWith("w")) {
							String trimmedRet = TrimJackid(jackidRet.get(i));
							System.out.println(trimmedRet + "= " + trimmedJackid+ " " + jackid);
							if(!temptrimmedJackid.toLowerCase().equals(trimmedRet.toLowerCase()) 
									&& !tempjackid.toLowerCase().equals(trimmedRet.toLowerCase())) {
								returnCables.remove(i);
								returnCablesX.remove(i);
								returnCablesNum.remove(i);
								ItemNbr.remove(i);
								jackidRet.remove(i);
							}
					
						}
						else {
							returnCables.remove(i);
							returnCablesX.remove(i);
							returnCablesNum.remove(i);
							ItemNbr.remove(i);
							jackidRet.remove(i);
						}
					}
					System.out.println("size: " + jackidRet.size() + " " + jackidRet.toString());
					System.out.println("size: " + ItemNbr.size() + " " + ItemNbr.toString());
					//GRAB WORKTAG and create new line here
					//if size is still two search original jackid
					//size == 1, GOOD
					//if not, manual update
					if(jackidRet.size() == 1) {
						manUpdate = false;
						worktag = GrabWorktag(returnCablesX.get(0));
						newLine = newjackid + ", " + worktag;
					}
					else if(jackidRet.size() == 2) {
						if(jackidRet.get(0).equals(jackidRet.get(1)) && jackidRet.get(0).contains("UPS") && jackidRet.get(1).contains("UPS")) {
							returnCables.remove(1);
							returnCablesX.remove(1);
							returnCablesNum.remove(1);
							ItemNbr.remove(1);
							jackidRet.remove(1);
							manUpdate = false;
							worktag = GrabWorktag(returnCablesX.get(0));
							newjackid = addEnd(jackid, jackidRet.get(0)); //added
							newLine = newjackid + ", " + worktag;
						}
						else if(ItemNbr.get(0).contains("TSPORT") || ItemNbr.get(1).contains("TSPORT")) {
							if(jackidRet.get(0).equals(jackidRet.get(1))) {
								if(ItemNbr.get(0).contains("TSPORT")) {
									returnCables.remove(0);
									returnCablesX.remove(0);
									returnCablesNum.remove(0);
									ItemNbr.remove(0);
									jackidRet.remove(0);
									manUpdate = false;
									worktag = GrabWorktag(returnCablesX.get(0));
									newjackid = addEnd(jackid, jackidRet.get(0)); //added
									newLine = newjackid + ", " + worktag;
								}
								
								else if(ItemNbr.get(1).contains("TSPORT")) {
									returnCables.remove(1);
									returnCablesX.remove(1);
									returnCablesNum.remove(1);
									ItemNbr.remove(1);
									jackidRet.remove(1);
									manUpdate = false;
									worktag = GrabWorktag(returnCablesX.get(0));
									newjackid = addEnd(jackid, jackidRet.get(0));
									newLine = newjackid + ", " + worktag;
								}
							}
						}
						else if(ItemNbr.get(0).toLowerCase().contains("ap") || ItemNbr.get(1).toLowerCase().contains("ap")) {
							System.out.println("AP " + jackid);
							worktag = GrabWorktag(returnCablesX.get(0));
							newjackid = addEnd(jackid, jackidRet.get(0));
							newLine = newjackid + "W, " + worktag;
							manUpdate = false;
						}
						
						else {
							ES.RWcell(ES.jackNum, row, jackid, 1);
							ES.RWcell(ES.noteNum, row, "Manual Update needed", 1);
						}
						
					}
					else {
						ES.RWcell(ES.jackNum, row, jackid, 1);
						ES.RWcell(ES.noteNum, row, "Manual Update needed", 1);
					}
					
				}
				
				Boolean FoundItem = false;
				if(!manUpdate && !noResults) {
					
					for(int i = 0; i < ItemNbr.size();++i) {
						/*
						 * Order of if statements is important
						 * For example: if you checked if the item Nbr contains 702
						 * before you check if it contains 3702
						 * All 3702s would get checked as 702s
						 */
						if(ItemNbr.get(i).contains("3702") || ItemNbr.get(i).contains("3802") || ItemNbr.get(i).contains("9120")) {
							ES.RWcell(ES.noteNum, row, "WIRELESS-AP-INDOOR", 1);
							FoundItem = true;
						
						}
						else if(ItemNbr.get(i).contains("702") || ItemNbr.get(i).contains("9105")) {
							ES.RWcell(ES.noteNum, row, "WIRELESS-AP-HOSPITALITY", 1);
							FoundItem = true;
							
						}
						else if(ItemNbr.get(i).contains("1815")) {
							ES.RWcell(ES.noteNum, row, "WIRELESS-AP-HOSPITALITY", 1);
							FoundItem = true;
							
						}
						else if(worktag.equals("0007")) {
							ES.RWcell(ES.noteNum, row, "UPS", 1);
							FoundItem = true;
						}
						else if(ItemNbr.get(i).contains("S-CENET") && !FoundItem) {
							ES.RWcell(ES.noteNum, row, "S-CENET", 1);
							FoundItem = true;
							
						}
						else if(ItemNbr.get(i).equals("DENET") || ItemNbr.get(i).equals("ENETALL") || ItemNbr.get(i).equals("ENET")) {
							ES.RWcell(ES.noteNum, row, "$7", 1);
							FoundItem = true;
							
						}
					}
					if(!FoundItem) {
						ES.RWcell(ES.noteNum, row, "CENET", 1);
					}
					ES.RWcell(ES.jackNum, row, newLine, 1);
						
			
				}
				
				
			}
			row++;
		}
	}
	
	public String addEnd(String cutsheetID, String retjackidStr) {
		String jackid = cutsheetID.toUpperCase();

		if(cutsheetID.length() != retjackidStr.length()) {
			if(!cutsheetID.toUpperCase().contains("-D") && retjackidStr.toUpperCase().contains("-D")) {
				return jackid+"-D";
			}
//			if(!cutsheetID.toUpperCase().contains("-DV") && retjackidStr.toUpperCase().contains("-DV")) {
//				return jackid+"-DV";
//			}
			if(!cutsheetID.toUpperCase().contains("-VD") && retjackidStr.toUpperCase().contains("-VD")) {
				return jackid+"-VD";
			}
		}
		return jackid;
	}
	public String TrimJackid(String jackid) {
		String trimmed = "none";
		if(jackid.contains("-D") || jackid.contains("-d")) {
			trimmed = jackid.substring(0, jackid.length()-2);
			//System.out.println("Trimmed: " + trimmed);
		}
		else if(jackid.contains("-VD") || jackid.contains("-DV") || jackid.contains("-vd") || jackid.contains("-dv")) {
			trimmed = jackid.substring(0, jackid.length()-3);
		}
		return trimmed;
	}
	
	public Boolean CheckVD(String jackid) {
		return jackid.toLowerCase().contains("-vd");
	}

	public String GrabWorktag(String x) throws InterruptedException {
		WebElement a = driver.findElement(By.xpath(x));
		Actions act = new Actions(driver);
		act.moveToElement(a).click().click().perform();
		new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"USERSEQ_ACCOUNTNUMBER\"]")));
		WebElement e =  driver.findElement(By.xpath("//*[@id=\"USERSEQ_ACCOUNTNUMBER\"]"));
		String title = e.getAttribute("value");//e.getAttribute("title"); 
		//System.out.println(title);
		try {
			/*
			 * I decided that instead of pressing exit and save after grabbing a worktag to press cancel
			 * This is because some invalid port nbrs exist and you will get an error if try to save and exit one
			 */
//			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]")));
//			a = driver.findElement(By.xpath("//*[@id=\"BUTTON_SAVEANDEXIT\"]"));
//			a.click(); a.click();
			/*
			 * wait for the cancel button to become available
			 * click it twice
			 */
			new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"BUTTON_CANCEL\"]")));
			a = driver.findElement(By.xpath("//*[@id=\"BUTTON_CANCEL\"]"));
			a.click(); a.click();
		}catch(StaleElementReferenceException | NoSuchElementException | NullPointerException ex){
			driver.navigate().refresh();
			Thread.sleep(5000);
			SwitchtoEquipServices();
			driver.switchTo().parentFrame();
			driver.switchTo().frame(driver.findElement(By.name("main")));
			//System.out.println("Caught stale element or no element: " + ex.getMessage());
		}
		
		return title;
	}
	
	
	/*
	 * This method will be called to switch the mysoft window to equipment services
	 */
	public void SwitchtoEquipServices() throws InterruptedException {
		Boolean switched = false;
		while(!switched) {
			try {
				/*
				 * Mysoft HTML uses frames. The way selenium works is you need to be within the same frame as the element
				 * you are trying to interact with
				 */
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("mainFrame")));
				driver.switchTo().frame(driver.findElement(By.name("mainFrame")));
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.name("contents")));
			    driver.switchTo().frame(driver.findElement(By.name("contents")));
			    new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td62\"]")));
			    new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"td62\"]")));
				driver.findElement(By.xpath("//*[@id=\"td62\"]")).click();
				new WebDriverWait(driver, 45).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"td63\"]")));
				new WebDriverWait(driver, 45).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"td63\"]")));
				WebElement element = driver.findElement(By.xpath("//*[@id=\"td63\"]"));
				element.click();
			
				switched = true;
			}catch(TimeoutException ex) {
					System.out.println(ex);
					driver.navigate().refresh();
					Thread.sleep(4000);
			}
		}
	}
	
	
	/*
	 * This method will search the cable and put the results for each return in the arrays
	 * X path is used to click into the elements
	 */
	public int Searchcable(String cable) throws InterruptedException {
		
		int j = 0;
		returnCables.clear();
		returnCablesNum.clear();
		returnCablesX.clear();
		ItemNbr.clear();
		jackidRet.clear();
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
				}catch(StaleElementReferenceException | NoSuchElementException | NullPointerException | TimeoutException ex){
					driver.navigate().refresh();
					Thread.sleep(5000);
					SwitchtoEquipServices();
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
			String pathItemNbr = "//*[@id=\"dgListView_" + digit + "_1\"]";
			String pathjackid = "//*[@id=\"dgListView_" + digit + "_0\"]";
			String enddate = "//*[@id=\"dgListView_" + digit + "_4\"]";
			//*[@id="dgListView_0_0"]
			for(int i = 0; i < 20; i++) {
				//System.out.println(path2);
				int enddatesize = driver.findElements(By.xpath(enddate)).size();
				int size0 = driver.findElements(By.xpath(path)).size();
				if(size0 == 1 && enddatesize == 0) {
					size0 = driver.findElements(By.xpath(path2)).size();
					if(size0 == 1) {
						returnCablesX.add(path);
						returnCables.add(driver.findElement(By.xpath(path)));
						returnCablesNum.add(digit);
						
						String title = driver.findElement(By.xpath(pathItemNbr)).getAttribute("title");
						ItemNbr.add(title);
						
						title = driver.findElement(By.xpath(pathjackid)).getAttribute("title");
						jackidRet.add(title);
						//System.out.println(title);
					}
				}
				digit++;
				path = "//*[@id=\"dgListView_" + digit + "\"]";
				path2 = "//*[@id=\"dgListView_" + digit + "_0\"]";
				pathItemNbr = "//*[@id=\"dgListView_" + digit + "_1\"]";
				pathjackid = "//*[@id=\"dgListView_" + digit + "_0\"]";
				enddate = "//*[@id=\"dgListView_" + digit + "_4\"]";
				
			}
			j++;
		//	System.out.println(j + " " + returnCables.size());
		}
		return returnCables.size();
		
	}
}
