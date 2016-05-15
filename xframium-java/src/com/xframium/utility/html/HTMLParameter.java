package com.xframium.utility.html;

public class HTMLParameter
{
    private String name;
    private String value;
    private String functionName = null;

    @Override
    public String toString()
    {
        return "HTMLParameter [name=" + name + ", value=" + value + "]";
    }

    public HTMLParameter( String name, String value )
    {
        this.name = name;
        this.value = value;
        
        if ( value.contains( ".*" ) )
        {
            if ( value.startsWith( ".*" ) && value.endsWith( ".*" ) )
                functionName = "contains";
            else if ( value.startsWith( ".*" ) )
                functionName = "starts-with";
            else if ( value.endsWith( ".*" ) )
                functionName = "ends-with";
            
            this.value = value.replace( ".*", "" );
        }
        
    }
    
    public String getName()
    {
        return name;
    }
    public void setName( String name )
    {
        this.name = name;
    }
    public String getValue()
    {
        return value;
    }
    public void setValue( String value )
    {
        this.value = value;
    }
    
    public String toXPath()
    {
        if ( name.equals( "innerText" ) )
        {
            if ( functionName != null )
                return "[" + functionName + "( text(), '" + value + "')]";
            else
                return "[text()='" + value + "']";
        }
        else
        {
            if ( functionName != null )
                return "[" + functionName + "( @" + name + ", '" + value + "' )]";
            else
                return "[@" + name + "='" + value + "']";
        }
    }
    
    
}
