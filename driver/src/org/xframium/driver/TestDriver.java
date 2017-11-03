/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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
package org.xframium.driver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.xframium.Initializable;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.reporting.ExecutionContext;
public class TestDriver
{
    private List<SuiteListener> listenerList = new ArrayList<SuiteListener>(3);
    
    public static String xFID = null;
    
    public void addSuiteListener( SuiteListener suiteListener )
    {
        listenerList.add( suiteListener );
    }
    
    public void execute( File configFile, Map<String,String> customConfig )
    {
        
        try
        {
            ConfigurationReader configReader = null;
            if ( configFile.getName().toLowerCase().endsWith( ".txt" ) )
            {
                configReader = new TXTConfigurationReader();
            }
            else if ( configFile.getName().toLowerCase().endsWith( ".xml" ) )
            {
                configReader = new XMLConfigurationReader();
            }
            
            for ( SuiteListener l : listenerList )
                l.beforeSuite( null, configFile );
            
            
            
            
            if ( customConfig == null )
                customConfig = new HashMap<String,String>(10);
            
            if ( xFID == null )
                xFID = UUID.randomUUID().toString();
            Initializable.xFID.set( xFID ); 
            customConfig.put( "xF-ID", Initializable.xFID.get() );
            
            configReader.readConfiguration( configFile, true, customConfig );
            
            for ( SuiteListener l : listenerList )
                l.afterSuite( configReader.getSuiteName(), configFile, ExecutionContext.instance( xFID ).getReportFolder( xFID ) );
            
            
            
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            if ( CloudRegistry.instance( xFID ).isEmbeddedGrid() )
                CloudRegistry.instance( xFID ).shutdown();
        }
    }
    
    public static void main( String[] args )
    {
        if ( args.length != 1 )
        {
            System.err.println( "Usage: TestDriver [configurationFile]" );
            System.exit( -1 );
        }

        File configFile = new File( args[0] );
        if ( !configFile.exists() )
        {
            System.err.println( "[" + configFile.getAbsolutePath() + "] could not be located" );
            System.exit( -1 );
        }
        
        
        
        new TestDriver().execute( configFile, null );
        
        
    }

    
    
    
}
