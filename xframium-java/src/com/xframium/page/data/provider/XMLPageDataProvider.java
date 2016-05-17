/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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
package com.xframium.page.data.provider;

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
import com.xframium.page.BY;
import com.xframium.page.ElementDescriptor;
import com.xframium.page.data.DefaultPageData;
import com.xframium.page.data.PageData;
import com.xframium.page.element.Element;
import com.xframium.page.element.ElementFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLPageDataProvider.
 */
public class XMLPageDataProvider extends AbstractPageDataProvider
{
	
	/** The Constant NAME. */
	private static final String NAME = "name";
	
	/** The Constant ACTIVE. */
	private static final String ACTIVE = "active";
	
	/** The Constant LOCK. */
	private static final String LOCK = "lockRecords";

	/** The x path factory. */
	private XPathFactory xPathFactory;
	
	/** The file name. */
	private File fileName;
	
	/** The resource name. */
	private String resourceName;
	
	private boolean asResource = false;

	/**
	 * Instantiates a new XML page data provider.
	 *
	 * @param fileName the file name
	 */
	public XMLPageDataProvider( File fileName )
	{
		xPathFactory = XPathFactory.newInstance();
		this.fileName = fileName;
	}

	/**
	 * Instantiates a new XML page data provider.
	 *
	 * @param resourceName the resource name
	 */
	public XMLPageDataProvider( String resourceName )
	{
		xPathFactory = XPathFactory.newInstance();
		this.resourceName = resourceName;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.data.provider.AbstractPageDataProvider#readPageData()
	 */
	@Override
	public void readPageData()
	{
		if (fileName == null)
		{
			if (log.isInfoEnabled())
				log.info( "Reading from CLASSPATH as " + resourceName );
			asResource = true;
			readElements( getClass().getClassLoader().getResourceAsStream( resourceName ) );
			
			populateTrees();
		}
		else
		{
			try
			{
				if (log.isInfoEnabled())
					log.info( "Reading from FILE SYSTEM as [" + fileName + "]" );
				asResource = false;
				readElements( new FileInputStream( fileName ) );
				
				populateTrees();
			}
			catch (FileNotFoundException e)
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

			NodeList nodeList = getNodes( xmlDocument, "//data/recordType" );
			for (int i = 0; i < nodeList.getLength(); i++)
				parseType( nodeList.item( i ) );
			
			NodeList importList = getNodes( xmlDocument, "//data/import" );
			if ( importList != null )
			{
    			for (int i = 0; i < importList.getLength(); i++)
    			{
    			    Node importElement = importList.item( i );
    			    String fileName = importElement.getAttributes().getNamedItem( "fileName" ).getNodeValue();
    			    
    			    if (log.isInfoEnabled())
                        log.info( "Reading imported data as [" + fileName + "]" );
    			    
    			    if ( asResource )
    			        readElements( getClass().getClassLoader().getResourceAsStream( fileName ) );
    			    else
    			        readElements( new FileInputStream( fileName ) );
    			}
			}	
		}
		catch (Exception e)
		{
			log.fatal( "Error reading CSV Element File", e );
		}
	}

	/**
	 * Parses the type.
	 *
	 * @param siteNode the site node
	 */
	private void parseType( Node siteNode )
	{
		String typeName = siteNode.getAttributes().getNamedItem( NAME ).getNodeValue();
		Node lockNode = siteNode.getAttributes().getNamedItem( LOCK );

		boolean lockRecords = false;
		if (lockNode != null)
			lockRecords = Boolean.parseBoolean( lockNode.getNodeValue() );

		if (log.isDebugEnabled())
			log.debug( "Extracted Record Type [" + typeName + "]" );

		addRecordType( typeName, lockRecords );

		NodeList nodeList = getNodes( siteNode.getOwnerDocument(), "//data/recordType[@name='" + typeName + "']/record" );

		for (int i = 0; i < nodeList.getLength(); i++)
			parseRecord( typeName, nodeList.item( i ) );
	}

	/**
	 * Parses the record.
	 *
	 * @param typeName the type name
	 * @param pageNode the page node
	 */
	private void parseRecord( String typeName, Node pageNode )
	{
		String recordName = pageNode.getAttributes().getNamedItem( NAME ).getNodeValue();

		boolean active = true;
		
		Node activeNode = pageNode.getAttributes().getNamedItem( ACTIVE );
		if ( activeNode != null )
			active = Boolean.parseBoolean( activeNode.getNodeValue() );
		
		if ( !active )
		{
			if (log.isDebugEnabled())
				log.debug( "Record [" + recordName + "] is being ignored as it is inactive" );
			return;
		}
		
		if (log.isDebugEnabled())
			log.debug( "Extracted Record [" + recordName + "]" );

		DefaultPageData currentRecord = new DefaultPageData( typeName, recordName, active );

		for (int i = 0; i < pageNode.getAttributes().getLength(); i++)
		{
			Node attributeNode = pageNode.getAttributes().item( i );
			if (!attributeNode.getNodeName().equals( NAME ))
			{
			    String currentName = attributeNode.getNodeName();
			    String currentValue = attributeNode.getNodeValue();
			    
			    if ( currentValue.startsWith( PageData.TREE_MARKER ) && currentValue.endsWith( PageData.TREE_MARKER ) )
			    {
			        //
			        // This is a reference to another page data table
			        //
			        currentRecord.addPageData( currentName );
			        currentRecord.addValue( currentName + PageData.DEF, currentValue );
			        currentRecord.setContainsChildren( true );
			    }
			    else
			        currentRecord.addValue( currentName, currentValue );
			}
		}

		for (int i = 0; i < pageNode.getChildNodes().getLength(); i++)
		{
			Node elementNode = pageNode.getChildNodes().item( i );

			if (elementNode.getNodeType() == Node.ELEMENT_NODE)
			{
			    String currentName = elementNode.getNodeName();
                String currentValue = elementNode.getTextContent();
                
                if ( currentValue.startsWith( PageData.TREE_MARKER ) && currentValue.endsWith( PageData.TREE_MARKER ) )
                {
                    //
                    // This is a reference to another page data table
                    //
                    currentRecord.addPageData( currentName );
                    currentRecord.addValue( currentName + PageData.DEF, currentValue );
                    currentRecord.setContainsChildren( true );
                }
                else
                    currentRecord.addValue( currentName, currentValue );
			}

		}

		addRecord( currentRecord );
	}

	/**
	 * Gets the nodes.
	 *
	 * @param xmlDocument the xml document
	 * @param xPathExpression the x path expression
	 * @return the nodes
	 */
	private NodeList getNodes( Document xmlDocument, String xPathExpression )
	{
		try
		{
			if (log.isDebugEnabled())
				log.debug( "Attempting to return Nodes for [" + xPathExpression + "]" );

			XPath xPath = xPathFactory.newXPath();
			return ( NodeList ) xPath.evaluate( xPathExpression, xmlDocument, XPathConstants.NODESET );
		}
		catch (Exception e)
		{
			log.error( "Error parsing xPath Expression [" + xPathExpression + "]" );
			return null;
		}
	}

}
