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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;
import org.testng.internal.TestResult;
import org.xframium.artifact.ArtifactManager;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.ng.AbstractSeleniumTest;
import org.xframium.device.ng.TestContainer;
import org.xframium.device.ng.TestName;
import org.xframium.device.ng.TestPackage;
import org.xframium.exception.DeviceException;
import org.xframium.exception.FilteredException;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.integrations.sauceLabs.rest.SauceREST;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.reporting.ExecutionContext;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.reporting.ExecutionContextTest.TestStatus;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.result.TestResultFactory;

public class XMLTestDriver extends AbstractSeleniumTest
{
 
    @AfterSuite
    public void afterSuite( ITestContext testContext )
    {
        
        TestContainer testContainer = (TestContainer) testContext.getAttribute( "testContainer" );
        
        if ( DeviceManager.instance( testContainer.getxFID() ).getFailedTestRetryCount() > 0 )
        {
            
            
            while ( testContainer.testsRemain() )
            {
                try
                {
                    TestResult tR = new TestResult();
                    beforeMethod( XMLTestDriver.class.getMethods()[ 0 ], new Object[] { testContainer }, testContext );
                    try
                    {
                        testDriver( testContainer );
                        tR.setStatus( ITestResult.SUCCESS );
                        afterMethod( XMLTestDriver.class.getMethods()[ 0 ], new Object[] { testContainer }, tR ,testContext );
                    }
                    catch( Exception e )
                    {
                        tR.setStatus( ITestResult.FAILURE );
                        afterMethod( XMLTestDriver.class.getMethods()[ 0 ], new Object[] { testContainer }, tR ,testContext ); 
                    }
                    
                }
                catch( Throwable e )
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Test ( dataProvider = "deviceManager" )
    public void testDriver( TestContainer testContainer ) throws Throwable
    {
        TestPackage testPackage = testPackageContainer.get();
        testFlow.info( Thread.currentThread().getName() + ": Performing initialization checked for " + testPackage.getRunKey() );
        
        TestName testName = testPackage.getTestName();
        
        KeyWordTest test = KeyWordDriver.instance( testPackage.getxFID() ).getTest( testName.getRawName() );
        if ( test == null )
            throw new ScriptConfigurationException( "The Test Name " + testName.getRawName() + " does not exist" );
        
        ExecutionContextTest executionContextTest = new ExecutionContextTest();
        executionContextTest.setxFID( testContainer.getxFID() );
        executionContextTest.setIterationCount( DeviceManager.instance( testContainer.getxFID() ).getIterationCount( testPackage.getRunKey() ) );
        executionContextTest.setTestName( testName.getTestName() );
        
        File artifactFolder = new File( testPackage.getConnectedDevice().getDevice().getEnvironment(), testName.getTestName() );
        testPackage.getConnectedDevice().getWebDriver().setArtifactFolder( artifactFolder );
   
        testPackage.getConnectedDevice().getWebDriver().setExecutionContext( executionContextTest );
        
        if ( testPackage.getConnectedDevice().getWebDriver().isConnected() )
        {
            try
            {
                if ( ArtifactManager.instance( executionContextTest.getxFID() ).isArtifactEnabled( ArtifactType.DEVICE_LOG.name() ) )
                    testPackage.getConnectedDevice().getWebDriver().getCloud().getCloudActionProvider().enabledLogging( testPackage.getConnectedDevice().getWebDriver() );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        
        CloudDescriptor cD = CloudRegistry.instance(executionContextTest.getxFID()).getCloud();
        if ( testPackage.getDevice().getCloud() != null && !testPackage.getDevice().getCloud().trim().isEmpty() )
            cD = CloudRegistry.instance(executionContextTest.getxFID()).getCloud( testPackage.getDevice().getCloud() );
        
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
        
        String deviceOs = testPackage.getPopulatedDevice().getOs();
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

            if ( DeviceManager.instance( testContainer.getxFID() ).isDryRun() )
            {
                log.info( "This would have executed " + testName.getTestName() );
                return;
            }

            if ( ArtifactManager.instance( executionContextTest.getxFID() ).isArtifactEnabled( ArtifactType.SAUCE_LABS.name() ) )
            {
                if ( cD != null && cD.getProvider() != null && cD.getProvider().equals( "SAUCELABS" ) )
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
                if ( ArtifactManager.instance( executionContextTest.getxFID() ).isArtifactEnabled( ArtifactType.REPORTIUM.name() )  )
                {
                    //
                    // Reportium Integration
                    //
                    List<String> tagList = new ArrayList<String>( 5 );
                    tagList.add( "xFramium" );
                    if ( CloudRegistry.instance(executionContextTest.getxFID()).getCloud() != null && CloudRegistry.instance(executionContextTest.getxFID()).getCloud().getUserName() != null && !CloudRegistry.instance(executionContextTest.getxFID()).getCloud().getUserName().isEmpty() )
                        tagList.add( CloudRegistry.instance(executionContextTest.getxFID()).getCloud().getUserName() );
                    
                    if ( testPackage.getConnectedDevice().getWebDriver().getAut() != null && testPackage.getConnectedDevice().getWebDriver().getAut().getName() != null && !testPackage.getConnectedDevice().getWebDriver().getAut().getName().isEmpty() && !testPackage.getConnectedDevice().getWebDriver().getAut().getName().equals( "NOOP" ) )
                        tagList.add( testPackage.getConnectedDevice().getWebDriver().getAut().getName() );
                    
                    if ( test.getTags() != null && test.getTags().length > 0 )
                    {
                        for ( String tag : test.getTags() )
                            tagList.add( tag );
                    }
                    
                    PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder().withProject( new Project( ExecutionContext.instance( executionContextTest.getxFID() ).getSuiteName(), "" ) ).withContextTags( tagList.toArray( new String[ 0 ] ) )
                            .withWebDriver( testPackage.getConnectedDevice().getWebDriver() ).build();
                    testPackage.getConnectedDevice().getWebDriver().setReportiumClient( new ReportiumClientFactory().createPerfectoReportiumClient( perfectoExecutionContext ) );

                    if ( testPackage.getConnectedDevice().getWebDriver().getReportiumClient() != null )
                        testPackage.getConnectedDevice().getWebDriver().getReportiumClient().testStart( testName.getTestName(), new com.perfecto.reportium.test.TestContext() );
                }
            }

            if ( test.getDescription() != null && !test.getDescription().isEmpty() )
                testPackage.getConnectedDevice().getWebDriver().setProperty( "testDescription", test.getDescription() );

            testFlow.info( Thread.currentThread().getName() + ": Executing " + testPackage.getRunKey() + " against " + testPackage.getConnectedDevice().getDevice().getKey() );
            executionContextTest = KeyWordDriver.instance( testPackage.getxFID() ).executeTest( testName, testPackage.getConnectedDevice().getWebDriver(), null );
            testFlow.info( Thread.currentThread().getName() + ": Completed Executing " + testPackage.getRunKey() + " against " + testPackage.getConnectedDevice().getDevice().getKey() );
            returnValue = executionContextTest.getStatus();
            testName.setTest( executionContextTest );

        }
        finally
        {
            try
            {
                if ( testPackage.getConnectedDevice().getWebDriver() != null )
                {
                    if ( returnValue )
                    {
                        if ( ArtifactManager.instance( executionContextTest.getxFID() ).isArtifactEnabled( ArtifactType.SAUCE_LABS.name() ) )
                        {
                            if ( cD.getProvider().equals( "SAUCELABS" ) )
                            {
                                SauceREST sR = new SauceREST( cD.getUserName(), cD.getPassword() );
                                sR.jobPassed( testPackage.getConnectedDevice().getWebDriver().getExecutionId() );
                            }
                        }
    
                        if ( ArtifactManager.instance( executionContextTest.getxFID() ).isArtifactEnabled( ArtifactType.REPORTIUM.name() ) )
                        {
                            if ( testPackage.getConnectedDevice().getWebDriver() != null && testPackage.getConnectedDevice().getWebDriver().getReportiumClient() != null )
                                testPackage.getConnectedDevice().getWebDriver().getReportiumClient().testStop( TestResultFactory.createSuccess() );
                            

                            for ( ConnectedDevice subDevice : executionContextTest.getDeviceMap().values() )
                            {                                
                                if ( subDevice.getWebDriver() != null && subDevice.getWebDriver().getReportiumClient() != null )
                                    testPackage.getConnectedDevice().getWebDriver().getReportiumClient().testStop( TestResultFactory.createSuccess() );
                            }
                            
                            
                            
                            
                        }
    
                        return;
                    }
                    else
                    {
                        if ( ArtifactManager.instance( executionContextTest.getxFID() ).isArtifactEnabled( ArtifactType.SAUCE_LABS.name() ) )
                        {
                            if ( cD.getProvider().equals( "SAUCELABS" ) )
                            {
                                SauceREST sR = new SauceREST( cD.getUserName(), cD.getPassword() );
                                sR.jobFailed( testPackage.getConnectedDevice().getWebDriver().getExecutionId() );
                            }
                        }
    
                        if ( ArtifactManager.instance( executionContextTest.getxFID() ).isArtifactEnabled( ArtifactType.REPORTIUM.name() ) )
                        {
                            if ( testPackage.getConnectedDevice().getWebDriver().getReportiumClient() != null )
                            {
                                Throwable currentException = executionContextTest.getStepException();
    
                                testPackage.getConnectedDevice().getWebDriver().getReportiumClient().testStop( TestResultFactory.createFailure( currentException != null ? currentException.getMessage() : "Unknown Failure", currentException ) );
    
                            }
                        }
                    }
                }
            }
            catch( Exception e )
            {
                log.error( "Error ending test", e );
            }

            Throwable currentException = executionContextTest.getStepException();

            if ( currentException != null )
                throw currentException;
        }
        
        Assert.assertTrue( returnValue );
    }

}
