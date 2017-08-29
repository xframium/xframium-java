package org.xframium.device.keepAlive;

import org.xframium.device.factory.DeviceWebDriver;

public interface DeviceKeepAlive
{
    public boolean keepAlive( DeviceWebDriver webDriver );
    public int getPollTime();
    public int getQuietTime();
    public void setPollTime( int pollTime );
    public void setQuietTime( int quietTime );
}
