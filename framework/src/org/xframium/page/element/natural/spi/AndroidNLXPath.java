package org.xframium.page.element.natural.spi;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.element.natural.NaturalLanguageDescriptor;

public class AndroidNLXPath extends AbstractNLXPath
{

    
    
    @Override
    protected String generateXPath( NaturalLanguageDescriptor nL )
    {
        List<StringBuilder> builderList = new ArrayList<StringBuilder>();
        switch( nL.getType() )
        {
            case BUTTON:
                builderList.add( new StringBuilder( "//android.widget.Button" ) );
                break;
                
            case CHECKBOX:
                builderList.add( new StringBuilder( "//android.widget.CheckBox" ) );
                break;
                
            case SWITCH:
                builderList.add( new StringBuilder( "//android.widget.Switch" ) );
                break;
                
            case DROPDOWN:
                builderList.add( new StringBuilder( "//select" ) );
                builderList.add( new StringBuilder( "//ul[@class='dropdown-menu']" ) );
                break;
                
            case LABEL:
                builderList.add( new StringBuilder( "//android.widget.TextView" ) );
                break;
                
            case OPTION:
                builderList.add( new StringBuilder( "//android.widget.CheckedTextView" ) );
                break;
                
            case TEXTBOX:
                builderList.add( new StringBuilder( "//android.widget.EditText[@password='false']" ) );
                break;
                
            case PASSWORD:
                builderList.add( new StringBuilder( "//android.widget.EditText[@password='true']" ) );
                break;
                
            case ELEMENT:
                builderList.add( new StringBuilder( "//*" ) );
                break;
                
        }
        
        if ( nL.getQuery() != null )
        {
            switch( nL.getQuery() )
            {
                case LABELED:
                case NAMED:
                    for ( StringBuilder s : builderList )
                    {
                        s.append( "[@resource-id='" ).append( nL.getQueryData() ).append( "' or @text='" ).append( nL.getQueryData() ).append( "' or contains( text(), '" ).append( nL.getQueryData() ).append( "')]" );
                    }
            }
        }
        
        if ( nL.getAttributeMap() != null && !nL.getAttributeMap().isEmpty() )
        {
            for ( StringBuilder s : builderList )
            {
                for ( String key : nL.getAttributeMap().keySet() )
                {
                    s.append( "[@" ).append( key  ).append( "='" ).append( nL.getAttributeMap().get( key ) ).append( "']" );
                }
            }
        }
            
        StringBuilder returnValue = new StringBuilder();
        for ( StringBuilder s : builderList )
        {
            returnValue.append( s.toString() ).append( "|" );
        }
        
        returnValue.deleteCharAt( returnValue.length() - 1 );
        
        // TODO Auto-generated method stub
        return returnValue.toString();
    }
    
    
    public static void main( String[] args )
    {
        System.out.println( new AndroidNLXPath().generateXPath( new NaturalLanguageDescriptor( "FIRST TEXTBOX LABELED 'DDD' WITH 'X=1;Y=2'" ) ) );
        System.out.println( new AndroidNLXPath().generateXPath( new NaturalLanguageDescriptor( "FIRST TEXTBOX LABELED 'DDD'" ) ));
        System.out.println( new AndroidNLXPath().generateXPath( new NaturalLanguageDescriptor( "FIRST BUTTON LABELED 'DDD'" ) ));
        System.out.println( new AndroidNLXPath().generateXPath( new NaturalLanguageDescriptor( "FIRST TEXTBOX WITH 'X=1;Y=2'" ) ));
        System.out.println( new AndroidNLXPath().generateXPath( new NaturalLanguageDescriptor( "FIRST TEXTBOX" ) ));
    }

}
