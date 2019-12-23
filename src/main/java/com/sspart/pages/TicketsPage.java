package com.sspart.pages;


import static com.ramSabhanam.hybridDriver.WebUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ramSabhanam.hybridDriver.ExtendedWebPage;

/**
 * 
 * Tickets page - Deals with all kind of things related to Seating in Book My Show application
 * 
 * @author sunnysabhanam
 *
 */
public class TicketsPage extends ExtendedWebPage{

	SortedMap<String,String> layout = new TreeMap<>();
	
	List<WebElement> allRows = null;
	ArrayList<String> seatIndex = new ArrayList<String>();
	
	/**
	 * verify whether there are any tickets available based on Above the row, No of tickets, Low Price and High Price
	 * 
	 * @param aboveRow : Above the row where you want your seating
	 * @param no_of_tickets : Total no of tickets you want to purchase
	 * @param lowPrice : Lowest price of tickets
	 * @param highPrice : Highest price of tickets
	 * 
	 * @return boolean : Tickets available or not
	 */
	public boolean verifyTickets(int aboveRow, int no_of_tickets, double lowPrice, double highPrice) {
		
		//Remove bottom rows as per user irrespective of pricing
		for(int i = 0; i < aboveRow; i++) {
			seatIndex.get(seatIndex.size()-(1+i));
			for(Entry<String,String> entry : layout.entrySet()) {
				layout.put(entry.getKey(), entry.getValue().replace(seatIndex.get(seatIndex.size()-(1+i)), "").replaceAll("\\;+", ";"));
			}
		}
		
		for(Entry<String,String> row : layout.entrySet()) {
			
			double currentPrice = Double.parseDouble(row.getKey().split("\\#")[1].trim());
			
			if(currentPrice <= highPrice &&
					lowPrice <= currentPrice) {
				
				for(String seatingRow : row.getValue().split("\\;")) {
					
					if(!seatingRow.isEmpty()) {
						
						if(driver
							.findElements(By.xpath("//*[@class='setmain']/tbody/tr["+(Integer.parseInt(seatingRow)+1)+"]/td/div/a[@class='_available']"))
							.size()>no_of_tickets) {
							return true;
						}

					}
					
				}
				
			}
			
		}
		
		return false;
		
	}

	/**
	 * Selecting the seats based on
	 * 1) Seats should be beside each other
	 * 2) Seats should be above certain row
	 * 3) Seats should be with in given price range
	 * 
	 * @param aboveRow : Above the row where you want your seating
	 * @param no_of_tickets : Total no of tickets you want to purchase
	 * @param lowPrice : Lowest price of tickets
	 * @param highPrice : Highest price of tickets
	 * 
	 * @throws Exception : If no seats are available beside each other with specified quantity
	 */
	public void selectTickets(int aboveRow, int no_of_tickets, double lowPrice, double highPrice) throws Exception{
		
		for(Entry<String,String> row : layout.entrySet()) {
			
			double currentPrice = Double.parseDouble(row.getKey().split("\\#")[1].trim());
			
			if(currentPrice <= highPrice &&
					lowPrice <= currentPrice) {
				
				//verify beside total number of seats
				for(String seatingRow : row.getValue().split("\\;")) {
					
					if(!seatingRow.isEmpty()) {
						
						List<WebElement> seatsElements = driver
								.findElements(By.xpath("//*[@class='setmain']/tbody/tr["+(Integer.parseInt(seatingRow)+1)+"]/td/div/a[@class='_available']/.."));
						
						ArrayList<String> seats = new ArrayList<String>();
						
						if(seatsElements.isEmpty()) {
							continue;
						}
						
						List<WebElement> totalSeats = driver
								.findElements(By.xpath("//*[@class='setmain']/tbody/tr["+(Integer.parseInt(seatingRow)+1)+"]/td/div[@class='seatI']"));
						
						int temp = 0;
						
						for(int i = 0; i < totalSeats.size(); i++) {
							try {
								if(totalSeats.get(i).equals(seatsElements.get(temp))) {
									temp++;
									seats.add(""+(i+1));
								}
							}catch (Exception ignore) { }
						}
						
//						for(WebElement seatElement : seatsElements) {
//							
//							seats.add(seatElement.getAttribute("id").split("_")[2]);
//							
//						}
						
						for(int i = 0; i < seats.size(); i++) {
							try {
								if(Integer.parseInt(seats.get(i))+(no_of_tickets-1) == Integer.parseInt(seats.get(i+(no_of_tickets-1)))){
									
									clickByJavaScript(driver
											.findElement(By.xpath("(//*[@class='setmain']/tbody/tr["+(Integer.parseInt(seatingRow)+1)+"]/td/div[contains(@id,'"+totalSeats.get(Integer.parseInt(seats.get(i))-1).getAttribute("id")+"')]/a[@class='_available'])[1]")));
										
									return;
									
								}
							}catch (Exception e) {
								
							}
						}
					}
				}
			}
		}
		
		throw new Exception("Seats not available beside");
		
	}
	
	/**
	 * Plotting the layout of seats
	 */
	public void setLayout() {
		
		allRows = getListOfWebElements("BMS.Seats.Table");
		List<WebElement> priceRows = getListOfWebElements("BMS.Price.Rows");
		List<WebElement> nonPriceRows = getListOfWebElements("BMS.NonPrice.Rows");
		
		int nonPriceIndex = 0;
		int index = 0;
		String keyStr = "";
		
		for(WebElement eachRow : allRows) {
			try {
				if(eachRow.equals(priceRows.get(index))) {
					String priceText = eachRow.getText();
					double currentPrice = 0.00;
					if(priceText.contains("Rs.")) {
						currentPrice = Double.parseDouble(eachRow.getText().split("Rs.")[1].trim());
					}
					keyStr = allRows.indexOf(eachRow)+"#"+currentPrice;
					layout.put(keyStr, "");
					index++;
				}
			}catch (Exception ignore) { }
			try {
				if(eachRow.equals(nonPriceRows.get(nonPriceIndex))) {
					//if text is alphabet then only consider
					if(nonPriceRows.get(nonPriceIndex)
							.getText()
							.replaceAll("[^a-zA-Z]", "").trim()
							.length()>0) {
						seatIndex.add(""+allRows.indexOf(eachRow));
						
						String value = layout.get(keyStr);
						
						if(!value.isEmpty()) {
							value += ";";
						}
						
						value += allRows.indexOf(eachRow);
						
						layout.put(keyStr, value);
						
					}
					nonPriceIndex++;
				}

			}catch (Exception ignore) { }
		}
		
	}

	/**
	 * Cancel the layout
	 */
	public void cancelLayout() {
		clickByJavaScript("BMS.CancelSeatLayout.Button");	
	}

	/**
	 * Click on Pay button
	 */
	public void clickOnPay() {
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		click("BMS.PayNow.Button");
		
		isVisible("BMS.BookingSummary.Header", 2500);
		
		waitTillPageLoadComplete();
		
	}
	
}
