package org.xframium.device.ng;

import org.xframium.device.ConnectedDevice;
import org.xframium.spi.Device;

public class TestPackage
{
    private TestName testName;
    private Device device;
    private String runKey;
    private ConnectedDevice connectedDevice;
    
    public ConnectedDevice getConnectedDevice()
    {
        return connectedDevice;
    }

    public void setConnectedDevice( ConnectedDevice connectedDevice )
    {
        this.connectedDevice = connectedDevice;
    }

    public TestPackage( TestName testName, Device device, String runKey )
    {
        super();
        this.testName = testName;
        this.device = device;
        this.runKey = runKey;
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
