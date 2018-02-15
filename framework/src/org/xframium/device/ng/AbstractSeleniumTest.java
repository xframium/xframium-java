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
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.xframium.Initializable;
import org.xframium.application.ApplicationRegistry;
import org.xframium.artifact.ArtifactManager;
import org.xframium.artifact.ArtifactTime;
import org.xframium.artifact.ArtifactType;
import org.xframium.content.ContentManager;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.factory.DriverManager;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordDriver.TRACE;
import org.xframium.page.keyWord.KeyWordPage;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.spi.KeyWordPageImpl;
import org.xframium.page.keyWord.step.KeyWordStepFactory;
import org.xframium.reporting.ExecutionContext;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.reporting.ExecutionContextTest.TestStatus;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractSeleniumTest.
 */
public abstract class AbstractSeleniumTest
{
    /** The log. */
    protected static Log log = LogFactory.getLog( AbstractSeleniumTest.class );

    
    protected ThreadLocal<TestPackage> testPackageContainer = new ThreadLocal<TestPackage>();
    
    private boolean xmlMode = true;


    public boolean isXmlMode()
    {
        return xmlMode;
    }

    public void setXmlMode( boolean xmlMode )
    {
        this.xmlMode = xmlMode;
    }

    protected Log testFlow = LogFactory.getLog( "testFlow" );
    /** The name of the default device **/

    /**
     * Gets the device data.
     *
     * @return the device data
     */
    @DataProvider ( name = "deviceManager", parallel = true)
    public Object[][] getDeviceData( ITestContext testContext )
    {
        String xFID = null;
        if ( Initializable.xFID == null || Initializable.xFID.get() == null )
        	xFID = (String) ((testContext.getAttribute( "xFID" ) != null)?(testContext.getAttribute( "xFID" )):testContext.getSuite().getAttribute( "xFID" ));
        else
            xFID = Initializable.xFID.get();

        List<Device> deviceList = DeviceManager.instance( xFID ).getDevices();
        return getDeviceData( deviceList, testContext, xFID );
    }

    private enum KeyType
    {
        PERSONA, CONTENT, ITERATION, TEST
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

