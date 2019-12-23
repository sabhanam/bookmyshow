package com.sspart.bookmyshow;

import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.sspart.pages.HomePage;
import com.sspart.pages.MoviePage;
import com.sspart.pages.TicketsPage;

/**
 * 
 * Book My Show automatic seat selection
 * This program thinks how a human thinks and books a ticket just like a human
 * Just give the basic parameter and find your seats exactly the way you want
 * 
 * This program is just a glimpse on our team's capability
 * Watch the full video of execution here: 
 * 
 * @author sunnysabhanam
 *
 */
public class BookMyShow 
{
	//Provide the movie name (Only movie name)
	String movieName = "Venky Mama";
	
	//Total number of tickets you want to purchase
	int no_of_tickets = 2;
	
	//Starting time and Ending time to pick the show
	String start_time = "11:20 AM";
	String end_time = "11:20 PM";
	
	//Area of Venue
	String area = "Gachibowli";
	
	//Date you want to watch movie
	String date = "24";
	
	//Price range
	Double lowPrice = 120.00;
	Double highPrice = 480.00;
	
	//Above how many rows you want to search
	int aboveRow = 5;
	
	//Language of choice
	String language = "Telugu";
	
	//Format of choice 2D/3D/4DS
	String format = "2D";
	
	/**
	 * 
	 * Book My Show front end automation script
	 * 
	 * @throws Exception
	 */
	@Test()
	public void automaticBookMyShow() throws Exception {
		
		ArrayList<String> validVenues = new ArrayList<String>();
		
		//Declaring variables
		HomePage homePage = new HomePage();
		MoviePage moviePage = new MoviePage();
		
		//Navigate to Book My Show home page
		homePage.navigateToHomePage();
		
		//Closing pop-up's which are intermittent on Home page
		homePage.closePopups();
		
		//Search for the movie from the list of all movies available
		homePage.searchMovie(movieName);
		
		//Changing movie language and format if necessary
		moviePage.changeMovieLanguageAndFormat(language,format);
		
		//Changing the date
		moviePage.selectDate(date);
		
		//Based on Area, booking start time and booking end time short-list the venue's & show times
		validVenues = moviePage.getAllValidVenues(area, start_time, end_time);
	
		//filter the validVenues list based on pricing
		validVenues = moviePage.updateValidVenues(validVenues, lowPrice, highPrice);
		
		boolean bool = false;
		
		for(String validVenue : validVenues) {
			
			//Selecting the venue
			moviePage.selectVenueWithTime(validVenue, no_of_tickets);
			
			TicketsPage ticketsPage = new TicketsPage();
			
			//Plot the layout of seats 
			ticketsPage.setLayout();
			
			//Before selecting the seats verify whether seats are available or not
			if(ticketsPage.verifyTickets(aboveRow, no_of_tickets, lowPrice, highPrice)) {
				
				try {
					
					//Select seats
					//Parameters involved:
					//1) Seats should be beside each other
					//2) Seats should be above certain row
					//3) Seats should be with in given price range
					ticketsPage.selectTickets(aboveRow, no_of_tickets, lowPrice, highPrice);
					
					//Click on payment button
					ticketsPage.clickOnPay();
					
					bool = true;
					
					break;
					
				}catch (Exception e) {
					
					//if tickets are not available beside, close the layout and go for next venue
					ticketsPage.cancelLayout();
					
				}
				
			}else {
				
				//if seats are not available, close the layout and go for next venue
				ticketsPage.cancelLayout();
				
			}
			
		}
		
		//Based on whether we selected anything or not, script will pass or fail after all retry's
		Assert.assertTrue(bool, "Verified all Venues and there are no seats available with given filters "
				+ "Movie Name: " + movieName
				+ "No of Tickets: " + no_of_tickets
				+ "Start Time: " + start_time
				+ "End Time: " + end_time
				+ "Area: " + area
				+ "Date: " + date
				+ "Low Price: " + lowPrice
				+ "High Price: " + highPrice
				+ "Above Row: " + aboveRow
				+ "Language: " + language
				+ "Formart: " + format);
		
	}
	
}
