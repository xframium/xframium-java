
package com.xframium.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xframium.page.element.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractPage.
 *
 * @author ageary
 */
public abstract class AbstractPage implements Page
{
    
    /** The log. */
    protected Log log = LogFactory.getLog( Page.class );

    /** The element cache. */
    private HashMap<String,Element> elementCache = new HashMap<String,Element>( 20 );
    
    /** The element list. */
    private List<Element> elementList = new ArrayList<Element>( 10 );
    
    /** The web driver. */
    protected Object webDriver;
    
    /**
     * Register element.
     *
     * @param elementDescriptor the element descriptor
     * @param currentElement the current element
     */
    public void registerElement( ElementDescriptor elementDescriptor, Element currentElement )
    {
    	if ( log.isDebugEnabled() )
    		log.debug( "Registering CACHED element using [" + elementDescriptor.toString() + "] as " + currentElement + " on " + getClass().getName() );
    	elementCache.put( elementDescriptor.toString(), currentElement );
    	elementList.add( currentElement );
    	currentElement.setDriver( webDriver );
    }
    
    /**
     * Gets the element.
     *
     * @param siteName the site name
     * @param pageName the page name
     * @param elementName the element name
     * @return the element
     */
    public Element getElement( String siteName, String pageName, String elementName )
    {
    	ElementDescriptor elementDescriptor = new ElementDescriptor( siteName, pageName, elementName );
    	
    	if ( log.isDebugEnabled() )
    		log.debug( "Attempting to locate element using [" + elementDescriptor.toString() + "]" );
    	
    	return elementCache.get( elementDescriptor.toString() );
    }
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.Page#getElement(java.lang.String, java.lang.String)
     */
    public Element getElement( String pageName, String elementName )
    {
    	return getElement( PageManager.instance().getSiteName(), pageName, elementName );
    }
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.Page#getElement(java.lang.String)
     */
    public Element getElement( String elementName )
    {
    	return getElement( PageManager.instance().getSiteName(), this.getClass().getInterfaces()[ 0 ].getSimpleName(), elementName );
    }
    
    
    /**
     * Gets the page name.  This will extract the Clas simple name
     *
     * @return the page name
     */
    public String getPageName()
    {
    	return getClass().getInterfaces()[ 0 ].getSimpleName();
    }
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.Page#setDriver(java.lang.Object)
     */
    public final void setDriver( Object webDriver )
    {
    	this.webDriver = webDriver;
    }
    
    
}
