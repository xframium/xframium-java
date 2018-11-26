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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.FlowException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.utility.DateUtility;
import org.xframium.utility.Randomizer;

public class KWSRandom extends AbstractKeyWordStep
{
    public KWSRandom()
    {
        kwName = "Randomizer";
        kwDescription = "Allows the generation of synthetic test case data";
        kwHelp = "https://www.xframium.org/keyword.html#kw-random";
        category = "Utility";
        orMapping = false;
        featureId = 40;
    }
    
    public enum DataType
    {
        WORD( 1, "WORD", "Select a word" ),
        NUMBER( 2, "NUMBER", "Generate a number" ),
        TEXT( 3, "TEXT", "Generate formatted text" ),
        DATE( 4, "DATE", "Generate a date" ),
        FIRST_NAME( 5, "FIRST_NAME", "Generate a random first name" ),
        LAST_NAME( 6, "LAST_NAME", "Generate a random last name" ),
        ADDRESS( 7, "ADDRESS", "Generate a random address" ),
        EMAIL_ADDRESS( 8, "EMAIL_ADDRESS", "Generates a random Email address" );
        
        
        public List<DataType> getSupported()
        {
            List<DataType> supportedList = new ArrayList<DataType>( 10 );
            supportedList.add( DataType.WORD );
            supportedList.add( DataType.NUMBER );
            supportedList.add( DataType.TEXT );
            supportedList.add( DataType.DATE );
            supportedList.add( DataType.FIRST_NAME );
            supportedList.add( DataType.LAST_NAME );
            supportedList.add( DataType.ADDRESS );
            supportedList.add( DataType.EMAIL_ADDRESS );
            
            return supportedList;
        }

        private DataType( int id, String name, String description )
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
        if ( getContext() == null )
            return true;
        
        switch( DataType.valueOf( getName() ) )
        {
            case WORD:
                if ( validateParameters( new String[] { "Word List" } ) )
                    addContext( getContext(), Randomizer.instance().randomWord( getParameterValue( getParameter( "Word List" ), contextMap, dataMap, executionContext.getxFID() ) ), contextMap, executionContext );
                
                break;
            
            case NUMBER:
                if ( validateParameters( new String[] { "Minimum", "Maximum", "Format" } ) )
                    addContext( getContext(), Randomizer.instance().randomNumber( Double.parseDouble( getParameterValue( getParameter( "Minimum" ), contextMap, dataMap, executionContext.getxFID() ) ),
                                                                                      Double.parseDouble( getParameterValue( getParameter( "Maximum" ), contextMap, dataMap, executionContext.getxFID() ) ),
                                                                                      getParameterValue( getParameter( "Format" ), contextMap, dataMap, executionContext.getxFID() ) ), contextMap, executionContext );
                
                break;
                
            case FIRST_NAME:
                addContext( getContext(), Randomizer.instance().randomFirstName(), contextMap, executionContext );
                break;
                
            case LAST_NAME:
                addContext( getContext(), Randomizer.instance().randomLastName(), contextMap, executionContext );
                break;
                
            case EMAIL_ADDRESS:
                if ( getParameter( "Domain" ) != null )
                    addContext( getContext(), Randomizer.instance().randomEmailAddress( getParameterValue( getParameter( "Domain" ), contextMap, dataMap, executionContext.getxFID() ) ) , contextMap, executionContext );
                else
                    addContext( getContext(), Randomizer.instance().randomEmailAddress( null ) , contextMap, executionContext );
                break;
                
            case DATE:
                if ( validateParameters( new String[] { "Minimum", "Maximum", "Format" } ) )
                    addContext( getContext(), Randomizer.instance().randomDate( DateUtility.instance().parseDate( getParameterValue( getParameter( "Minimum" ), contextMap, dataMap, executionContext.getxFID() ) ),
                                                                                    DateUtility.instance().parseDate( getParameterValue( getParameter( "Maximum" ), contextMap, dataMap, executionContext.getxFID() ) ),
                                                                                    getParameterValue( getParameter( "Format" ), contextMap, dataMap, executionContext.getxFID() ) ), contextMap, executionContext );
                break;
                
            case ADDRESS:
                addContext( getContext() + "_ADDRESS_ONE", Randomizer.instance().randomInt( 100, 900 ) + " " + Randomizer.instance().randomStreetName(), contextMap, executionContext );
                addContext( getContext() + "_STREET", Randomizer.instance().randomStreetName(), contextMap, executionContext );
                addContext( getContext() + "_HOUSE_NUMBER", Randomizer.instance().randomInt( 100, 900 ), contextMap, executionContext );
                addContext( getContext() + "_CITY", Randomizer.instance().randomCityName(), contextMap, executionContext );;
                addContext( getContext() + "_STATE", Randomizer.instance().randomState(), contextMap, executionContext );
                addContext( getContext() + "_ZIP", Randomizer.instance().randomInt( 10000, 65000 ), contextMap, executionContext );
                break;
                

            case TEXT:
                if ( validateParameters( new String[] { "Format" } ) )
                    addContext( getContext(), Randomizer.instance().randomText( getParameterValue( getParameter( "Format" ), contextMap, dataMap, executionContext.getxFID() ) ), contextMap, executionContext );
                break;
            
        }

        return true;
    }

}
