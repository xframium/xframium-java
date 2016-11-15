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
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSValue.
 */
public class KWSMouse extends AbstractKeyWordStep
{
    public KWSMouse()
    {
        kwName = "Mouse Action";
        kwDescription = "Allows the script to move the pointer to a specific locations and press/release it";
        kwHelp = "https://www.xframium.org/keyword.html#kw-mouse";
    }
    
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC )
	{
	    String mouseAction = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
	    
	    switch( mouseAction.toUpperCase() )
	    {
	        case "MOVE_TO":
	            return getElement( pageObject, contextMap, webDriver, dataMap ).moveTo();
	            
	        case "PRESS":
                return getElement( pageObject, contextMap, webDriver, dataMap ).press();
                
	        case "CLICK_AT":
	            return getElement( pageObject, contextMap, webDriver, dataMap ).clickAt( Integer.parseInt( getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "" ), Integer.parseInt( getParameterValue( getParameterList().get( 2 ), contextMap, dataMap ) + "" ) );
                        
	        case "RELEASE":
                return getElement( pageObject, contextMap, webDriver, dataMap ).release();
	            
	    }
		
		return true;
	}

}
