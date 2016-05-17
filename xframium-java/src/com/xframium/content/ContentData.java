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
package com.xframium.content;

// TODO: Auto-generated Javadoc
/**
 * Represents a single row in a page data table.
 */
public interface ContentData
{
	
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue();
	
	/**
	 * Gets the value from the underlying implementation using a zero based index.  
	 *
	 * @param matrixIndex the matrix index
	 * @return the value or null of the index is out of boundds
	 */
	public String getValue( int matrixIndex );
	
	/**
	 * Gets the value.
	 *
	 * @param matrixName the matrix name
	 * @return the value or null if the key does not exist
	 */
	public String getValue( String matrixName );
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();
}
