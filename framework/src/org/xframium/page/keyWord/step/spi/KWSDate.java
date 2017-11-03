package org.xframium.page.keyWord.step.spi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        TODAY( 1, "TODAY", "Gets the current date" ), DAYS_BETWEEN( 2, "DAYS_BETWEEN", "Gets the count of days between 2 dates" ), HOURS_BETWEEN( 3, "HOURS_BETWEEN", "Gets the count of hours between 2 dates" ), MINUTES_BETWEEN( 4, "MINUTES_BETWEEN",
                "Gets the count of minutes between 2 dates" ), SECONDS_BETWEEN( 5, "SECONDS_BETWEEN", "Gets the count of seconds between 2 dates" ), ADD_DAYS( 6, "ADD_DAYS", "Add or subtracts days from the supplied date" ), ADD_HOURS( 7, "ADD_HOURS",
                        "Add or subtracts hours from the supplied date" ), ADD_MINUTES( 8, "ADD_MINUTES",
                                "Add or subtracts minutes from the supplied date" ), ADD_SECONDS( 9, "ADD_SECONDS", "Add or subtracts seconds from the supplied date" ), PARSE( 10, "PARSE", "Parse a string into a date" );

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

        String operationName = getName();
        String format = getParameterAsString( FORMAT, contextMap, dataMap, executionContext.getxFID() );
        String valueOne = getParameterAsString( VALUE_ONE, contextMap, dataMap, executionContext.getxFID() );
        String valueTwo = getParameterAsString( VALUE_TWO, contextMap, dataMap, executionContext.getxFID() );
        String countValue = getParameterAsString( COUNT, contextMap, dataMap, executionContext.getxFID() );
        String returnValue = null;

        int count = countValue != null ? Integer.parseInt( countValue ) : 0;
        Calendar dateOne = Calendar.getInstance();
        if ( valueOne != null )
            dateOne.setTime( DateUtility.instance().parseDate( valueOne ) );

        Calendar dateTwo = Calendar.getInstance();
        if ( valueTwo != null )
            dateTwo.setTime( DateUtility.instance().parseDate( valueTwo ) );

        switch ( DateOperation.valueOf( operationName.toUpperCase() ) )
        {
            case ADD_DAYS:
                if ( valueOne == null || count == 0 || format == null )
                    throw new ScriptConfigurationException( "ADD_DAYS must contain Format, Value One and Count parameters" );

                dateOne.add( Calendar.DATE, count );
                returnValue = new SimpleDateFormat( format ).format( dateOne.getTime() );
                break;

            case ADD_HOURS:
                if ( valueOne == null || count == 0 || format == null )
                    throw new ScriptConfigurationException( "ADD_HOURS must contain Format, Value One and Count parameters" );

                dateOne.add( Calendar.HOUR, count );
                returnValue = new SimpleDateFormat( format ).format( dateOne.getTime() );
                break;

            case ADD_MINUTES:
                if ( valueOne == null || count == 0 || format == null )
                    throw new ScriptConfigurationException( "ADD_MINUTES must contain Format, Value One and Count parameters" );

                dateOne.add( Calendar.MINUTE, count );
                returnValue = new SimpleDateFormat( format ).format( dateOne.getTime() );
                break;

            case ADD_SECONDS:
                if ( valueOne == null || count == 0 || format == null )
                    throw new ScriptConfigurationException( "ADD_SECONDS must contain Format, Value One and Count parameters" );

                dateOne.add( Calendar.SECOND, count );
                returnValue = new SimpleDateFormat( format ).format( dateOne.getTime() );
                break;

            case DAYS_BETWEEN:
                if ( valueOne == null || valueTwo == null )
                    throw new ScriptConfigurationException( "DAYS_BETWEEN must contain Value One and Value Two parameters" );

                returnValue = ((int)(Math.ceil(Math.abs( dateOne.getTimeInMillis() - dateTwo.getTimeInMillis() ) / (24.0 * 60.0 * 60.0 * 1000.0)))) + "";
                break;

            case HOURS_BETWEEN:
                if ( valueOne == null || valueTwo == null )
                    throw new ScriptConfigurationException( "HOURS_BETWEEN must contain Value One and Value Two parameters" );

                returnValue = ((int)(Math.ceil(Math.abs( dateOne.getTimeInMillis() - dateTwo.getTimeInMillis() ) / (60 * 60 * 1000)))) + "";
                break;

            case MINUTES_BETWEEN:
                if ( valueOne == null || valueTwo == null )
                    throw new ScriptConfigurationException( "MINUTES_BETWEEN must contain Value One and Value Two parameters" );

                returnValue = ((int)(Math.ceil(Math.abs( dateOne.getTimeInMillis() - dateTwo.getTimeInMillis() ) / (60 * 1000)))) + "";
                break;

            case PARSE:
                if ( valueOne == null || format == null )
                    throw new ScriptConfigurationException( "PARSE must contain Value One and Format parameters" );

                returnValue = new SimpleDateFormat( format ).format( dateOne.getTime() );
                break;

            case SECONDS_BETWEEN:
                if ( valueOne == null || valueTwo == null )
                    throw new ScriptConfigurationException( "SECONDS_BETWEEN must contain Value One and Value Two parameters" );

                returnValue = (Math.abs( dateOne.getTimeInMillis() - dateTwo.getTimeInMillis() ) / (1000)) + "";
                break;

            case TODAY:
                if ( format == null )
                    throw new ScriptConfigurationException( "TODAY must contain a FORMAT parameter" );

                returnValue = new SimpleDateFormat( format ).format( System.currentTimeMillis() );
                break;

        }

        if ( !validateData( returnValue ) )
            throw new IllegalStateException( "STRING Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + returnValue + "]" );

        if ( getContext() != null )
        {
            if ( log.isDebugEnabled() )
                log.debug( "Setting Context Data to [" + returnValue + "] for [" + getContext() + "]" );
            addContext( getContext(), returnValue, contextMap, executionContext );
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
