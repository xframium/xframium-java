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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.device.DeviceManager;
import org.xframium.device.SimpleDevice;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class CSVDataProvider.
 */
public class CSVDataProvider implements DataProvider
{
	
	/** The log. */
	private Log log = LogFactory.getLog( CSVDataProvider.class );
	
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
	public CSVDataProvider( File fileName, DriverType driverType )
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
	public CSVDataProvider( String resourceName, DriverType driverType )
	{
		this.resourceName = resourceName;
		this.driverType = driverType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.perfectoMobile.device.data.DataProvider#readData()
	 */
	public void readData()
	{
		if (fileName == null)
		{
			if (log.isInfoEnabled())
				log.info( "Reading Device Data from Resource " + resourceName );

			readData( getClass().getClassLoader().getResourceAsStream( resourceName ) );
		}
		else
		{
			try
			{
				readData( new FileInputStream( fileName ) );
			}
			catch (Exception e)
			{
				log.fatal( "Could mot read from " + fileName, e );
			}
		}
	}

	/**
	 * Read data.
	 *
	 * @param inputStream the input stream
	 */
	private void readData( InputStream inputStream )
	{
		BufferedReader fileReader = null;

		try
		{
			fileReader = new BufferedReader( new InputStreamReader( inputStream ) );

			String currentLine = null;
			while (( currentLine = fileReader.readLine() ) != null)
			{
				if (log.isDebugEnabled())
					log.debug( "Read [" + currentLine + "]" );

				String[] lineData = currentLine.split( "," );
				
				
				
				String driverName = "";
				switch( driverType )
				{
					case APPIUM:
						if ( lineData[3].toUpperCase().equals( "IOS" ) )
							driverName = "IOS";
						else if ( lineData[3].toUpperCase().equals( "ANDROID" ) )
							driverName = "ANDROID";
						else
							throw new IllegalArgumentException( "Appium is not supported on the following OS " + lineData[3].toUpperCase() );
						break;
						
					case PERFECTO:
						driverName = "PERFECTO";
						break;
						
					case WEB:
						driverName = "WEB";
						break;
				}
				
				Device currentDevice = new SimpleDevice( lineData[0], lineData[1], lineData[2], lineData[3], lineData[4], lineData[5], lineData[6], Integer.parseInt( lineData[7] ), driverName, Boolean.parseBoolean( lineData[8] ), null );

				if ( currentDevice.isActive() )
				{				
					if (log.isDebugEnabled())
						log.debug( "Extracted: " + currentDevice );
	
					DeviceManager.instance().registerDevice( currentDevice );
				}
				else
				{				
					if (log.isDebugEnabled())
						log.debug( "Extracted inactive device: " + currentDevice );

					DeviceManager.instance().registerInactiveDevice( currentDevice );
				}
			}

		}
		catch (Exception e)
		{
			log.fatal( "Error reading device data", e );
		}
		finally
		{
			try
			{
				fileReader.close();
			}
			catch (Exception e)
			{
			}
		}
	}
}
