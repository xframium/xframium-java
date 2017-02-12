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
package org.xframium;

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Interface Initializable.
 */
public interface Initializable
{
	public static final String VERSION = "1.0.8";
	/**
	 * Initialize.
	 *
	 * @param propertyPrefix the property prefix
	 * @param propertyMap the property map
	 */
	public void initialize( String propertyPrefix, Map<String,String> propertyMap );
}
