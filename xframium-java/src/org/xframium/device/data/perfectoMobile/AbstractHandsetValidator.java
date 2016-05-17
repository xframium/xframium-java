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
/*
 * 
 */
package org.xframium.device.data.perfectoMobile;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractHandsetValidator.
 */
public abstract class AbstractHandsetValidator implements PerfectoMobileHandsetValidator
{
	
	/**
	 * Gets the value.
	 *
	 * @param handSet the hand set
	 * @param tagName the tag name
	 * @return the value
	 */
	protected String getValue( Node handSet, String tagName )
	{
		NodeList params = handSet.getChildNodes();
		
		for ( int i=0; i<params.getLength(); i++ )
		{
			if ( params.item( i ).getNodeName().toLowerCase().equals( tagName.toLowerCase() ) )
			{
				return params.item( i ).getTextContent();
			}
		}
		
		return null;
	}
}
