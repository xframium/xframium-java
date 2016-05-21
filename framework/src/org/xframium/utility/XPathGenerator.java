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
package org.xframium.utility;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Node;

public class XPathGenerator
{
    private static final String COMMA = ",";
    private static final String AMP = "&";
    private static final String EQUALS = "=";
    private static final String AT = "@";
    private static final String COLON = ":";
    private static final String RESOURCE_ID = "resource-id";
    
    public static void main( String[] args )
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put(  "resource-id", "com.discoverfinancial.mobile" );
        
        System.out.println( generateXPathFromProperty( map, "resource-id=password_field&name=Forgot Password,text=Bank" ) );
        
        
    }
    
    public static String generateXPathFromProperty( Map<String,String> propertyMap, String propertyDefinition )
    {
        StringBuilder xpathBuilder = new StringBuilder();
        String[] ors = propertyDefinition.split( COMMA );
        
        for ( String myOr : ors )
        {
            xpathBuilder.append( "/*" );
            String[] ands = myOr.split( AMP );
            
            for ( int i=0; i<ands.length; i++ )
            {
                String[] nameValue = ands[ i ].split( EQUALS ); 
                
                xpathBuilder.append( "[" ).append( AT ).append( nameValue[ 0 ] ).append( EQUALS ).append( "'" );
                if ( RESOURCE_ID.equals( nameValue[ 0 ].toLowerCase() ) )
                {
                    if ( propertyMap.get( RESOURCE_ID ) != null && !nameValue[ 1 ].contains( COLON ) )
                        xpathBuilder.append( propertyMap.get( RESOURCE_ID ) ).append( ":id/" ).append(  nameValue[ 1 ] ).append( "'" );
                    else
                        xpathBuilder.append( nameValue[ 1 ] ).append( "'" );
                }
                else
                    xpathBuilder.append( nameValue[ 1 ] ).append( "'" );
                
                xpathBuilder.append( "]" );
            }
            
            xpathBuilder.append( "|" );
        }
        
        return xpathBuilder.substring( 0, xpathBuilder.length() - 1 );
        
    }
    
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
