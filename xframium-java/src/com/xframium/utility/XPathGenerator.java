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
package com.xframium.utility;

import org.w3c.dom.Node;

public class XPathGenerator
{

    public static String genrateXpath( Node currentNode )
    {
        StringBuilder xpath = new StringBuilder();
        generateXpath( xpath, currentNode );
        return xpath.toString();

    }
    
    private static void generateXpath( StringBuilder xpath, Node currentNode )
    {

        if ( currentNode.getParentNode() != null )
        {
            currentNode.getParentNode().getChildNodes();
            int currentIndex = 0;
            for ( int i = 0; i < currentNode.getParentNode().getChildNodes().getLength(); i++ )
            {
                if ( currentNode.getParentNode().getChildNodes().item( i ).getNodeType() == Node.ELEMENT_NODE )
                {
                    currentIndex++;
                    if ( currentNode == currentNode.getParentNode().getChildNodes().item( i ) )
                    {
                        xpath.insert( 0, "/*[" + (currentIndex) + "]" );
                        generateXpath( xpath, currentNode.getParentNode() );
                        
                    }
                }
            }

        }
    }
}
