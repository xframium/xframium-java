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
package org.xframium.gesture.device.action;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.xframium.gesture.Gesture;
import org.xframium.spi.PropertyProvider;
import org.xframium.spi.driver.NativeDriverProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractDefaultAction.
 */
public abstract class AbstractDefaultAction implements DeviceAction
{
	
	/** The Constant EXECUTION_ID. */
	private static final String EXECUTION_ID = "EXECUTION_ID";
	
	/** The Constant DEVICE_NAME. */
	private static final String DEVICE_NAME = "DEVICE_NAME";
	/** The log. */
	protected Log log = LogFactory.getLog( Gesture.class );
	
	/**
	 * _execute action.
	 *
	 * @param webDriver the web driver
	 * @param parameterList the parameter list
	 * @return true, if successful
	 */
	public abstract boolean _executeAction( WebDriver webDriver, List<Object> parameterList );
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.device.action.DeviceAction#executeAction(org.openqa.selenium.WebDriver)
	 */
	@Override
	public boolean executeAction( WebDriver webDriver )
	{
		
		return executeAction( webDriver, null );
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.device.action.DeviceAction#executeAction(org.openqa.selenium.WebDriver, java.util.List)
	 */
	@Override
	public boolean executeAction( WebDriver webDriver, List<Object> parameterList )
	{
		return _executeAction( webDriver, parameterList );
	}
	
	/**
	 * Gets the execution id.
	 *
	 * @param webDriver the web driver
	 * @return the execution id
	 */
	protected String getExecutionId( WebDriver webDriver )
	{
		String executionId = null;
		
		if ( webDriver instanceof PropertyProvider )
		{
			executionId = ( (PropertyProvider) webDriver ).getProperty( EXECUTION_ID );
		}
		
		if ( executionId == null )
		{
			if ( webDriver instanceof HasCapabilities )
			{
				Capabilities caps = ( (HasCapabilities) webDriver ).getCapabilities();
				executionId = caps.getCapability( "executionId" ).toString();
			}
		}
		
		if ( executionId == null )
		{
			if ( webDriver instanceof NativeDriverProvider )
			{
				WebDriver nativeDriver = ( (NativeDriverProvider) webDriver ).getNativeDriver();
				if ( nativeDriver instanceof HasCapabilities )
				{
					Capabilities caps = ( (HasCapabilities) webDriver ).getCapabilities();
					executionId = caps.getCapability( "executionId" ).toString();
				}
			}
		}
		
		if ( executionId == null )
			log.warn( "No Execution ID could be located" );
		
		return executionId;
	}
	
	/**
	 * Gets the device name.
	 *
	 * @param webDriver the web driver
	 * @return the device name
	 */
	protected String getDeviceName( WebDriver webDriver )
	{
		String executionId = null;
		
		if ( webDriver instanceof PropertyProvider )
		{
			executionId = ( (PropertyProvider) webDriver ).getProperty( DEVICE_NAME );
		}
		
		if ( executionId == null )
		{
			if ( webDriver instanceof HasCapabilities )
			{
				Capabilities caps = ( (HasCapabilities) webDriver ).getCapabilities();
				executionId = caps.getCapability( "deviceName" ).toString();
			}
		}
		
		if ( executionId == null )
		{
			if ( webDriver instanceof NativeDriverProvider )
			{
				WebDriver nativeDriver = ( (NativeDriverProvider) webDriver ).getNativeDriver();
				if ( nativeDriver instanceof HasCapabilities )
				{
					Capabilities caps = ( (HasCapabilities) webDriver ).getCapabilities();
					executionId = caps.getCapability( "deviceName" ).toString();
				}
			}
		}
		
		if ( executionId == null )
			log.warn( "No Execution ID could be located" );
		
		return executionId;
	}

}
