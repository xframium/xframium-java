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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.openqa.selenium.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xframium.application.xsd.Application;
import org.xframium.application.xsd.DeviceCapability;
import org.xframium.application.xsd.ObjectFactory;
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

    /**
     * Instantiates a new XML application provider.
     *
     * @param fileName
     *            the file name
     */
    public XMLApplicationProvider( File fileName )
    {
        this.fileName = fileName;
        readData();
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
        readData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.device.application.ApplicationProvider#readData()
     */
    public void readData()
    {
        ApplicationRegistry.instance().clear();
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
            catch ( FileNotFoundException e )
            {
                log.fatal( "Could not read from " + fileName, e );
            }
        }
    }

    /**
     * Read elements.
     *
     * @param inputStream
     *            the input stream
     */
    private void readElements( InputStream inputStream )
    {

        try
        {

            JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class );
            Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<?> rootElement = (JAXBElement<?>) u.unmarshal( inputStream );

            RegistryRoot rRoot = (RegistryRoot) rootElement.getValue();

            for ( Application app : rRoot.getApplication() )
                parseApplication( app );

        }
        catch ( Exception e )
        {
            log.fatal( "Error reading CSV Element File", e );
        }
    }

    /**
     * Parses the application.
     *
     * @param appNode
     *            the app node
     */
    private void parseApplication( Application app )
    {
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

                    case "OBJECT":
                        capabilities.put( cap.getName(), cap.getValue() );
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

        ApplicationRegistry.instance().addApplicationDescriptor( new ApplicationDescriptor( app.getName(), "", app.getAppPackage(), app.getBundleId(), app.getUrl(), app.getIosInstall(), app.getAndroidInstall(), capabilities ) );
    }

}
