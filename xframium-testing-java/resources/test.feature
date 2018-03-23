Feature: Call Gherkin Functions
 
Scenario: Call method one
	Given I call method one
	
Scenario: Call two methods
	Given I call method one
	Then I call method two with 'testData'