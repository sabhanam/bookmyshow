# Book My Show automatic seat selection
# This program thinks how a human thinks and books a ticket just like a human
# Just give the basic parameter and find your seats exactly the way you want
#  
# This program is just a glimpse on our team's capability
# Watch the full video of execution here: 
@Sample
Feature: Book My Show automation

	Background: Navigating to home screen and closing any pop-up's
		Given I am navigating to Book My Show home page
		When There are multiple pop-up's close them
		
	#Book My Show front end automation script
	Scenario Outline: Book My Show automatic seat selection
		Then I will search for the movie "<Movie Name>"
		Then I will set the "<Language>" and "<Format>" 
		And I will change the date to "<Date>"
		Then I will filter the venues based on "<Area>" , "<Start Time>" and "<End Time>"
		And I will filter venues again based on "<Low Price>" and "<High Price>"
		Then I will search all venues and find appropriate venue based on "<No of Tickets>" , "<Above Row>" , "<Low Price>" , and "<High Price>"
		
	Examples:
	|Movie Name			    |No of Tickets |Start Time	 |Language|Format|End Time|Area		   |Date |Low Price|High Price |Above Row|
#	|Sample Movie Name		|5			   |02:30 PM	 |Hindi	  |3D	 |10:20 PM|Gachibowli  |02   |40.00	   |180.00	   |5		 |
	|Syeraa Narasimha Reddy |5			   |11:30 AM	 |Telugu  |2D	 |10:20 PM|Kukatpally  |08   |40.00	   |180.00	   |5		 |
	|War			    	|6			   |02:30 PM	 |Hindi	  |2D	 |05:20 PM|Kukatpally  |04   |10.00	   |180.00	   |5		 |
	|Gaddalakonda Ganesh    |3			   |04:30 PM	 |Telugu  |2D	 |10:20 PM|Uppal	   |03   |100.00   |500.00	   |2		 |
	|War				    |5			   |12:30 PM	 |Telugu  |2D	 |10:20 PM|Moosapet    |04   |80.00	   |250.00	   |3		 |
	|Syeraa Narasimha Reddy |6			   |10:30 AM	 |Hindi   |2D	 |10:20 PM|Abids	   |08   |120.00   |180.00	   |6		 |
	
	
	