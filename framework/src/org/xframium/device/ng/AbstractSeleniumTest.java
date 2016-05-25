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

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.xframium.artifact.ArtifactTime;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.artifact.Artifact;
import org.xframium.device.artifact.ArtifactProducer;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataManager;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.spi.Device;
import org.xframium.spi.RunDetails;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractSeleniumTest.
 */
public abstract class AbstractSeleniumTest
{
	
    /** The log. */
    protected static Log log = LogFactory.getLog( AbstractSeleniumTest.class );

    /** The set of devices in use on this thread. */
    private static ThreadLocal<HashMap<String,ConnectedDevice>> threadDevices = new ThreadLocal<HashMap<String,ConnectedDevice>>();

    /** The test context running on this thread. */
    private static ThreadLocal<TestContext> threadContext = new ThreadLocal<TestContext>();

    /** The name of the default device **/
    private static String DEFAULT = "default";

    /**
     * The Class TestName.
     */
    protected class TestName
    {
		
        /** The test name. */
        private String testName;
		
        /** The full name. */
        private String fullName;
		
        /** The persona name. */
        private String personaName;
		
        private String testContext;

        /**
         * Gets the full name.
         *
         * @return the full name
         */
        public String getFullName()
        {
            return fullName;
        }

        /**
         * Sets the full name.
         *
         * @param fullName the new full name
         */
        public void setFullName( String fullName )
        {
            this.fullName = fullName;
        }
		
		

        /**
         * Gets the persona name.
         *
         * @return the persona name
         */
        public String getPersonaName() 
        {
            return personaName;
        }

        /**
         * Sets the persona name.
         *
         * @param personaName the new persona name
         */
        public void setPersonaName(String personaName) 
        {
            this.personaName = personaName;
            formatTestName();
        }

		
		
        public String getTestContext()
        {
            return testContext;
        }

        public void setTestContext( String testContext )
        {
            this.testContext = testContext;
            formatTestName();
        }

        /**
         * Instantiates a new test name.
         */
        public TestName()
        {
        }

        /**
         * Instantiates a new test name.
         *
         * @param testName the test name
         */
        public TestName( String testName )
        {
            this.testName = testName;
            formatTestName();

        }

        /**
         * Gets the test name.
         *
         * @return the test name
         */
        public String getTestName()
        {
            return testName;
        }
		
		

        public void setTestName( String testName )
        {
            this.testName = testName;
            formatTestName();
        }

        private void formatTestName()
        {
            if ( testName == null )
                return;
            String useTest = testName;
		    
            if ( personaName != null && !personaName.isEmpty() )
                useTest = useTest + "." + personaName;
		    
            if ( testContext != null && !testContext.isEmpty() )
                useTest = useTest + "[" + testContext + "]";
		    
            this.testName = useTest;
        }
		
        /**
         * Gets the device.
         *
         * @return the device
         */
        public ConnectedDevice getDevice()
        {
            return getConnectedDevice( DEFAULT );
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            if ( fullName != null )
                return fullName;
			
            if (getConnectedDevice( DEFAULT ) != null)
            {
                if (testName != null)
                    return testName + "-->" + getConnectedDevice( DEFAULT ).toString();
                else
                    return getConnectedDevice( DEFAULT ).toString();

            }
            if (testName != null)
                return testName;
            else
                return "Unknown Test";
        }
		
        public String getKeyName()
        {
            return testName + ( personaName != null && !personaName.isEmpty() ? "." + personaName : "" );
        }
    }

    /**
     * Gets the device data.
     *
     * @return the device data
     */
    @DataProvider ( name = "deviceManager", parallel = true)
    public Object[][] getDeviceData()
    {
        List<Device> deviceList = DeviceManager.instance().getDevices();

        int testCount = 0;
        boolean hasPersonas = false;

        if (DataManager.instance().getTests() != null && DataManager.instance().getTests().length > 0)
            testCount = deviceList.size() * DataManager.instance().getTests().length;
        else
            testCount = deviceList.size();

        if ( DataManager.instance().getPersonas() != null && DataManager.instance().getPersonas().length > 0 )
        {
            hasPersonas = true;
            testCount = testCount * DataManager.instance().getPersonas().length;
        }
		
        Object[][] newArray = new Object[testCount][1];

        int currentPosition = 0;

        while (currentPosition < testCount)
        {
            if (DataManager.instance().getTests() != null && DataManager.instance().getTests().length > 0)
            {
                for (int i = 0; i < DataManager.instance().getTests().length; i++)
                {
                    if ( hasPersonas )
                    {
                        for ( int j=0; j<DataManager.instance().getPersonas().length; j++ )
                        {
                            TestName testName = new TestName( DataManager.instance().getTests()[i] );
                            testName.setPersonaName( DataManager.instance().getPersonas()[ j ] );
                            newArray[currentPosition++][0] = testName;
                        }
                    }
                    else
                        newArray[currentPosition++][0] = new TestName( DataManager.instance().getTests()[i] );
                }
            }
            else
            {
                if ( hasPersonas )
                {
                    for ( int j=0; j<DataManager.instance().getPersonas().length; j++ )
                    {
                        TestName testName = new TestName();
                        testName.setPersonaName( DataManager.instance().getPersonas()[ j ] );
                        newArray[currentPosition++][0] = testName;
                    }
                }
                else
                    newArray[currentPosition++][0] = new TestName();
            }
        }

        return newArray;
    }

