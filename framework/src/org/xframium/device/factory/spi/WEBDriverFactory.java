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

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.xframium.application.ApplicationRegistry;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.factory.AbstractDriverFactory;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.spi.Device;
import com.xframium.serialization.SerializationManager;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating WEBDriver objects.
 */
public class WEBDriverFactory extends AbstractDriverFactory
{

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.device.factory.AbstractDriverFactory#_createDriver(com
     * .perfectoMobile.device.Device)
     */
    @Override
    protected DeviceWebDriver _createDriver( Device currentDevice )
    {
        DeviceWebDriver webDriver = null;
        try
        {
            DesiredCapabilities dc = null;
            
            CloudDescriptor useCloud = CloudRegistry.instance().getCloud();
            
            if ( currentDevice.getBrowserName() != null && !currentDevice.getBrowserName().isEmpty() )
                dc = new DesiredCapabilities(  useCloud.getCloudActionProvider().getCloudBrowserName(currentDevice.getBrowserName()), "", Platform.ANY );
            else
                dc = new DesiredCapabilities( "", "", Platform.ANY );

            

            if ( currentDevice.getCloud() != null )
            {
                useCloud = CloudRegistry.instance().getCloud( currentDevice.getCloud() );
                if ( useCloud == null )
                {
                    useCloud = CloudRegistry.instance().getCloud();
                    log.warn( "A separate grid instance was specified but it does not exist in your cloud registry [" + currentDevice.getCloud() + "] - using the default Cloud instance" );
                }
            }
            
            DeviceManager.instance().setCurrentCloud( useCloud );
            
            URL hubUrl = new URL( useCloud.getCloudUrl() );

            if ( currentDevice.getDeviceName() != null && !currentDevice.getDeviceName().isEmpty() )
            {
                dc.setCapability( ID, currentDevice.getDeviceName() );
                dc.setCapability( USER_NAME, useCloud.getUserName() );
                dc.setCapability( PASSWORD, useCloud.getPassword() );
            }
            else
            {
                dc.setCapability( useCloud.getCloudActionProvider().getCloudPlatformName(currentDevice), currentDevice.getOs() );
            	dc.setCapability( PLATFORM_VERSION, currentDevice.getOsVersion() );
                
                dc.setCapability( MODEL, currentDevice.getModel() );
                dc.setCapability( USER_NAME, useCloud.getUserName() );
                dc.setCapability( PASSWORD, useCloud.getPassword() );
            }

            if ( currentDevice.getBrowserName() != null && !currentDevice.getBrowserName().isEmpty() )
                dc.setCapability( BROWSER_NAME,  useCloud.getCloudActionProvider().getCloudBrowserName(currentDevice.getBrowserName()) );
            if ( currentDevice.getBrowserVersion() != null && !currentDevice.getBrowserVersion().isEmpty() )
                dc.setCapability( BROWSER_VERSION, currentDevice.getBrowserVersion() );
            	
            for ( String name : currentDevice.getCapabilities().keySet() )
				dc = setCapabilities(currentDevice.getCapabilities().get(name), dc, name);
			
            for ( String name : ApplicationRegistry.instance().getAUT().getCapabilities().keySet() )
            	dc = setCapabilities(ApplicationRegistry.instance().getAUT().getCapabilities().get( name ), dc, name);

            if ( log.isInfoEnabled() )
                log.info( Thread.currentThread().getName() + ": Acquiring Device as: \r\n" + capabilitiesToString( dc ) + "\r\nagainst " + hubUrl );

            webDriver = new DeviceWebDriver( new RemoteWebDriver( hubUrl, dc ), DeviceManager.instance().isCachingEnabled(), currentDevice );
            webDriver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );

            Capabilities caps = ((RemoteWebDriver) webDriver.getWebDriver()).getCapabilities();
           
            webDriver.setExecutionId( useCloud.getCloudActionProvider().getExecutionId( webDriver ) );
            webDriver.setReportKey( caps.getCapability( "reportKey" ) + "" );
            webDriver.setDeviceName( caps.getCapability( "deviceName" ) + "" );
            if ( useCloud.getProvider().equals( "PERFECTO" ) )
                webDriver.setWindTunnelReport( caps.getCapability( "windTunnelReportUrl" ).toString() );
            
            webDriver.setArtifactProducer( getCloudActionProvider( useCloud ).getArtifactProducer() );
            webDriver.setCloud( useCloud );

            if ( ApplicationRegistry.instance().getAUT().getUrl() != null && !ApplicationRegistry.instance().getAUT().getUrl().isEmpty() )
            {
                webDriver.get( ApplicationRegistry.instance().getAUT().getUrl() );
            }

            return webDriver;
        }
        catch ( Exception e )
        {
            log.fatal( "Could not connect to Cloud instance for " + currentDevice, e );
            if ( webDriver != null )
            {
                try
                {
                    webDriver.close();
                }
                catch ( Exception e2 )
                {
                }
                try
                {
                    webDriver.quit();
                }
                catch ( Exception e2 )
                {
                }
            }
            return null;
        }

    }
}
