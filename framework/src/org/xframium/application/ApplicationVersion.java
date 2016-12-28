package org.xframium.application;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ApplicationVersion
{
    private Map<String,Double[]> versionMap = new HashMap<String,Double[]>( 20 );
    private static Pattern APP_VERSION = Pattern.compile( "([^\\(]*)\\(([^\\)]*)\\),*" );
    private static Log log = LogFactory.getLog( ApplicationVersion.class );
    public ApplicationVersion( String versionDescriptor )
    {
        
        try
        {
            if ( log.isDebugEnabled() )
                log.debug( "Processing Application Version [" + versionDescriptor + "]" );
            Matcher versionMatcher = APP_VERSION.matcher( versionDescriptor );
            
            while ( versionMatcher.find() )
            {
                if ( log.isDebugEnabled() )
                    log.debug( "Processing Application Version Entry [" + versionMatcher.group() + "]" );
                String appName = versionMatcher.group( 1 ).trim();
                String appVersion = versionMatcher.group( 2 ).trim();
                
                Double[] doubleVersion = new Double[ 2 ];
                if ( appVersion.equals( "*" ) )
                {
                    doubleVersion[ 0 ] = Double.MIN_VALUE;
                    doubleVersion[ 1 ] = Double.MAX_VALUE;
                }
                else if ( appVersion.contains( "-" ) )
                {
                    String[] splitValue = appVersion.split( "-" );
                    
                    doubleVersion[ 0 ] = Double.parseDouble( splitValue[ 0 ].trim() );
                    
                    if ( splitValue[1].trim().equals( "*") )
                        doubleVersion[ 1 ] = Double.MAX_VALUE;
                    else
                        doubleVersion[ 1 ] = Double.parseDouble( splitValue[1].trim() );
                }
                else
                {
                    doubleVersion[ 0 ] = Double.parseDouble( appVersion );
                    doubleVersion[ 1 ] = doubleVersion[ 0 ];
                }
                
                versionMap.put( appName, doubleVersion );
            }
        }
        catch( Exception e )
        {
            log.error( "Error parsing application version string [" + versionDescriptor + "]", e );
        }
    }
    
    public boolean isVersion( ApplicationDescriptor applicationDescriptor )
    {
        if ( applicationDescriptor.getVersion() == 0 )
            return true;
        else
        {
            Double[] versionRange = versionMap.get( applicationDescriptor.getName() );
            if ( versionRange == null )
                return false;
            else
                return applicationDescriptor.getVersion() >= versionRange[ 0 ] && applicationDescriptor.getVersion() <= versionRange[ 1 ];
                    
        }
    }
    
    public static void main( String[] args )
    {
        
        ApplicationDescriptor x = new ApplicationDescriptor( "FordPass", "", "", "", "", "", "", null, 1.0 );
        
        
        System.out.println( new ApplicationVersion( "FordPass(1.0-1.6 ),LincolnWay(*),FordPass-EU(1.0-*)" ).isVersion( x ) );
        System.out.println( new ApplicationVersion( "FordPass(1.3-1.6 ),LincolnWay(*),FordPass-EU(1.0-*)" ).isVersion( x ) );
        x = new ApplicationDescriptor( "LincolnWay", "", "", "", "", "", "", null, 36.0 );
        System.out.println( new ApplicationVersion( "FordPass(1.3-1.6 ),LincolnWay(*),FordPass-EU(1.0-*)" ).isVersion( x ) );
        x = new ApplicationDescriptor( "FordPass-EU", "", "", "", "", "", "", null, 0.9 );
        System.out.println( new ApplicationVersion( "FordPass(1.3-1.6 ),LincolnWay(*),FordPass-EU(1.0-*)" ).isVersion( x ) );
        x = new ApplicationDescriptor( "FordPass-EU", "", "", "", "", "", "", null, 36.3 );
        System.out.println( new ApplicationVersion( "FordPass(1.3-1.6 ),LincolnWay(*),FordPass-EU(1.0-*)" ).isVersion( x ) );
        
    }
}
