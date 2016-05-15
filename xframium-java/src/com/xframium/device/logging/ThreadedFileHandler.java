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
    
    private static final String X_NAMESPACE = "org.xframium";
    
    public ThreadedFileHandler()
    {
        LogFactory.getLog( X_NAMESPACE );
    }
    
    public void configureHandler( Level baseLevel )
    {
        setLevel( baseLevel );
        setFormatter( new SimpleFormatter() );
        
        LogManager.getLogManager().getLogger( "" ).addHandler( this );
        LogManager.getLogManager().getLogger( "" ).setLevel( baseLevel );
        if ( LogManager.getLogManager().getLogger( X_NAMESPACE ) != null )
        {
	        LogManager.getLogManager().getLogger( X_NAMESPACE ).setLevel( baseLevel );
	        LogManager.getLogManager().getLogger( X_NAMESPACE ).addHandler( this );
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
