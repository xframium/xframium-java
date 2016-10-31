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
package org.xframium.application;

import java.io.ByteArrayInputStream;
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
import org.openqa.selenium.Platform;
import org.xframium.application.xsd.Application;
import org.xframium.application.xsd.Capabilities;
import org.xframium.application.xsd.DeviceCapability;
import org.xframium.application.xsd.ObjectDeviceCapability;
import org.xframium.application.xsd.ObjectFactory;
import org.xframium.application.xsd.Options;
import org.xframium.application.xsd.RegistryRoot;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLApplicationProvider.
 */
public class XMLApplicationProvider extends AbstractApplicationProvider
{
    /** The file name. */
    private File fileName;

    /** The resource name. */
    private String resourceName;
    private byte[] resourceData;

    /**
     * Instantiates a new XML application provider.
     *
     * @param fileName
     *            the file name
     */
    public XMLApplicationProvider( File fileName )
    {
        this.fileName = fileName;
    }
    
    public XMLApplicationProvider( byte[] resourceData )
    {
        this.resourceData = resourceData;
    }

    /**
     * Instantiates a new XML application provider.
     *
     * @param resourceName
     *            the resource name
     */
    public XMLApplicationProvider( String resourceName )
    {
        this.resourceName = resourceName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.device.application.ApplicationProvider#readData()
     */
    public List<ApplicationDescriptor> readData()
    {
        if ( resourceData != null )
        {
            return readElements( new ByteArrayInputStream( resourceData ) );
        }
        else if ( fileName == null )
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
            catch ( FileNotFoundException e )
            {
                log.fatal( "Could not read from " + fileName, e );
            }
        }
        
        return null;
    }

    /**
     * Read elements.
     *
     * @param inputStream
     *            the input stream
     */
    private List<ApplicationDescriptor> readElements( InputStream inputStream )
    {

        try
        {

            JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class );
            Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<?> rootElement = (JAXBElement<?>) u.unmarshal( inputStream );

            RegistryRoot rRoot = (RegistryRoot) rootElement.getValue();

            List<ApplicationDescriptor> appList = new ArrayList<ApplicationDescriptor>( 10 );
            for ( Application app : rRoot.getApplication() )
                appList.add( parseApplication( app ) );
            
            return appList;

        }
        catch ( Exception e )
        {
            log.fatal( "Error reading XML Element File", e );
            return null;
        }
        
        
    }

    /**
     * Parses the application.
     *
     * @param appNode
     *            the app node
     */
    private ApplicationDescriptor parseApplication( Application app )
    {
		String driverName = "";
		List<String> list = null;
		String factoryName = null;
		Map<String, Object> keyOptions = null;
		Map<String, Object> browserOptionMap = null;
        Map<String, Object> capabilities = new HashMap<String, Object>( 10 );
        
        if ( app.getCapability() != null )
        {
            for ( DeviceCapability cap : app.getCapability() )
            {
                switch ( cap.getClazz() )
                {
                    case "BOOLEAN":
                        capabilities.put( cap.getName(), Boolean.parseBoolean( cap.getValue() ) );
                        break;

                    case "STRING":
                        capabilities.put( cap.getName(), cap.getValue() );
                        break;

                    case "PLATFORM":
                        capabilities.put( cap.getName(), Platform.valueOf( cap.getValue().toUpperCase() ) );
                        break;
                }
            }
        }

      //Parse the Object Capability element for browser options
  		if ( app.getObjectCapability() != null )
  		{
  		    for ( ObjectDeviceCapability cap : app.getObjectCapability() )
  		    {
  		    	browserOptionMap = new HashMap<String, Object>();
  		    	
  		    	if ( cap.getCapabilities() != null )
  		    	{
  		    		for ( Capabilities objCapabilities : cap.getCapabilities() ) 
  		    		{
  		    			factoryName = objCapabilities.getFactoryName();
  		    			
  		    			if ( objCapabilities.getOptions() != null )
  		    			{
  		    				for ( Options option : objCapabilities.getOptions() )
  		    				{
  		    					if (option.getKey() == null) {
  		    						
	  		    					if (browserOptionMap.get(option.getName()) == null) {
	  		    						list = new ArrayList<String>();
	
	  		    					} else {
	  		    						list = (List<String>) browserOptionMap.get(option.getName());
	
	  		    					}
	  		    					list.add(option.getValue());
	  		    					browserOptionMap.put(option.getName(), list);
	  		    					
	  		    				} else {
									
									if (browserOptionMap.get(option.getName()) == null) {
										keyOptions = new HashMap<String, Object>();
									
									} else {
										keyOptions = (HashMap<String, Object>) browserOptionMap.get(option.getName());
									}
									keyOptions.put(option.getKey(), option.getValue());
									browserOptionMap.put(option.getName(), keyOptions);
								}
  		    				}
  		    			}
  		    		}
  		    	}
  		    	capabilities.put(factoryName, browserOptionMap);
  		    }
  		}
  		
  		return new ApplicationDescriptor( app.getName(), "", app.getAppPackage(), app.getBundleId(), app.getUrl(), app.getIosInstall(), app.getAndroidInstall(), capabilities );
    }

}
