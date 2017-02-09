package org.xframium.page.element.spi.set;

import java.util.HashMap;
import java.util.Map;

public class SetMethodFactory
{
    private static final SetMethodFactory singleton = new SetMethodFactory();
    
    private SetMethodFactory()
    {
        registerSetMethod( new PickerSetMethod(), "uiapickerwheel" );
        registerSetMethod( new SelectSetMethod(), "select" );
        registerSetMethod( new ULSetMethod(), "ul" );
    }
    
    public static SetMethodFactory instance()
    {
        return singleton;
    }
    
    
    private Map<String,SetMethod> methodMap = new HashMap<String,SetMethod>( 20 );
    private SetMethod defaultSetMethod = new DefaultSetMethod();
    
    public SetMethod createSetMethod( String tagName )
    {
        SetMethod setMethod = methodMap.get( tagName.toLowerCase() );
        
        if ( setMethod == null )
            return defaultSetMethod;
        else
            return setMethod;
    }

    public void registerSetMethod( SetMethod setMethod, String tagName )
    {
        methodMap.put( tagName.toLowerCase(), setMethod );
    }
}
