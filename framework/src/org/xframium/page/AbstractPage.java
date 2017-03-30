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

package org.xframium.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.page.element.Element;
import org.xframium.reporting.ExecutionContextTest;

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
    
    private ExecutionContextTest executionContext;
    
    @Override
    public ExecutionContextTest getExecutionContext()
    {
        return executionContext;
    }
    
    @Override
    public void setExecutionContext( ExecutionContextTest executionContext )
    {
        this.executionContext = executionContext;
    }
    
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
    	return getElement( elementDescriptor );
    	
    }
    
    public Element getElement( ElementDescriptor elementDescriptor )
    {
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
