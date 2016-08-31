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
package org.xframium.driver;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.xframium.application.ApplicationRegistry;
import org.xframium.artifact.ArtifactType;
import org.xframium.content.ContentManager;
import org.xframium.device.DeviceManager;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.ng.AbstractSeleniumTest;
import org.xframium.page.PageManager;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.spi.PropertyProvider;
import org.xframium.spi.driver.ReportiumProvider;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.result.TestResultFactory;


public class XMLTestDriver extends AbstractSeleniumTest
{
    @Test( dataProvider = "deviceManager")
    public void testDriver( TestName testName ) throws Throwable
    {
        String deviceOs = getDevice().getOs();
        boolean returnValue = false;

        KeyWordTest test = KeyWordDriver.instance().getTest( testName.getTestName().split( "\\." )[ 0 ] );
        if ( test == null )
            throw new IllegalArgumentException( "The Test Name " + testName + " does not exist" );

        String contentKey = testName.getContentKey();

        if (( contentKey != null ) &&
            ( contentKey.length() > 0 ))
        {
            ContentManager.instance().setCurrentContentKey( contentKey );
        }
        else
        {
            ContentManager.instance().setCurrentContentKey( null );
        }
            
        try
        {	
            if ( test.getOs() != null && deviceOs != null )
            {
                if ( !deviceOs.toUpperCase().equals(  test.getOs().toUpperCase() ) )
                    throw new SkipException( "This test is not designed to work on a device with [" + deviceOs + "]  It needs [" + test.getOs() + "]" );
            }
    		
            if ( DeviceManager.instance().isDryRun() )
            {
                log.info( "This would have executed " +  testName.getTestName() );
                return;
            }
    		
            if ( ( (DeviceWebDriver) getWebDriver() ).getCloud().getProvider().equals( "PERFECTO" ) )
            {
                if( DataManager.instance().isArtifactEnabled( ArtifactType.REPORTIUM ) && getWebDriver() instanceof ReportiumProvider )
                {
                    //
                    // Reportium Integration
                    //
                    String[] tags = new String[] { "xFramium" };
                    if ( test.getTags() != null && test.getTags().length > 0 )
                        tags = test.getTags();
        
                    PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder().withProject(new Project(ApplicationRegistry.instance().getAUT().getName(), "1.0")).withContextTags( tags ).withWebDriver(getWebDriver() ).build();
                    ( (ReportiumProvider) getWebDriver() ).setReportiumClient( new ReportiumClientFactory().createPerfectoReportiumClient( perfectoExecutionContext ) );
        		    
                    if ( ( (ReportiumProvider) getWebDriver() ).getReportiumClient() != null )
                        ( (ReportiumProvider) getWebDriver() ).getReportiumClient().testStart( testName.getTestName(), new com.perfecto.reportium.test.TestContext() );
                }
            }
    		
            if ( test.getDescription() != null && !test.getDescription().isEmpty() && getWebDriver() instanceof PropertyProvider )
                ( (PropertyProvider) getWebDriver() ).setProperty( "testDescription", test.getDescription() );
    		
            returnValue = KeyWordDriver.instance().executeTest( testName.getTestName().split( "\\." )[ 0 ], getWebDriver() );
        }
        finally
        {
            if ( returnValue )
            {
                if( DataManager.instance().isArtifactEnabled( ArtifactType.REPORTIUM ) )
                {
                    if ( ( (ReportiumProvider) getWebDriver() ).getReportiumClient() != null )
                    {
                        if ( returnValue )
                            ( (ReportiumProvider) getWebDriver() ).getReportiumClient().testStop( TestResultFactory.createSuccess() );
                    }
                }

                return;
            }
            else
            {
                if( DataManager.instance().isArtifactEnabled( ArtifactType.REPORTIUM ) )
                {
                    if ( ( (ReportiumProvider) getWebDriver() ).getReportiumClient() != null )
                    {
                        ( (ReportiumProvider) getWebDriver() ).getReportiumClient().testStop( TestResultFactory.createFailure( PageManager.instance().getThrowable() != null ? PageManager.instance().getThrowable().getMessage() : "Test returned false", PageManager.instance().getThrowable() ) );
                    }
                }
            }
		
            if ( PageManager.instance().getThrowable() != null )
                throw PageManager.instance().getThrowable();

            Assert.assertTrue( returnValue );
        }
    } 

}
