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
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.element.Element.WAIT_FOR;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSWaitFor.
 */
public class KWSWaitFor extends AbstractKeyWordStep
{
    public KWSWaitFor()
    {
        kwName = "Wait for element";
        kwDescription = "Allows the script to wait for an element to be exist in a certain state";
        kwHelp = "https://www.xframium.org/keyword.html#kw-waitfor";
    }
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
	    if ( pageObject == null )
            throw new IllegalStateException( "There was no Page Object defined" );
		
		int waitFor = 15;
		
		if ( getParameterList().size() > 0 )
		{
			try
			{
				waitFor = Integer.parseInt( getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "" );
			}
			catch( Exception e ) {}
		}
		boolean returnValue = false;

        String[] waitType = getName().split( "\\." );
        if ( waitType.length == 2 )
            returnValue = getElement( pageObject, contextMap, webDriver, dataMap, waitType[ 0 ] ).waitFor( waitFor, TimeUnit.SECONDS, WAIT_FOR.valueOf( waitType[ 1 ] ), "" );
        else
            returnValue = getElement( pageObject, contextMap, webDriver, dataMap ).waitForPresent( waitFor, TimeUnit.SECONDS );

		
		return returnValue;
		
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
	 */
	public boolean isRecordable()
	{
		return false;
	}

}
