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
package org.xframium.device.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Platform;
import org.xframium.device.DeviceManager;
import org.xframium.device.SimpleDevice;
import org.xframium.device.data.xsd.Capabilities;
import org.xframium.device.data.xsd.DeviceCapability;
import org.xframium.device.data.xsd.ObjectDeviceCapability;
import org.xframium.device.data.xsd.ObjectFactory;
import org.xframium.device.data.xsd.Options;
import org.xframium.device.data.xsd.RegistryRoot;
import org.xframium.spi.Device;

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
	public List<Device> readData()
	{
		if ( fileName == null )
		{
			if ( log.isInfoEnabled() )
				log.info( "Reading from CLASSPATH as " + resourceName );
			return readElements( getClass().getClassLoader().getResourceAsStream( resourceName ) );
		}
		else
		{
			try
			{
				if ( log.isInfoEnabled() )
					log.info( "Reading from FILE SYSTEM as [" + fileName + "]" );
				return readElements( new FileInputStream( fileName ) );
			}
			catch( FileNotFoundException e )
			{
				log.fatal( "Could not read from " + fileName, e );
				return null;
			}
		}
	}
	
	/**
	 * Read elements.
	 *
	 * @param inputStream the input stream
	 */
	private List<Device> readElements( InputStream inputStream )
	{
	    List<Device> deviceList = new ArrayList<Device>( 10 );
		try
		{
		
		    JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class );
            Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<?> rootElement = (JAXBElement<?>)u.unmarshal( inputStream );
            
            RegistryRoot rRoot = (RegistryRoot)rootElement.getValue();
		
			for ( org.xframium.device.data.xsd.Device device : rRoot.getDevice() )
				deviceList.add( parseDevice( device ) );
			
			return deviceList;
			
		}
		catch( Exception e )
		{
			log.fatal( "Error reading XML Element File", e );
			return null;
		}
	}
	
	/**
	 * Parses the device.
	 *
	 * @param deviceNode the device node
	 */
	@SuppressWarnings("unchecked")
	private Device parseDevice( org.xframium.device.data.xsd.Device device )
	{
		String driverName = "";
		List<Object> list = null;
		String factoryName = null;
		Map<String, Object> keyOptions = null;
		Map<String, Object> browserOptionMap = null;
		DriverType tempDriverType = driverType;
		
		if ( device.getDriverType() != null && !device.getDriverType().isEmpty() )
			tempDriverType = DriverType.valueOf(device.getDriverType());

		switch( tempDriverType )
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
		
		SimpleDevice currentDevice = new SimpleDevice(device.getName(), device.getManufacturer(), device.getModel(), device.getOs(), device.getOsVersion(), device.getBrowserName(), device.getBrowserVersion(), device.getAvailableDevices().intValue(), driverName, device.isActive(), device.getId() );
		
		
		if ( device.getCloud() != null && !device.getCloud().isEmpty() )
		    currentDevice.setCloud( device.getCloud() );
		if ( device.getCapability() != null )
		{
		    for ( DeviceCapability cap : device.getCapability() )
		    {
		        switch( cap.getClazz() )
		        {
		            case "BOOLEAN":
		                currentDevice.addCapability( cap.getName(), Boolean.parseBoolean( cap.getValue() ), cap.getClazz() );
		                break;
                        
		            case "STRING":
		                currentDevice.addCapability( cap.getName(), cap.getValue(), cap.getClazz() );
                        break;
                        
		            case "PLATFORM":
		                currentDevice.addCapability( cap.getName(), Platform.valueOf( cap.getValue().toUpperCase() ), cap.getClazz() );
                        break;
		        }
		    }
		}
		
		//Parse the Object Capability element for browser options
		if (device.getObjectCapability() != null) {
			
			for (ObjectDeviceCapability cap : device.getObjectCapability()) {
				
				browserOptionMap = new HashMap<String, Object>();

				if (cap.getCapabilities() != null) {
					
					for (Capabilities capabilities : cap.getCapabilities()) {
						
						factoryName = capabilities.getFactoryName();

						if (capabilities.getOptions() != null) {
							
							for (Options option : capabilities.getOptions()) {
							
								if (option.getKey() == null) {
								
									if (browserOptionMap.get(option.getName()) == null) {
										list = new ArrayList<Object>();
									
									} else {
										list = (List<Object>) browserOptionMap.get(option.getName());
									}
									browserOptionMap.put(option.getName(), list);
									list.add(option.getValue());
								    
								} else {
									
									if (browserOptionMap.get(option.getName()) == null) {
										keyOptions = new HashMap<String, Object>();
									
									} else {
										keyOptions = (HashMap<String, Object>) browserOptionMap.get(option.getName());
									}
									keyOptions.put(option.getKey(), option.getValue());
									browserOptionMap.put(option.getName(), keyOptions);
								}
								currentDevice.addCapability(factoryName, browserOptionMap, "OBJECT");
							}
						}
					}
				}
			}
		}
		
		return currentDevice;
	}
}
