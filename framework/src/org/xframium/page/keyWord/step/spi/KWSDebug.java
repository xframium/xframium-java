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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.utility.ContextWalker;
import org.xframium.utility.FrameWalker;
import org.xframium.utility.WindowWalker;

public class KWSDebug extends AbstractKeyWordStep
{
    private static FrameWalker fW = new FrameWalker();
    private static ContextWalker cW = new ContextWalker();
    private static WindowWalker wW = new WindowWalker();
    
    public KWSDebug()
    {
        kwName = "Debug";
        kwDescription = "Utility commands for debugging scripts";
        kwHelp = "https://www.xframium.org/keyword.html#kw-debug";
        orMapping = false;
        category = "Utility";
    }
    
    public enum DEBUG_TYPE
    {
        WALK_WINDOWS( 1, "WALK_WINDOWS", "Walk Windows"),
        WALK_FRAMES( 2, "WALK_FRAMES", "Walk Frames"),
        WALK_CONTEXT( 3, "WALK_CONTEXT", "Walk Context");
        


		private DEBUG_TYPE( int id, String name, String description )
        {
            this.id = id;
            this.name= name;
            this.description = description;
        }
        
        private int id;
        private String name;
        private String description;
        
        public List<DEBUG_TYPE> getSupported()
        {
            List<DEBUG_TYPE> alertList = new ArrayList<DEBUG_TYPE>( 10 );
            alertList.add( WALK_WINDOWS );
            alertList.add( WALK_FRAMES );
            alertList.add( WALK_CONTEXT );
            return alertList;
        }
        
    }
    
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
	{
	    StringBuilder reportData = new StringBuilder();
        
        reportData.append( "**********************************************************************************************************************\r\n" );
        reportData.append( "*   " ).append(  getName() ).append( "\r\n" );

        String dataValue = null;
		switch( DEBUG_TYPE.valueOf( getName() ) )
		{
		    case WALK_CONTEXT:
		        dataValue = cW.walkContext( (DeviceWebDriver ) webDriver ).toString();
		        break;
		        
		    case WALK_FRAMES:
		        dataValue = fW.walkFrames( (DeviceWebDriver ) webDriver ).toString();
		        break;
		        
		    case WALK_WINDOWS:
		        dataValue = wW.walkWindows( (DeviceWebDriver ) webDriver ).toString();
		        break;
		            
		}

		executionContext.getStep().addExecutionParameter( "REPORT_ENTRY", dataValue );
		reportData.append( "*       " ).append( dataValue ).append( "\r\n" );
		
		reportData.append( "**********************************************************************************************************************\r\n" );
		log.warn( reportData.toString() );
		return true;
	}
	
	
	
	

}
