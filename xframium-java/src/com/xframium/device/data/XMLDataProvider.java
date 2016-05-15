/*
 * 
 */
package com.xframium.device.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.xframium.device.DeviceManager;
import com.xframium.device.SimpleDevice;
import com.xframium.device.data.xsd.DeviceCapability;
import com.xframium.device.data.xsd.ObjectFactory;
import com.xframium.device.data.xsd.RegistryRoot;
import com.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class CSVDataProvider.
 */
public class XMLDataProvider implements DataProvider
{
	
	/** The log. */
	private Log log = LogFactory.getLog( XMLDataProvider.class );
	
	
	/** The file name. */
	private File fileName;
	
	/** The resource name. */
	private String resourceName;
	
	/** The driver type. */
	private DriverType driverType;
	
	/**
	 * Instantiates a new CSV data provider.
	 *
	 * @param fileName            the file name
	 * @param driverType the driver type
	 */
	public XMLDataProvider( File fileName, DriverType driverType )
	{
		this.fileName = fileName;
		this.driverType = driverType;
	}

	/**
	 * Instantiates a new CSV data provider.
	 *
	 * @param resourceName            the resource name
	 * @param driverType the driver type
	 */
	public XMLDataProvider( String resourceName, DriverType driverType )
	{
		this.resourceName = resourceName;
		this.driverType = driverType;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.data.DataProvider#readData()
	 */
	public void readData()
	{
		if ( fileName == null )
		{
			if ( log.isInfoEnabled() )
				log.info( "Reading from CLASSPATH as " + resourceName );
			readElements( getClass().getClassLoader().getResourceAsStream( resourceName ) );
		}
		else
		{
			try
			{
				if ( log.isInfoEnabled() )
					log.info( "Reading from FILE SYSTEM as [" + fileName + "]" );
				readElements( new FileInputStream( fileName ) );
			}
			catch( FileNotFoundException e )
			{
				log.fatal( "Could not read from " + fileName, e );
			}
		}
	}
	
	/**
	 * Read elements.
	 *
	 * @param inputStream the input stream
	 */
	private void readElements( InputStream inputStream )
	{
		try
		{
		
		    JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class );
            Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<?> rootElement = (JAXBElement<?>)u.unmarshal( inputStream );
            
            RegistryRoot rRoot = (RegistryRoot)rootElement.getValue();
		
			for ( com.xframium.device.data.xsd.Device device : rRoot.getDevice() )
				parseDevice( device );
			
		}
		catch( Exception e )
		{
			log.fatal( "Error reading XML Element File", e );
		}
	}
	
	/**
	 * Parses the device.
	 *
	 * @param deviceNode the device node
	 */
	private void parseDevice( com.xframium.device.data.xsd.Device device )
	{
		String driverName = "";
		switch( driverType )
		{
			case APPIUM:
				if ( device.getOs().toUpperCase().equals( "IOS" ) )
					driverName = "IOS";
				else if ( device.getOs().toUpperCase().equals( "ANDROID" ) )
					driverName = "ANDROID";
				else
					log.warn( "Appium is not supported on the following OS " + device.getOs().toUpperCase() + " - this device will be ignored" );
				break;
				
			case PERFECTO:
				driverName = "PERFECTO";
				break;
				
			case WEB:
				driverName = "WEB";
				break;
		}
		
		Device currentDevice = new SimpleDevice(device.getName(), device.getManufacturer(), device.getModel(), device.getOs(), device.getOsVersion(), device.getBrowserName(), null, device.getAvailableDevices().intValue(), driverName, device.isActive(), device.getId() );
		if ( device.getCapability() != null )
		{
		    for ( DeviceCapability cap : device.getCapability() )
		    {
		        switch( cap.getClazz() )
		        {
		            case "BOOLEAN":
		                currentDevice.addCapability( cap.getName(), Boolean.parseBoolean( cap.getValue() ) );
		                break;
		                
		            case "OBJECT":
		                currentDevice.addCapability( cap.getName(), cap.getValue() );
                        break;
                        
		            case "STRING":
		                currentDevice.addCapability( cap.getName(), cap.getValue() );
                        break;
                        
		            case "PLATFORM":
		                currentDevice.addCapability( cap.getName(), Platform.valueOf( cap.getValue().toUpperCase() ) );
                        break;
		        }
		    }
		}

		if ( currentDevice.isActive() )
		{				
			if (log.isDebugEnabled())
				log.debug( "Extracted: " + currentDevice );

			DeviceManager.instance().registerDevice( currentDevice );
		}
		
	}
}
