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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.container.PageContainer;
import org.xframium.container.SiteContainer;
import org.xframium.page.ElementDescriptor;
import org.xframium.page.activity.PageActivity;
import org.xframium.page.element.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractElementProvider.
 */
public abstract class AbstractElementProvider implements ElementProvider 
{
    private Map<String,SiteContainer> siteTree = new HashMap<String,SiteContainer>( 20 );
    private List<SiteContainer> siteList = new ArrayList<SiteContainer>( 10 );

	private static XPathFactory xPathFactory = XPathFactory.newInstance();
	/** The log. */
	protected Log log = LogFactory.getLog(ElementProvider.class);
	
	private ElementProvider internalElementProvider = null;
	
	private ThreadLocal<Element> previousElement = new ThreadLocal<Element>();
	private ThreadLocal<ElementDescriptor> previousElementDescriptor = new ThreadLocal<ElementDescriptor>();
	
	@Override
	public void setCachedElement( Element cachedElement, ElementDescriptor elementDescriptor )
	{
	    //this.previousElement.set( cachedElement );
	    //this.previousElementDescriptor.set( elementDescriptor );
	    
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.provider.ElementProvider#getElement(com.perfectoMobile.page.ElementDescriptor)
	 */
	@Override
	public Element getElement( ElementDescriptor elementDescriptor )
	{
	    Element returnElement = null;
	    if ( internalElementProvider != null )
	        returnElement = internalElementProvider.getElement( elementDescriptor );
	    
	    if ( returnElement == null )
	        returnElement = _getElement( elementDescriptor );

	    
	    return returnElement;
	}
	
	@Override
	public void addElementProvider( ElementProvider elementProvider )
	{
	    this.internalElementProvider = elementProvider;
	    
	}
	
	private boolean initialized = false;
	
	private String siteName;
	
	@Override
	public List<SiteContainer> getSiteList()
	{
	    return siteList;
	}
	
	public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName( String siteName )
    {
        this.siteName = siteName;
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public void setInitialized( boolean initialized )
    {
        this.initialized = initialized;
    }
    
    protected void addActivity( String siteName, String pageName, PageActivity pageActivity )
    {
        SiteContainer siteContainer = siteTree.get( siteName );
        
        if ( siteContainer == null )
        {
            siteContainer = new SiteContainer( siteName );
            siteTree.put( siteContainer.getSiteName(), siteContainer );
            siteList.add( siteContainer );
        }
        
        siteContainer.getPage( pageName ).getActivityList().add( pageActivity );
        
        
    }

    /**
	 * _get element.
	 *
	 * @param elementDescriptor the element descriptor
	 * @return the element
	 */
	protected abstract Element _getElement( ElementDescriptor elementDescriptor	 );
	
	protected boolean validateElement( ElementDescriptor elementDescriptor, Element currentElement ) throws Exception
	{
	    SiteContainer siteContainer = siteTree.get( elementDescriptor.getSiteName() );
	    
	    if ( siteContainer == null )
	    {
	        siteContainer = new SiteContainer( elementDescriptor.getSiteName() );
	        siteTree.put( siteContainer.getSiteName(), siteContainer );
	        siteList.add( siteContainer );
	    }
	    
	    PageContainer elementList = siteContainer.getPage( elementDescriptor.getPageName() );
	    elementList.getElementList().add( currentElement );
	    
	    
	    try
	    {
    	    switch ( currentElement.getBy() )
    	    {
    	        case CLASS:
    	        case ID:
    	        case LINK_TEXT:
    	        case NAME:
    	        case TAG_NAME:
    	        case V_TEXT:
    	            break;
    	            
    	        case CSS:
    	        case PROP:    
    	        case HTML:
    	        case V_IMAGE:
    	            break;
    	        
    	        case XPATH:
    	            xPathFactory.newXPath().compile( currentElement.getRawKey().replace( "{", "" ).replace( "}", "" ) );
    	            
    	    }
	    }
	    catch( Exception e )
	    {
	        log.fatal( "Could not process page element identified by [" + elementDescriptor.toString() + "] as [" + currentElement.getKey() + "]" );
	        return false;
	    }
	    
	    return true;
	}

}
