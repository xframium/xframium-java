package org.xframium.page.element.natural.spi;

import java.util.ArrayList;
import java.util.List;
import org.xframium.page.element.natural.NaturalLanguageDescriptor;

public class IOSNLXPath extends AbstractNLXPath
{

    @Override
    protected String generateXPath( NaturalLanguageDescriptor nL )
    {
        List<StringBuilder> builderList = new ArrayList<StringBuilder>();
        switch( nL.getType() )
        {
            case BUTTON:
                builderList.add( new StringBuilder( "//UIAButton" ) );
                builderList.add( new StringBuilder( "//XCUIElementTypeButton" ) );
                break;
                
            case CHECKBOX:
                builderList.add( new StringBuilder( "//UIASwitch" ) );
                builderList.add( new StringBuilder( "//XCUIElementTypeSwitch" ) );
                break;
                
            case SWITCH:
                builderList.add( new StringBuilder( "//UIASwitch" ) );
                builderList.add( new StringBuilder( "//XCUIElementTypeSwitch" ) );
                break;
                
            case DROPDOWN:
                builderList.add( new StringBuilder( "//UIAPicker" ) );
                builderList.add( new StringBuilder( "//XCUIElementTypePicker" ) );
                break;
                
            case LABEL:
                builderList.add( new StringBuilder( "//UIATextView" ) );
                builderList.add( new StringBuilder( "//XCUIElementTypeTextView" ) );
                break;
                
            case OPTION:
                builderList.add( new StringBuilder( "//android.widget.CheckedTextView" ) );
                break;
                
            case TEXTBOX:
                builderList.add( new StringBuilder( "//UIATextField" ) );
                builderList.add( new StringBuilder( "//XCUIElementTypeTextField" ) );
                break;
                
            case PASSWORD:
                builderList.add( new StringBuilder( "//UIASecureTextField" ) );
                builderList.add( new StringBuilder( "//XCUIElementTypeSecureTextField" ) );
                break;
                
            case ELEMENT:
                builderList.add( new StringBuilder( "//UIAElement" ) );
                builderList.add( new StringBuilder( "//XCUIElementTypeAny" ) );
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
                        s.append( "[@label='" ).append( nL.getQueryData() ).append( "' or @name='" ).append( nL.getQueryData() ).append( "' or contains( text(), '" ).append( nL.getQueryData() ).append( "')]" );
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

}
