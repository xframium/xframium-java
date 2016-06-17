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
package org.xframium.page.keyWord.spi;

import org.xframium.page.AbstractPage;
import org.xframium.page.ElementDescriptor;
import org.xframium.page.PageManager;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.KeyWordPage;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyWordPageImpl.
 */
public class KeyWordPageImpl extends AbstractPage implements KeyWordPage
{

	/** The _page name. */
	private String _pageName;
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.Page#initializePage()
	 */
	@Override
	public void initializePage()
	{
		// TODO Auto-generated method stub

	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.AbstractPage#getElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public Element getElement( String siteName, String pageName, String elementName )
    {
    	ElementDescriptor elementDescriptor = new ElementDescriptor( siteName, pageName, elementName );
    	
    	if ( log.isInfoEnabled() )
    		log.info( Thread.currentThread().getName() + ": Attempting to locate element using [" + elementDescriptor.toString() + "]" + " - " + webDriver );
    	
    	Element myElement = PageManager.instance().getElementProvider().getElement( elementDescriptor ).cloneElement();
    	myElement.setDriver( webDriver );
    	return myElement;
    }
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.AbstractPage#getElement(java.lang.String, java.lang.String)
	 */
	public Element getElement( String pageName, String elementName )
    {
    	return getElement( PageManager.instance().getSiteName(), pageName, elementName );
    }
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.AbstractPage#getElement(java.lang.String)
     */
    public Element getElement( String elementName )
    {
    	return getElement( PageManager.instance().getSiteName(), _pageName, elementName );
    }
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.AbstractPage#getPageName()
     */
    @Override
    public String getPageName()
    {
    	return _pageName;
    }
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.KeyWordPage#setPageName(java.lang.String)
     */
    @Override
    public void setPageName( String pageName )
    {
    	this._pageName = pageName;
    }
    
    

}
