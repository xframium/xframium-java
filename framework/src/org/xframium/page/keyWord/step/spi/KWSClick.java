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
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * <b>Keyword(s):</b> <code>CLICK</code><br>
 * The click keyword allows you to locate an element on the screen and click on it <br><br>
 * <br><b>Example(s): </b><ul>
 * <li> This example will click on an element named 'TEST_ELEMENT' from TEST_PAGE<br>
 * {@literal <step name="TEST_ELEMENT" type="CLICK" page="TEST_PAGE" /> }<br>
 * </li>
 * </ul>.
 */
public class KWSClick extends AbstractKeyWordStep
{
	public KWSClick()
	{
		kwName = "Click";
		kwDescription = "Allows the script to click on the center of the named element";
		kwHelp = "https://www.xframium.org/keyword.html#kw-click";
		category = "Interaction";
	}
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC )
	{

		if ( pageObject == null )
			throw new ScriptConfigurationException( "There was no Page Object defined");
		
		if(getParameterList().size() == 0){
			getElement( pageObject, contextMap, webDriver, dataMap ).click();
		} 
		else if(getParameterList().size() == 1)
		{	
			String clicks = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
			int intClicks = Integer.parseInt(clicks);
			getElement( pageObject, contextMap, webDriver, dataMap ).click(intClicks,250);
		}
		else {	
			String clicks = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
			String waitTime = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "";
			int intClicks = Integer.parseInt(clicks);
			int intwaitTime =Integer.parseInt(waitTime);			
			getElement( pageObject, contextMap, webDriver, dataMap ).click(intClicks,intwaitTime);
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
