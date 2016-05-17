/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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
 * The Enum BY.
 */
public enum BY
{
	
	/** The xpath. */
	XPATH( 0, "XPath" ),
	
	/** The class. */
	CLASS( 1, "Class Name" ),
	
	/** The css. */
	CSS( 2, "CSS" ),
	
	/** The id. */
	ID( 3, "ID" ),
	
	/** The link text. */
	LINK_TEXT( 4, "Link Text" ),
	
	/** The name. */
	NAME( 5, "Name" ),
	
	/** The tag name. */
	TAG_NAME( 6, "Tag Name" ),
	
	/** The v text. */
	V_TEXT( 7, "Visual Text", "VISUAL" ),
	
	/** The v image. */
	V_IMAGE( 8, "Visual Image", "VISUAL" ),
	
	/** The html. */
	HTML( 9, "HTML Properties" );
	
	/** The id. */
	private int id;
	
	/** The description. */
	private String description;
	
	/** The required context. */
	private String requiredContext;
	
	/**
	 * Instantiates a new by.
	 *
	 * @param id the id
	 * @param description the description
	 */
	BY( int id, String description )
	{
		this( id, description, null );
	}
	
	/**
	 * Instantiates a new by.
	 *
	 * @param id the id
	 * @param description the description
	 * @param requiredContext the required context
	 */
	BY( int id, String description, String requiredContext )
	{
		this.id = id;
		this.description = description;
		this.requiredContext = requiredContext;
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