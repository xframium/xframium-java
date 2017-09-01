/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
package org.xframium.page.keyWord.step.spi;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.utility.DateUtility;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSValue.
 */
public class KWSString2 extends AbstractKeyWordStep
{
    private static final String FORMAT = "Decimal Format";
    private static final String _FORMAT = "Format";
    private static final String O_VALUE = "Original Value";
    private static final String REGEX = "Regex";
    private static final String VALUE = "Value";
    private static final String BEGIN = "Begin";
    private static final String END = "End";
    private static final String COMPARE = "Compare To";
    private static final String ADD_DAYS = "Add Days";
    private static final String SUBTRACT_DAYS = "Subtract Days";
    
    public KWSString2()
    {
        kwName = "String Operations";
        kwDescription = "Allows the script to perform some basic string and formatting operations";
        kwHelp = "https://www.xframium.org/keyword.html#kw-string";
        orMapping = false;
        category = "Utility";
    }
    
    public enum OperationType
    {
        TRIM( 1, "TRIM", "Trim excess space" ), 
        LOWER( 2, "LOWER", "Change to lowercase" ), 
        UPPER( 3, "UPPER", "Change to uppercase" ), 
        DECIMAL( 4, "DECIMAL", "Apply decimal formatting" ), 
        REGEX( 5, "REGEX", "Validate with regular Expressions" ), 
        CONTAINS( 6, "CONTAINS", "Find a string in a string" ), 
        CONCAT( 7, "CONCAT", "Add all strings together" ), 
        SUBSTR( 8, "SUBSTR","Extract a portion of the string" ),
        DATE( 9, "DATE","Convert a date" )
        ;

        public List<OperationType> getSupported()
        {
            List<OperationType> supportedList = new ArrayList<OperationType>( 10 );
            supportedList.add( OperationType.TRIM );
            supportedList.add( OperationType.LOWER );
            supportedList.add( OperationType.UPPER );
            supportedList.add( OperationType.DECIMAL );
            supportedList.add( OperationType.REGEX );
            supportedList.add( OperationType.CONTAINS );
            supportedList.add( OperationType.CONCAT );
            supportedList.add( OperationType.SUBSTR );
            supportedList.add( OperationType.DATE );
            return supportedList;
        }

        private OperationType( int id, String name, String description )
        {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        private int id;
        private String name;
        private String description;
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
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
    {
        boolean rtn = true;
        String originalValue = null;
        String operationName = getName();
        int paramCount = getParameterList().size();

        originalValue = getParameterValue( getParameter( O_VALUE ), contextMap, dataMap, executionContext.getxFID() );

        String newValue = null;

        switch ( OperationType.valueOf( getName().toUpperCase() ) )
        {
            case TRIM:
                newValue = originalValue.trim();
                break;

            case LOWER:
                newValue = originalValue.toLowerCase();
                break;

            case DECIMAL:
                
                DecimalFormat decimalFormat = new DecimalFormat( getParameterValue( getParameter( FORMAT ), contextMap, dataMap, executionContext.getxFID() ) );
                decimalFormat.setRoundingMode( RoundingMode.DOWN );
                newValue = decimalFormat.format( Double.parseDouble( originalValue ) );
                break;

            case DATE:
                validateParameters( new String[] { _FORMAT } );
                
                Date useDate = null;
                
                if ( getParameter( O_VALUE ) == null )
                    useDate = new Date( System.currentTimeMillis() );
                else
                    useDate = DateUtility.instance().parseDate( originalValue );

                if ( getParameter( ADD_DAYS ) != null )
                {
                    Calendar useCalendar = Calendar.getInstance();
                    useCalendar.setTime( useDate );
                    useCalendar.add( Calendar.DATE, Integer.parseInt( getParameterValue( getParameter( ADD_DAYS ), contextMap, dataMap, executionContext.getxFID() ) ) );
                    useDate = useCalendar.getTime();
                }
                
                if ( getParameter( SUBTRACT_DAYS ) != null )
                {
                    Calendar useCalendar = Calendar.getInstance();
                    useCalendar.setTime( useDate );
                    useCalendar.add( Calendar.DATE, 0 - Integer.parseInt( getParameterValue( getParameter( SUBTRACT_DAYS ), contextMap, dataMap, executionContext.getxFID() ) ) );
                    useDate = useCalendar.getTime();
                }
                
                newValue = new SimpleDateFormat( getParameterValue( getParameter( _FORMAT ), contextMap, dataMap, executionContext.getxFID() ) ).format( useDate );
                
                break;
                
            case UPPER:
                newValue = originalValue.toUpperCase();
                break;

            case REGEX:
                String regex = getParameterValue( getParameter( REGEX ), contextMap, dataMap, executionContext.getxFID() );
                Pattern regexPattern = Pattern.compile( regex );
               
                if ( log.isInfoEnabled() )
                {
                    log.info(  "Attempting to locate [" + regex + "] in \r\n[" + originalValue + "]" );
                }
                
                Matcher regexMatcher = regexPattern.matcher( originalValue );
                if ( regexMatcher.find() )
                {
                    if ( getContext() != null )
                    {
                        for ( int i=1; i<regexMatcher.groupCount() + 1; i++ )
                        {
                            addContext( getContext() + "_Group " + i, regexMatcher.group( i ), contextMap, executionContext );
                        }
                    }
                    newValue = regexMatcher.group( 0 );
                }
                else
                    throw new ScriptException( "Could not locate value using " + regex );
                
                break;
                
            case CONTAINS:
                String expectedValue = getParameterValue( getParameter( VALUE ), contextMap, dataMap, executionContext.getxFID() );

                if ( expectedValue.isEmpty() )
                {
                    throw new ScriptConfigurationException( "STRING Expected format is: [" + expectedValue + "]" );
                }

                if ( !originalValue.contains( expectedValue ) && !(expectedValue == null) && !(expectedValue.equals( null )) )
                {
                    throw new ScriptException( "STRING Expected [" + expectedValue + "] and original[" + originalValue + "] format is not matching " );
                }
                else
                {
                    newValue = expectedValue;
                }
                break;

            case CONCAT:
                StringBuilder buff = new StringBuilder();
                for( int i = 0; i < paramCount; ++i )
                {
                    buff.append( getParameterValue( getParameterList().get( i ), contextMap, dataMap, executionContext.getxFID() ) );
                }
                newValue = buff.toString();
                break;

            case SUBSTR:

                int beginIndex = getInt( getParameterValue( getParameter( BEGIN ), contextMap, dataMap, executionContext.getxFID() ) );
                int endIndex = getInt( getParameterValue( getParameter( END ), contextMap, dataMap, executionContext.getxFID() ) );

                newValue = originalValue.substring( beginIndex, endIndex );

                if ( getParameter( VALUE ) != null )
                {
                    String compareTo = getParameterValue( getParameter( COMPARE ), contextMap, dataMap, executionContext.getxFID() );
                    rtn = newValue.equals( compareTo );
                }
                
                break;
        }

        if ( !validateData( newValue ) )
            throw new IllegalStateException( "STRING Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + newValue + "]" );

        if ( getContext() != null )
        {
            if ( log.isDebugEnabled() )
                log.debug( "Setting Context Data to [" + newValue + "] for [" + getContext() + "]" );
            addContext( getContext(), newValue , contextMap, executionContext );
        }

        return rtn;
    }

    //
    // Helpers
    //

    private int getInt( String value )
    {
        int rtn = 0;
        
        try
        {
            rtn = Integer.parseInt( value );
        }
        catch( Exception e )
        {
            throw new IllegalStateException( e );
        }

        return rtn;
    }
}
