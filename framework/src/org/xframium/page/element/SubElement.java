package org.xframium.page.element;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import org.xframium.application.ApplicationVersion;
import org.xframium.content.ContentManager;
import org.xframium.page.BY;

public class SubElement
{
    private BY by;
    private Map<String,String> elementProperties = new HashMap<String,String>( 5 );
    private String key;
    private String os;
    private ApplicationVersion version;
    
    public SubElement( BY by, String key, String os, ApplicationVersion version )
    {
        this.by = by;
        this.key = key;
        if ( os != null )
            this.os = os.toUpperCase();
        this.version = version;
    }
    
    public void addProperty( String name, String value )
    {
        elementProperties.put( name, value );
    }
    
    public BY getBy()
    {
        return by;
    }
    public void setBy( BY by )
    {
        this.by = by;
    }
    public Map<String, String> getElementProperties()
    {
        return elementProperties;
    }
    public void setElementProperties( Map<String, String> elementProperties )
    {
        this.elementProperties = elementProperties;
    }
    
    public String getKey()
    {
        return key;
    }
    public void setKey( String key )
    {
        this.key = key;
    }
    public String getOs()
    {
        return os;
    }
    public void setOs( String os )
    {
        this.os = os;
    }
    public ApplicationVersion getVersion()
    {
        return version;
    }
    public void setVersion( ApplicationVersion version )
    {
        this.version = version;
    }
    
    
}
