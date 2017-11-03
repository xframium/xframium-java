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
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSValue.
 */
public class KWSValue extends AbstractKeyWordStep
{
    public KWSValue()
    {
        kwName = "Get Value";
        kwDescription = "Allows the script to extract a value for an element";
        kwHelp = "https://www.xframium.org/keyword.html#kw-get";
        category = "Verification";
    }
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
	{
		String elementValue = getElement( pageObject, contextMap, webDriver, dataMap, executionContext ).getValue();
		
		if ( getParameterList().size() == 1 )
		{
			Object compareTo = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() );
			if ( !elementValue.equals( compareTo ) )
				throw new ScriptException( "GET Expected [" + compareTo + "] but found [" + elementValue + "]" );
		}
		
		if ( !validateData( elementValue + "" ) )
			throw new ScriptException( "GET Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + elementValue + "]" );
		
		if ( getContext() != null )
		{
			if ( log.isDebugEnabled() )
				log.debug( "Setting Context Data to [" + elementValue + "] for [" + getContext() + "]" );
			addContext( getContext(), elementValue, contextMap, executionContext );
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
	 */
	public boolean isRecordable()
	{
		return false;
	}

}
