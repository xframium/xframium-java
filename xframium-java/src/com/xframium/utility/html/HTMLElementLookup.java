package com.xframium.utility.html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLElementLookup
{
    private static final Pattern FUNCTION = Pattern.compile( "(\\w+)\\(([^\\)]+)" );
    private List<HTMLFunction> functionPath = new ArrayList<HTMLFunction>( 10 );
    
    public HTMLElementLookup( String elementDefinition )
    {
        Matcher functionMatcher = FUNCTION.matcher( elementDefinition );
        while( functionMatcher.find() )
        {
            functionPath.add( new HTMLFunction( functionMatcher.group( 1 ), functionMatcher.group( 2 ) ) );
        }
    }
    
    public List<HTMLFunction> getFunctionPaths()
    {
        return functionPath;
    }

    public void setFunctionPaths( List<HTMLFunction> functionPath )
    {
        this.functionPath = functionPath;
    }

    public String toString()
    {
        return functionPath.toString();
    }
    
    public String toXPath()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for ( HTMLFunction htmlFunction : functionPath )
        {
            stringBuilder.append( htmlFunction.toXPath( false ) );
        }
        
        return stringBuilder.toString();
    }
    
}
