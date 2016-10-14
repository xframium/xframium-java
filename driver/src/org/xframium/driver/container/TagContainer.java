package org.xframium.driver.container;

public class TagContainer
{
    private String name;
    private String description;
    private String xPath;
    
    public TagContainer( String name, String description, String xPath )
    {
        this.name = name;
        this.description = description;
        this.xPath = xPath;
    }
    
    public String getName()
    {
        return name;
    }
    public void setName( String name )
    {
        this.name = name;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription( String description )
    {
        this.description = description;
    }
    public String getxPath()
    {
        return xPath;
    }
    public void setxPath( String xPath )
    {
        this.xPath = xPath;
    }
}
