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
package org.xframium.page.element.provider;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import org.xframium.page.BY;
import org.xframium.page.ElementDescriptor;
import org.xframium.page.element.Element;
import org.xframium.page.element.ElementFactory;
import org.xframium.page.element.provider.xsd.ElementParameter;
import org.xframium.page.element.provider.xsd.Import;
import org.xframium.page.element.provider.xsd.ObjectFactory;
import org.xframium.page.element.provider.xsd.Page;
import org.xframium.page.element.provider.xsd.RegistryRoot;
import org.xframium.page.element.provider.xsd.Site;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLElementProvider.
 */
public class XMLElementProvider extends AbstractElementProvider
{
	/** The element map. */
	private Map<String,Element> elementMap = new HashMap<String,Element>(20);
	
	/** The file name. */
	private File fileName;
	private File folderName;
	
	/** The resource name. */
	private String resourceName;
	
	/**
	 * Instantiates a new XML element provider.
	 *
	 * @param fileName the file name
	 */
	public XMLElementProvider( File fileName )
	{
		this.fileName = fileName;
		this.folderName = fileName.getParentFile();
		readElements();
	}
	
	/**
	 * Instantiates a new XML element provider.
	 *
	 * @param resourceName the resource name
	 */
	public XMLElementProvider( String resourceName )
	{
		this.resourceName = resourceName;
		readElements();
	}
	
	public XMLElementProvider( byte[] resourceData )
    {
        readElements( new ByteArrayInputStream( resourceData ) );
    }
	
	/**
	 * Read elements.
	 */
	private void readElements()
	{
		if ( fileName == null )
		{
			if ( log.isInfoEnabled() )
				log.info( "Reading from CLASSPATH as CSVElementProvider.elementFile" );
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

			for ( Site site : rRoot.getSite() )
			{
			    if ( getSiteName() == null )
			        setSiteName( site.getName() );
				parseSite( site);
			}

			for ( Import imp : rRoot.getImport() )
				parseImport( imp );
			
		}
		catch( Exception e )
		{
			log.fatal( "Error reading CSV Element File", e );
		}
	}
	
	/**
	 * Parses the import.
	 *
	 * @param imp the imp
	 */
	private void parseImport( Import imp )
	{
		if (imp.getFileName() != null)
		{
			try
			{
				

				if ( fileName == null )
				{
				    readElements( getClass().getClassLoader().getResourceAsStream( imp.getFileName() ) );
				}
				else
				{
    				File newFile = new File( imp.getFileName() );
    				if ( newFile.exists() ) 
    				{
    				    if (log.isInfoEnabled())
    	                    log.info( "Attempting to import file [" + newFile.getAbsolutePath() + "]" );
    				    readElements( new FileInputStream( newFile ) );
    				}
    				    
    				newFile = new File( folderName, imp.getFileName() );
    				if ( newFile.exists() )
    				{
    				    if (log.isInfoEnabled())
                            log.info( "Attempting to import file [" + newFile.getAbsolutePath() + "]" );
    				    readElements( new FileInputStream( newFile ) );
    				}
				}
				
			}
			catch (FileNotFoundException e)
			{
				log.fatal( "Could not read from " + fileName, e );
			}
		}
	}
	
	/**
	 * Parses the site.
	 *
	 * @param site the site
	 */
	private void parseSite( Site site ) throws Exception
	{
		if ( log.isDebugEnabled() )
			log.debug( "Extracted Site [" + site.getName() + "]" );
		
		boolean elementsRead = true;
		
	    for ( Page page : site.getPage() )
	    {
	        for ( org.xframium.page.element.provider.xsd.Element ele : page.getElement() )
	        {
	            ElementDescriptor elementDescriptor = new ElementDescriptor( site.getName(), page.getName(), ele.getName() );
	            Element currentElement = ElementFactory.instance().createElement( BY.valueOf( ele.getDescriptor() ), ele.getValue(), ele.getName(), page.getName(), ele.getContextName() );
	            
	            if ( ele.getParameter() != null )
                {
                    for ( ElementParameter xP : ele.getParameter() )
                    {
                        currentElement.addElementProperty( xP.getName(), xP.getValue() );
                    }
                }
	            
	            if ( ele.getDeviceContext() != null )
	                currentElement.setDeviceContext( ele.getDeviceContext() );
	            
	            //if (log.isDebugEnabled())
	            //    log.debug( "Adding XML Element using [" + elementDescriptor.toString() + "] as [" + currentElement + "]" );
	            
	            elementsRead = elementsRead & validateElement( elementDescriptor, currentElement );
	            
	            elementMap.put(elementDescriptor.toString(), currentElement );
	        }
	    }
	    
	    setInitialized( elementsRead );
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.provider.AbstractElementProvider#_getElement(com.perfectoMobile.page.ElementDescriptor)
	 */
	@Override
	protected Element _getElement( ElementDescriptor elementDescriptor )
	{
		return elementMap.get(  elementDescriptor.toString() );
	}
	
}
