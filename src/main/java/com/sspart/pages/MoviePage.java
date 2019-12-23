package com.sspart.pages;

import java.util.ArrayList;

import org.openqa.selenium.By;

import com.ramSabhanam.date_util.DateUtil;
import com.ramSabhanam.hybridDriver.ExtendedWebPage;

import static com.ramSabhanam.hybridDriver.WebUtil.*;
import static com.ramSabhanam.resources_util.ResourcesUtil.*;

/**
 * Movie Page
 * 
 * @author sunnysabhanam
 *
 */
public class MoviePage extends ExtendedWebPage{

	/**
	 * 
	 * @param date
	 * @throws Exception
	 */
	public void selectDate(String date) throws Exception {
		
		try {
			
			waitTillPageLoadComplete();
			
			isVisible(driver.findElement(By.xpath("//*[@id='showDates']//*[contains(text(),'"+date+"')]")), 250);
			
			click(driver.findElement(By.xpath("//*[@id='showDates']//*[contains(text(),'"+date+"')]")));
			
			waitTillPageLoadComplete();
			
		}catch (Exception e) {
			
			throw new Exception("Selected movie is not available on "+date+"; possible reasons:\n"
					+ "1) Date should be given in 'dd' format\n"
					+ "2) Selected movie is not available on " + date);
			
		}
		
	}

	/**
	 * 
	 * @param area
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getAllValidVenues(String area, String startTime, String endTime) throws Exception {

		ArrayList<String> validVenues = new ArrayList<String>();
		
		DateUtil dateUtil = new DateUtil();
		
		for(String venue : getListOfWebElementsText("BMS.VenueNames.Text", "text")) {
			
			if(venue.toLowerCase().contains(area.toLowerCase())) {
				
				for(String actual_time:getListOfWebElementsText(driver.findElements(By.xpath("(//li[@data-name='"+venue+"'])/descendant::*[contains(@class,'body')]//a[contains(@class,'showtime')]")), "text")) {
				
					if(dateUtil.compareDates(startTime, actual_time, "hh:mm a") <= 0 &&
							dateUtil.compareDates(endTime, actual_time, "hh:mm a") >= 0) {
						
						//System.out.println(actual_time);
						validVenues.add(venue+"#"+actual_time);
						
					}
				}
				
			}
		}
		
		if(!(validVenues.size()>0)) {
			
			throw new Exception("No venues available at "+area+" between "+startTime+" and "+endTime+"; possible reasons:\n"
					+ "1) Area name is wrong includes case sensitive\n"
					+ "2) start time or end time is in wrong format; should be hh:mm a\n"
					+ "3) No venues available at "+area+" between "+startTime+" and "+endTime);
			
		}
		
		return validVenues;
		
	}

	/**
	 * 
	 * @param validVenues
	 * @param lowPrice
	 * @param highPrice
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> updateValidVenues(ArrayList<String> validVenues, Double lowPrice, Double highPrice) throws Exception {
		
		ArrayList<String> validVenueWithMoreDetails = new ArrayList<String>();
		
		//validating time and availability of seats
		for(String valid_movie : validVenues) {
			
			String current_movie_details = driver.findElement(By.xpath("(//li[@data-name='"+valid_movie.split("\\#")[0]+"'])/descendant::*[contains(@class,'body')]//a[@data-date-time='"+valid_movie.split("\\#")[1]+"']")).getAttribute("data-cat-popup");
			
			for(String seat_details : current_movie_details.split("\\}\\,\\{")) {
				
				String seat_detail = seat_details.replace("{", "").replace("[", "").replace("]", "");
				boolean avail = false;
				boolean price = false;
				
				for(String internal_details : seat_detail.split("\\,")) {
					
					if(internal_details.contains("price")) {
						
						if(Double.parseDouble(internal_details.split("\\:")[1].replace("\"", "")) <= highPrice &&
								lowPrice <= Double.parseDouble(internal_details.split("\\:")[1].replace("\"", ""))) {
							price = true;
						}
						
						
					}else if(internal_details.contains("availabilityText")) {
						
						if(!internal_details.contains("Sold Out")) {
							avail = true;
						}
						
					}
					
				}
				
				//if all conditions satisfy add to valid venues with more details
				if(avail && price) {
					validVenueWithMoreDetails.add(valid_movie+"#"+seat_detail);
				}
			}
			
		}
		
		if(!(validVenueWithMoreDetails.size()>0)) {
			
			throw new Exception("No venues available between "+lowPrice+" and "+highPrice+"; possible reasons:\n"
					+ "1) Low price or High price is wrong should be in 100.00\n"
					+ "2) No venues available between "+lowPrice+" and "+highPrice);
			
		}
		
		return validVenueWithMoreDetails;
	}

	/**
	 * 
	 * @param validVenue
	 * @param seatsCount
	 */
	public void selectVenueWithTime(String validVenue, int seatsCount) {

		isVisible(driver.findElement(By.xpath("(//li[@data-name='"+validVenue.split("\\#")[0]+"'])/descendant::*[contains(@class,'body')]//a[@data-date-time='"+validVenue.split("\\#")[1]+"']")), 250);
		
		clickByJavaScript(driver.findElement(By.xpath("(//li[@data-name='"+validVenue.split("\\#")[0]+"'])/descendant::*[contains(@class,'body')]//a[@data-date-time='"+validVenue.split("\\#")[1]+"']")));
		
		if(isVisible("BMS.TermsAndConditions.PopUp", 125)) {
		
			click("BMS.TermsAndConditions.Accept.Button");
			
		}
		
		if(isVisible("BMS.SelectQuantity.PopUp", 125)) {
			
			click(driver.findElement(By.xpath("//*[@class='popover _qty-modal']/descendant::li[text()='"+seatsCount+"']")));
			
			click("BMS.SelectSeats.Button");
			
		}
		
		isVisible("BMS.Seats.TableLayout", 250);
		
	}

	/**
	 * 
	 * @param language
	 * @param format
	 * @throws Exception
	 */
	public void changeMovieLanguageAndFormat(String language, String format) throws Exception {
		
		if(isVisible("BMS.Language.Dropdown", 2)) {
			
			String currentLanguage = driver.findElement(getBy("BMS.CurrentLanguage.Dropdown")).getText();
			
			if(!(currentLanguage.contains(language) &&
					currentLanguage.contains(format))) {
				
				try {
					
					click("BMS.CurrentLanguage.Dropdown");
					
					isVisible(driver.findElement(By.xpath("//li[contains(@data-value,'"+language+"')][contains(@data-value,'"+format+"')]")), 25);
					
					click(driver.findElement(By.xpath("//li[contains(@data-value,'"+language+"')][contains(@data-value,'"+format+"')]")));
				
				}catch (Exception e) {
					
					throw new Exception("Provided language & format combination is not available; possible reasons:\n"
							+ "1) Language or format is wrong includes case sensitive\n"
							+ "2) Language or format doesn't exist for selected movie in application");
					
				}
			}
			
			
		}else {
			
			//verify only those are available
			String currentLanguage = driver.findElement(getBy("BMS.CurrentLanguage.Dropdown")).getText();
			
			if(!(currentLanguage.contains(language) &&
					currentLanguage.contains(format))) {
				throw new Exception("Provided language & format combination is not available; possible reasons:\n"
						+ "1) Language or format is wrong includes case sensitive\n"
						+ "2) Language or format doesn't exist for selected movie in application");
			}
			
		}
		
		
	}
	
}
