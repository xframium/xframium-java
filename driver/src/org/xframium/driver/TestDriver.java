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

import org.openqa.selenium.WebDriver;
import org.xframium.Initializable;
import org.xframium.container.SuiteContainer;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.listener.KeyWordListener;
import org.xframium.reporting.ExecutionContext;
import org.xframium.reporting.ExecutionContextTest;
public class TestDriver implements KeyWordListener
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
            KeyWordDriver.instance(xFID).addStepListener( this );
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
            {
                CloudRegistry.instance( xFID ).shutdown();
                  
            }
        }
    }
    
    
    
    public static void main( String[] args )
    {
        if ( args.length != 1 )
        {
            System.err.println( "Usage: TestDriver [configurationFile]" );
            System.exit( 1 );
        }

        File configFile = new File( args[0] );
        if ( !configFile.exists() )
        {
            System.err.println( "[" + configFile.getAbsolutePath() + "] could not be located" );
            System.exit( 1 );
        }
        
        TestDriver tD = new TestDriver();
        tD.execute( configFile, null );
        
        System.out.println( "Total Tests: " + ( tD.getPassCount() + tD.getFailCount() ) );
        System.out.println( "Passed Tests: " + ( tD.getPassCount() ) );
        System.out.println( "Failed Tests: " + ( tD.getFailCount() ) );
        
        if ( !Boolean.getBoolean( "XF_DONT_EXIT" ) )
        {
          System.exit( tD.getFailCount() > 0 ? 1 : 0 );
        }
        
    }

    private int passCount;
    private int failCount;
    
    
    
    public int getPassCount()
    {
      return passCount;
    }

    public void setPassCount( int passCount )
    {
      this.passCount = passCount;
    }

    public int getFailCount()
    {
      return failCount;
    }

    public void setFailCount( int failCount )
    {
      this.failCount = failCount;
    }

    @Override
    public boolean beforeStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
    {
      // TODO Auto-generated method stub
      return true;
    }

    @Override
    public void afterStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, StepStatus stepStatus, SuiteContainer sC, ExecutionContextTest eC )
    {
      // TODO Auto-generated method stub
      
    }

    @Override
    public boolean beforeTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
    {
      return true;
    }

    @Override
    public void afterTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC )
    {
      if ( stepPass )
        passCount++;
      else
        failCount++;
      
    }

    @Override
    public void afterArtifacts( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC )
    {
      // TODO Auto-generated method stub
      
    }

    
    
    
}
