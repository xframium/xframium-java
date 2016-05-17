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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.openqa.selenium.WebDriver;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.page.keyWord.step.StepSync;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSFork.
 */
public class KWSSync extends AbstractKeyWordStep
{
    
    /** The thread pool. */
    private static ExecutorService threadPool = Executors.newCachedThreadPool();
	/**
	 * Instantiates a new KWS fork.
	 */
	public KWSSync()
	{
		setFork( true );
	}
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap ) throws Exception
	{
			
	    if ( getStepList().isEmpty() )
	        throw new IllegalArgumentException( "There were no steps to execute" );
	    
		StepSync stepSync = new StepSync( webDriver, contextMap, dataMap, pageMap, getStepList().toArray( new KeyWordStep[ 0 ] ) );
		
		while ( !stepSync.stepsComplete() )
		{
		    try
		    {
		        Thread.sleep( 500 );
		    }
		    catch( Exception e )
		    {
		        
		    }
		}
		
		return stepSync.stepsSuccessful();
	    
	}

}
