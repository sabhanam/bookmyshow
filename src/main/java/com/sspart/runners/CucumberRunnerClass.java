package com.sspart.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * 
 * Cucumber runner class
 * 
 * @author sunnysabhanam
 *
 */
@CucumberOptions(
		glue = {"com.sspart.stepDefs"},
		features = "src/main/resources/features",
		plugin = { "json:target/cucumber-reports/Cucumber.json",
				 "junit:target/cucumber-reports/Cucumber.xml",
				 "html:target/cucumber-reports"}
		
)
public class CucumberRunnerClass extends AbstractTestNGCucumberTests{
	
	
	
}
