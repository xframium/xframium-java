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

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xframium.application.ApplicationRegistry;
import org.xframium.artifact.ArtifactManager;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataManager;
import org.xframium.device.ng.AbstractSeleniumTest;
import org.xframium.device.ng.TestContainer;
import org.xframium.device.ng.TestName;
import org.xframium.device.ng.TestPackage;
import org.xframium.exception.DeviceException;
import org.xframium.exception.FilteredException;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.integrations.sauceLabs.rest.SauceREST;
import org.xframium.page.PageManager;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.reporting.ExecutionContextTest.TestStatus;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.result.TestResultFactory;

public class XMLTestDriver extends AbstractSeleniumTest
{

    @Test ( dataProvider = "deviceManager")
    public void testDriver( TestContainer testContainer ) throws Throwable
    {
        TestPackage testPackage = testPackageContainer.get();
        testFlow.info( Thread.currentThread().getName() + ": Performing initialization checked for " + testPackage.getRunKey() );
        
        TestName testName = testPackage.getTestName();
        
        KeyWordTest test = KeyWordDriver.instance().getTest( testName.getRawName() );
        if ( test == null )
            throw new ScriptConfigurationException( "The Test Name " + testName.getRawName() + " does not exist" );
        
        ExecutionContextTest executionContextTest = new ExecutionContextTest();
        
        testPackage.getConnectedDevice().getWebDriver().setExecutionContext( executionContextTest );
        
        CloudDescriptor cD = CloudRegistry.instance().getCloud();
        if ( testPackage.getDevice().getCloud() != null && !testPackage.getDevice().getCloud().trim().isEmpty() )
            cD = CloudRegistry.instance().getCloud( testPackage.getDevice().getCloud() );
        
        if ( !testPackage.getConnectedDevice().getWebDriver().isConnected() )
        {
            executionContextTest.setTest( test );
            executionContextTest.setDevice( testPackage.getDevice() );
            executionContextTest.setCloud( cD );
            executionContextTest.completeTest( TestStatus.FAILED, new DeviceException( "Could not connect to " + testPackage.getDevice() ) );
            testName.setTest( executionContextTest );
            testFlow.error( Thread.currentThread().getName() + ": Device not connected for " + testPackage.getRunKey() );
            throw new DeviceException( "Could not connect to " + testPackage.getDevice() );
        }
        
        String deviceOs = testPackage.getDevice().getOs();
        String[] deviceTags = testPackage.getDevice().getTagNames();
        
        boolean returnValue = false;

        try
        {

            if ( test.getOs() != null && deviceOs != null )
            {
                String[] osArray = test.getOs().split( "," );
                boolean osFound = false;
                for ( String localOs : osArray )
                {
                    if ( deviceOs.toUpperCase().contains( localOs.trim().toUpperCase() ) )
                    {
                        osFound = true;
                        break;
                    }
                }

                if ( !osFound )
                {
                    executionContextTest.setTest( test );
                    executionContextTest.setDevice( testPackage.getDevice() );
                    executionContextTest.setCloud( cD );
                    executionContextTest.completeTest( TestStatus.SKIPPED, new FilteredException( "This test is not designed to work on a device with [" + deviceOs + "]  It needs [" + test.getOs() + "]" ) );
                    testName.setTest( executionContextTest );
                    testFlow.error( Thread.currentThread().getName() + ": Mismatched OS for " + testPackage.getRunKey() );
                    throw new FilteredException( "This test is not designed to work on a device with [" + deviceOs + "]  It needs [" + test.getOs() + "]" );
                }
            }

            //
            // Device tagging implementation
            //
            if ( test.getDeviceTags() != null && test.getDeviceTags().length > 0 )
            {
                boolean tagFound = false;
                if ( deviceTags != null && deviceTags.length > 0 )
                {
                    for ( String localTag : test.getDeviceTags() )
                    {
                        for ( String deviceTag : deviceTags )
                        {
                            if ( localTag.toUpperCase().trim().equals( deviceTag.toUpperCase() ) )
                            {
                                tagFound = true;
                                break;
                            }
                        }
                        if ( tagFound )
                            break;
                    }
                }

                if ( !tagFound )
                {
                    executionContextTest.setTest( test );
                    executionContextTest.setCloud( cD );
                    executionContextTest.completeTest( TestStatus.SKIPPED, new FilteredException( "This test did not contain the specified device tag and will be skipped" ) );
                    testName.setTest( executionContextTest );
                    testFlow.error( Thread.currentThread().getName() + ": Mismatched TAG name for " + testPackage.getRunKey() );
                    throw new FilteredException( "This test did not contain the specified tag and will be skipped" );
                }
            }

            if ( DeviceManager.instance().isDryRun() )
            {
                log.info( "This would have executed " + testName.getTestName() );
                return;
            }

            if ( ArtifactManager.instance().isArtifactEnabled( ArtifactType.SAUCE_LABS.name() ) )
            {
                if ( cD.getProvider().equals( "SAUCELABS" ) )
                {
                    SauceREST sR = new SauceREST( cD.getUserName(), cD.getPassword() );
                    Map<String, Object> jobInfo = new HashMap<String, Object>( 10 );
                    jobInfo.put( "name", testName.getTestName() );

                    String[] tags = new String[] { "xFramium" };
                    if ( test.getTags() != null && test.getTags().length > 0 )
                        tags = test.getTags();

                    jobInfo.put( "tags", tags );

                    sR.updateJobInfo( testPackage.getConnectedDevice().getWebDriver().getExecutionId(), jobInfo );

                }
            }

            if ( testPackage.getConnectedDevice().getWebDriver().getCloud().getProvider().equals( "PERFECTO" ) )
            {
                if ( ArtifactManager.instance().isArtifactEnabled( ArtifactType.REPORTIUM.name() )  )
                {
                    //
                    // Reportium Integration
                    //
                    String[] tags = new String[] { "xFramium", CloudRegistry.instance().getCloud().getUserName(), ApplicationRegistry.instance().getAUT().getName(), PageManager.instance().getSiteName() };
                    if ( test.getTags() != null && test.getTags().length > 0 )
                    {
                        for ( String tag : test.getTags() )
                            ArrayUtils.add( tags, tag );
                    }
                    PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder().withProject( new Project( ApplicationRegistry.instance().getAUT().getName(), ApplicationRegistry.instance().getAUT().getVersion() + "" ) ).withContextTags( tags )
                            .withWebDriver( testPackage.getConnectedDevice().getWebDriver() ).build();
                    testPackage.getConnectedDevice().getWebDriver().setReportiumClient( new ReportiumClientFactory().createPerfectoReportiumClient( perfectoExecutionContext ) );

                    if ( testPackage.getConnectedDevice().getWebDriver().getReportiumClient() != null )
                        testPackage.getConnectedDevice().getWebDriver().getReportiumClient().testStart( testName.getTestName(), new com.perfecto.reportium.test.TestContext() );
                }
            }

            if ( test.getDescription() != null && !test.getDescription().isEmpty() )
                testPackage.getConnectedDevice().getWebDriver().setProperty( "testDescription", test.getDescription() );

            testFlow.info( Thread.currentThread().getName() + ": Executing " + testPackage.getRunKey() + " against " + testPackage.getConnectedDevice().getDevice().getKey() );
            executionContextTest = KeyWordDriver.instance().executeTest( testName, testPackage.getConnectedDevice().getWebDriver(), null );
            testFlow.info( Thread.currentThread().getName() + ": Completed Executing " + testPackage.getRunKey() + " against " + executionContextTest.getDevice().getKey() );
            returnValue = executionContextTest.getStatus();
            testName.setTest( executionContextTest );

        }
        finally
        {
            if ( testPackage.getConnectedDevice().getWebDriver() != null )
            {
                if ( returnValue )
                {
                    if ( ArtifactManager.instance().isArtifactEnabled( ArtifactType.SAUCE_LABS.name() ) )
                    {
                        if ( cD.getProvider().equals( "SAUCELABS" ) )
                        {
                            SauceREST sR = new SauceREST( cD.getUserName(), cD.getPassword() );
                            sR.jobPassed( testPackage.getConnectedDevice().getWebDriver().getExecutionId() );
                        }
                    }

                    if ( ArtifactManager.instance().isArtifactEnabled( ArtifactType.REPORTIUM.name() ) )
                    {
                        if ( testPackage.getConnectedDevice().getWebDriver().getReportiumClient() != null )
                        {
                            if ( returnValue )
                                testPackage.getConnectedDevice().getWebDriver().getReportiumClient().testStop( TestResultFactory.createSuccess() );
                        }
                    }

                    return;
                }
                else
                {
                    if ( ArtifactManager.instance().isArtifactEnabled( ArtifactType.SAUCE_LABS.name() ) )
                    {
                        if ( cD.getProvider().equals( "SAUCELABS" ) )
                        {
                            SauceREST sR = new SauceREST( cD.getUserName(), cD.getPassword() );
                            sR.jobFailed( testPackage.getConnectedDevice().getWebDriver().getExecutionId() );
                        }
                    }

                    if ( ArtifactManager.instance().isArtifactEnabled( ArtifactType.REPORTIUM.name() ) )
                    {
                        if ( testPackage.getConnectedDevice().getWebDriver().getReportiumClient() != null )
                        {
                            Throwable currentException = executionContextTest.getStepException();

                            testPackage.getConnectedDevice().getWebDriver().getReportiumClient().testStop( TestResultFactory.createFailure( currentException != null ? currentException.getMessage() : "Unknown Failure", currentException ) );

                        }
                    }
                }
            }

            Throwable currentException = executionContextTest.getStepException();

            if ( currentException != null )
                throw currentException;

            
            
            Assert.assertTrue( returnValue );
        }
    }

}
