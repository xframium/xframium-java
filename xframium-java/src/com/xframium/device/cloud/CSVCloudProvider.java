/*
 * 
 */
package com.xframium.device.cloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

// TODO: Auto-generated Javadoc
/**
 * The Class CSVApplicationProvider.
 */
public class CSVCloudProvider extends AbstractCloudProvider
{
	
	/** The file name. */
	private File fileName;
	
	/** The resource name. */
	private String resourceName;
	
	/**
	 * Instantiates a new CSV application provider.
	 *
	 * @param fileName the file name
	 */
	public CSVCloudProvider( File fileName )
	{
		this.fileName = fileName;
		readData();
	}
	
	/**
	 * Instantiates a new CSV application provider.
	 *
	 * @param resourceName the resource name
	 */
	public CSVCloudProvider( String resourceName )
	{
		this.resourceName = resourceName;
		readData();
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.application.ApplicationProvider#readData()
	 */
	public void readData()
	{
	    CloudRegistry.instance().clear();
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
		BufferedReader fileReader = new BufferedReader( new InputStreamReader( inputStream ) );
		String currentLine = null;
		
		try
		{
			while ( ( currentLine = fileReader.readLine() ) != null )
			{
				if ( log.isDebugEnabled() )
					log.debug( "Attempting to parse current line as [" + currentLine + "]" );
				
				String[] lineData = currentLine.split( "," );
				
				CloudRegistry.instance().addCloudDescriptor( new CloudDescriptor( lineData[ 0 ], lineData[ 1 ], lineData[ 2 ], lineData[ 3 ], lineData[ 4 ], lineData[ 5 ], lineData[ 7 ], lineData[ 6 ] ) );
			}
		}
		catch( Exception e )
		{
			log.fatal( "Error reading CSV Element File", e );
		}
	}
}
