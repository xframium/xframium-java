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
