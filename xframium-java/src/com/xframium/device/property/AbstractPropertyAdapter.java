package com.xframium.device.property;

import java.util.Properties;

public abstract class AbstractPropertyAdapter implements PropertyAdapter
{

    protected int getIntProperty( Properties configurationProperties, String keyName, int defaultValue )
    {
        String value = configurationProperties.getProperty( keyName );
        if ( value != null )
        {
            try
            {
                return Integer.parseInt( value );
            }
            catch( Exception e )
            {
                return defaultValue;
            }
        }
        return defaultValue;
    }

}
