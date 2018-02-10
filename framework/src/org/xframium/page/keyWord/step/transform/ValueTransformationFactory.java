package org.xframium.page.keyWord.step.transform;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueTransformationFactory
{
    private Pattern transformationPattern = Pattern.compile( "(\\w*)\\{([^\\}]*)\\}" );
    private static ValueTransformationFactory singleton = new ValueTransformationFactory();
    private Map<String,ValueTransformation> vMap = new HashMap<String,ValueTransformation>( 20 );
    
    private ValueTransformationFactory()
    {
        vMap.put( "default", new DefaultTransformation() );
        vMap.put( "secure", new SecureTransformation() );
    }

    public static ValueTransformationFactory instance()
    {
        return singleton;
    }
    
    public String transformValue( String value )
    {
        Matcher m = transformationPattern.matcher( value );
        
        if ( m.matches() )
            return vMap.get( m.group( 1 ) ).transformValue( m.group( 2 ) );
        else
            return value;
    }
    
    public String wrapValue( String value, String transformMethod )
    {
        return transformMethod + "{" + vMap.get( transformMethod ).wrapValue(value) + "}";
    }
    
    public static void main( String[] args ) throws Exception
    {
        if ( args.length != 2 )
        {
            System.err.println( "[unwrap/wrap method] [value]" );
            System.exit( -1 );
        }
        
        if ( !args[ 0 ].equals( "unwrap" ) )
            System.out.println( instance().wrapValue( args[ 1 ],  args[ 0 ] ) );
        else if ( args[ 0 ].equals( "unwrap" ) )
            System.out.println( instance().transformValue( args[ 1 ] ) );
        else
        {
            System.err.println( "[unwrap/wrap method] [value]" );
            System.exit( -1 );
        }
    }
}
