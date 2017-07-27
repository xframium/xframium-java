package org.xframium.page.keyWord.step.spi;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.utility.DateUtility;

public class KWSDate extends AbstractKeyWordStep
{

	private static String FORMAT = "Format";
	private static String VALUE_ONE = "Value One";
	private static String VALUE_TWO = "Value Two";
	private static String COUNT = "Count";

    public enum DateOperation
    {
        TODAY( 1, "TODAY", "Gets the current date" ), 
        DAYS_BETWEEN( 2, "DAYS_BETWEEN", "Gets the count of days between 2 dates" ),
        HOURS_BETWEEN( 3, "HOURS_BETWEEN", "Gets the count of hours between 2 dates" ),
        MINUTES_BETWEEN( 4, "MINUTES_BETWEEN", "Gets the count of minutes between 2 dates" ),
        SECONDS_BETWEEN( 5, "SECONDS_BETWEEN", "Gets the count of seconds between 2 dates" ),
        ADD_DAYS( 6, "ADD_DAYS", "Add or subtracts days from the supplied date" ), 
        ADD_HOURS( 7, "ADD_HOURS", "Add or subtracts hours from the supplied date" ), 
        ADD_MINUTES( 8, "ADD_MINUTES", "Add or subtracts minutes from the supplied date" ), 
        ADD_SECONDS( 9, "ADD_SECONDS", "Add or subtracts seconds from the supplied date" ),
        PARSE( 10, "PARSE", "Parse a string into a date");
        

        public List<DateOperation> getSupported()
        {
            List<DateOperation> supportedList = new ArrayList<DateOperation>( 10 );
            supportedList.add( DateOperation.TODAY );
            supportedList.add( DateOperation.DAYS_BETWEEN );
            supportedList.add( DateOperation.HOURS_BETWEEN );
            supportedList.add( DateOperation.MINUTES_BETWEEN );
            supportedList.add( DateOperation.SECONDS_BETWEEN );
            supportedList.add( DateOperation.ADD_DAYS );
            supportedList.add( DateOperation.ADD_HOURS );
            supportedList.add( DateOperation.ADD_MINUTES );
            supportedList.add( DateOperation.ADD_SECONDS );
            supportedList.add( DateOperation.PARSE );
            return supportedList;
        }

        private DateOperation( int id, String name, String description )
        {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        private int id;
        private String name;
        private String description;
    }

    
    public KWSDate()
    {
        kwName = "Date Operations";
        kwDescription = "Allows the script to perform some basic date operations";
        kwHelp = "https://www.xframium.org/keyword.html#kw-date";
        orMapping = false;
        category = "Utility";
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com
     * .perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map,
     * java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext ) throws Exception
    {
        if ( log.isDebugEnabled() )
            log.debug( "Execution Function " + getName() );

        if ( getParameterList().size() < 1 )
            throw new IllegalArgumentException( "First Parameter Switchtype should be provided with values CURRENTDATE| CUSTOMDATE" );

        try
        {
            String operationName = getName();
            String format = getParameterAsString( FORMAT, contextMap, dataMap, executionContext.getxFID() );
            String valueOne = getParameterAsString( VALUE_ONE, contextMap, dataMap, executionContext.getxFID() );
            String valueTwo = getParameterAsString( VALUE_TWO, contextMap, dataMap, executionContext.getxFID() );
            String countValue = getParameterAsString( COUNT, contextMap, dataMap, executionContext.getxFID() );
            
            int count = countValue != null ? Integer.parseInt( countValue ) : 0;
            Calendar dateOne = Calendar.getInstance();
            if ( valueOne != null )
                dateOne.setTime( DateUtility.instance().parseDate( valueOne ) );
            
            Calendar dateTwo = Calendar.getInstance();
            if ( valueTwo != null )
                dateTwo.setTime( DateUtility.instance().parseDate( valueTwo ) );
            
            switch( DateOperation.valueOf( operationName.toUpperCase() ) )
            {
                case ADD_DAYS:
                    if ( valueOne == null || count == 0 || format == null )
                        throw new ScriptConfigurationException( "ADD_DAYS must cotnains Format, Value One and Count parameters" );
                    
                    
                        
                case ADD_HOURS:
                case ADD_MINUTES:
                case ADD_SECONDS:
                case DAYS_BETWEEN:
                case HOURS_BETWEEN:
                case MINUTES_BETWEEN:
                case PARSE:
                case SECONDS_BETWEEN:
                case TODAY:
            }
            
            
           
            
        }
        catch ( Exception e )
        {
            log.error( "Error executing function for validation [" + getName() + "] on page [" + getPageName() + "]", e );
            return false;
        }

        return true;
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
     */
    public boolean isRecordable()
    {
        return false;
    }

   
}
