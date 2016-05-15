package com.xframium.device.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import org.apache.commons.logging.LogFactory;
import com.xframium.device.DeviceManager;

public class ThreadedFileHandler extends Handler
{
    
    private static final String PERFECTO_NAMESPACE = "com.perfectoMobile";
    private static final String MORELAND_NAMESPACE = "com.morelandLabs";
    
    public ThreadedFileHandler()
    {
        LogFactory.getLog( "com.perfectoMobile" );
        LogFactory.getLog( "com.morelandLabs" );
    }
    
    public void configureHandler( Level baseLevel )
    {
        setLevel( baseLevel );
        setFormatter( new SimpleFormatter() );
        
        LogManager.getLogManager().getLogger( "" ).addHandler( this );
        LogManager.getLogManager().getLogger( "" ).setLevel( baseLevel );
        if ( LogManager.getLogManager().getLogger( PERFECTO_NAMESPACE ) != null )
        {
	        LogManager.getLogManager().getLogger( PERFECTO_NAMESPACE ).setLevel( baseLevel );
	        LogManager.getLogManager().getLogger( PERFECTO_NAMESPACE ).addHandler( this );
        }
        if ( LogManager.getLogManager().getLogger( MORELAND_NAMESPACE ) != null )
        {
	        LogManager.getLogManager().getLogger( MORELAND_NAMESPACE ).setLevel( baseLevel );
	        LogManager.getLogManager().getLogger( MORELAND_NAMESPACE ).addHandler( this );
        }
    }
    
    @Override
    public void publish( LogRecord record )
    {
        if ( isLoggable( record ) )
            DeviceManager.instance().addLog( getFormatter().format( record ) );
        
    }

    @Override
    public void flush()
    {
        
        
    }

    @Override
    public void close() throws SecurityException
    {
        
        
    }

}
