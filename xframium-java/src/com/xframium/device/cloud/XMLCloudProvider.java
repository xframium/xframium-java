/*
 * 
 */
package com.xframium.device.cloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import com.xframium.device.cloud.xsd.Cloud;
import com.xframium.device.cloud.xsd.ObjectFactory;
import com.xframium.device.cloud.xsd.RegistryRoot;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLApplicationProvider.
 */
public class XMLCloudProvider extends AbstractCloudProvider
{
	
	/** The file name. */
	private File fileName;
	
	/** The resource name. */
	private String resourceName;
	
	/**
	 * Instantiates a new XML application provider.
	 *
	 * @param fileName the file name
	 */
	public XMLCloudProvider( File fileName )
	{
		this.fileName = fileName;
		readData();
	}
	
	/**
	 * Instantiates a new XML application provider.
	 *
	 * @param resourceName the resource name
	 */
	public XMLCloudProvider( String resourceName )
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
		
		try
		{
		
		    JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class );
		    Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<?> rootElement = (JAXBElement<?>)u.unmarshal( inputStream );
            
            RegistryRoot rRoot = (RegistryRoot)rootElement.getValue();
		    
			for ( Cloud cloud : rRoot.getCloud() )
				parseApplication( cloud );
			
		}
		catch( Exception e )
		{
			log.fatal( "Error reading CSV Element File", e );
		}
	}
	
	/**
	 * Parses the application.
	 *
	 * @param appNode the app node
	 */
	private void parseApplication( Cloud cloud )
	{
	    CloudRegistry.instance().addCloudDescriptor( new CloudDescriptor( cloud.getName(), cloud.getUserName(), cloud.getPassword(), cloud.getHostName(), cloud.getProxyHost(), cloud.getProxyPort() + "", "", cloud.getGrid() ) );
	}

	
}
