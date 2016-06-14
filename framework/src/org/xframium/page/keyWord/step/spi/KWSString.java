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
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSValue.
 */
public class KWSString extends AbstractKeyWordStep
{

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
	    String originalValue = null;
	    String operationName = null;
	    
	    if ( getParameterList().size() == 1 )
        {
            originalValue = getElement( pageObject, contextMap, webDriver, dataMap ).getValue();
            operationName = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
        }
	    else if ( getParameterList().size() == 2 )
	    {
	        originalValue = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
	        operationName = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "";
	    }

		String newValue = null;
		
		switch ( operationName.toLowerCase() )
		{
		    case "trim":
		        newValue = originalValue.trim();
		        break;
		        
		    case "lower":
		        newValue = originalValue.toLowerCase();
		        break;
		        
		    case "upper":
		        newValue = originalValue.toUpperCase();
		        break;
		}
		
		
		if ( !validateData( newValue + "" ) )
            throw new IllegalStateException( "STRING Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + newValue + "]" );
        
        if ( getContext() != null )
        {
            if ( log.isDebugEnabled() )
                log.debug( "Setting Context Data to [" + newValue + "] for [" + getContext() + "]" );
            contextMap.put( getContext(), newValue );
        }
		
		return true;
	}

}
