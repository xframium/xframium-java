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
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Enum BY.
 */
public enum BY
{
	
	/** The xpath. */
	XPATH( 0, "XPATH", "XPath is a syntax for defining parts of an XML document. XPath uses path expressions to navigate XML documents and locate elements" ),
	
	/** The class. */
	CLASS( 1, "CLASS", "The class attribute specifies one or more classnames for an element. The class attribute is mostly used to point to a class in a style sheet. However, it can also be used by a JavaScript (via the HTML DOM) to make changes to HTML elements with a specified class" ),
	
	/** The css. */
	CSS( 2, "CSS", "CSS Selectors are patterns that match against elements in a tree, and as such form one of several technologies that can be used to select nodes in an XML document" ),
	
	/** The id. */
	ID( 3, "ID", "The id attribute specifies a unique id for an HTML element (the value must be unique within the HTML document) there can be used to locate an element" ),
	
	/** The link text. */
	LINK_TEXT( 4, "LINK_TEXT", "Used to locate an item using the text associated with an HTML link (anchor tag)" ),
	
	/** The name. */
	NAME( 5, "NAME", "The name attribute specifies a name for an HTML element (the value is not gauranteed to be unique within the HTML document) there can be used to locate an element" ),
	
	/** The tag name. */
	TAG_NAME( 6, "TAG_NAME", "Used to search the tree using a tags name" ),
	
	/** The v text. */
	V_TEXT( 7, "V_TEXT", "Uses OCR (Optical Character Recognition) to convert images to text and locate the specified string", "VISUAL" ),
	
	/** The v image. */
	V_IMAGE( 8, "V_IMAGE", "Uses image identification techniques to locate the specified image on the screen", "VISUAL" ),
	
	/** The html. */
	HTML( 9, "HTML", "Uses the UFT WebElement format to locate elements in a tree" ),
	
	/** The html. */
    PROP( 10, "PROP", "A simple proeprty based locator working only on attributes of a tag" );
	
	/** The id. */
	private int id;
	
	/** The description. */
	private String description;
	
	/** The required context. */
	private String requiredContext;
	
	private String name;
	
	/**
	 * Instantiates a new by.
	 *
	 * @param id the id
	 * @param description the description
	 */
	BY( int id, String name, String description )
	{
		this( id, name, description, null );
	}
	
	/**
	 * Instantiates a new by.
	 *
	 * @param id the id
	 * @param description the description
	 * @param requiredContext the required context
	 */
	BY( int id, String name, String description, String requiredContext )
	{
		this.id = id;
		this.description = description;
		this.requiredContext = requiredContext;
		this.name = name;
	}
	
	public List<BY> getSupported()
	{
	    List<BY> byList = new ArrayList<BY>( 10 );
	    byList.add( XPATH );
	    byList.add( CLASS );
	    byList.add( CSS );
	    byList.add( ID );
	    byList.add( LINK_TEXT );
	    byList.add( NAME );
	    byList.add( TAG_NAME );
	    byList.add( V_TEXT );
	    byList.add( V_IMAGE );
	    byList.add( HTML );
	    byList.add( PROP );
	    return byList;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public String getContext()
	{
		return requiredContext;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString()
	{
		return description;
	}
}