package org.xframium.device.ng;

import org.xframium.device.ConnectedDevice;
import org.xframium.spi.Device;

public class TestPackage
{
    private TestName testName;
    private Device device;
    private String runKey;
    private ConnectedDevice connectedDevice;
    private String xFID;
    private Device populatedDevice;
    
    public ConnectedDevice getConnectedDevice()
    {
        return connectedDevice;
    }

    public void setConnectedDevice( ConnectedDevice connectedDevice )
    {
        this.connectedDevice = connectedDevice;
    }

    public TestPackage( TestName testName, Device device, String runKey, String xFID )
    {
        super();
        this.testName = testName;
        this.device = device;
        this.runKey = runKey;
        this.xFID = xFID;
    }
    
    
    
    public Device getPopulatedDevice()
    {
        if ( populatedDevice != null )
            return populatedDevice;
        else
            return device;
    }

    public void setPopulatedDevice( Device populatedDevice )
    {
        this.populatedDevice = populatedDevice;
    }

    public String getxFID()
    {
        return xFID;
    }

    public String getRunKey()
    {
        return runKey;
    }

    public void setRunKey( String runKey )
    {
        this.runKey = runKey;
    }

    public TestName getTestName()
    {
        return testName;
    }
    public void setTestName( TestName testName )
    {
        this.testName = testName;
    }
    public Device getDevice()
    {
        return device;
    }
    public void setDevice( Device device )
    {
        this.device = device;
    }
    
    
}
