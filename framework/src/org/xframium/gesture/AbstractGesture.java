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
package org.xframium.gesture;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.xframium.spi.PropertyProvider;
import org.xframium.spi.driver.NativeDriverProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractGesture.
 */
public abstract class AbstractGesture implements Gesture
{
	
	/** The Constant EXECUTION_ID. */
	private static final String EXECUTION_ID = "EXECUTION_ID";
	
	/** The Constant DEVICE_NAME. */
	private static final String DEVICE_NAME = "DEVICE_NAME";
	/** The log. */
	protected Log log = LogFactory.getLog( Gesture.class );
	
	/** The web element. */
	protected WebElement webElement;
	
	/**
	 * _execute gesture.
	 *
	 * @param webDriver the web driver
	 * @return true, if successful
	 */
	protected abstract boolean _executeGesture( WebDriver webDriver );

	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.Gesture#executeGesture(org.openqa.selenium.WebDriver)
	 */
	public boolean executeGesture( WebDriver webDriver )
	{
		return _executeGesture( webDriver );
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.Gesture#executeGesture(org.openqa.selenium.WebDriver, org.openqa.selenium.WebElement)
	 */
	@Override
	public boolean executeGesture(WebDriver webDriver, WebElement webElement) 
	{
		this.webElement = webElement;
		return _executeGesture( webDriver );
	}
	
	/**
	 * Gets the actual point.
	 *
	 * @param percentagePoint the percentage point
	 * @param screenDimension the screen dimension
	 * @return the actual point
	 */
	protected Point getActualPoint( Point percentagePoint, Dimension screenDimension )
	{
		if ( webElement != null )
		{
			if ( webElement.getLocation() != null && webElement.getSize() != null && webElement.getSize().getWidth() > 0 && webElement.getSize().getHeight() > 0 )
			{
				int x = percentagePoint.getX() * webElement.getSize().getWidth() + webElement.getLocation().getX();
				int y = percentagePoint.getY() * webElement.getSize().getHeight() + webElement.getLocation().getY();
				return new Point( x, y );
			}
		}
		
		return new Point( (int) ( (percentagePoint.getX() / 100.0 ) * (double)screenDimension.getWidth() ), (int) ( (percentagePoint.getY() / 100.0 ) * (double)screenDimension.getHeight() ) );
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
	
	/**
	 * Gets the url.
	 *
	 * @param currentUrl the current url
	 * @return the url
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected String getUrl( URL currentUrl ) throws IOException
	{
		
		if ( log.isInfoEnabled() )
			log.info( "Executing GET on " + currentUrl.toString() );
		
		InputStream inputStream = null;
		try
		{
			inputStream = currentUrl.openStream();
			
			StringBuilder returnValue = new StringBuilder();
			
			int bytesread = 0;
			byte[] buffer = new byte[ 512];
			
			while ( ( bytesread = inputStream.read( buffer ) ) > 0 )
				returnValue.append( new String( buffer, 0, bytesread ) );
			
			if ( log.isDebugEnabled() )
				log.debug( "Returned: " + returnValue.toString() );

			return returnValue.toString();
		}
		finally
		{
			try { inputStream.close(); } catch( Exception e) {}
		}
	}

}
