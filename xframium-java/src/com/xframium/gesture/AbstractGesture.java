package com.xframium.gesture;

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
import com.xframium.integrations.common.PercentagePoint;
import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import com.xframium.spi.PropertyProvider;
import com.xframium.spi.driver.NativeDriverProvider;

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
