# xframium-java
xFramium - Rapid unified test case development

xFramium is an easy to use test automation framework design around rapid test development for use with native, hybrid, mobile web and desktop browser applications.  Write tests using a simple and elegant XML keyword-driven alternative to coding. Use the same test cases on desktop and mobile web browsers. Take advantage of high-performance parallel execution, advanced caching and intelligent device selection algorithms. Easily connect test data from Excel, XML, JSON, CSV, JDBC and more.

# Current Release
1.0.1 

Chronos
6/4/2016
Reorganization of package structure
Added checks for page object not appearing in the XML model.
Added checks for validating all XPATH statements - The test will error out at the begining if invalid XPATH statements are und
Added database backed application registry
Added database backed cloud registry
Added database backed device registry
Added database backed page data
Added database backed object registry
Added database backed test definitions
Added a simple else clause. The else clause can be added to a step to execute on failure
Pulled latest selenium JARS
Added context variable for looping for more loop control
XML configuration option added for xframium-driver. Added the ability for a self contained test for POC and small tests
Press gestures added relative to element by percentages
Configuration files are not located relative to the location of the driver configuration files
output files are now relative to the driver configuration file
Added the property object descriptor - this is a simplified descriptor finding elements using a simple property reference
Added the ALIGN keyword to allow for alignment checks to various elements in relation to other elements on teh page
Added the GUI Launcher
Various documentation additions and bug fixes
Added snapshot keyword to dumped screen shots and object tree into the HTML report


