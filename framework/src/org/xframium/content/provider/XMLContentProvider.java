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
package org.xframium.content.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xframium.content.ContentData;
import org.xframium.content.ContentManager;
import org.xframium.content.DefaultContentData;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLElementProvider.
 */
public class XMLContentProvider extends AbstractContentProvider
{
	
	/** The Constant NAME. */
	private static final String NAME = "name";

	/** The x path factory. */
	private XPathFactory xPathFactory;
	
	/** The file name. */
	private File fileName;
	
	/** The resource name. */
	private String resourceName;
	
	
	
	/**
	 * Instantiates a new XML element provider.
	 *
	 * @param fileName the file name
	 */
	public XMLContentProvider( File fileName )
	{
		xPathFactory = XPathFactory.newInstance();
		this.fileName = fileName;
	}
	
	/**
	 * Instantiates a new XML element provider.
	 *
	 * @param resourceName the resource name
	 */
	public XMLContentProvider( String resourceName )
	{
		xPathFactory = XPathFactory.newInstance();
		this.resourceName = resourceName;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.content.provider.ContentProvider#readContent()
	 */
	public void readContent()
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
		
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware( true );
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document xmlDocument = dBuilder.parse( inputStream );
		
			Node modelNode = getNode( xmlDocument, "//content/model" );
			if ( modelNode == null )
				throw new IllegalArgumentException( "A model must be specified at content/model" );
			
			String[] items = modelNode.getTextContent().split( "," );
			ContentManager.instance(xFID).setMatrixData( items );
						
			NodeList nodeList = getNodes( xmlDocument, "//content/record");
			
			for ( int i=0; i<nodeList.getLength(); i++ )
			{
				if ( nodeList.item( i ).getAttributes().getNamedItem( NAME ) == null )
					throw new IllegalArgumentException( "Name must be a specified attributes on each record" );
				
				String keyName = nodeList.item( i ).getAttributes().getNamedItem( NAME ).getNodeValue();
				
				String[] values = new String[ items.length ];
				
				for ( int x=0; x<items.length; x++ )
				{
					if ( nodeList.item( i ).getAttributes().getNamedItem( items[ x ] ) == null )
						throw new IllegalArgumentException( "The Model item [" + items[ x ] + "] was not specified" );
					
					values[ x ] = nodeList.item( i ).getAttributes().getNamedItem( items[ x ] ).getNodeValue();
				}
				
				ContentData contentData = new DefaultContentData( keyName, values, ContentManager.instance( xFID ) );
				
				ContentManager.instance( xFID ).addContentData( contentData );
			}
			
		}
		catch( Exception e )
		{
			log.fatal( "Error reading CSV Element File", e );
		}
	}
	
	
	
	/**
	 * Gets the node.
	 *
	 * @param xmlDocument the xml document
	 * @param xPathExpression the x path expression
	 * @return the node
	 */
	private  Node getNode( Document xmlDocument, String xPathExpression )
	{
		try
		{
			if ( log.isDebugEnabled() )
				log.debug( "Attempting to return Node for [" + xPathExpression + "]" );
			
			XPath xPath = xPathFactory.newXPath();
			return (Node) xPath.evaluate( xPathExpression, xmlDocument, XPathConstants.NODE );
		}
		catch( Exception e )
		{
			log.error( "Error parsing xPath Expression [" + xPathExpression + "]" );
			return null;
		}
	}
	
	/**
	 * Gets the nodes.
	 *
	 * @param xmlDocument the xml document
	 * @param xPathExpression the x path expression
	 * @return the nodes
	 */
	private  NodeList getNodes( Document xmlDocument, String xPathExpression )
	{
		try
		{
			if ( log.isDebugEnabled() )
				log.debug( "Attempting to return Nodes for [" + xPathExpression + "]" );
			
			XPath xPath = xPathFactory.newXPath();
			return (NodeList) xPath.evaluate( xPathExpression, xmlDocument, XPathConstants.NODESET );
		}
		catch( Exception e )
		{
			log.error( "Error parsing xPath Expression [" + xPathExpression + "]" );
			return null;
		}
	}
	
	
}
