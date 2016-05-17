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
package com.xframium.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;



public class ExecutionDefinition 
{
	private String date;
	private String time;
	private String testCase;
	private String deviceId;
	private boolean success;
	private String manufacturer;
	private String model;
	private Properties propertyMap;
	
	private File snapshotFile;
	private File domFile;
	
	public ExecutionDefinition( File propertyFile )
	{
		try
		{
			propertyMap = new Properties();
			propertyMap.load( new FileInputStream( propertyFile ) );
			
			date = propertyMap.getProperty( "DATE" );
			time = propertyMap.getProperty( "TIME" );
			testCase = propertyMap.getProperty( "TEST_CASE" );
			deviceId = propertyMap.getProperty( "DEVICE" );
			manufacturer = propertyMap.getProperty( "MANUFACTURER" );
			model = propertyMap.getProperty( "MODEL" );
			success = Boolean.parseBoolean( propertyMap.getProperty( "SUCCESS", "false" ) );
			
			snapshotFile = new File( propertyFile.getParentFile(), "failure-screenshot.png" );
			domFile = new File( propertyFile.getParentFile(), "failureDOM.xml" );
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public String toString()
	{
		return testCase + "-->" + deviceId + " (" + date + ")";
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getTestCase() {
		return testCase;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getModel() {
		return model;
	}

	public Properties getPropertyMap() {
		return propertyMap;
	}


	public File getSnapshotFile() {
		return snapshotFile;
	}

	public File getDomFile() {
		return domFile;
	}
	
	public boolean filesExist()
	{
		return snapshotFile.exists() && domFile.exists();
	}

}
