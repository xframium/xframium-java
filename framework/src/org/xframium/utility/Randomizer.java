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
    	StringBuilder returnValue = new StringBuilder();
        byte[] buffer = format.getBytes();
        int bufferPosition = 0;
        
        boolean isEscape = false;
        
        for ( int i=0; i<format.length(); i++ )
        {
        	if ( isEscape )
        	{
        		returnValue.append( (char) buffer[ i ] );
        		isEscape = false;
        		continue;
        	}
        	
            switch ( buffer[ i ] )
            {
                case 84:
                    returnValue.append( (char) (numberGenerator.nextInt( 26 ) + 65) );
                    break;
                    
                case 116:
                	returnValue.append( (char) (numberGenerator.nextInt( 26 ) + 97) );
                    break;
                    
                case 35:
                	returnValue.append( (char) (numberGenerator.nextInt( 26 ) + 48) );
                    break;
                    
                case 92:
                	isEscape = true;
                	break;
                	
                default:
                	returnValue.append( (char) buffer[ i ] );
                	break;
            }
        }
        
        return returnValue.toString();
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
        
        System.out.println( Randomizer.instance().randomText( "\\T\\t\\#" ) );
    }
    
}
