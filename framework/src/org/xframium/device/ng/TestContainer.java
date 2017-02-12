package org.xframium.device.ng;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.cloud.action.CloudActionProvider;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.ng.RunContainer.RunStatus;
import org.xframium.spi.Device;


public class TestContainer
{
    private Log log = LogFactory.getLog( TestContainer.class );
    private RunContainer runContainer = new RunContainer();
    private LinkedBlockingDeque<TestName> testList;
    private List<TestName> checkedOut = new ArrayList<TestName>(64);
    private List<TestName> completedList = new ArrayList<TestName>( 64 );
    
    private List<Device> checkedOutDevice = new ArrayList<Device>(64);
    private LinkedBlockingDeque<Device> deviceList;
    
    private List<Device> errorOutDevice = new ArrayList<Device>(64); 
    
    public TestContainer( TestName[] testNames, Device[] devices )
    {
        testList = new LinkedBlockingDeque<TestName>( testNames.length );
        testList.addAll( (List<TestName>) Arrays.asList( testNames ) );
        
        deviceList = new LinkedBlockingDeque<Device>( devices.length );
        deviceList.addAll( (List<Device> ) Arrays.asList( devices ) );
        
        log.warn( "Executing " + testList.size() + " tests across " + deviceList.size() + " devices" );
        
    }
    
    public String getRunKey( Device currentDevice, Method currentMethod, String testContext, String personaName )
    {
        String runKey = currentDevice.getKey() + " - " + (testContext != null ? (testContext) : "");

        if ( personaName != null && !personaName.isEmpty() && !runKey.endsWith( personaName ) )
        {
            runKey = runKey + "." + personaName;
        }

        return runKey;
    }
    
    public CloudActionProvider getCloudActionProvider( CloudDescriptor currentCloud )
    {
        try
        {
            CloudActionProvider actionProvider = (CloudActionProvider) Class.forName( CloudActionProvider.class.getPackage().getName() + "." + currentCloud.getProvider() + "CloudActionProvider" ).newInstance();
            return actionProvider;
        }
        catch ( Exception e )
        {
            log.error( "Could not load cloud provider for " + currentCloud.getProvider() );
            return null;
        }
    }
    
    public TestPackage getTestPackage( Method currentMethod, boolean attachDevice )
    {
        Thread.currentThread().setName( "xF-Acquiring Test Package..." );
        TestName testName = null;
        
        while( true )
        {
            Device useDevice = getDevice();
            
            CloudDescriptor currentCloud = CloudRegistry.instance().getCloud();
            if ( useDevice.getCloud() != null && !useDevice.getCloud().isEmpty() )
                currentCloud = CloudRegistry.instance().getCloud( useDevice.getCloud() );

            
            for ( int i=0; i<testList.size(); i++ )
            {
                testName = getTest();
                String runKey = getRunKey( useDevice, currentMethod, testName.getTestName(), testName.getPersonaName() );
                
                if ( runContainer.addRun( runKey, RunStatus.RUNNING ) )
                {
                    Thread.currentThread().setName( "xF-" + testName.getRawName() + "-->" + useDevice.getEnvironment() );
                    TestPackage testPackage = new TestPackage( testName, useDevice, runKey );
                    ConnectedDevice cD = DeviceManager.instance().getDevice( testPackage, attachDevice );
                    if ( cD != null )
                    {
                        testPackage.setConnectedDevice( cD );
                        log.warn( "Test Started: " + runKey + " - " + cD );
                        return testPackage;
                    }
                    else
                    {
                        runContainer.addRun( runKey, RunStatus.RESET );
                        if ( DeviceManager.instance().isDeviceInvalid( useDevice ) )
                        {
                            TestPackage tP = new TestPackage( testName, useDevice, null );
                            DeviceWebDriver webDriver = new DeviceWebDriver( null, false, useDevice );
                            webDriver.setArtifactProducer( getCloudActionProvider( currentCloud ).getArtifactProducer() );
                            tP.setConnectedDevice( new ConnectedDevice( webDriver, useDevice, null ) );
                            return tP;
                        }
                    }
                }
                
                returnTest( testName );
            }
            
            returnDevice( useDevice );
            
            
            try { Thread.sleep( 1500 ); } catch( Exception e ) {}
        }
    }
    
    public int getDeviceCount()
    {
        return deviceList.size();
    }
    
    public Device getDevice()
    {
        Device device = null;
        try
        {
            while ( device == null && ( deviceList.size() > 0 || checkedOut.size() > 0 ) )
            {
                device = deviceList.poll( 5000, TimeUnit.MILLISECONDS );
                if ( device != null )
                    checkedOutDevice.add( device );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        log.debug( "ACQUIRED: Available Devices: " + deviceList.size() + " - Running Devices: " + checkedOutDevice.size() + " - Test Remaining: " + testList.size() );
        
        return device;
    }
    
    public void returnDevice( Device currentDevice )
    {
        try
        {
            
            deviceList.putLast( currentDevice );
            checkedOutDevice.remove( currentDevice );
            log.debug( "RETURNED: Available Devices: " + deviceList.size() + " - Running Devices: " + checkedOutDevice.size() + " - Test Remaining: " + testList.size() );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
    }
    
    public void returnTest( TestName currentTest )
    {
        try
        {
            if ( currentTest != null )
            {
                checkedOut.remove( currentTest );
                testList.putLast( currentTest );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public TestName getTest()
    {
        TestName testName = null;
        
        try
        {    
            testName = testList.take();
            if ( testName != null )
                checkedOut.add( testName );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        
        return testName;
    }
    
    public void destroyDevice( Device currentDevice )
    {
        checkedOutDevice.remove( currentDevice );
        errorOutDevice.add( currentDevice );
        log.error( "************** DESTROYING DEVICE due to errors :" + currentDevice );
    }
    
    public void completeTest( TestName testName, String runKey, RunStatus runStatus )
    {
        log.warn( "Test Completed: " + runKey + " - " + runStatus );
        runContainer.addRun( runKey, runStatus );
        checkedOut.remove( testName );
        completedList.add( testName );
    }
}
