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
