package org.xframium.device.keepAlive;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.device.factory.DeviceWebDriver;

public abstract class AbstractDeviceKeepAlive implements DeviceKeepAlive
{
    protected Log log = LogFactory.getLog( DeviceKeepAlive.class );
    protected abstract boolean _keepAlive( DeviceWebDriver webDriver );
    private String implementationName = this.getClass().getName();
    private int pollTime;
    private int quietTime;
    private boolean active = true;
    
    @Override
    public boolean keepAlive( DeviceWebDriver webDriver )
    {
        if ( log.isInfoEnabled() )
            log.info( "Executing Device Keep Alive " + getClass().getSimpleName() );
        webDriver.setKeepAliveRunning( true );

        try
        {
            return _keepAlive( webDriver );
        }
        finally
        {
            webDriver.setKeepAliveRunning( false  );
        }
        
    }

    public int getPollTime()
    {
        return pollTime;
    }

    public int getQuietTime()
    {
        return quietTime;
    }

    public void setPollTime( int pollTime )
    {
        this.pollTime = pollTime;
    }

    public void setQuietTime( int quietTime )
    {
        this.quietTime = quietTime;
    }
    
    
    
    
}
