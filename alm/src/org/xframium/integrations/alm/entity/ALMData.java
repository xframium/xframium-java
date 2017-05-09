package org.xframium.integrations.alm.entity;

public class ALMData
{
    private String physicalName;
    private String name;
    private String label;
    private Object value;
    
    public ALMData( String physicalName, String name, String label, Object value )
    {
        this.physicalName = physicalName;
        this.name = name;
        this.label = label;
        this.value = value;
    }
    
    public String getPhysicalName()
    {
        return physicalName;
    }
    public void setPhysicalName( String physicalName )
    {
        this.physicalName = physicalName;
    }
    
    public String getLabel()
    {
        return label;
    }
    public void setLabel( String label )
    {
        this.label = label;
    }
    
    public String getName()
    {
        return name;
    }
    public void setName( String name )
    {
        this.name = name;
    }
    
    public Object getValue()
    {
        return value;
    }
    public void setValue( Object value )
    {
        this.value = value;
    }
    
}
