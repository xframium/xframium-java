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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationProvider;
import org.xframium.application.ApplicationRegistry;
import org.xframium.artifact.ArtifactTime;
import org.xframium.artifact.ArtifactType;
import org.xframium.content.ContentManager;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.artifact.Artifact;
import org.xframium.device.artifact.ArtifactProducer;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudProvider;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.logging.ThreadedFileHandler;
import org.xframium.gesture.GestureManager;
import org.xframium.gesture.factory.spi.PerfectoGestureFactory;
import org.xframium.page.PageManager;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.element.provider.ElementProvider;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.listener.LoggingExecutionListener;
import org.xframium.reporting.ExecutionContext;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;
import org.xframium.spi.driver.ReportiumProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractSeleniumTest.
 */
public abstract class AbstractSeleniumTest
{

    /** The log. */
    protected static Log log = LogFactory.getLog( AbstractSeleniumTest.class );

    /** The set of devices in use on this thread. */
    private static ThreadLocal<HashMap<String, ConnectedDevice>> threadDevices = new ThreadLocal<HashMap<String, ConnectedDevice>>();

    /** The test context running on this thread. */
    private static ThreadLocal<TestContext> threadContext = new ThreadLocal<TestContext>();

    /** The name of the default device **/
    

    /**
     * The Class TestName.
     */
    

    /**
     * Gets the device data.
     *
     * @return the device data
     */
    @DataProvider ( name = "deviceManager", parallel = true)
    public Object[][] getDeviceData()
    {
        List<Device> deviceList = DeviceManager.instance().getDevices();

        return getDeviceData( deviceList );
    }

    private enum KeyType
    {
        PERSONA,
        CONTENT,
        ITERATION,
        TEST
    }
    
    private class TestKey
    {
        private String key;
        private KeyType type;
        
        public TestKey( String key, KeyType type )
        {
            super();
            this.key = key;
            this.type = type;
        }
        public String getKey()
        {
            return key;
        }
        public void setKey( String key )
        {
            this.key = key;
        }
        public KeyType getType()
        {
            return type;
        }
        public void setType( KeyType type )
        {
            this.type = type;
        }
        
        
    }
    
