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
import org.xframium.artifact.ArtifactType;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.ng.AbstractSeleniumTest;
import org.xframium.exception.DeviceAcquisitionException;
import org.xframium.exception.FilteredException;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.integrations.sauceLabs.rest.SauceREST;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.reporting.ExecutionContextTest.TestStatus;
import org.xframium.spi.PropertyProvider;
import org.xframium.spi.driver.ReportiumProvider;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.result.TestResultFactory;

public class XMLTestDriver extends AbstractSeleniumTest
{
    
    
    @Test ( dataProvider = "deviceManager")
    public void testDriver( TestName testName ) throws Throwable
    {
        String deviceOs = getDevice().getOs();
        String[] deviceTags = getDevice().getTagNames();
        
        CloudDescriptor cD = CloudRegistry.instance().getCloud();
        if ( getDevice().getCloud() != null && !getDevice().getCloud().trim().isEmpty() )
            cD = CloudRegistry.instance().getCloud( getDevice().getCloud() );
        
        boolean returnValue = false;

        ExecutionContextTest executionContextTest = new ExecutionContextTest();

        KeyWordTest test = KeyWordDriver.instance().getTest( testName.getTestName().split( "\\." )[0] );
        if ( test == null )
            throw new ScriptConfigurationException( "The Test Name " + testName + " does not exist" );

        try
        {
            if ( !((DeviceWebDriver) getWebDriver()).isConnected() )
            {
                executionContextTest.setDevice( getDevice() );
                executionContextTest.setCloud( cD );
                executionContextTest.setTest( test );
                executionContextTest.completeTest( TestStatus.FAILED, new DeviceAcquisitionException( getDevice() ) );
                testName.setTest( executionContextTest );
                throw new DeviceAcquisitionException( getDevice() );
            }

            if ( test.getOs() != null && deviceOs != null )
            {
                String[] osArray = test.getOs().split( "," );
                boolean osFound = false;
                for ( String localOs : osArray )
                {
                    if ( localOs.toUpperCase().trim().equals( deviceOs.toUpperCase() ) )
                    {
                        osFound = true;
                        break;
                    }
                }

                if ( !osFound )
                {
                    executionContextTest.setTest( test );
                    executionContextTest.setDevice( getDevice() );
                    executionContextTest.setCloud( cD );
                    executionContextTest.completeTest( TestStatus.SKIPPED, new FilteredException( "This test is not designed to work on a device with [" + deviceOs + "]  It needs [" + test.getOs() + "]" ) );
                    testName.setTest( executionContextTest );
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
                    executionContextTest.completeTest( TestStatus.SKIPPED, new FilteredException( "This test did not contain the specified tag and will be skipped" ) );
                    testName.setTest( executionContextTest );
                    throw new FilteredException( "This test did not contain the specified tag and will be skipped" );
                }
            }

            if ( DeviceManager.instance().isDryRun() )
            {
                log.info( "This would have executed " + testName.getTestName() );
                return;
            }

            if ( DataManager.instance().isArtifactEnabled( ArtifactType.SAUCE_LABS ) )
            {
                CloudDescriptor currentCloud = CloudRegistry.instance().getCloud();
                if ( getDevice().getCloud() != null && !getDevice().getCloud().isEmpty() )
                    currentCloud = CloudRegistry.instance().getCloud( getDevice().getCloud() );

                if ( currentCloud.getProvider().equals( "SAUCELABS" ) )
                {
                    SauceREST sR = new SauceREST( currentCloud.getUserName(), currentCloud.getPassword() );
                    Map<String, Object> jobInfo = new HashMap<String, Object>( 10 );
                    jobInfo.put( "name", testName.getTestName() );

                    String[] tags = new String[] { "xFramium" };
                    if ( test.getTags() != null && test.getTags().length > 0 )
                        tags = test.getTags();

                    jobInfo.put( "tags", tags );

                    sR.updateJobInfo( ((DeviceWebDriver) getWebDriver()).getExecutionId(), jobInfo );

                }
            }

            if ( ((DeviceWebDriver) getWebDriver()).getCloud().getProvider().equals( "PERFECTO" ) )
            {
                if ( DataManager.instance().isArtifactEnabled( ArtifactType.REPORTIUM ) && getWebDriver() instanceof ReportiumProvider )
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
                    PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder().withProject( new Project( ApplicationRegistry.instance().getAUT().getName(), "1.0" ) ).withContextTags( tags )
                            .withWebDriver( getWebDriver() ).build();
                    ((ReportiumProvider) getWebDriver()).setReportiumClient( new ReportiumClientFactory().createPerfectoReportiumClient( perfectoExecutionContext ) );

                    if ( ((ReportiumProvider) getWebDriver()).getReportiumClient() != null )
                        ((ReportiumProvider) getWebDriver()).getReportiumClient().testStart( testName.getTestName(), new com.perfecto.reportium.test.TestContext() );
                }
            }

            if ( test.getDescription() != null && !test.getDescription().isEmpty() && getWebDriver() instanceof PropertyProvider )
                ((PropertyProvider) getWebDriver()).setProperty( "testDescription", test.getDescription() );

            executionContextTest = KeyWordDriver.instance().executeTest( testName.getTestName().split( "\\." )[0], getWebDriver(), null );
            returnValue = executionContextTest.getStatus();
            testName.setTest( executionContextTest );
        }
        finally
        {
            if ( getWebDriver() != null )
            {
                if ( returnValue )
                {
                    if ( DataManager.instance().isArtifactEnabled( ArtifactType.SAUCE_LABS ) )
                    {
                        CloudDescriptor currentCloud = CloudRegistry.instance().getCloud();
                        if ( getDevice().getCloud() != null && !getDevice().getCloud().isEmpty() )
                            currentCloud = CloudRegistry.instance().getCloud( getDevice().getCloud() );

                        if ( currentCloud.getProvider().equals( "SAUCELABS" ) )
                        {
                            SauceREST sR = new SauceREST( currentCloud.getUserName(), currentCloud.getPassword() );
                            sR.jobPassed( ((DeviceWebDriver) getWebDriver()).getExecutionId() );
                        }
                    }

                    if ( DataManager.instance().isArtifactEnabled( ArtifactType.REPORTIUM ) )
                    {
                        if ( ((ReportiumProvider) getWebDriver()).getReportiumClient() != null )
                        {
                            if ( returnValue )
                                ((ReportiumProvider) getWebDriver()).getReportiumClient().testStop( TestResultFactory.createSuccess() );
                        }
                    }

                    return;
                }
                else
                {
                    if ( DataManager.instance().isArtifactEnabled( ArtifactType.SAUCE_LABS ) )
                    {
                        CloudDescriptor currentCloud = CloudRegistry.instance().getCloud();
                        if ( getDevice().getCloud() != null && !getDevice().getCloud().isEmpty() )
                            currentCloud = CloudRegistry.instance().getCloud( getDevice().getCloud() );

                        if ( currentCloud.getProvider().equals( "SAUCELABS" ) )
                        {
                            SauceREST sR = new SauceREST( currentCloud.getUserName(), currentCloud.getPassword() );
                            sR.jobFailed( ((DeviceWebDriver) getWebDriver()).getExecutionId() );
                        }
                    }

                    if ( DataManager.instance().isArtifactEnabled( ArtifactType.REPORTIUM ) )
                    {
                        if ( ((ReportiumProvider) getWebDriver()).getReportiumClient() != null )
                        {
                            Throwable currentException = executionContextTest.getStepException();

                            ((ReportiumProvider) getWebDriver()).getReportiumClient().testStop( TestResultFactory.createFailure( currentException != null ? currentException.getMessage() : "Unknown Failure", currentException ) );

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
