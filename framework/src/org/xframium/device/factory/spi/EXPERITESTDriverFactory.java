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
import org.xframium.application.ApplicationRegistry;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.factory.AbstractDriverFactory;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.spi.Device;
import com.experitest.selenium.MobileWebDriver;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating IOSDriver objects.
 */
public class EXPERITESTDriverFactory extends AbstractDriverFactory
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.factory.AbstractDriverFactory#_createDriver(com.perfectoMobile.device.Device)
	 */
	@Override
	protected DeviceWebDriver _createDriver( Device currentDevice, CloudDescriptor useCloud )
	{
		DeviceWebDriver webDriver = null;
		try
		{
			
            DeviceManager.instance().setCurrentCloud( useCloud );
			
			URL hubUrl = new URL( useCloud.getCloudUrl() );

            if ( log.isDebugEnabled() )
                log.debug( Thread.currentThread().getName() + ": Acquiring Device as: \r\n" + currentDevice.getDeviceName() + "\r\nagainst " + hubUrl );
			MobileWebDriver mDriver = new MobileWebDriver( useCloud.getHostName().split( ":" )[ 0 ], Integer.parseInt( useCloud.getHostName().split( ":" )[ 1 ] ) );
			mDriver.setDevice( currentDevice.getDeviceName() );
			
			if( ApplicationRegistry.instance().getAUT() != null && 
			    ( ( ApplicationRegistry.instance().getAUT().getAppleIdentifier() != null && !ApplicationRegistry.instance().getAUT().getAppleIdentifier().isEmpty() || 
			    ApplicationRegistry.instance().getAUT().getAndroidIdentifier() != null && !ApplicationRegistry.instance().getAUT().getAndroidIdentifier().isEmpty() ) ) )
            {
			    if ( currentDevice != null && currentDevice.getOs() != null )
	            {
			        if ( currentDevice.getOs().toLowerCase().equals( "ios" ) )
			            mDriver.application( ApplicationRegistry.instance().getAUT().getAppleIdentifier() ).launch( true, false );
			        else if ( currentDevice.getOs().toLowerCase().equals( "android" ) )
			            mDriver.application( ApplicationRegistry.instance().getAUT().getAndroidIdentifier() ).launch( true, false );
	            }
            }
			else
			{
			    if ( ApplicationRegistry.instance().getAUT() != null && !ApplicationRegistry.instance().getAUT().getUrl().isEmpty() )
			    {
			        mDriver.get( ApplicationRegistry.instance().getAUT().getUrl() );
			    }
			}
            
			webDriver = new DeviceWebDriver( mDriver, DeviceManager.instance().isCachingEnabled(), currentDevice );
			webDriver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
            webDriver.setArtifactProducer( getCloudActionProvider( useCloud ).getArtifactProducer() );
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