    /**
     * Adds the capabilities.
     *
     * @param dc the dc
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
        if (getConnectedDevice( DEFAULT ) != null)
        {
            return getConnectedDevice( DEFAULT ).getWebDriver();
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
        if (getConnectedDevice( DEFAULT ) != null)
            return getConnectedDevice( DEFAULT ).getDevice();
        else
            return null;
    }

    /**
     * Before method.
     *
     * @param currentMethod the current method
     * @param testArgs the test args
     */
    @BeforeMethod
    public void beforeMethod( Method currentMethod, Object[] testArgs )
    {
        Thread.currentThread().setName( testArgs[0].toString() );
        if (log.isInfoEnabled())
            log.info( "Attempting to acquire device for " + currentMethod.getName() );

        ConnectedDevice connectedDevice = DeviceManager.instance().getDevice( currentMethod,
                                                                              ( ( TestName ) testArgs[0] ).getTestName(),
                                                                              true,
                                                                              ( ( TestName ) testArgs[0] ).getPersonaName() );

        
        if( DataManager.instance().isArtifactEnabled( ArtifactType.DEVICE_LOG ) )
            PerfectoMobile.instance().device().startDebug( connectedDevice.getExecutionId(), connectedDevice.getDeviceName() );
        
        TestContext ctx = new TestContext();
        ctx.currentMethod = currentMethod;
        ctx.testArgs = testArgs;

        threadContext.set( ctx );

        if (connectedDevice != null)
        {
            putConnectedDevice( DEFAULT, connectedDevice );
			
            TestName testName = ( ( TestName ) testArgs[0] );
            if ( testName.getTestName() == null || testName.getTestName().isEmpty() )
                testName.setTestName( currentMethod.getDeclaringClass().getSimpleName() + "." + currentMethod.getName() );
			
            ( ( TestName ) testArgs[0] ).setFullName( testArgs[0].toString() );
            Thread.currentThread().setName( testArgs[0].toString() );
        }
    }

    public static ConnectedDevice getConnectedDevice( String name )
    {
        HashMap<String,ConnectedDevice> map = getDeviceMap();

        return map.get( name );
    }

    public static void registerSecondaryDeviceOnName( String name, String deviceId )
    {
        TestContext ctx = threadContext.get();

        if ( ctx != null )
        {
            if (log.isInfoEnabled())
                log.info( "Attempting to acquire device for " + ctx.currentMethod.getName() );

            ConnectedDevice connectedDevice = DeviceManager.instance().getUnconfiguredDevice( ctx.currentMethod,
                                                                                              ( ( TestName ) ctx.testArgs[0] ).getTestName(),
                                                                                              ( ( TestName ) ctx.testArgs[0] ).getPersonaName(),
                                                                                              deviceId );

            if (connectedDevice != null)
            {
                putConnectedDevice( name, connectedDevice );
            }
        }
    }

    /**
     * After method.
     *
     * @param currentMethod the current method
     * @param testArgs the test args
     * @param testResult the test result
     */
    @AfterMethod
    public void afterMethod( Method currentMethod, Object[] testArgs, ITestResult testResult )
    {
        HashMap<String,ConnectedDevice> map = getDevicesToCleanUp();
        threadContext.set( null );
        Iterator<String> keys = ((map != null) ? map.keySet().iterator() : null );

        if( DataManager.instance().isArtifactEnabled( ArtifactType.DEVICE_LOG ) )
            PerfectoMobile.instance().device().startDebug( map.get( DEFAULT ).getExecutionId(), map.get( DEFAULT ).getDeviceName() );
        
        while(( keys != null ) &&
              ( keys.hasNext() ))
        {
            String name = keys.next();
            ConnectedDevice device = map.get( name );

            cleanUpConnectedDevice( name, device, currentMethod, testArgs, testResult );
        }
        
        //
        // Write out the index file for all tests
        //
        
        if( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_RECORD_HTML ) )
            RunDetails.instance().writeHTMLIndex( DataManager.instance().getReportFolder() );
        
