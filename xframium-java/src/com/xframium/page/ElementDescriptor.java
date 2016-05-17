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
package com.xframium.page;

// TODO: Auto-generated Javadoc
/**
 * The Class ElementDescriptor.
 */
public class ElementDescriptor 
{
	
	/** The Constant DELIMITER. */
	private static final String DELIMITER = ".";
	
	/** The site name. */
	private String siteName;
	
	/** The page name. */
	private String pageName;
	
	/** The element name. */
	private String elementName;
	
	
	/**
	 * Instantiates a new element descriptor.
	 */
	public ElementDescriptor()
	{
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Instantiates a new element descriptor using the System site name defined by page manager.
	 *
	 * @param pageClass the page class
	 * @param elementName the element name
	 */
	public ElementDescriptor( Class pageClass, String elementName )
	{
		this.siteName = PageManager.instance().getSiteName();
		this.pageName = pageClass.getInterfaces()[0].getSimpleName();
		this.elementName = elementName;
	}
	
	/**
	 * Instantiates a new element descriptor.
	 *
	 * @param siteName the site name
	 * @param pageName the page name
	 * @param elementName the element name
	 */
	public ElementDescriptor( String siteName, String pageName, String elementName )
	{
		this.siteName = siteName;
		this.pageName = pageName;
		this.elementName = elementName;
	}

	/**
	 * Gets the site name.
	 *
	 * @return the site name
	 */
	public String getSiteName()
	{
		return siteName;
	}

	/**
	 * Sets the site name.
	 *
	 * @param siteName the new site name
	 */
	public void setSiteName( String siteName )
	{
		this.siteName = siteName;
	}

	/**
	 * Gets the page name.
	 *
	 * @return the page name
	 */
	public String getPageName()
	{
		return pageName;
	}

	/**
	 * Sets the page name.
	 *
	 * @param pageName the new page name
	 */
	public void setPageName( String pageName )
	{
		this.pageName = pageName;
	}

	/**
	 * Gets the element name.
	 *
	 * @return the element name
	 */
	public String getElementName()
	{
		return elementName;
	}

	/**
	 * Sets the element name.
	 *
	 * @param elementName the new element name
	 */
	public void setElementName( String elementName )
	{
		this.elementName = elementName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
    {
    	StringBuilder keyBuilder = new StringBuilder();
    	if ( siteName != null )
    		keyBuilder.append( siteName ).append( DELIMITER );
    	
    	if ( pageName != null )
    		keyBuilder.append( pageName ).append( DELIMITER );
    	
    	return keyBuilder.append( elementName ).toString();
    		
    }
}
