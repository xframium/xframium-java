package org.xframium.container;

public class ContextContainer
{
    private String name;

    public ContextContainer( String name )
    {
        super();
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }
    
    public String toString()
    {
        return name;
    }
    
    
    
}
