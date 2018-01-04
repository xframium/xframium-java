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
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import org.xframium.application.ApplicationVersion;
import org.xframium.page.BY;
import org.xframium.page.ElementDescriptor;
import org.xframium.page.activity.ActivityInitiator;
import org.xframium.page.activity.ActivityValidator;
import org.xframium.page.activity.PageActivity;
import org.xframium.page.element.Element;
import org.xframium.page.element.ElementFactory;
import org.xframium.page.element.SubElement;
import org.xframium.page.element.provider.xsd.Activity;
import org.xframium.page.element.provider.xsd.ElementParameter;
import org.xframium.page.element.provider.xsd.Import;
import org.xframium.page.element.provider.xsd.Initiator;
import org.xframium.page.element.provider.xsd.Page;
import org.xframium.page.element.provider.xsd.SimpleElement;
import org.xframium.page.element.provider.xsd.Site;
import org.xframium.page.element.provider.xsd.Validator;

// TODO: Auto-generated Javadoc
/**
 * The Class QAFElementProvider.
 */
public class QAFElementProvider extends AbstractElementProvider
{
	/** The element map. */
	private Map<String,Element> elementMap = new HashMap<String,Element>(20);
	
	/** The file name. */
	private File fileName;
	private File folderName;
	
	/** The resource name. */
	private String resourceName;
	
	/**
	 * Instantiates a new QAF element provider.
	 *
	 * @param fileName the file name
	 */
	public QAFElementProvider( File fileName )
	{
		this.fileName = fileName;
		this.folderName = fileName.getParentFile();
		readElements();
	}
	
	/**
	 * Instantiates a new QAF element provider.
	 *
	 * @param resourceName the resource name
	 */
	public QAFElementProvider( String resourceName )
	{
		this.resourceName = resourceName;
		readElements();
	}
	
	public QAFElementProvider( byte[] resourceData )
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
				log.info( "Reading from CLASSPATH as XMLElementProvider.elementFile" );
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
	

	 @Override
	protected Element _getElement( ElementDescriptor elementDescriptor )
	{
	    return elementMap.get( elementDescriptor.toString() );
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
		    LineNumberReader lineReader = new LineNumberReader( new InputStreamReader( inputStream ) );
	        
	        String currentLine;
	        
	        while ( ( currentLine = lineReader.readLine() ) != null )
	        {
	            if ( !currentLine.trim().startsWith( "#" ) && currentLine.contains( "=" ) )
	            {
	                String[] currentName = currentLine.substring( 0, currentLine.indexOf( "=" ) ).trim().split( "\\." );
	                if ( currentName.length >= 3 )
	                {
	                    String siteName = currentName[ 0 ];
	                    String pageName = currentName[ 1 ];
	                    
	                    StringBuilder elementName = new StringBuilder();
	                    for (int i=2; i<currentName.length; i++ )
	                    {
	                        elementName.append( currentName[ i ] );
	                        if ( i < currentName.length - 1 )
	                            elementName.append( "." );
	                    }

	                    try
	                    {
    	                    String descriptor = currentLine.substring( currentLine.indexOf( "=" ) + 1 ).trim();
    	                    String descriptorType = descriptor.substring( 0, descriptor.indexOf( "=" ) ).trim();
    	                    String descriptorValue = descriptor.substring( descriptor.indexOf( "=" ) + 1 ).trim();
    	                    
    	                    
    	                    ElementDescriptor eD = new ElementDescriptor( siteName, pageName, elementName.toString() );
    	                    Element e = ElementFactory.instance().createElement( BY.valueOf( descriptorType.toUpperCase() ), descriptorValue, elementName.toString(), pageName, null );
    	                    
    	                    elementMap.put(eD.toString(), e );
    	                    
    	                    if ( log.isDebugEnabled() )
    	                        log.debug( "Extracted Element [" + eD.toString() + "]" );

	                    }
	                    catch( Exception e )
	                    {
	                        
	                    }
	                }
	            }
	        }
	        
	        log.warn( "Imported " + elementMap.size() + " elements" );
			
		}
		catch( Exception e )
		{
			log.fatal( "Error reading CSV Element File", e );
		}
	}
	
	
	
	public static void main( String[] args )
    {
	    QAFElementProvider x = new QAFElementProvider( new File( "C:\\Users\\Allen\\git\\nfcuWeb\\NFCU Web\\src\\main\\resources\\web\\web.loc" ) );
	    x.readElements();
	    System.out.println( x.elementMap.toString() );
    }
	
}
