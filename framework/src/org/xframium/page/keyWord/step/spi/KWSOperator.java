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
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.element.Element;
import org.xframium.page.element.ElementFork;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSFork.
 */
public class KWSOperator extends AbstractKeyWordStep
{
    public KWSOperator()
    {
        kwName = "Parameter Comparison";
        kwDescription = "Allows the script to use or, and and exclusive or operators to compare multiple sets of values";
        kwHelp = "https://www.xframium.org/keyword.html#kw-operator";
        orMapping = false;
    }
    
    private enum OPERATOR
    {
        AND,
        OR,
        XOR;
    }
    
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC ) throws Exception
	{
	    boolean returnValue = false;
	    OPERATOR operator = OPERATOR.valueOf( getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "" );
	    switch( operator )
	    {
	        case AND:
	            for ( int i=1; i<getParameterList().size(); i+=2 )
	            {
	                String valueOne = getParameterValue( getParameterList().get( i ), contextMap, dataMap ) + "";
	                String valueTwo = getParameterValue( getParameterList().get( i + 1 ), contextMap, dataMap ) + "";
	                if ( !valueOne.equals( valueTwo ) )
	                    throw new ScriptException( "[" + valueOne + "] was not equal to [" + valueTwo + "]" );
	                
	            }
	            break;
	            
	        case OR:
	            
	            for ( int i=1; i<getParameterList().size(); i+=2 )
                {
                    String valueOne = getParameterValue( getParameterList().get( i ), contextMap, dataMap ) + "";
                    String valueTwo = getParameterValue( getParameterList().get( i + 1 ), contextMap, dataMap ) + "";
                    returnValue = valueOne.equals( valueTwo );
                    if ( returnValue )
                        return true;
                    
                }
	            if ( !returnValue )
                    throw new ScriptException( "None of the conditions were met" );
                break;
                
	        case XOR:
                for ( int i=1; i<getParameterList().size(); i+=2 )
                {
                    String valueOne = getParameterValue( getParameterList().get( i ), contextMap, dataMap ) + "";
                    String valueTwo = getParameterValue( getParameterList().get( i + 1 ), contextMap, dataMap ) + "";
                    if ( valueOne.equals( valueTwo ) )
                    {
                        if ( returnValue )
                            throw new ScriptException( "Multiple conditions were met however only one was expectec" );
                        else
                            returnValue = true;
                    }
                    
                }
                if ( !returnValue )
                    throw new ScriptException( "None of the conditions were met" );
                    
                break;
	    }
		return true;
	}

}
