package org.xframium.page.keyWord.step.transform;

import java.io.ByteArrayInputStream;

import com.xframium.serialization.SerializationManager;

public class SecureTransformation implements ValueTransformation
{
    @Override
    public String transformValue(String value)
    {
        try
        {
            String fullValue = new StringBuilder( value ).reverse().toString();
            String version = fullValue.substring( 0, 2 );
            String actualValue = fullValue.substring( 2 );
            
            if ( version.equals( "01" ) )
            {
                ByteArrayInputStream inputStream = new ByteArrayInputStream( actualValue.getBytes() );
                return (String)SerializationManager.instance().readData( inputStream );
            }
            else
            {
                return value;
            }
        }
        catch( Exception e )
        {
            return value;
        }
    }
    
    @Override
    public String wrapValue(String value)
    {
        return new StringBuilder( new String( SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.EXTENDED_SERIALIZATION ), value, 9 ) ).replace( "\r\n", "") ).insert( 0, "01" ).reverse().toString();
    }
}
