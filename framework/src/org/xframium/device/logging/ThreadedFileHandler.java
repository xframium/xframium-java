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
package org.xframium.device.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import org.apache.commons.logging.LogFactory;
import org.xframium.device.DeviceManager;

public class ThreadedFileHandler extends Handler
{
    
    private static final String X_NAMESPACE = "org.xframium";
    private String xFID;
    public ThreadedFileHandler( String xFID )
    {
        LogFactory.getLog( X_NAMESPACE );
        this.xFID = xFID;
    }
    
    public void configureHandler( Level baseLevel )
    {
        
        
        Handler[] hList = LogManager.getLogManager().getLogger( "" ).getHandlers();
        for ( Handler h : hList )
        {
            if ( h instanceof ThreadedFileHandler )
                return;
        }
        
        setLevel( baseLevel );
        setFormatter( new SimpleFormatter() );
        
        LogManager.getLogManager().getLogger( "" ).addHandler( this );
    }
    
    @Override
    public void publish( LogRecord record )
    {
        if ( isLoggable( record ) )
            DeviceManager.instance( xFID ).addLog( getFormatter().format( record ) );
        
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
