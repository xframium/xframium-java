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


public class KWSNavigate extends  AbstractKeyWordStep {

    public KWSNavigate()
    {
        kwName = "Web Navigation";
        kwDescription = "Allows the script to click forward, back and refresh on a web based application";
        kwHelp = "https://www.xframium.org/keyword.html#kw-navigate";
    }

	/**
	 * The Enum NavgationType.
	 */
	private enum NavigationType
	{

		/** Navigate Forward */
		FORWARD, 

		/** Navigate back */
		BACK, 
		
		/** Refresh */
		REFRESH,
		

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
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
		Boolean success = false;
		if ( pageObject == null )
			throw new IllegalStateException( "There was no Page Object defined" );
		
		if ( getParameterList().size() < 1)
			throw new IllegalArgumentException( "Please provide the type of navigation as the first parameter" );
		
		try{
			String NavigateType = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";

			switch ( NavigationType.valueOf( NavigateType ) )
			{
			case BACK:				
				webDriver.navigate().back();
				success = true;
				break;
			case FORWARD:
				webDriver.navigate().forward();
				success=true;
				break;
			case REFRESH:
				webDriver.navigate().refresh();
				success=true;
				break;			
			default:
				throw new IllegalArgumentException( "Parameter NavigationType should be FORWARD|BACK|REFRESH|MAXIMIZE" );
			}
		}
		catch(Exception c){
			throw new IllegalStateException( c );
		}
		return  success;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
	 */
	public boolean isRecordable()
	{
		return false;
	}





}
