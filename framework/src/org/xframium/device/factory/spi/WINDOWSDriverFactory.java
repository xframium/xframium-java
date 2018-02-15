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
/*
 * 
 */
package org.xframium.device.factory.spi;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudDescriptor.ProviderType;
import org.xframium.device.factory.AbstractDriverFactory;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.reporting.ExecutionContext;
import org.xframium.spi.Device;
import io.appium.java_client.ios.IOSDriver;

// TODO: Auto-generated Javadoc
/**
 * 
 * A factory for creating IOSDriver objects.
 */
public class WINDOWSDriverFactory extends AbstractDriverFactory
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.factory.AbstractDriverFactory#_createDriver(com.perfectoMobile.device.Device)
	 */
	@Override
	protected DeviceWebDriver _createDriver( Device currentDevice, CloudDescriptor useCloud, String xFID )
	{
		DeviceWebDriver webDriver = null;
		try
		{
			DesiredCapabilities dc = new DesiredCapabilities();

            DeviceManager.instance( xFID ).setCurrentCloud( useCloud );

            if ( ProviderType.BROWSERSTACK.equals( ProviderType.valueOf( useCloud.getProvider() ) ) )
            {
                dc.setCapability( "project", ExecutionContext.instance(xFID).getSuiteName() );
                if ( KeyWordDriver.instance(xFID).getTags() != null )
                    dc.setCapability( "build", KeyWordDriver.instance(xFID).getTags()[ 0 ] );
            }
			
			for ( String name : currentDevice.getCapabilities().keySet() )
				dc = setCapabilities(currentDevice.getCapabilities().get(name), dc, name);
			
			URL hubUrl = new URL( useCloud.getCloudUrl( dc ) );
			
			if ( log.isInfoEnabled() )
			    log.info( "Acquiring Windows Application as: \r\n" + capabilitiesToString( dc ) + "\r\nagainst " + hubUrl );
			
			webDriver = new DeviceWebDriver( new IOSDriver( hubUrl, dc ), DeviceManager.instance(xFID).isCachingEnabled(), currentDevice, dc );
			webDriver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
			
			
			Capabilities caps = ( (IOSDriver) webDriver.getWebDriver() ).getCapabilities();
			
			if ( useCloud.getProvider().equals( "PERFECTO" ) && caps.getCapability( "windTunnelReportUrl" ) != null )
                webDriver.setWindTunnelReport( caps.getCapability( "windTunnelReportUrl" ).toString() );
			
			webDriver.setExecutionId( useCloud.getCloudActionProvider().getExecutionId( webDriver ) );
			webDriver.setDeviceName( useCloud.getCloudActionProvider().getExecutionId( webDriver ) );
            webDriver.setCloud( useCloud );
            
			return webDriver;
		}
		catch( Exception e )
		{
		    log.fatal( "Could not connect to " + currentDevice + " (" + e.getMessage() + ")" );
            log.debug( e );
			if ( webDriver != null )
			{
				try { webDriver.close(); } catch( Exception e2 ) {}
				try { webDriver.quit(); } catch( Exception e2 ) {}
			}
			return null;
		}
	}
}
