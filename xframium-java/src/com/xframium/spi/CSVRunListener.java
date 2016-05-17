/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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
package com.xframium.spi;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving CSVRun events.
 * The class that is interested in processing a CSVRun
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addCSVRunListener</code> method. When
 * the CSVRun event occurs, that object's appropriate
 * method is invoked.

 */
public class CSVRunListener implements RunListener
{
	
	/** The run map. */
	private Map <String,Long> runMap = new HashMap<String,Long>(20);
	
	/** The output file. */
	private File outputFile = null;
	
	/**
	 * Instantiates a new CSV run listener.
	 *
	 * @param outputFile the output file
	 */
	public CSVRunListener( File outputFile )
	{
		this.outputFile = outputFile;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.listener.RunListener#beforeRun(com.morelandLabs.spi.Device, java.lang.String)
	 */
	@Override
	public boolean beforeRun( Device currentDevice, String runKey )
	{
		runMap.put( runKey, System.currentTimeMillis() );
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.listener.RunListener#afterRun(com.morelandLabs.spi.Device, java.lang.String, boolean)
	 */
	@Override
	public synchronized void afterRun( Device currentDevice, String runKey, boolean successful )
	{
		FileOutputStream outputStream = null;
		
		try
		{
			long runLength = System.currentTimeMillis() - runMap.get( runKey );
			outputStream = new FileOutputStream( outputFile, true );
			outputStream.write( ( runKey + "," + successful + "," + System.currentTimeMillis() + "," + runLength + "," + currentDevice.getDeviceName() + "," + currentDevice.getOs() + "," + currentDevice.getOsVersion() + "," + currentDevice.getModel() + "," + currentDevice.getManufacturer() + "," + currentDevice.getBrowserName() + "," + currentDevice.getBrowserVersion() + "," + currentDevice.getDriverType() + "\r\n" ).getBytes() );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try { outputStream.close(); } catch( Exception e ) {}
		}
	}

}
