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
import org.xframium.gesture.Gesture.Direction;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSExists.
 */
public class KWSVisible extends AbstractKeyWordStep
{
    public KWSVisible()
    {
        kwName = "Is Visible";
        kwDescription = "Allows the script to validate that the element exists and is visible on the screen - Can scroll and search as well";
        kwHelp = "https://www.xframium.org/keyword.html#kw-visible";
        category = "Visual";
    }
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC )
	{
		if ( pageObject == null )
			throw new IllegalStateException( "There was not page object defined" );
		
		if ( getParameterList().size() == 2 )
		{
			int searchCount = Integer.parseInt( getParameterValue(getParameterList().get(0), contextMap, dataMap) + "" );
			for ( int i=0; i<searchCount; i++)
			{
				try
				{
					if ( getElement( pageObject, contextMap, webDriver, dataMap ).isVisible() )
						return true;
				}
				catch( Exception e )
				{
					
				}
				
				scroll( Direction.valueOf( getParameterValue(getParameterList().get(1), contextMap, dataMap) + "" ), webDriver);

			}
			return false;
		}
		else
			return getElement( pageObject, contextMap, webDriver, dataMap ).isVisible();
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
	 */
	public boolean isRecordable()
	{
		return false;
	}


}
