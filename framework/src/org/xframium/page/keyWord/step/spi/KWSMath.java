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
import org.xframium.exception.ScriptException;
import org.xframium.gesture.device.action.DeviceAction.ActionType;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSValue.
 */
public class KWSMath extends AbstractKeyWordStep
{
    public KWSMath()
    {
        kwName = "Math";
        kwDescription = "Allows the script to perform some basic math operations";
        kwHelp = "https://www.xframium.org/keyword.html#kw-math";
        orMapping = false;
        category = "Utility";
    }
    /**
     * Format string.
     *
     * @param parameterValue the parameter value
     * @return the string
     */
    private String formatString( String parameterValue )
    {
        return parameterValue.replace( "$", " " ).replace( "%", " " ).trim();
    }
    
    public enum MATH_TYPE
    {
        add(1, "add" , "Add numbers and compare the results "),
        subtract(2, "subtract", "Subtract numbers and compare the results");
        
        public List<MATH_TYPE> getSupported()
        {
            List<MATH_TYPE> supportedList = new ArrayList<MATH_TYPE>( 10 );
            supportedList.add( MATH_TYPE.add );
            supportedList.add( MATH_TYPE.subtract );
            return supportedList;
        }
        
        private MATH_TYPE( int id, String name, String description )
        {
            this.id = id;
            this.name= name;
            this.description = description;
        }
        
        private int id;
        private String name;
        private String description;
    }
    
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC )
	{
	    String compareToString = null;
	    double currentValue = 0;
	    boolean valueAdded = false;
	    for ( int i=0; i<getParameterList().size(); i++ )
	    {
	        

	        String currentParameter = getParameterValue( getParameterList().get( i ), contextMap, dataMap ) + "";
	        
	        if ( getParameterList().get( i ).getValue().startsWith( "=" ) )
	        {
	            KeyWordParameter localParam = new KeyWordParameter( getParameterList().get( i ).getType(), getParameterList().get( i ).getValue().substring( 1 ), null, null );
	            compareToString = getParameterValue( localParam, contextMap, dataMap ) + "";
	        }
	        else
	        {
	            switch ( MATH_TYPE.valueOf( getName().toLowerCase() ) )
	            {
	                case add:
	                    currentValue += Double.parseDouble( formatString( currentParameter ) );
	                    break;
	                    
	                case subtract:
	                    if ( valueAdded )
	                        currentValue -= Double.parseDouble( formatString( currentParameter ) );
	                    else
	                    {
	                        currentValue = Double.parseDouble( formatString( currentParameter ) );
	                        valueAdded = true;
	                    }
	            }
	        }
	    }
	    
	    if ( compareToString != null )
	    {
	        double compareTo = Double.parseDouble( formatString( compareToString ) );
	        
	        if ( compareTo != currentValue )
                throw new ScriptException( "GET Expected [" + compareTo + "] but found [" + currentValue + "]" );
	    }
		
		if ( !validateData( currentValue + "" ) )
            throw new ScriptException( "MATH Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + currentValue + "]" );
        
        if ( getContext() != null )
        {
            if ( log.isDebugEnabled() )
                log.debug( "Setting Context Data to [" + currentValue + "] for [" + getContext() + "]" );
            contextMap.put( getContext(), currentValue + "" );
        }
		
		return true;
	}

}
