package org.xframium.page.element;

import java.util.HashMap;
import java.util.Map;
import org.xframium.application.ApplicationVersion;
import org.xframium.device.cloud.CloudDescriptor.ProviderType;
import org.xframium.page.BY;

public class SubElement
{
    private BY by;
    private Map<String,String> elementProperties = new HashMap<String,String>( 5 );
    private String key;
    private String os;
    private ApplicationVersion version;
    private ProviderType cloudProvider;
    private String deviceTag;
    private String context;
    
    public SubElement( BY by, String key, String os, ApplicationVersion version, ProviderType cloudProvider, String deviceTag, String context )
    {
        this.by = by;
        this.key = key;
        if ( os != null )
            this.os = os.toUpperCase();
        this.version = version;
        this.cloudProvider = cloudProvider;
        this.deviceTag = deviceTag;
        this.context = context;
    }
    
    public SubElement( BY by, String key, String os, ApplicationVersion version, String cloudProvider, String deviceTag, String context )
    {
        this.by = by;
        this.key = key;
        if ( os != null )
            this.os = os.toUpperCase();
        this.version = version;
        if ( cloudProvider != null )
        this.cloudProvider = ProviderType.valueOf( cloudProvider.toUpperCase() );
        this.deviceTag = deviceTag;
        this.context = context;
    }
    
    public String getContext()
    {
        return context;
    }

    public void setContext( String context )
    {
        this.context = context;
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

    public ProviderType getCloudProvider()
    {
        return cloudProvider;
    }

    public void setCloudProvider( ProviderType cloudProvider )
    {
        this.cloudProvider = cloudProvider;
    }

    public String getDeviceTag()
    {
        return deviceTag;
    }

    public void setDeviceTag( String deviceTag )
    {
        this.deviceTag = deviceTag;
    }
}
