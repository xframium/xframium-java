/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
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
