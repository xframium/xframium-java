/*
 * 
 */
package com.xframium.device.factory;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.xframium.device.data.DataManager;
import com.xframium.device.interrupt.DeviceInterrupt;
import com.xframium.device.interrupt.DeviceInterruptFactory;
import com.xframium.device.interrupt.DeviceInterrupt.INTERRUPT_TYPE;
import com.xframium.spi.Device;

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
	 * @param currentDevice the current device
	 * @return the device web driver
	 */
	protected abstract DeviceWebDriver _createDriver( Device currentDevice );
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.factory.DriverFactory#createDriver(com.perfectoMobile.device.Device)
	 */
	public DeviceWebDriver createDriver( Device currentDevice )
	{
		if ( log.isDebugEnabled() )
			log.debug( "Creating Driver for " + getClass().getSimpleName() );
		
		DeviceWebDriver webDriver = _createDriver( currentDevice ); 
		
		return webDriver;
	}
	
	/**
	 * Capabilities to string.
	 *
	 * @param caps the caps
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
	 * @param currentDevice the current device
	 * @param caps the caps
	 */
	protected void addDeviceCapabilities( Device currentDevice, DesiredCapabilities caps )
	{
		for ( String name : currentDevice.getCapabilities().keySet() )
			caps.setCapability( name, currentDevice.getCapabilities().get( name ) );
		
		
	}

}
