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
package com.xframium.page.keyWord.step.spi;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import com.xframium.page.Page;
import com.xframium.page.data.PageData;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;
import com.xframium.spi.driver.CachingDriver;



// TODO: Auto-generated Javadoc
/**
 * The Class KWSCache.
 */
public class KWSCache extends AbstractKeyWordStep
{

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
		if ( log.isDebugEnabled() )
			log.debug( "Executing Device Action " + getName() + " using " + getParameterList() );
		
		if ( webDriver instanceof CachingDriver )
		{
		    if ( log.isInfoEnabled() )
		    {
    		    if ( ( (CachingDriver) webDriver ).isCachingEnabled() )
        		    log.info( Thread.currentThread().getName() + ": Disabling Caching" );
    		    else
    		        log.info( Thread.currentThread().getName() + ": Enabling Caching" );
		    }
		    
		    ((CachingDriver) webDriver).setCachingEnabled( !( (CachingDriver) webDriver ).isCachingEnabled() );
		    
		}
		
		return true;
	}

}
