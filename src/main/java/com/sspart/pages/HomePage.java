package com.sspart.pages;

import org.openqa.selenium.By;

import static com.ramSabhanam.hybridDriver.WebUtil.*;

import com.ramSabhanam.hybridDriver.ExtendedWebPage;
import static com.ramSabhanam.configuration.Configuration.getBundle;

/**
 * 
 * @author sunnysabhanam
 *
 */
public class HomePage extends ExtendedWebPage{

	/**
	 * 
	 */
	public void navigateToHomePage() {
		
		driver.get(getBundle().get("app.url"));
		
	}
	
	/**
	 * 
	 */
	public void closePopups() {
		
		if(isVisible("BMS.Ad.Video", 2)) {
			clickByJavaScript("BMS.AdClose.Button");
		}
		
		isVisible("BMS.Search.TextBox", 250);
		
		if(isVisible("BMS.GetPersonalizedUpdates.Window", 25)) {
			click("BMS.NotNow.Button");
		}
		
	}

	/**
	 * 
	 * @param movieName
	 * @throws Exception
	 */
	public void searchMovie(String movieName) throws Exception {
		
		try {
			
			click("BMS.Search.TextBox");
			
			isVisible(driver.findElement(By.xpath("//div[@class='__event-container']/a[contains(.,'"+movieName+"')]")), 250);
	
			click(driver.findElement(By.xpath("//div[@class='__event-container']/a[contains(.,'"+movieName+"')]")));
		
		}catch (Exception e) {
			
			throw new Exception("Unable to find movie; Possible reasons:\n"
					+ "1) Movie name is wrong includes case sensitive\n"
					+ "2) Movie name doesn't exist in application");
			
		}
	}

	
	
}
