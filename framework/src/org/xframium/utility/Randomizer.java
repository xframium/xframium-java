package org.xframium.utility;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Randomizer
{
    private Random numberGenerator = new Random();
    private static final Randomizer singleton = new Randomizer();
    
    private String[] firstNames;
    private String[] lastNames;
    private String[] emailDomains;
    private String[] stateNames;
    private String[] cityNames;
    private String[] streetNames;
    
    private Randomizer()
    {
        
    }
    
    public static Randomizer instance()
    {
        return singleton;
    }
    
    public String randomWord( String wordList )
    {
        String[] words = wordList.split( "," );
        
        return randomWord( words );
    }
    
    public String randomWord( String[] wordList )
    {
        if ( wordList == null )
            return "";
        return wordList[ numberGenerator.nextInt( wordList.length ) ];
    }
    
    public int randomInt( int lowValue, int highValue )
    {
        return numberGenerator.nextInt( highValue - lowValue ) + lowValue;
    }
    
    public String randomNumber( double lowValue, double highValue, String format )
    {
        double range = highValue - lowValue;
        double newNumber = (numberGenerator.nextDouble() * range) + lowValue;
        
        DecimalFormat decimalFormat = new DecimalFormat( format );
        decimalFormat.setRoundingMode( RoundingMode.DOWN );
        
        return decimalFormat.format( newNumber );
    }
    
    public String randomText( String format )
    {
        byte[] buffer = format.getBytes();
        for ( int i=0; i<format.length(); i++ )
        {
            switch ( buffer[ i ] )
            {
                case 84:
                    buffer[ i ] = (byte) (numberGenerator.nextInt( 26 ) + 65);
                    break;
                    
                case 116:
                    buffer[ i ] = (byte) (numberGenerator.nextInt( 26 ) + 97);
                    break;
                    
                case 35:
                    buffer[ i ] = (byte) ( numberGenerator.nextInt( 10 ) + 48 );
                    break;
            }
        }
        
        return new String( buffer );
    }
    
    public String randomDate( Date fromDate, Date toDate, String format )
    {
        double range = toDate.getTime() - fromDate.getTime();
        double newNumber = (numberGenerator.nextDouble() * range) + fromDate.getTime();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat( format );
        return dateFormat.format( new Date( (long) newNumber ) );
    }
    
    public String randomFirstName()
    {
        if ( firstNames == null )
            firstNames = populateList( "firstNames.txt" );
        
        return randomWord( firstNames );
    }
    
    public String randomStreetName()
    {
        if ( streetNames == null )
            streetNames = populateList( "streetNames.txt" );
        
        return randomWord( streetNames );
    }
    
    public String randomState()
    {
        if ( stateNames == null )
            stateNames = populateList( "stateNames.txt" );
        
        return randomWord( stateNames );
    }
    
    public String randomCityName()
    {
        if ( cityNames == null )
            cityNames = populateList( "cityNames.txt" );
        
        return randomWord( cityNames );
    }
    
    public String randomLastName()
    {
        if ( lastNames == null )
            lastNames = populateList( "lastNames.txt" );
        
        return randomWord( lastNames );
    }
    
    public String randomDomain()
    {
        if ( emailDomains == null )
            emailDomains = populateList( "emailDomains.txt" );
        
        return randomWord( emailDomains );
    }
    
    public String randomEmailAddress( String domainName )
    {
        StringBuilder eA = new StringBuilder();
        eA.append( randomFirstName() ).append( "." ).append( randomLastName() ).append( "@" );
        
        if ( domainName == null )
            eA.append( randomDomain() );
        else
            eA.append( domainName );
        
        return eA.toString();
        
    }
    
    private String[] populateList( String listName )
    {
        List<String> stringList = new ArrayList<String>( 100 );
        InputStream inputStream = null;
        try
        {
            inputStream = getClass().getResourceAsStream( "/org/xframium/utility/data/" + listName );
            
            LineNumberReader lineReader = new LineNumberReader( new InputStreamReader( inputStream ) );
            String currentLine = null;
            while( ( currentLine = lineReader.readLine() ) != null )
            {
                stringList.add( currentLine );
            }
            
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
        
        return stringList.toArray( new String[ 0 ] );
    }
    
    public static void main( String[] args )
    {
        System.out.println( Randomizer.instance().randomWord( "allen,is,cool" ) );
        
        System.out.println( Randomizer.instance().randomNumber( 1, 100, "$##0.00 Dollars" ));
        System.out.println( Randomizer.instance().randomNumber( 1, 100, "$##0.00 Dollars" ));
        
        
        System.out.println( Randomizer.instance().randomText( "tT###tttTTTAllen" ) );
        
        System.out.println( Randomizer.instance().randomFirstName(  ) );
        System.out.println( Randomizer.instance().randomLastName(  ) );
        System.out.println( Randomizer.instance().randomDomain(  ) );
        System.out.println( Randomizer.instance().randomEmailAddress( null ) );
        System.out.println( Randomizer.instance().randomEmailAddress( "morelandlabs.com" ) );
        
        System.out.println( Randomizer.instance().randomCityName(  ) );
        System.out.println( Randomizer.instance().randomState(  ) );
        System.out.println( Randomizer.instance().randomStreetName(  ) );
        System.out.println( Randomizer.instance().randomDate( new Date(System.currentTimeMillis() / 2), new Date(System.currentTimeMillis()), "HH.mm.ss" ) );
    }
    
}
