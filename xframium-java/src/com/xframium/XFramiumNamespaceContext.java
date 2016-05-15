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
