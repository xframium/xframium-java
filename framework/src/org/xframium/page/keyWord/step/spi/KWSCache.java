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
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.driver.CachingDriver;



// TODO: Auto-generated Javadoc
/**
 * The Class KWSCache.
 */
public class KWSCache extends AbstractKeyWordStep
{
    public KWSCache()
    {
        kwName = "Intelligent Caching";
        kwDescription = "Allows the script enable or disable the intelligent caching subsystem";
        kwHelp = "https://www.xframium.org/keyword.html#kw-cache";
        orMapping = false;
        category = "Utility";
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
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
	{
		if ( log.isDebugEnabled() )
			log.debug( "Executing Device Action " + getName() + " using " + getParameterList() );
		
		if ( webDriver instanceof CachingDriver )
		{
		    KeyWordParameter enabled = getParameter( "Enabled" );
		    
		    boolean toState = !( (CachingDriver) webDriver ).isCachingEnabled();
		    
		    if ( enabled != null )
		        toState = Boolean.parseBoolean( getParameterValue( enabled, contextMap, dataMap, executionContext.getxFID() ) );
		    
		    if ( log.isInfoEnabled() )
		    {
    		    if ( toState )
        		    log.info( Thread.currentThread().getName() + ": Enabling Caching" );
    		    else
    		        log.info( Thread.currentThread().getName() + ": Disabling Caching" );
		    }
		    
		    ((CachingDriver) webDriver).setCachingEnabled( toState );
		    
		}
		
		return true;
	}

}
