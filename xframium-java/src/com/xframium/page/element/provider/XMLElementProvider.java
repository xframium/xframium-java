package com.xframium.page.element.provider;

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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.xframium.page.BY;
import com.xframium.page.ElementDescriptor;
import com.xframium.page.PageManager;
import com.xframium.page.element.Element;
import com.xframium.page.element.ElementFactory;
import com.xframium.page.element.provider.xsd.Import;
import com.xframium.page.element.provider.xsd.ObjectFactory;
import com.xframium.page.element.provider.xsd.Page;
import com.xframium.page.element.provider.xsd.RegistryRoot;
import com.xframium.page.element.provider.xsd.Site;

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
				parseSite( site);

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
				if (log.isInfoEnabled())
					log.info( "Attempting to import file [" + Paths.get(".").toAbsolutePath().normalize().toString() + imp.getFileName() + "]" );

				readElements( new FileInputStream( imp.getFileName() ) );
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
	private void parseSite( Site site )
	{
		if ( log.isDebugEnabled() )
			log.debug( "Extracted Site [" + site.getName() + "]" );
		
		if ( PageManager.instance().getSiteName().equals( site.getName() ) )
		{
		    for ( Page page : site.getPage() )
		    {
		        for ( com.xframium.page.element.provider.xsd.Element ele : page.getElement() )
		        {
		            ElementDescriptor elementDescriptor = new ElementDescriptor( site.getName(), page.getName(), ele.getName() );
		            Element currentElement = ElementFactory.instance().createElement( BY.valueOf( ele.getDescriptor() ), ele.getValue(), ele.getName(), page.getName(), ele.getContextName() );
		            
		            if (log.isDebugEnabled())
		                log.debug( "Adding XML Element using [" + elementDescriptor.toString() + "] as [" + currentElement + "]" );
		            elementMap.put(elementDescriptor.toString(), currentElement );
		        }
		    }
		}
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
