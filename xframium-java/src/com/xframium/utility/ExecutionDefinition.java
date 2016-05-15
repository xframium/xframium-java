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