    protected Object[][] getDeviceData( List<Device> deviceList, ITestContext testContext, String xFID )
    {
        List<TestName> rawList = new ArrayList<TestName>( 10 );
        List<TestName> finalList = new ArrayList<TestName>( 10 );
        List<TestKey> personaList = new ArrayList<TestKey>( 10 );
        List<TestKey> testList = new ArrayList<TestKey>( 10 );

        if ( DataManager.instance( xFID ).getPersonas() != null && DataManager.instance( xFID ).getPersonas().length > 0 )
        {
            for ( String pN : DataManager.instance( xFID ).getPersonas() )
                personaList.add( new TestKey( pN, KeyType.PERSONA ) );
        }
        
        if ( xmlMode )
        {
            for ( String pN : DataManager.instance( xFID ).getTests() )
            {
                testList.add( new TestKey( pN, KeyType.TEST ) );
            }
        

            for ( TestKey tK : testList )
            {
                KeyWordTest kT = KeyWordDriver.instance( xFID ).getTest( tK.getKey() );
                
                if ( kT == null )
                {
                    log.error( "Could not find test: " + tK.getKey() );
                }
                
                if ( kT.getContentKeys() != null && kT.getContentKeys().length > 0 )
                {
                    for ( String contentKey : kT.getContentKeys() )
                    {
                        if ( kT.getCount() > 1 )
                        {
                            for ( int i = 0; i < kT.getCount(); i++ )
                            {
                                if ( kT.getDataDriver() != null && kT.getDataDriver().trim().length() > 0 )
                                {
                                    PageData[] pageData = PageDataManager.instance( xFID ).getRecords( kT.getDataDriver() );
                                    for ( PageData pD : pageData )
                                    {
                                        TestName testName = new TestName( tK.getKey() );
                                        testName.setIteration( i + 1 );
                                        testName.setTestContext( contentKey );
                                        testName.setDataDriven( pD );
                                        rawList.add( testName );
                                    }
                                }
                                else
                                {
                                    TestName testName = new TestName( tK.getKey() );
                                    testName.setIteration( i + 1 );
                                    testName.setTestContext( contentKey );
                                    rawList.add( testName );
                                }
                            }
                        }
                        else
                        {
                            if ( kT.getDataDriver() != null && kT.getDataDriver().trim().length() > 0 )
                            {
                                PageData[] pageData = PageDataManager.instance( xFID ).getRecords( kT.getDataDriver() );
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
                        for ( int i = 0; i < kT.getCount(); i++ )
                        {
                            if ( kT.getDataDriver() != null && kT.getDataDriver().trim().length() > 0 )
                            {
                                PageData[] pageData = PageDataManager.instance( xFID ).getRecords( kT.getDataDriver() );
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
                            PageData[] pageData = PageDataManager.instance( xFID ).getRecords( kT.getDataDriver() );
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
                    testName.setDataDriven( tN.getDataDriven() );
                    finalList.add( testName );
                }
            }
        }
        else
            finalList.addAll( rawList );

        TestName[] newArray = null;

        newArray = new TestName[(finalList.size() == 0 ? 1 : finalList.size()) * deviceList.size()];

        if ( finalList.size() > 0 )
        {
            for ( int i = 0; i < finalList.size(); ++i )
            {
                for ( int j = 0; j < deviceList.size(); j++ )

                    newArray[i * deviceList.size() + j] = finalList.get( i ).clone();
            }
        }
        else
        {
            for ( int i = 0; i < deviceList.size(); i++ )

                newArray[i] = new TestName();
        }

        List<Device> fullDeviceList = new ArrayList<Device>( 10 );
        for ( Device d : deviceList )
        {
            for ( int i = 0; i < d.getAvailableDevices(); i++ )
                fullDeviceList.add( d.cloneDevice() );
        }

        StringBuilder logOut = new StringBuilder();
        
        if ( xmlMode )
        {
            logOut.append( "\r\n*********************************************************************\r\nPreparing to execute the following " + finalList.size() + " tests\r\n" );
            for ( TestName t : finalList )
                logOut.append( "\t" + t.getTestName() + "\r\n" );
        }
        else
            logOut.append( "\r\n*********************************************************************\r\nPreparing to execute\r\n" );
        
        logOut.append( "\r\nAgainst the following " ).append(  deviceList.size() ).append( " devices\r\n" );
        for ( Device d : deviceList )
        {
            logOut.append( "\t" + d.getEnvironment() + "\r\n" );
        }
        
        if ( finalList.isEmpty() && xmlMode )
        {
            log.fatal( "No scripts were defined - nothign to do!" );
            throw new ScriptConfigurationException( "No scripts were defined - nothign to do!" );
        }
        
        
        try
        {
            testContext.getSuite().getXmlSuite().setDataProviderThreadCount( fullDeviceList.size() );
            log.warn( "Thread count configured as " + fullDeviceList.size() );
        }
        catch( Exception e )
        {
            System.setProperty( "dataproviderthreadcount", fullDeviceList.size() + "" );
            log.warn( "Thread count configured as " + fullDeviceList.size() + " via system property" );
        }

        TestContainer testContainer = new TestContainer( newArray, fullDeviceList.toArray( new Device[0] ), xFID );

        
        Object[][] returnArray = new Object[newArray.length][1];
        for ( int i = 0; i < returnArray.length; i++ )
            returnArray[i][0] = testContainer;
        
        if ( xmlMode )
            logOut.append( "\r\nFor a total of " ).append( returnArray.length ).append( " total execution\r\n*********************************************************************" );

        log.warn( logOut.toString() );
        
        return returnArray;
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
     * Before method.
     *
     * @param currentMethod
     *            the current method
     * @param testArgs
     *            the test args
     */
    @BeforeMethod ( alwaysRun = true)
    public void beforeMethod( Method currentMethod, Object[] testArgs, ITestContext testContext )
    {
        try
        {
            TestContainer tC = ((TestContainer) testArgs[0]);
            if ( testContext.getAttribute( "testContainer" ) == null )
                testContext.setAttribute( "testContainer", tC );

            TestPackage testPackage = tC.getTestPackage( currentMethod, true, xmlMode );

            testPackageContainer.set( testPackage );

            TestName testName = testPackage.getTestName();
            
            
            
            if ( !xmlMode )
            {
                //
                // Stub out the test and the execution context
                //
                ExecutionContextTest eC = new ExecutionContextTest();
                eC.setxFID( tC.getxFID() );
                eC.setTestName( currentMethod.getName() );
                
                CloudDescriptor cD = CloudRegistry.instance(tC.getxFID()).getCloud();
                if ( testPackage.getDevice().getCloud() != null && !testPackage.getDevice().getCloud().trim().isEmpty() )
                    cD = CloudRegistry.instance(tC.getxFID()).getCloud( testPackage.getDevice().getCloud() );
                
                KeyWordTest kwt = new KeyWordTest( currentMethod.getName(), true, null, null, false, null, null, 0, currentMethod.getName() + " from " + currentMethod.getClass().getName(), null, null, null, ExecutionContext.instance(tC.getxFID()).getConfigProperties(), 0, null, null, null, null, 0, 0, TRACE.OFF.name() );
                eC.setTest( kwt );
                eC.setAut( ApplicationRegistry.instance(tC.getxFID()).getAUT() );
                eC.setCloud( cD );
                eC.setDevice( testPackage.getConnectedDevice().getPopulatedDevice() );
                testPackage.getConnectedDevice().getWebDriver().setExecutionContext( eC );
                
                testName.setTestName( currentMethod.getName() );
                
                testName.setTest( eC );
                
                File artifactFolder = new File( testPackage.getConnectedDevice().getDevice().getEnvironment(), testName.getTestName() );
                testPackage.getConnectedDevice().getWebDriver().setArtifactFolder( artifactFolder );
                
            }

            
            ConnectedDevice connectedDevice = testPackage.getConnectedDevice();

            String contentKey = testName.getContentKey();

            if ( (contentKey != null) && (contentKey.length() > 0) )
            {
                ContentManager.instance(tC.getxFID()).setCurrentContentKey( contentKey );
            }
            else
            {
                ContentManager.instance(tC.getxFID()).setCurrentContentKey( null );
            }

            if ( connectedDevice != null )
            {
                if ( testName.getTestName() == null || testName.getTestName().isEmpty() )
                    testName.setTestName( currentMethod.getDeclaringClass().getSimpleName() + "." + currentMethod.getName() );

                testName.setFullName( testArgs[0].toString() );

                if ( testFlow.isInfoEnabled() )
                    testFlow.info( Thread.currentThread().getName() + ": acquired for " + currentMethod.getName() );
            }

            
        }
        catch ( Exception e )
        {
            testFlow.fatal( Thread.currentThread().getName() + ": Fatal error configuring test", e );
        }
    }


    private String exceptionToString(Throwable t )
    {
        StringBuilder s = new StringBuilder();
        s.append( t.toString() ).append(  "\r\n" );
        for( StackTraceElement e : t.getStackTrace() )
            s.append( "\t" ).append( e.toString() ).append( "\r\n" );
        
        return s.toString();
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
    public void afterMethod( Method currentMethod, Object[] testArgs, ITestResult testResult, ITestContext testContext )
    {
        TestPackage testPackage = testPackageContainer.get();
        testPackageContainer.remove();
        try
        {

            if ( testPackage.getConnectedDevice().getWebDriver() != null && testPackage.getConnectedDevice().getWebDriver().isConnected() )
            {
                testPackage.getConnectedDevice().getWebDriver().getExecutionContext().setSessionId( testPackage.getConnectedDevice().getWebDriver().getExecutionId() );
                if ( !testResult.isSuccess() )
                {
                    if ( testResult.getThrowable() != null )
                    {
                        if ( testPackage.getConnectedDevice().getWebDriver().getExecutionContext().getStep() != null )
                        {
                            log.error( "Uncaught test failure", testResult.getThrowable() );
                            testPackage.getConnectedDevice().getWebDriver().getExecutionContext().getStep().getStep().dumpState( testPackage.getConnectedDevice().getWebDriver(), new HashMap<String,Object>(0), new HashMap<String,PageData>(0), testPackage.getConnectedDevice().getWebDriver().getExecutionContext() );
                            testPackage.getConnectedDevice().getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, new ScriptException( exceptionToString( testResult.getThrowable() ) ) );
                        }
                    }
                }
                
                try
                {
                    if ( ArtifactManager.instance( testPackage.getxFID() ).isArtifactEnabled( ArtifactType.DEVICE_LOG.name() ) )
                        testPackage.getConnectedDevice().getWebDriver().getCloud().getCloudActionProvider().disableLogging( testPackage.getConnectedDevice().getWebDriver() );

                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
            
            cleanUpConnectedDevice( "DEFAULT", testPackage.getTestName(), testPackage.getConnectedDevice(), testResult, true, testPackage );
            if ( testPackage.getConnectedDevice().getDevice() != null )
            {
                if ( DeviceManager.instance( testPackage.getxFID() ).addRun( testPackage.getConnectedDevice().getWebDriver().getPopulatedDevice(), testPackage, (TestContainer) testArgs[0], testResult.isSuccess() ) )
                {
                    if ( testFlow.isInfoEnabled() )
                        testFlow.info( Thread.currentThread().getName() + ": Adding Execution for " + testPackage.getRunKey() + " - " + testPackage.getTestName().getTest().getDevice().getKey() + " - " + testPackage.getDevice().getKey() + " - "+ testPackage + " - " + testPackage.getTestName() );
                    
                    ExecutionContext.instance( testPackage.getxFID() ).addExecution( testPackage.getTestName().getTest() );
                }
            }
            else
            {
            
                if ( testFlow.isInfoEnabled() )
                    testFlow.info( Thread.currentThread().getName() + ": Adding Execution for " + testPackage.getRunKey() + " - " + testPackage.getTestName().getTest().getDevice().getKey() + " - " + testPackage.getDevice().getKey() + " - "+ testPackage + " - " + testPackage.getTestName() );
                
                ExecutionContext.instance( testPackage.getxFID() ).addExecution( testPackage.getTestName().getTest() );
            }
            
            
            Map<String, ConnectedDevice> map = testPackage.getTestName().getTest().getDeviceMap();
            Iterator<String> keys = ((map != null) ? map.keySet().iterator() : null);
            while ( (keys != null) && (keys.hasNext()) )
            {
                String name = keys.next();
                ConnectedDevice device = map.get( name );
                
                cleanUpConnectedDevice( name, testPackage.getTestName(), device, testResult, true, testPackage );
            }

            

            try
            {
                Thread.currentThread().setName( "xF-Idle Thread" );
                KeyWordDriver.instance( testPackage.getxFID() ).notifyAfterArtifacts( testPackage.getConnectedDevice().getWebDriver(), testPackage.getConnectedDevice().getWebDriver().getExecutionContext().getTest(), null, null, null, testPackage.getConnectedDevice().getWebDriver().getExecutionContext().getStatus(), null, testPackage.getConnectedDevice().getWebDriver().getExecutionContext() );
            }
            catch ( Exception e )
            {
                testFlow.error( Thread.currentThread() + ": Error flushing artifacts", e );
            }
        }
        catch ( Exception e )
        {
            testFlow.fatal( Thread.currentThread().getName() + ": Fatal error completing test", e );
        }
        finally
        {
            ((TestContainer) testArgs[0]).returnDevice( testPackage.getDevice() );

            
        }
    }
    
    /**
     * Captures the state (image and source) of the running application
     *
     * @param webDriver the web driver
     * @throws Exception the exception
     */
    public void dumpState( DeviceWebDriver webDriver ) throws Exception
    {
        createStep( "STATE", "", "", new String[ 0 ]).executeStep( null, webDriver, null, null, null, null, webDriver.getExecutionContext() );
    }
    
    /**
     * Captures the state (image and source) of the running application and compares it for deviation against the previous historical values
     *
     * @param webDriver the web driver
     * @param name the name
     * @param historicalCount The number of historical records to compare to
     * @param deviationPercentage The amount of image deviation from 0 to 100
     * @throws Exception the exception
     */
    public void dumpState( DeviceWebDriver webDriver, String name, int historicalCount, int deviationPercentage) throws Exception
    {
        KeyWordStep step = createStep( "STATE", "", "", new String[ 0 ]);
        step.addParameter( new KeyWordParameter( ParameterType.STATIC, name, "checkPointName", null ) );
        step.addParameter( new KeyWordParameter( ParameterType.STATIC, historicalCount + "", "historicalCount", null ) );
        step.addParameter( new KeyWordParameter( ParameterType.STATIC, deviationPercentage + "", "deviationPercentage", null ) );
        step.executeStep( null, webDriver, null, null, null, null, webDriver.getExecutionContext() );
    }
    
    /**
     * Creates a KeyWord step to be used in a Java test
     *
     * @param keyword The name of the keyword
     * @param pageName the page name
     * @param elementName the element name
     * @param parameterList A list of parameter to pass to the keyword.  User name==value for named parameters
     * @return The created step
     */
    public KeyWordStep createStep( String keyword, String pageName, String elementName, String[] parameterList )
    {
        KeyWordStep step = KeyWordStepFactory.instance().createStep( elementName, pageName, true, keyword, null, false, StepFailure.ERROR, false, null, null, null, 0, null, 0, keyword, null, null, null, null, false, false, null, null, null, null, null, null, false );
        
        if ( parameterList != null )
        {
            for ( String s : parameterList )
            {
                String[] sArray = s.split( "==" );
                if ( s.contains( "==" ) )
                    step.addParameter( new KeyWordParameter( ParameterType.STATIC, sArray.length > 1 ? sArray[1] : "{EMPTY}", sArray[0], null ) );
                else
                    step.addParameter( new KeyWordParameter( ParameterType.STATIC, s, null, null ) );
            }
        }
        
        return step;
        
    }
    
    /**
     * Execute a keyword step
     *
     * @param step the step
     * @param webDriver the web driver
     * @return true, if successful
     * @throws Exception the exception
     */
    public Map<String,Object> executeStep( KeyWordStep step, DeviceWebDriver webDriver ) throws Exception
    {
        KeyWordPage p = new KeyWordPageImpl(PageManager.instance(webDriver.getxFID()).getElementProvider(), PageManager.instance(webDriver.getxFID()).getSiteName());
        p.setPageName( step.getPageName() );
        Map<String,Object> contextMap = new HashMap<String,Object>( 10 );
        contextMap.put( "RESULT", step.executeStep( p, webDriver, contextMap, null, null, null, webDriver.getExecutionContext() ) );
        return contextMap;
    }
    
    /**
     * Creates and executes a KeyWord step
     *
     * @param keyword The name of the keyword
     * @param pageName the page name
     * @param elementName the element name
     * @param parameterList A list of parameter to pass to the keyword.  User name==value for named parameters
     * @param webDriver The webDriver for the active run
     * @return The created step
     */
    public Map<String,Object> executeStep( String keyword, String pageName, String elementName, String[] parameterList, DeviceWebDriver webDriver ) throws Exception
    {
        KeyWordPage p = new KeyWordPageImpl(PageManager.instance(webDriver.getxFID()).getElementProvider(), PageManager.instance(webDriver.getxFID()).getSiteName());
        p.setPageName( pageName );
        Map<String,Object> contextMap = new HashMap<String,Object>( 10 );
        contextMap.put( "RESULT", createStep( keyword, pageName, elementName, parameterList ).executeStep( p, webDriver, null, null, null, null, webDriver.getExecutionContext() ) );
        return contextMap;
    }

    private void cleanUpConnectedDevice( String name, TestName testName, ConnectedDevice device, ITestResult testResult, boolean primaryDevice, TestPackage testPackage )
    {
        DeviceWebDriver webDriver = device.getWebDriver();
        ExecutionContextTest test = null;

        if ( testFlow.isInfoEnabled() )
        {
            testFlow.info( Thread.currentThread().getName() + ": Attempting to clean up " + testName.getTestName() + " on " + device.getPopulatedDevice().getEnvironment() );
        }
        
        try
        {
            if ( webDriver != null )
            {

                if ( primaryDevice )
                {
                    test = testName.getTest();
                    
                    if ( test != null && !xmlMode )
                    {
                        webDriver.getExecutionContext().completeTest( testResult.isSuccess() ? TestStatus.PASSED : TestStatus.FAILED, null );
                    }
                    
                }

                if ( DataManager.instance( testPackage.getxFID() ).getReportFolder() == null )
                    DataManager.instance( testPackage.getxFID() ).setReportFolder( new File( "." ) );
                
                File rootFolder = null;
                try
                {
                    try
                    {
                        rootFolder = new File( ExecutionContext.instance( testPackage.getxFID() ).getReportFolder( testPackage.getxFID() ), webDriver.getArtifactFolder().getPath() );
                        rootFolder.mkdirs();
                    }
                    catch( Exception e )
                    {
                        log.warn( "Not generating artifacts for " + device );
                        rootFolder = null;
                    }
                    
                    
                    
                    
                    if ( webDriver.isConnected() && !testResult.isSuccess() )
                    {
                        List<String> aList = ArtifactManager.instance( testPackage.getxFID() ).getEnabledArtifacts( ArtifactTime.ON_FAILURE );
                        if ( aList != null )
                        {
                            for ( String artifactType : aList )
                            {
                                if ( rootFolder != null )
                                    ArtifactManager.instance( testPackage.getxFID() ).generateArtifact( artifactType, rootFolder.getAbsolutePath(), webDriver, webDriver.getxFID() );
                            }
                        }
                    }
                }
                finally
                {
                    try
                    {
                        if ( testFlow.isInfoEnabled() )
                            testFlow.info( Thread.currentThread().getName() + ":Closing WebDriver " );
                        webDriver.close();
                    }
                    catch ( Exception e )
                    {
                        
                    }
                }
                
                List<String> aList = ArtifactManager.instance( testPackage.getxFID() ).getEnabledArtifacts( ArtifactTime.AFTER_TEST );
                
                if ( aList != null )
                {
                    for ( String artifactType : aList )
                    {
                        if ( rootFolder != null )
                            ArtifactManager.instance( testPackage.getxFID() ).generateArtifact( artifactType, rootFolder.getAbsolutePath(), webDriver, webDriver.getxFID() );
                    }
                }
            
                aList = ArtifactManager.instance( testPackage.getxFID() ).getEnabledArtifacts( ArtifactTime.AFTER_ARTIFACTS );
                if ( aList != null )
                {
                    for ( String artifactType : aList )
                    {
                        if ( rootFolder != null )
                            ArtifactManager.instance( testPackage.getxFID() ).generateArtifact( artifactType, rootFolder.getAbsolutePath(), webDriver, webDriver.getxFID() );
                    }
                }

            }
        }
        finally
        {
            try
            {
                if ( testFlow.isInfoEnabled() )
                    testFlow.info( Thread.currentThread().getName() + ": Quiting WebDriver " );
                webDriver.quit();
                
            }
            catch ( Exception e )
            {
                
            }

        }

    }

    //
    // Helpers
    //

//    private static class TestContext
//    {
//        public Method currentMethod = null;
//        public Object[] testArgs = null;
//    }

//    private static void putConnectedDevice( String name, ConnectedDevice device )
//    {
//        HashMap<String, ConnectedDevice> map = getDeviceMap();
//
//        map.put( name, device );
//    }
//
//    private static HashMap<String, ConnectedDevice> getDeviceMap()
//    {
//        HashMap<String, ConnectedDevice> map = threadDevices.get();
//
//        if ( map == null )
//        {
//            map = new HashMap<String, ConnectedDevice>();
//
//            threadDevices.set( map );
//        }
//
//        return map;
//    }
//
//    private HashMap<String, ConnectedDevice> getDevicesToCleanUp()
//    {
//        HashMap<String, ConnectedDevice> map = threadDevices.get();
//
//        threadDevices.set( null );
//
//        return map;
//    }
}
