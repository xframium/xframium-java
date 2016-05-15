package com.xframium.page.element.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import com.xframium.page.BY;
import com.xframium.page.ElementDescriptor;
import com.xframium.page.PageManager;
import com.xframium.page.element.Element;
import com.xframium.page.element.ElementFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class CSVElementProvider.
 */
public class CSVElementProvider extends AbstractElementProvider
{
	
	/** The element map. */
	private Map<String,Element> elementMap = new HashMap<String,Element>(20);
	
	/** The file name. */
	private File fileName;
	
	/** The resource name. */
	private String resourceName;
	
	/**
	 * Instantiates a new CSV element provider.
	 *
	 * @param fileName the file name
	 */
	public CSVElementProvider( File fileName )
	{
		this.fileName = fileName;
		readElements();
	}
	
	/**
	 * Instantiates a new CSV element provider.
	 *
	 * @param resourceName the resource name
	 */
	public CSVElementProvider( String resourceName )
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
				
				if ( lineData[ 0 ].equals( PageManager.instance().getSiteName() ) )
				{
					ElementDescriptor elementDescriptor = new ElementDescriptor( lineData[ 0 ],  lineData[ 1 ],  lineData[ 2 ] );
					
					String contextName = null;
					if ( lineData.length > 5 )
						contextName = lineData[ 5 ];
					
					Element currentElement = ElementFactory.instance().createElement( BY.valueOf( lineData[ 3 ] ), lineData[ 4 ].replace( "$$", ","), lineData[ 2 ], lineData[ 1 ], contextName );
					
					if ( log.isDebugEnabled() )
						log.debug( "Adding CSV Element using [" + elementDescriptor.toString() + "] as [" + currentElement );
					elementMap.put(elementDescriptor.toString(), currentElement );
				}
			}
		}
		catch( Exception e )
		{
			log.fatal( "Error reading CSV Element File", e );
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