        if( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_DEFINITION ) )
            RunDetails.instance().writeDefinitionIndex( DataManager.instance().getReportFolder() );
		
        DeviceManager.instance().clearAllArtifacts();
    }

    private void cleanUpConnectedDevice( String name, ConnectedDevice device, Method currentMethod, Object[] testArgs, ITestResult testResult )
    {
        WebDriver webDriver = device.getWebDriver();
        Device currentDevice = device.getDevice();

        if (webDriver != null)
        {
            String runKey = ((DEFAULT.equals( name )) ? (( TestName ) testArgs[0] ).getTestName() : (( TestName ) testArgs[0] ).getTestName() + "-" + name );
		    
            File rootFolder = new File( DataManager.instance().getReportFolder(), RunDetails.instance().getRootFolder() );
            rootFolder.mkdirs();
            //
            // If this test failed, run through the automatic downloads for a
            // failed test
            //
            if (!testResult.isSuccess())
            {
                if ( webDriver instanceof TakesScreenshot )
                {
                    OutputStream os = null;
                    try
                    {
                        byte[] screenShot = ( ( TakesScreenshot ) webDriver ).getScreenshotAs( OutputType.BYTES );
                        File useFolder = new File( rootFolder, runKey );
                        File outputFile = new File( useFolder, currentDevice.getKey() + System.getProperty( "file.separator" ) + "failure-screenshot.png" );
                        outputFile.getParentFile().mkdirs();
                        os = new BufferedOutputStream( new FileOutputStream( outputFile ) );
                        os.write( screenShot );
                        os.flush();
                        os.close();
                    }
                    catch( Exception e )
                    {
                        log.error( "Error taking screenshot", e );
                        try { os.close(); } catch( Exception e2 ) {}
                    }
                }

                if (DataManager.instance().getAutomaticDownloads() != null)
                {
                    if (webDriver instanceof ArtifactProducer)
                    {
                        if (DataManager.instance().getReportFolder() == null)
                            DataManager.instance().setReportFolder( new File( "." ) );

                        for (ArtifactType aType : DataManager.instance().getAutomaticDownloads())
                        {
                            if (aType.getTime() == ArtifactTime.ON_FAILURE)
                            {
                                try
                                {
                                    Artifact currentArtifact = ( ( ArtifactProducer ) webDriver ).getArtifact( webDriver, aType, device, runKey, testResult.getStatus() == ITestResult.SUCCESS );
                                    if ( currentArtifact != null )
                                        currentArtifact.writeToDisk( rootFolder );
                                }
                                catch (Exception e)
                                {
                                    log.error( "Error acquiring Artifacts", e );
                                }
                            }
                        }
                    }
                }
            }

            try
            {
                webDriver.close();
            }
            catch (Exception e)
            {
            }

            if (DataManager.instance().getAutomaticDownloads() != null)
            {
                if (webDriver instanceof ArtifactProducer)
                {
                    if (DataManager.instance().getReportFolder() == null)
                        DataManager.instance().setReportFolder( new File( "." ) );

                    for (ArtifactType aType : DataManager.instance().getAutomaticDownloads())
                    {
                        if (aType.getTime() == ArtifactTime.AFTER_TEST)
                        {
                            try
                            {
                                Artifact currentArtifact = ( ( ArtifactProducer ) webDriver ).getArtifact( webDriver, aType, device, runKey, testResult.getStatus() == ITestResult.SUCCESS );
                                if ( currentArtifact != null )
                                    currentArtifact.writeToDisk( rootFolder );
                            }
                            catch (Exception e)
                            {
                                log.error( "Error acquiring Artifacts - " + e);
                            }
                        }
                    }
                }
            }

            try
            {
                webDriver.quit();
            }
            catch (Exception e)
            {
            }
        }
		
        if (currentDevice != null)
        {
            DeviceManager.instance().addRun( currentDevice, currentMethod, ( ( TestName ) testArgs[0] ).getTestName(), testResult.isSuccess(), device.getPersona() );
            DeviceManager.instance().releaseDevice( currentDevice );
        }

    }
	
    @AfterSuite
    public void afterSuite()
    {
        if( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_RECORD_HTML ) )
        {
            File htmlFile = RunDetails.instance().getIndex( DataManager.instance().getReportFolder() );
            try
            {
                Desktop.getDesktop().browse( htmlFile.toURI() );
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }
        
        CloudRegistry.instance().shutdown();
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
        HashMap<String,ConnectedDevice> map = getDeviceMap();

        map.put( name, device );
    }

    private static HashMap<String,ConnectedDevice> getDeviceMap()
    {
        HashMap<String,ConnectedDevice> map = threadDevices.get();

        if ( map == null )
        {
            map = new HashMap<String,ConnectedDevice>();

            threadDevices.set( map );
        }

        return map;
    }

    private HashMap<String,ConnectedDevice> getDevicesToCleanUp()
    {
        HashMap<String,ConnectedDevice> map = threadDevices.get();

        threadDevices.set( null );

        return map;
    }
}
