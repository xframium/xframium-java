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
package org.xframium.page.keyWord.step;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class ElementFork.
 */
public class StepSync
{
	
	/** The log. */
	private Log log = LogFactory.getLog( StepSync.class );
	
	/** The thread pool. */
    private static ExecutorService threadPool = Executors.newCachedThreadPool();
	
	/** The web driver. */
	private WebDriver webDriver;
	
	/** The context map. */
	private Map<String, Object> contextMap;
	
	/** The data map. */
	private Map<String,PageData> dataMap;
	
	/** The page map. */
	private Map<String,Page> pageMap;
	
	/** The steps successful. */
	private boolean stepsSuccessful = false;
	
	/** The step threads. */
	private List<StepThread> stepThreads = new ArrayList<StepThread>( 10 );
	
	/**
	 * Instantiates a new step sync.
	 *
	 * @param webDriver the web driver
	 * @param contextMap the context map
	 * @param dataMap the data map
	 * @param pageMap the page map
	 * @param stepArray the step array
	 */
	public StepSync( WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, KeyWordStep[] stepArray )
    {
        this.webDriver = webDriver;
        this.contextMap = contextMap;
        this.dataMap = dataMap;
        this.pageMap = pageMap;

        for ( KeyWordStep step : stepArray )
        {
            StepThread s = new StepThread( step );
            stepThreads.add( s );
            threadPool.submit( s );
            try
            {
                Thread.sleep( 250 );
            }
            catch( Exception e )
            {
                
            }
        }
    }

	
	
	/**
	 * Steps complete.
	 *
	 * @return true, if successful
	 */
	public boolean stepsComplete()
	{
	    for ( StepThread step : stepThreads )
	    {
	        if ( step.isRunning() )
	            return false;
	    }
	    
	    return true;
	}
	
	/**
	 * Steps successful.
	 *
	 * @return true, if successful
	 */
	public boolean stepsSuccessful()
    {
        return stepsSuccessful;
    }

    
	
	/**
	 * The Class ElementThread.
	 */
	private class StepThread implements Runnable
	{
		
		/** The step. */
		private KeyWordStep step;
		
		/** The running. */
		private boolean running = true;
		
		/**
		 * Instantiates a new element thread.
		 *
		 * @param step the step
		 */
		public StepThread( KeyWordStep step )
		{
			this.step = step;
		}
		
		
		
		/**
		 * Checks if is running.
		 *
		 * @return true, if is running
		 */
		public boolean isRunning()
        {
            return running;
        }

        /* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run()
		{
			try
			{
			    Page page = null;
	            if ( step.getPageName() != null )
	            {
	                page = pageMap.get( step.getPageName() );
	                if ( page == null )
	                {
	                    page = PageManager.instance().createPage( KeyWordDriver.instance().getPage( step.getPageName() ), webDriver );
	                    pageMap.put( step.getPageName(), page );
	                }
	            }
	            
	            if ( !step.executeStep( page, webDriver, contextMap, dataMap, pageMap ) )
	            {
	                if ( log.isWarnEnabled() )
                        log.warn( "***** Step [" + step.getName() + "] Failed" );
                    stepsSuccessful = false;
	            }
			}
			catch( Exception e )
			{
			    if ( log.isWarnEnabled() )
                    log.warn( "***** Step [" + step.getName() + "] Failed", e );
                stepsSuccessful = false;
			}
			
			running = false;
		}
	}
}
