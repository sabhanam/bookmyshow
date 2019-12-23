package com.sspart.stepDefs;

import java.util.ArrayList;

import org.testng.Assert;

import com.sspart.pages.HomePage;
import com.sspart.pages.MoviePage;
import com.sspart.pages.TicketsPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BookMyShowStepDefs {

	HomePage homePage = new HomePage();
	MoviePage moviePage = new MoviePage();
	static ArrayList<String> validVenues = new ArrayList<String>();
	
	@Given("^I am navigating to Book My Show home page$")
	public void i_am_navigating_to_Book_My_Show_home_page() throws Throwable {
		
		//Navigate to Book My Show home page
		homePage.navigateToHomePage();
		
	}

	@When("^There are multiple pop-up's close them$")
	public void there_are_multiple_pop_up_s_close_them() throws Throwable {
	    
		//Closing pop-up's which are intermittent on Home page
		homePage.closePopups();
		
	}

	@Then("^I will search for the movie \"([^\"]*)\"$")
	public void i_will_search_for_the_movie(String movieName) throws Throwable {
	    
		//Search for the movie from the list of all movies available
		homePage.searchMovie(movieName);
		
	}

	@Then("^I will change the date to \"([^\"]*)\"$")
	public void i_will_change_the_date_to(String date) throws Throwable {
	    
		//Changing the date
		moviePage.selectDate(date);
		
	}

	@Then("^I will filter the venues based on \"([^\"]*)\" , \"([^\"]*)\" and \"([^\"]*)\"$")
	public void i_will_filter_the_venues_based_on_and(String area, String start_time, String end_time) throws Throwable {
	    
		validVenues = moviePage.getAllValidVenues(area, start_time, end_time);
		
	}

	@Then("^I will filter venues again based on \"([^\"]*)\" and \"([^\"]*)\"$")
	public void i_will_filter_venues_again_based_on_and(String lowPrice, String highPrice) throws Throwable {
	    
		validVenues = moviePage.updateValidVenues(validVenues, Double.parseDouble(lowPrice), Double.parseDouble(highPrice));
		
	}

	@Then("^I will search all venues and find appropriate venue based on \"([^\"]*)\" , \"([^\"]*)\" , \"([^\"]*)\" , and \"([^\"]*)\"$")
	public void i_will_search_all_venues_and_find_appropriate_venue_based_on_and(String tickets, String abvRow, String lowP, String highP) throws Throwable {
	    
		int no_of_tickets = Integer.parseInt(tickets);

		Double lowPrice = Double.parseDouble(lowP);
		Double highPrice = Double.parseDouble(highP);
		int aboveRow = Integer.parseInt(abvRow);
		
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
					//1) Seats should be beside of each other
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
		Assert.assertTrue(bool, "Verified all Venues and there are no seats available with given filters");
		
	}
	
	@Then("I will set the \"([^\"]*)\" and \"([^\"]*)\"")
	public void i_will_set_the_and(String language, String format) throws Exception {
	    
		//Changing movie language and format if necessary
		moviePage.changeMovieLanguageAndFormat(language, format);
		
	}
	
}
