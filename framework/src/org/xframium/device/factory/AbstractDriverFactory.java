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
package org.xframium.device.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xframium.browser.capabilities.BrowserCapabilityManager;
import org.xframium.device.SimpleDevice;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.cloud.action.CloudActionProvider;
import org.xframium.device.cloud.xsd.Cloud;
import org.xframium.device.interrupt.DeviceInterrupt;
import org.xframium.device.interrupt.DeviceInterrupt.INTERRUPT_TYPE;
import org.xframium.device.interrupt.DeviceInterruptFactory;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating AbstractDriver objects.
 */
public abstract class AbstractDriverFactory implements DriverFactory
{

    /** The log. */
    protected Log log = LogFactory.getLog( DriverFactory.class );

    /**
     * _create driver.
     *
     * @param currentDevice
     *            the current device
     * @return the device web driver
     */
    protected abstract DeviceWebDriver _createDriver( Device currentDevice );

    public CloudActionProvider getCloudActionProvider( CloudDescriptor currentCloud )
    {
        try
        {
            CloudActionProvider actionProvider = (CloudActionProvider) Class.forName( CloudActionProvider.class.getPackage().getName() + "." + currentCloud.getProvider() + "CloudActionProvider" ).newInstance();
            return actionProvider;
        }
        catch ( Exception e )
        {
            log.error( "Could not load cloud provider for " + currentCloud.getProvider() );
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.device.factory.DriverFactory#createDriver(com.
     * perfectoMobile.device.Device)
     */
    public DeviceWebDriver createDriver( Device currentDevice )
    {
        if ( log.isDebugEnabled() )
            log.debug( "Creating Driver for " + getClass().getSimpleName() );

        DeviceWebDriver webDriver = _createDriver( currentDevice );

        if ( webDriver != null )
        {
            try
            {

                Device newDevice = new SimpleDevice( currentDevice.getKey(), currentDevice.getDriverType() );
                newDevice.setBrowserName( currentDevice.getBrowserName() );
                newDevice.setBrowserVersion( currentDevice.getBrowserVersion() );
                CloudActionProvider actionProvider = (CloudActionProvider) Class.forName( CloudActionProvider.class.getPackage().getName() + "." + webDriver.getCloud().getProvider() + "CloudActionProvider" ).newInstance();
                if ( actionProvider.popuplateDevice( webDriver, webDriver.getDeviceName(), newDevice ) )
                    webDriver.setPopulatedDevice( newDevice );
            }
            catch ( Exception e )
            {
                log.error( "Error populating device specifics", e );
            }
        }

        return webDriver;
    }

    /**
     * Capabilities to string.
     *
     * @param caps
     *            the caps
     * @return the string
     */
    protected String capabilitiesToString( Capabilities caps )
    {
        StringBuilder capData = new StringBuilder();

        for ( String keyName : caps.asMap().keySet() )
        {
            capData.append( keyName ).append( "=" ).append( caps.getCapability( keyName ) + "\r\n" );
        }

        return capData.toString();
    }

    protected List<DeviceInterrupt> getDeviceInterrupts( String interruptString, String executionId, String deviceName )
    {
        if ( interruptString != null && !interruptString.isEmpty() )
        {
            String[] interrupts = interruptString.split( "," );
            List<DeviceInterrupt> interruptList = new ArrayList<DeviceInterrupt>( interrupts.length );

            for ( String interruptName : interrupts )
            {
                interruptList.add( DeviceInterruptFactory.instance().getDeviceInterrupt( INTERRUPT_TYPE.valueOf( interruptName.trim().toUpperCase() ), executionId, deviceName ) );
            }

            return interruptList;
        }

        return null;
    }

    /**
     * Adds the device capabilities.
     *
     * @param currentDevice
     *            the current device
     * @param caps
     *            the caps
     */
    protected void addDeviceCapabilities( Device currentDevice, DesiredCapabilities caps )
    {
        for ( String name : currentDevice.getCapabilities().keySet() )
            caps.setCapability( name, currentDevice.getCapabilities().get( name ) );

    }
    
    /**
	 * This method sets the browser capability to the Desired Capabilities
	 * @param Object - current device Object value
	 * @param DesiredCapabilities
	 * @param String - name of the option
	 * @return DesiredCapabilities
	 */
	protected DesiredCapabilities setCapabilities(Object value, DesiredCapabilities dc, String name) {
		if (value instanceof Boolean)
		{
			dc.setCapability( name, value );
		}
		
		else if (value instanceof String) 
		{
			dc.setCapability( name, value );
		}
		
		else if (value instanceof Platform) 
		{
			dc.setCapability( name, value );
		}
		
		else if (value instanceof Map) 
		{
			dc = BrowserCapabilityManager.instance().getBrowsercapabilityFactory(name)
					.createBrowserOptions(dc, (Map<String,List<String>>)value);
		}
		return dc;
	}

}
