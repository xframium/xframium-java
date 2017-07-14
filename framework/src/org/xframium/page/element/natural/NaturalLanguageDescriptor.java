package org.xframium.page.element.natural;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NaturalLanguageDescriptor
{
    public enum NLPosition
    {
        FIRST,SECOND,THIRD,FOURTH,FIFTH,SIXTH,SEVENTH,EIGHTH,NINTH,TENTH,LAST,ONLY;
    }
    
    public enum NLType
    {
        TEXTBOX,CHECKBOX,BUTTON,OPTION,DROPDOWN,LABEL,PASSWORD,ELEMENT
    }
    
    public enum NLQuery
    {
        LABELED, NAMED
    }
    
    private NLPosition position;
    private NLType type;
    private NLQuery query;
    private String queryData;
    private Map<String,String> attributeMap = new HashMap<String,String>( 10 );
    
    public NLPosition getPosition()
    {
        return position;
    }


    public void setPosition( NLPosition position )
    {
        this.position = position;
    }


    public NLType getType()
    {
        return type;
    }


    public void setType( NLType type )
    {
        this.type = type;
    }


    public NLQuery getQuery()
    {
        return query;
    }


    public void setQuery( NLQuery query )
    {
        this.query = query;
    }


    public String getQueryData()
    {
        return queryData;
    }


    public void setQueryData( String queryData )
    {
        this.queryData = queryData;
    }


    public Map<String, String> getAttributeMap()
    {
        return attributeMap;
    }


    public void setAttributeMap( Map<String, String> attributeMap )
    {
        this.attributeMap = attributeMap;
    }


    public NaturalLanguageDescriptor( String queryString )
    {
        //
        // Required position
        //
        int startPosition = 0;
        int endPosition = queryString.indexOf( " " );
        position = NLPosition.valueOf( queryString.substring( 0, endPosition ) );
        
        //
        // Required type
        //
        
        startPosition = endPosition + 1;
        endPosition = queryString.indexOf( " ", startPosition );
        if ( endPosition == -1 )
            endPosition = queryString.length();
        type = NLType.valueOf( queryString.substring( startPosition, endPosition ) );
        
        //
        // Optional query 
        //
        startPosition = endPosition + 1;
        endPosition = queryString.indexOf( " ", startPosition );
        if ( endPosition != -1 )
        {
            String nextItem = queryString.substring( startPosition, endPosition );
            
            if ( nextItem.equals( "WITH" ) )
            {
                //
                // Parse the attributes
                //
                startPosition = queryString.indexOf( "'", endPosition ) + 1;
                endPosition = queryString.indexOf( "'", startPosition + 1 );
                //
                // Parse the attributes
                //
                parseAttributes( queryString.substring( startPosition, endPosition ) );
            }
            else
            {
                query = NLQuery.valueOf( nextItem );
                startPosition = queryString.indexOf( "'", endPosition ) + 1;
                endPosition = queryString.indexOf( "'", startPosition + 1 );
                queryData = queryString.substring( startPosition, endPosition );
                
                
                startPosition = endPosition + 1;
                endPosition = queryString.indexOf( " ", startPosition + 1 );
                if ( endPosition != -1 )
                {
                    nextItem = queryString.substring( startPosition + 1, endPosition );
                    if ( nextItem.equals( "WITH" ) )
                    {
                        startPosition = queryString.indexOf( "'", endPosition ) + 1;
                        endPosition = queryString.indexOf( "'", startPosition + 1 );
                        //
                        // Parse the attributes
                        //
                        parseAttributes( queryString.substring( startPosition, endPosition ) );
                    }
                }
                
            }
        }
    }
    
    private static Pattern ATTR_PATTERN = Pattern.compile( "(\\w*=\\w*)" );
    private void parseAttributes( String attributeString )
    {
        Matcher m = ATTR_PATTERN.matcher( attributeString );
        
        while( m.find() )
        {
            String[] values = m.group( 0 ).split(  "=" );
            attributeMap.put( values[ 0 ],  values[ 1 ] );
        }
        
    }
    
    
    
    
    
    
}


