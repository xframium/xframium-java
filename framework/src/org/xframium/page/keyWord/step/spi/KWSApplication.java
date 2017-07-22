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
import org.openqa.selenium.WebDriver;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationRegistry;
import org.xframium.container.SuiteContainer;
import org.xframium.device.cloud.action.CloudActionProvider;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContext;
import org.xframium.reporting.ExecutionContextTest;



// TODO: Auto-generated Javadoc
/**
 * The Class KWSDevice.
 */
public class KWSApplication extends AbstractKeyWordStep
{
    public KWSApplication()
    {
        kwName = "Application";
        kwDescription = "Allows the script install, uninstall, launch and close native applications";
        kwHelp = "https://www.xframium.org/keyword.html#kw-device";
        orMapping = false;
        category = "Utility";
    }
    
    public enum ApplicationAction
    {
        INSTALL( 1, "INSTALL", "Install an application" ),
        UNINSTALL( 2, "UNINSTALL", "Uninstall an application" ),
        OPEN( 3, "OPEN", "Launch an application" ),
        CLOSE( 4, "CLOSE", "Close an application" );

        public List<ApplicationAction> getSupported()
        {
            List<ApplicationAction> supportedList = new ArrayList<ApplicationAction>( 10 );
            supportedList.add( ApplicationAction.INSTALL );
            supportedList.add( ApplicationAction.UNINSTALL );
            supportedList.add( ApplicationAction.OPEN );
            supportedList.add( ApplicationAction.CLOSE );
            return supportedList;
        }

        private ApplicationAction( int id, String name, String description )
        {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        private int id;
        private String name;
        private String description;
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
		CloudActionProvider cP = ( (DeviceWebDriver) webDriver ).getCloud().getCloudActionProvider();
	    switch ( ApplicationAction.valueOf( getName().toUpperCase() ) )
        {
	        case CLOSE:
	            ( (DeviceWebDriver) webDriver ).setAut( null, sC.getxFID() );
	            return cP.closeApplication( getParameterValue( getParameter( "Application Name" ), contextMap, dataMap, executionContext.getxFID() ) + "", (DeviceWebDriver)webDriver );
	            
	        case INSTALL:
	            return cP.installApplication( getParameterValue( getParameter( "Application Name" ), contextMap, dataMap, executionContext.getxFID() ) + "", (DeviceWebDriver)webDriver, Boolean.parseBoolean( getParameterValue( getParameter( "Instrument" ), contextMap, dataMap, executionContext.getxFID() ) + "" ) );
	            
	        case OPEN:
	            if ( isTimed() )
                    cP.startTimer( (DeviceWebDriver) webDriver, null, executionContext );
	            if ( cP.openApplication( getParameterValue( getParameter( "Application Name" ), contextMap, dataMap, executionContext.getxFID() ) + "", (DeviceWebDriver)webDriver, ((DeviceWebDriver)webDriver ).getxFID() ) )
	            {
	                ApplicationRegistry aR = ApplicationRegistry.instance( ( (DeviceWebDriver) webDriver ).getxFID() );
	                ApplicationDescriptor aD = aR.getApplication( getParameterValue( getParameter( "Application Name" ), contextMap, dataMap, executionContext.getxFID() ) + "" );
	                ( (DeviceWebDriver) webDriver ).setAut( aD, executionContext.getxFID() );
	                return true;
	            }
	            else 
	                return false;
	             
	            
	        case UNINSTALL:
	            return cP.uninstallApplication( getParameterValue( getParameter( "Application Name" ), contextMap, dataMap, executionContext.getxFID() ) + "", (DeviceWebDriver)webDriver );
        }
		
		return false;
	}

}
