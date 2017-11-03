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
package org.xframium.device.cloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import org.xframium.device.cloud.xsd.Cloud;
import org.xframium.device.cloud.xsd.ObjectFactory;
import org.xframium.device.cloud.xsd.RegistryRoot;
import org.xframium.device.keepAlive.DeviceKeepAlive;

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
	}
	
	/**
	 * Instantiates a new XML application provider.
	 *
	 * @param resourceName the resource name
	 */
	public XMLCloudProvider( String resourceName )
	{
		this.resourceName = resourceName;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.application.ApplicationProvider#readData()
	 */
	public List<CloudDescriptor> readData()
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
			}
		}
		
		return null;
	}
	
	/**
	 * Read elements.
	 *
	 * @param inputStream the input stream
	 */
	private List<CloudDescriptor> readElements( InputStream inputStream )
	{
	    List<CloudDescriptor> cList = new ArrayList<CloudDescriptor>( 10 );
		try
		{
		
		    JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class );
		    Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<?> rootElement = (JAXBElement<?>)u.unmarshal( inputStream );
            
            RegistryRoot rRoot = (RegistryRoot)rootElement.getValue();
		    
			for ( Cloud cloud : rRoot.getCloud() )
			    cList.add( parseCloud( cloud ) );
			return cList;
		}
		catch( Exception e )
		{
			log.fatal( "Error reading CSV Element File", e );
			return null;
		}
	}
	
	/**
	 * Parses the application.
	 *
	 * @param appNode the app node
	 */
	private CloudDescriptor parseCloud( Cloud cloud )
	{
	    CloudDescriptor cD = new CloudDescriptor( cloud.getName(), cloud.getUserName(), cloud.getPassword(), cloud.getHostName(), cloud.getProxyHost(), cloud.getProxyPort() + "", "", cloud.getGrid(), cloud.getProviderType(), cloud.getGesture(), cloud.getDeviceAction() );
	    
	    if ( cloud.getKeepAlive() != null && cloud.getKeepAlive().getImplementation() != null && !cloud.getKeepAlive().getImplementation().isEmpty() )
	    {
	        try
	        {
	           DeviceKeepAlive kA =  (DeviceKeepAlive) Class.forName( cloud.getKeepAlive().getImplementation() ).newInstance();
	           kA.setPollTime( cloud.getKeepAlive().getPollTime() );
	           kA.setQuietTime( cloud.getKeepAlive().getQuietTime() );
	           cD.setKeepAlive( kA );
	        }
	        catch( Exception e )
	        {
	            log.error( "Could not configure device keep alive for the [" + cloud.getName() + "] cloud", e );
	        }
	    }
	    
	    return cD;
	    
	}

	
}
