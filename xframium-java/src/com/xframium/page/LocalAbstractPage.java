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
