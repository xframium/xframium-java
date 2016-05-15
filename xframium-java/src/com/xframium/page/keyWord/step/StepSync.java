package com.xframium.page.keyWord.step;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import com.xframium.page.Page;
import com.xframium.page.PageManager;
import com.xframium.page.data.PageData;
import com.xframium.page.keyWord.KeyWordDriver;
import com.xframium.page.keyWord.KeyWordStep;

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
