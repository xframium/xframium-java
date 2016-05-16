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
package com.xframium;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;

public class XFramiumNamespaceContext implements NamespaceContext
{

    private Map<String,String> namespaceMap;
    
    public XFramiumNamespaceContext( Map<String,String> namespaceMap )
    {
        this.namespaceMap = namespaceMap;
    }
    
    public XFramiumNamespaceContext()
    {
        this.namespaceMap = new HashMap<String,String>( 10 );
    }
    
    public void registerNamespace( String prefix, String uri )
    {
        namespaceMap.put( prefix,  uri );
    }
    
    public String getNamespaceURI( String prefix )
    {
        return namespaceMap.get( prefix );
    }

    public String getPrefix( String namespaceURI )
    {
        if ( namespaceMap.containsValue( namespaceURI ) )
        {
            for ( String value : namespaceMap.keySet() )
            {
                if ( namespaceURI.equals( namespaceMap.get( value ) ) )
                    return value;
            }
        }
        
        return null;
    }

    public Iterator<String> getPrefixes( String namespaceURI )
    {
        return namespaceMap.keySet().iterator();
    }
}
