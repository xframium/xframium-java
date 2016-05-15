package com.xframium.device.interrupt;

import java.util.List;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xframium.device.factory.DeviceWebDriver;

public class DeviceInterruptThread implements Runnable
{
    private static final int QUIET_PERIOD = 60;
    private static final int RANDOM_PERIOD = 300;
    private List<DeviceInterrupt> interruptList;
    private static Random numberGenerator = new Random( System.currentTimeMillis() );
    private boolean runFlag = true;
    private DeviceWebDriver webDriver;
    private Log log = LogFactory.getLog( DeviceInterruptThread.class );
    
    public DeviceInterruptThread( List<DeviceInterrupt> interruptList, DeviceWebDriver webDriver )
    {
        this.interruptList = interruptList;
        this.webDriver = webDriver;
    }

    @Override
    public void run()
    {
        while ( runFlag )
        {
            long waitTime = (long)( numberGenerator.nextDouble() * RANDOM_PERIOD + QUIET_PERIOD ) * 1000;
            long startTime = System.currentTimeMillis();
            
            while( runFlag && ( System.currentTimeMillis() - startTime ) < waitTime )
            {
                try
                {
                    Thread.sleep( 1000 );
                }
                catch( Exception e )
                {
                }
            }
            
            if ( !runFlag )
                break;
            
            int interruptIndex = (int) ( numberGenerator.nextDouble() * interruptList.size() );
            
            interruptList.get( interruptIndex ).interruptDevice( webDriver );
            
        }
    }
    
    public void stop()
    {
        runFlag = false;
    }

}
