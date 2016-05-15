package com.xframium.page;

import java.util.HashMap;
import java.util.Map;
import com.xframium.page.element.Element;
import com.xframium.page.element.provider.ElementDataSource;

// TODO: Auto-generated Javadoc
/**
 * Holds local reference to all loaded page elements.  This is the default page object abstraction
 */
public abstract class LocalAbstractPage extends AbstractPage
{
	
	/** The element map. */
	private Map<String,Element> elementMap = new HashMap<String,Element>( 20 );
	
	/**
	 * Adds the local element.
	 *
	 * @param elementDescriptor the element descriptor
	 * @param currentElement the current element
	 */
	protected void addLocalElement( ElementDescriptor elementDescriptor, Element currentElement )
	{
		if ( log.isDebugEnabled() )
			log.debug( "Adding LOCAL Element using [" + elementDescriptor.toString() + "] as " + currentElement );
		elementMap.put(  elementDescriptor.toString(), currentElement );
	}
	
	/**
	 * Gets the element.
	 *
	 * @param elementDescriptor the element descriptor
	 * @return the element
	 */
	public Element getElement( ElementDescriptor elementDescriptor )
	{
		if ( log.isDebugEnabled() )
			log.debug( "Extracting LOCAL element using [" + elementDescriptor.toString() + "]" );
		return elementMap.get( elementDescriptor.toString() );
	}

}