    public Object[][] getDeviceData( List<Device> deviceList )
    {
        List<TestName> rawList = new ArrayList<TestName>( 10 );
        List<TestName> finalList = new ArrayList<TestName>( 10 );
        List<TestKey> personaList = new ArrayList<TestKey>( 10 );
        List<TestKey> testList = new ArrayList<TestKey>( 10 );
        
        if ( DataManager.instance().getPersonas() != null && DataManager.instance().getPersonas().length > 0 )
        {
            for ( String pN : DataManager.instance().getPersonas() )
                personaList.add( new TestKey( pN, KeyType.PERSONA ) );
        }

        if ( DataManager.instance().getTests() != null && DataManager.instance().getTests().length > 0 )
        {
            for ( String pN : DataManager.instance().getTests() )
            {        
                testList.add( new TestKey( pN, KeyType.TEST ) );
            }
        }
        else
        {
            for ( String pN : KeyWordDriver.instance().getTestNames() )
            {        
                testList.add( new TestKey( pN, KeyType.TEST ) );
            }
        }

        for ( TestKey tK : testList )
        {
            KeyWordTest kT = KeyWordDriver.instance().getTest( tK.getKey() );
            if ( kT.getContentKeys() != null && kT.getContentKeys().length > 0 )
            {
                for ( String contentKey : kT.getContentKeys() )
                {
                    if ( kT.getCount() > 1 )
                    {
                        for ( int i=0; i<kT.getCount(); i++ )
                        {
                            if ( kT.getDataDriver() != null && kT.getDataDriver().trim().length() > 0 )
                            {
                                PageData[] pageData = PageDataManager.instance().getRecords( kT.getDataDriver() );
                                for ( PageData pD : pageData )
                                {
                                    TestName testName = new TestName( tK.getKey() );
                                    testName.setIteration( i+1 );
                                    testName.setTestContext( contentKey );
                                    testName.setDataDriven( pD );
                                    rawList.add( testName );
                                }
                            }
                            else
                            {
                                TestName testName = new TestName( tK.getKey() );
                                testName.setIteration( i+1 );
                                testName.setTestContext( contentKey );
                                rawList.add( testName );
                            }
                        }
                    }
                    else
                    {
                        if ( kT.getDataDriver() != null && kT.getDataDriver().trim().length() > 0 )
                        {
                            PageData[] pageData = PageDataManager.instance().getRecords( kT.getDataDriver() );
                            for ( PageData pD : pageData )
                            {
                                TestName testName = new TestName( tK.getKey() );
                                testName.setTestContext( contentKey );
                                testName.setDataDriven( pD );
                                rawList.add( testName );
                            }
                        }
                        else
                        {
                            TestName testName = new TestName( tK.getKey() );
                            testName.setTestContext( contentKey );
                            rawList.add( testName );
                        }
                    }
                }
            }
            else
            {
                if ( kT.getCount() > 1 )
                {
                    for ( int i=0; i<kT.getCount(); i++ )
                    {
                        if ( kT.getDataDriver() != null && kT.getDataDriver().trim().length() > 0 )
                        {
                            PageData[] pageData = PageDataManager.instance().getRecords( kT.getDataDriver() );
                            for ( PageData pD : pageData )
                            {
                                TestName testName = new TestName( tK.getKey() );
                                testName.setIteration( i );
                                testName.setDataDriven( pD );
                                rawList.add( testName );
                            }
                        }
                        else
                        {
                            TestName testName = new TestName( tK.getKey() );
                            testName.setIteration( i );
                            rawList.add( testName );
                        }
                    }
                }
                else
                {
                    if ( kT.getDataDriver() != null && kT.getDataDriver().trim().length() > 0 )
                    {
                        PageData[] pageData = PageDataManager.instance().getRecords( kT.getDataDriver() );
                        for ( PageData pD : pageData )
                        {
                            TestName testName = new TestName( tK.getKey() );
                            testName.setDataDriven( pD );
                            rawList.add( testName );
                        }
                    }
                    else
                    {
                        TestName testName = new TestName( tK.getKey() );
                        rawList.add( testName );
                    }
                }
            }
        }

        
        if ( personaList.size() > 0 )
        {
            for ( TestKey tK : personaList )
            {
                for ( TestName tN : rawList )
                {
                    TestName testName = new TestName( tN.getRawName() );
                    testName.setIteration( tN.getIteration() );
                    testName.setContentKey( tN.getContentKey() );
                    testName.setPersonaName( tK.getKey() );
                    finalList.add( testName );
                }
            }
        }
        else
            finalList.addAll( rawList );
        
        Object[][] newArray = null;
        
        newArray = new Object[(finalList.size()==0 ? 1 : finalList.size()) * deviceList.size()][1];

        
        if  ( finalList.size() > 0 )
        {
            for ( int i = 0; i < finalList.size(); ++i )
            {
                for ( int j = 0; j < deviceList.size(); j++ )
    
                    newArray[i * deviceList.size() + j][0] = finalList.get( i );
            }
        }
        else
        {
            for ( int i = 0; i < deviceList.size(); i++ )
                
                newArray[i][0] = new TestName();
        }

        return newArray;
    }

    /**
     * Adds the capabilities.
     *
     * @param dc
     *            the dc
     */
    public void addCapabilities( DesiredCapabilities dc )
    {

    }

    /**
     * Gets the web driver.
     *
     * @return the web driver
     */
    protected WebDriver getWebDriver()
    {
        if ( getConnectedDevice( TestName.DEFAULT ) != null )
        {
            return getConnectedDevice( TestName.DEFAULT ).getWebDriver();
        }
        else
            return null;
    }

    /**
     * Gets the device.
     *
     * @return the device
     */
    protected Device getDevice()
    {
        if ( getConnectedDevice( TestName.DEFAULT ) != null )
            return getConnectedDevice( TestName.DEFAULT ).getDevice();
        else
            return null;
    }

