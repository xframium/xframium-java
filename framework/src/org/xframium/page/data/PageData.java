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
package org.xframium.page.data;

import java.util.List;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * Represents a single row in a page data table.
 */
public interface PageData
{
	
	/** The selector. */
	public static Pattern SELECTOR = Pattern.compile( "\\|(\\w+):([^\\|]+)\\|" );
	
	/** The values. */
	public static Pattern VALUES = Pattern.compile( "(?:\\[(\\w+)=[']([^']+)[']\\])" );
	
	/** The def. */
	public static String DEF = ".def";
	
	/** The tree marker. */
	public static String TREE_MARKER = "|";
	
	/**
	 * Gets the named field from this page data object.
	 *
	 * @param fieldName the field name
	 * @return The value to return in String format
	 */
	public String getData( String fieldName );
	
	/**
	 * Gets the object record type.
	 *
	 * @return the type
	 */
	public String getType();
	
	/**
	 * Gets the name of this object value.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Checks if the record is active.
	 *
	 * @return true, if is active
	 */

	public boolean isActive();
	
	/**
	 * Gets the page data.
	 *
	 * @param fieldName the field name
	 * @return the page data
	 */
	public List<PageData> getPageData( String fieldName );
	
	/**
	 * Contains children.
	 *
	 * @return true, if successful
	 */
	public boolean containsChildren();
	
	/**
	 * Populate tree structure.
	 */
	public void populateTreeStructure();
	
	public Object get( String fieldName );
}
