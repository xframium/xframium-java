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
/*
 * 
 */
package org.xframium.device.ng;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationProvider;
import org.xframium.application.ApplicationRegistry;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudProvider;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataManager;
import org.xframium.device.logging.ThreadedFileHandler;
import org.xframium.gesture.GestureManager;
import org.xframium.gesture.factory.spi.PerfectoGestureFactory;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.element.provider.ElementProvider;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.step.SyntheticStep;
import org.xframium.page.listener.LoggingExecutionListener;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractSeleniumTest.
 */
public abstract class AbstractJavaTest extends AbstractSeleniumTest
{

    @BeforeMethod ( alwaysRun = true)
    public void beforeMethod( Method currentMethod, Object[] testArgs )
    {
        super.beforeMethod( currentMethod, testArgs );
        TestName testName = ((TestName) testArgs[0]);
        
        ExecutionContextTest eC = new ExecutionContextTest();
        eC.setTestName( currentMethod.getName() );
        
        KeyWordTest kwt = new KeyWordTest( currentMethod.getName(), true, null, null, false, null, null, 0, currentMethod.getName() + " from " + currentMethod.getClass().getName(), null, null, null, null, 0, null, null, null );
        eC.setTest( kwt );
        eC.setAut( ApplicationRegistry.instance().getAUT() );
        eC.setCloud( CloudRegistry.instance().getCloud() );
        eC.setDevice( getConnectedDevice( TestName.DEFAULT ).getDevice() );
        testName.setTest( eC );
    }

    public void startStep( TestName testName, String stepName )
    {
        testName.getTest().startStep( new SyntheticStep( stepName, stepName ), null, null );
    }
    
    public void stopStep( TestName testName, StepStatus stepStatus, Throwable e )
    {
        testName.getTest().completeStep( stepStatus, e );
    }

    /**
     * After method.
     *
     * @param currentMethod
     *            the current method
     * @param testArgs
     *            the test args
     * @param testResult
     *            the test result
     */
    @AfterMethod ( alwaysRun = true)
    public void afterMethod( Method currentMethod, Object[] testArgs, ITestResult testResult )
    {
        super.afterMethod( currentMethod, testArgs, testResult );
    }

    protected void configureFramework( CloudProvider cloudProvider, String cloudName, org.xframium.device.data.DataProvider dataProvider, ApplicationProvider applicationProvider, String AUT, ElementProvider elementProvider, String siteName,
            String reportFolder, ArtifactType[] artifactList )
    {
        //
        // Configure Cloud
        //
        List<CloudDescriptor> cloudData = cloudProvider.readData();
        for ( CloudDescriptor c : cloudData )
            CloudRegistry.instance().addCloudDescriptor( c );

        CloudRegistry.instance().setCloud( cloudName );

        //
        // Configure Devices
        //
        for ( Device x : dataProvider.readData() )
        {
            if ( x.isActive() )
                DeviceManager.instance().registerDevice( x );
            else
                DeviceManager.instance().registerInactiveDevice( x );
        }

        GestureManager.instance().setGestureFactory( new PerfectoGestureFactory() );

        //
        // Configure Applications
        //
        for ( ApplicationDescriptor a : applicationProvider.readData() )
            ApplicationRegistry.instance().addApplicationDescriptor( a );
        ApplicationRegistry.instance().setAUT( AUT );

        //
        // Configure the driver and load the devices
        //
        DataManager.instance().setReportFolder( new File( reportFolder ) );
        DataManager.instance().setAutomaticDownloads( artifactList );
        DataManager.instance().readData( dataProvider );

        //
        // Configure the object repository
        //
        PageManager.instance().setSiteName( siteName );
        PageManager.instance().registerExecutionListener( new LoggingExecutionListener() );
        PageManager.instance().setElementProvider( elementProvider );

        //
        // Configure the thread logger to separate test case log files
        //
        ThreadedFileHandler threadedHandler = new ThreadedFileHandler();
        threadedHandler.configureHandler( Level.INFO );

        //
        // The RunDetail singleton can be registered to track all runs for the
        // consolidated output report
        //
    }
}
