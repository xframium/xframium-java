package com.xframium.utility.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLFunction
{
    private static final String HTML_TAG = "html tag";
    private String name;
    private Map<String,HTMLParameter> parameterMap = new HashMap<String,HTMLParameter>( 10 );
    private List<HTMLParameter> parameterList = new ArrayList<HTMLParameter>( 10 );
    private static final Pattern PARAMETERS = Pattern.compile( "(?:[\"']([^:]*):=([^\"']*)[\"'])" );
    
    public HTMLFunction( String name, String parameterDefinition )
    {
        this.name = name;

        Matcher paramMatcher = PARAMETERS.matcher( parameterDefinition );
        
        while( paramMatcher.find() )
        {
            HTMLParameter currentParameter = new HTMLParameter( paramMatcher.group( 1 ), paramMatcher.group( 2 ) );
            parameterList.add( currentParameter );
            parameterMap.put( currentParameter.getName(), currentParameter );
        }
    }

    public String getName()
    {
        return name;
    }
    
    public String getTagName()
    {
        if ( parameterMap.containsKey( HTML_TAG ) )
            return parameterMap.get( HTML_TAG ).getValue();
        else
            return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public List<HTMLParameter> getParameterList()
    {
        return parameterList;
    }

    public void setParameterList( List<HTMLParameter> parameterList )
    {
        this.parameterList = parameterList;
    }

    @Override
    public String toString()
    {
        return "HTMLFunction [name=" + name + ", parameterList=" + parameterList + "]";
    }
    
    public String toXPath( boolean relativePath )
    {
        StringBuilder stringBuilder = new StringBuilder();
        if ( relativePath )
            stringBuilder.append( "." );
        stringBuilder.append( "//" ).append( getTagName() );
        for ( HTMLParameter p : parameterList )
        {
            if ( !p.getName().equals( HTML_TAG ) )
                stringBuilder.append( p.toXPath() );
        }
        
        return stringBuilder.toString();
    }
    
    
    
    
}