    /**
     * Before method.
     *
     * @param currentMethod
     *            the current method
     * @param testArgs
     *            the test args
     */
    @BeforeMethod ( alwaysRun = true)
    public void beforeMethod( Method currentMethod, Object[] testArgs )
    {
        try
        {
            TestName testName = ((TestName) testArgs[0]);

            String contentKey = testName.getContentKey();

            if ( (contentKey != null) && (contentKey.length() > 0) )
            {
                ContentManager.instance().setCurrentContentKey( contentKey );
            }
            else
            {
                ContentManager.instance().setCurrentContentKey( null );
            }

            Thread.currentThread().setName( "Device Acquisition --> " + Thread.currentThread().getId() );
            if ( log.isInfoEnabled() )
                log.info( Thread.currentThread().getName() + ": Attempting to acquire device for " + currentMethod.getName() );

            ConnectedDevice connectedDevice = DeviceManager.instance().getDevice( currentMethod, testName.getTestName(), true, testName.getPersonaName() );

            if ( log.isInfoEnabled() )
                log.info( Thread.currentThread().getName() + ": Device acquired for " + currentMethod.getName() );

            if ( connectedDevice.getWebDriver() != null && connectedDevice.getWebDriver().isConnected() )
            {
                try
                {
                    if ( DataManager.instance().isArtifactEnabled( ArtifactType.DEVICE_LOG ) )
                        connectedDevice.getWebDriver().getCloud().getCloudActionProvider().enabledLogging( connectedDevice.getWebDriver() );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }

            TestContext ctx = new TestContext();
            ctx.currentMethod = currentMethod;
            ctx.testArgs = testArgs;

            threadContext.set( ctx );

            if ( connectedDevice != null )
            {

                putConnectedDevice( TestName.DEFAULT, connectedDevice );

                if ( testName.getTestName() == null || testName.getTestName().isEmpty() )
                    testName.setTestName( currentMethod.getDeclaringClass().getSimpleName() + "." + currentMethod.getName() );

                ((TestName) testArgs[0]).setFullName( testArgs[0].toString() );
                Thread.currentThread().setName( testName.getRawName() + "-->" + connectedDevice.getWebDriver().getPopulatedDevice().getEnvironment() + " (" + Thread.currentThread().getId() + ")" );
            }
        }
        catch ( Exception e )
        {
            log.fatal( Thread.currentThread().getName() + ": Fatal error configuring test", e );
        }
    }

    public static ConnectedDevice getConnectedDevice( String name )
    {
        HashMap<String, ConnectedDevice> map = getDeviceMap();

        return map.get( name );
    }

    public static void registerSecondaryDeviceOnName( String name, String deviceId )
    {
        TestContext ctx = threadContext.get();

        if ( ctx != null )
        {
            if ( log.isInfoEnabled() )
                log.info( "Attempting to acquire device for " + ctx.currentMethod.getName() );

            ConnectedDevice connectedDevice = DeviceManager.instance().getUnconfiguredDevice( ctx.currentMethod, ((TestName) ctx.testArgs[0]).getTestName(), ((TestName) ctx.testArgs[0]).getPersonaName(), deviceId );

            if ( connectedDevice != null )
            {
                putConnectedDevice( name, connectedDevice );
            }
        }
    }

    public static void registerInactiveDeviceOnName( String name )
    {
        TestContext ctx = threadContext.get();

        if ( ctx != null )
        {
            if ( log.isInfoEnabled() )
                log.info( "Attempting to acquire Inactive device for " + ctx.currentMethod.getName() );

            ConnectedDevice connectedDevice = DeviceManager.instance().getInactiveDevice( name );

            if ( connectedDevice != null )
            {
                putConnectedDevice( name, connectedDevice );
            }
        }
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
        try
        {

            HashMap<String, ConnectedDevice> map = getDevicesToCleanUp();
            threadContext.set( null );
            Iterator<String> keys = ((map != null) ? map.keySet().iterator() : null);

            if ( map.get( TestName.DEFAULT ).getWebDriver().isConnected() )
            {
                try
                {
                    if ( DataManager.instance().isArtifactEnabled( ArtifactType.DEVICE_LOG ) )
                        map.get( TestName.DEFAULT ).getWebDriver().getCloud().getCloudActionProvider().disableLogging( map.get( TestName.DEFAULT ).getWebDriver() );

                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }

            while ( (keys != null) && (keys.hasNext()) )
            {
                String name = keys.next();
                ConnectedDevice device = map.get( name );

                cleanUpConnectedDevice( name, device, currentMethod, testArgs, testResult );
            }

            //
            // Write out the index file for all tests
            //
            try
            {
                DeviceManager.instance().clearAllArtifacts();
                Thread.currentThread().setName( "Idle Thread (" + Thread.currentThread().getId() + ")" );
            }
            catch ( Exception e )
            {
                log.error( Thread.currentThread() + ": Error flushing artifacts", e );
            }
        }
        catch ( Exception e )
        {
            log.fatal( Thread.currentThread().getName() + ": Fatal error completing test", e );
        }
    }

    

    private void cleanUpConnectedDevice( String name, ConnectedDevice device, Method currentMethod, Object[] testArgs, ITestResult testResult )
    {
        WebDriver webDriver = device.getWebDriver();
        Device currentDevice = device.getDevice();
        ExecutionContextTest test = null;

        try
        {
            if ( webDriver != null )
            {
                String runKey = ((TestName.DEFAULT.equals( name )) ? ((TestName) testArgs[0]).getTestName() : ((TestName) testArgs[0]).getTestName() + "-" + name);

                if ( TestName.DEFAULT.equals( name ) )
                {
                    test = ((TestName) testArgs[0]).getTest();
                }

                File rootFolder = ExecutionContext.instance().getReportFolder();
                rootFolder.mkdirs();

                try
                {
                    if ( !testResult.isSuccess() )
                    {

                        if ( DataManager.instance().getAutomaticDownloads() != null )
                        {
                            if ( webDriver instanceof ArtifactProducer )
                            {
                                if ( DataManager.instance().getReportFolder() == null )
                                    DataManager.instance().setReportFolder( new File( "." ) );

                                for ( ArtifactType aType : DataManager.instance().getAutomaticDownloads() )
                                {
                                    if ( aType.getTime() == ArtifactTime.ON_FAILURE )
                                    {
                                        try
                                        {
                                            Artifact currentArtifact = ((ArtifactProducer) webDriver).getArtifact( webDriver, aType, device, runKey, testResult.getStatus() == ITestResult.SUCCESS, test );
                                            if ( currentArtifact != null )
                                            {
                                                if ( test != null )
                                                {
                                                    test.addExecutionParameter( aType.name(), currentArtifact.getArtifactName() );
                                                    test.addExecutionParameter( aType.name() + "_FILE", new File( currentArtifact.getArtifactName() ).getName() );
                                                }
                                                currentArtifact.writeToDisk( rootFolder );
                                            }
                                        }
                                        catch ( Exception e )
                                        {
                                            log.error( "Error acquiring Artifacts " + e.getMessage() );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                finally
                {
                    try
                    {
                        webDriver.close();
                    }
                    catch ( Exception e )
                    {
                    }
                }

                if ( DataManager.instance().getAutomaticDownloads() != null )
                {
                    if ( webDriver instanceof ArtifactProducer )
                    {
                        if ( DataManager.instance().getReportFolder() == null )
                            DataManager.instance().setReportFolder( new File( "." ) );

                        for ( ArtifactType aType : DataManager.instance().getAutomaticDownloads() )
                        {
                            if ( aType.getTime() == ArtifactTime.AFTER_TEST )
                            {
                                try
                                {
                                    Artifact currentArtifact = ((ArtifactProducer) webDriver).getArtifact( webDriver, aType, device, runKey, testResult.getStatus() == ITestResult.SUCCESS, test );
                                    if ( currentArtifact != null )
                                    {
                                        if ( test != null )
                                        {
                                            test.addExecutionParameter( aType.name(), currentArtifact.getArtifactName() );
                                            test.addExecutionParameter( aType.name() + "_FILE", new File( currentArtifact.getArtifactName() ).getName() );
                                        }
                                        currentArtifact.writeToDisk( rootFolder );
                                    }
                                }
                                catch ( Exception e )
                                {
                                    log.error( "Error acquiring Artifacts - " + e.getMessage() );
                                }
                            }
                        }
                    }
                    
                    //
                    // Cloud specific artifacts
                    //
                    CloudDescriptor currentCloud = CloudRegistry.instance().getCloud();
                    if ( device.getDevice().getCloud() != null && !device.getDevice().getCloud().isEmpty() )
                        currentCloud = CloudRegistry.instance().getCloud( device.getDevice().getCloud() );
                    String cloudProvider = currentCloud.getProvider();
                    
                    
                    //
                    // Perfecto Wind Tunnel
                    //
                    String wtUrl = ((DeviceWebDriver) webDriver).getWindTunnelReport();
                    if ( cloudProvider.equals( "PERFECTO" ) && wtUrl != null && !wtUrl.isEmpty() )
                        test.addExecutionParameter( "PERFECTO_WT", wtUrl );

                    //
                    // Perfecto Wind Tunnel
                    //
                    if ( DataManager.instance().isArtifactEnabled( ArtifactType.SAUCE_LABS ) && cloudProvider.equals( "SAUCELABS" ) )
                        test.addExecutionParameter( "SAUCELABS", "https://saucelabs.com/beta/tests/" + test.getSessionId() + "/commands#0" );

                    if ( DataManager.instance().isArtifactEnabled( ArtifactType.REPORTIUM ) && ((DeviceWebDriver) webDriver).isConnected() && cloudProvider.equals( "PERFECTO" ) )
                    {
                        if ( ((ReportiumProvider) webDriver).getReportiumClient() != null )
                        {
                            test.addExecutionParameter( "SAUCELABS", ((ReportiumProvider) webDriver).getReportiumClient().getReportUrl() );
                        }
                    }

                    if ( test != null )
                    {

                        try
                        {
                            test.popupateSystemProperties();
                            Artifact currentArtifact = ((ArtifactProducer) webDriver).getArtifact( webDriver, ArtifactType.EXECUTION_RECORD_JSON, device, runKey, testResult.getStatus() == ITestResult.SUCCESS, test );
                            if ( currentArtifact != null )
                                currentArtifact.writeToDisk( rootFolder );

                            currentArtifact = ((ArtifactProducer) webDriver).getArtifact( webDriver, ArtifactType.EXECUTION_RECORD_HTML, device, runKey, testResult.getStatus() == ITestResult.SUCCESS, test );
                            if ( currentArtifact != null )
                                currentArtifact.writeToDisk( rootFolder );

                        }
                        catch ( Exception e )
                        {
                            log.error( "Error acquiring Artifacts - " + e );
                        }

                    }

                }

            }

            if ( currentDevice != null )
            {
                if ( webDriver instanceof DeviceWebDriver )
                    DeviceManager.instance().addRun( ((DeviceWebDriver) webDriver).getPopulatedDevice(), currentMethod, ((TestName) testArgs[0]).getTestName(), testResult.isSuccess(), device.getPersona() );
                else
                    DeviceManager.instance().addRun( currentDevice, currentMethod, ((TestName) testArgs[0]).getTestName(), testResult.isSuccess(), device.getPersona() );
            }
        }
        finally
        {
            try
            {
                webDriver.quit();
            }
            catch ( Exception e )
            {
            }

            if ( currentDevice != null )
                DeviceManager.instance().releaseDevice( currentDevice );

            if ( test != null && TestName.DEFAULT.equals( name ) )
            {
                ExecutionContext.instance().addExecution( test );
            }
        }

    }

    //
    // Helpers
    //

    private static class TestContext
    {
        public Method currentMethod = null;
        public Object[] testArgs = null;
    }

    private static void putConnectedDevice( String name, ConnectedDevice device )
    {
        HashMap<String, ConnectedDevice> map = getDeviceMap();

        map.put( name, device );
    }

    private static HashMap<String, ConnectedDevice> getDeviceMap()
    {
        HashMap<String, ConnectedDevice> map = threadDevices.get();

        if ( map == null )
        {
            map = new HashMap<String, ConnectedDevice>();

            threadDevices.set( map );
        }

        return map;
    }

    private HashMap<String, ConnectedDevice> getDevicesToCleanUp()
    {
        HashMap<String, ConnectedDevice> map = threadDevices.get();

        threadDevices.set( null );

        return map;
    }
}
