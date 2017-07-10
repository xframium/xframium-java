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

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ScriptException;
import org.xframium.gesture.Gesture.Direction;
import org.xframium.page.BY;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;

// TODO: Auto-generated Javadoc
/**
 * <b>Keyword(s):</b> <code>EXISTS</code><br>
 * The exists keyword verifies that an element is present. It does not check vo
 * visibility it just verifies that it is in the XML structure<br>
 * <br>
 * <br>
 * <b>Example(s): </b>
 * <ul>
 * <li>This example will locate element named 'TEST_ELEMENT' from TEST_PAGE<br>
 * {@literal <step name="TEST_ELEMENT" type="EXISTS" page="TEST_PAGE" /> }<br>
 * </li>
 * </ul>
 */
public class KWSExists extends AbstractKeyWordStep
{
    public KWSExists()
    {
        kwName = "Present";
        kwDescription = "Allows the script validate that the named element is present in the tree";
        kwHelp = "https://www.xframium.org/keyword.html#kw-exists";
        category = "Verification";
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
        boolean returnValue = false;
        Element currentElement = null;
        if ( pageObject == null )
            throw new IllegalStateException( "There was no Page Object defined" );

        if ( getParameterList().size() == 2 )
        {
            int searchCount = Integer.parseInt( getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "" );
            for ( int i = 0; i < searchCount; i++ )
            {
                try
                {
                    currentElement = getElement( pageObject, contextMap, webDriver, dataMap, executionContext );
                    if ( currentElement.isPresent() )
                    {
                        returnValue = true;
                        break;
                    }
                }
                catch ( Exception e )
                {

                }
                scroll( Direction.valueOf( getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "" ), webDriver );
            }
        }
        else
        {

            currentElement = getElement( pageObject, contextMap, webDriver, dataMap, executionContext );
            returnValue = currentElement.isPresent();
        }

        if ( !currentElement.getBy().equals( BY.V_IMAGE ) && !currentElement.getBy().equals( BY.V_TEXT ) )
        {
            if ( !validateData( currentElement.getValue() + "" ) )
                throw new ScriptException( "EXISTS Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + currentElement.getValue() + "]" );
            
            if ( getContext() != null )
            {
                int elementCount = currentElement.getCount();
    
                if ( elementCount > 1 )
                {
                    if ( log.isDebugEnabled() )
                        log.debug( "Setting Context Data to [" + currentElement.getValue() + "] for [" + getContext() + "]" );
                    contextMap.put( getContext() + "_count", elementCount + "" );
                }
    
                if ( log.isDebugEnabled() )
                    log.debug( "Setting Context Data to [" + currentElement.getValue() + "] for [" + getContext() + "]" );
                contextMap.put( getContext(), currentElement.getValue() );
            }
        }

        return returnValue;
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
