package org.xframium.exception;

import org.xframium.spi.Device;

public class DeviceAcquisitionException extends XFramiumException
{
    /**
     * 
     */
    private static final long serialVersionUID = 1532390382770613035L;
    private Device device;
    public DeviceAcquisitionException( Device device )
    {
        super( ExceptionType.CLOUD );
        this.device = device;
    }

    @Override
    public String toString()
    {
        return "Could not acquire a " + device.getKey();
    }

    @Override
    public String getMessage()
    {
        return "Could not acquire a "  + device.getKey();
    }

}
