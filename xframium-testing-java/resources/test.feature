Feature: Call Gherkin Functions
 
Scenario: Call method one
	Given I call method one
	Then I trace {CLICK} on {WebHomePage.toggleValue}
	Then I use {CLICK} on {WebHomePage.toggleValue}
	
Scenario: Call two methods
	Given I call method one
	Then I call method two with shouldBeTestData

	
Scenario Outline: Call two methods with data
	Given I call method one
	Then I call method two with <testData>
	
Examples: 
	| testData  | name  |
	| dataOne   | one   |
	| dataTwo   | two   |
	| dataThree | three |
	

Scenario Outline: Call two methods with local data
	Given I call method one
	Then I call method two with <value>
	
Examples: Page Data=dataLoop
||


Scenario: Call two methods with a data table
	Given I call method one
	 | Fields | Values |
	 | name   | myName |
	 | age    | 10     |
	 | uName  | uName  | 
	Then I call method two with shouldBeTestData

