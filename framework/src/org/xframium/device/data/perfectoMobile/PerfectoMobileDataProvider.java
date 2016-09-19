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
package org.xframium.device.data.perfectoMobile;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xframium.device.DeviceManager;
import org.xframium.device.SimpleDevice;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataProvider;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class PerfectoMobileDataProvider.
 */
public class PerfectoMobileDataProvider implements DataProvider
{
	
	
	
	/** The log. */
	private Log log = LogFactory.getLog( PerfectoMobileDataProvider.class );
	
	/** The pm validator. */
	private PerfectoMobileHandsetValidator pmValidator;
	
	/** The driver type. */
	private DriverType driverType;
	
	/**
	 * Instantiates a new perfecto mobile data provider.
	 *
	 * @param pmValidator the pm validator
	 * @param driverType the driver type
	 */
	public PerfectoMobileDataProvider( PerfectoMobileHandsetValidator pmValidator, DriverType driverType )
	{
		this.pmValidator = pmValidator;
		this.driverType = driverType;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.data.DataProvider#readData()
	 */
	public List<Device> readData()
	{
	    List<Device> deviceList = new ArrayList<Device>( 10 );
		try
		{
			URL deviceURL = new URL( "https://" + CloudRegistry.instance().getCloud().getHostName() + "/services/handsets?operation=list&user=" + CloudRegistry.instance().getCloud().getUserName() + "&password=" + CloudRegistry.instance().getCloud().getPassword() );
			
			if ( log.isInfoEnabled() )
				log.info( "Reading Devices from " + deviceURL.toString() );
			
			URLConnection urlConnection = deviceURL.openConnection();
			urlConnection.setDoInput( true );
			
			InputStream inputStream = urlConnection.getInputStream();
				
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse( inputStream );
			
			NodeList handSets = doc.getElementsByTagName( "handset" );
			
			if ( log.isInfoEnabled() )
				log.info( "Analysing handsets using [" + pmValidator.getClass().getSimpleName() + "]" );
			
			boolean deviceFound = false;
			
			for ( int i=0; i<handSets.getLength(); i++ )
			{
				
				if ( pmValidator.validate( handSets.item(  i  ) ) )
				{
					String driverName = "";
					switch( driverType )
					{
						case APPIUM:
							String osType = getValue( handSets.item(  i  ), "os" );
							if ( osType.equals( "iOS" ) )
								driverName = "IOS";
							else if ( osType.equals( "Android" ) )
								driverName = "ANDROID";
							else
								throw new IllegalArgumentException( "Appium is not supported on the following OS " + osType );
							break;
							
						case PERFECTO:
							driverName = "PERFECTO";
							break;
							
						case WEB:
							driverName = "WEB";
							break;
					}
					
					deviceFound = true;
					deviceList.add( new SimpleDevice( getValue( handSets.item(  i  ), "deviceId" ), getValue( handSets.item(  i  ), "manufacturer" ), getValue( handSets.item(  i  ), "model" ), getValue( handSets.item(  i  ), "os" ), getValue( handSets.item(  i  ), "osVersion" ), null, null, 1, driverName, true, getValue( handSets.item(  i  ), "deviceId" ) ) );
				}
				
			}
			
			if ( !deviceFound )
				log.warn( pmValidator.getMessage() );
			
			inputStream.close();
			return deviceList;
			
			
		}
		catch( Exception e )
		{
			e.printStackTrace( );
			return null;
		}
	}
	
	/**
	 * Gets the value.
	 *
	 * @param handSet the hand set
	 * @param tagName the tag name
	 * @return the value
	 */
	private String getValue( Node handSet, String tagName )
	{
		NodeList params = handSet.getChildNodes();
		
		for ( int i=0; i<params.getLength(); i++ )
		{
			if ( params.item( i ).getNodeName().toLowerCase().equals( tagName.toLowerCase() ) )
			{
				return params.item( i ).getTextContent();
			}
		}
		
		return null;
	}
	

}
