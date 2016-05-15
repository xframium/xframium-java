/*
 * 
 */
package com.xframium.device;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xframium.artifact.ArtifactListener;
import com.xframium.artifact.ArtifactManager;
import com.xframium.artifact.ArtifactType;
import com.xframium.device.comparator.WeightedDeviceComparator;
import com.xframium.device.data.NamedDataProvider;
import com.xframium.device.data.DataProvider.DriverType;
import com.xframium.device.factory.DeviceWebDriver;
import com.xframium.device.factory.DriverManager;
import com.xframium.device.property.PropertyAdapter;
import com.xframium.spi.Device;
import com.xframium.spi.RunListener;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceManager.
 */
public class DeviceManager implements ArtifactListener
{
    /** The singleton. */
    private static DeviceManager singleton = new DeviceManager();
	
    /** The execution id. */
    private ThreadLocal<String> executionId = new ThreadLocal<String>();
	
    /** Stores an instance to a log handler */
    private ThreadLocal<StringBuffer> logHandler = new ThreadLocal<StringBuffer>();
	
    private ThreadLocal<Map<ArtifactType,List<Object>>> artifactMap = new ThreadLocal<Map<ArtifactType,List<Object>>>();
	
    private String deviceInterrupts;
	
    private List<PropertyAdapter> propertyAdapterList = new ArrayList<PropertyAdapter>( 10 );
	
    private Properties configurationProperties;
	
    private Device selectedDevice;

    private DriverType driverType;

    public void setDriverType( DriverType val )
    {
        this.driverType = val;
    }
	
    public DriverType getDriverType()
    {
        return driverType;
    }


    public void setSelectedDevice( Device selectedDevice )
    {
        this.selectedDevice = selectedDevice;
    }
	
    public Device getSelectedDevice()
    {
        return selectedDevice;
    }

    public Properties getConfigurationProperties()
    {
        return configurationProperties;
    }

    public void setConfigurationProperties( Properties configurationProperties )
    {
        this.configurationProperties = configurationProperties;
    }

    public String getDeviceInterrupts()
    {
        return deviceInterrupts;
    }

    public void setDeviceInterrupts( String deviceInterrupts )
    {
        this.deviceInterrupts = deviceInterrupts;
    }
    
    public void registerPropertyAdapter( PropertyAdapter propertyAdapter )
    {
        propertyAdapterList.add( propertyAdapter );
    }
    
    public void notifyPropertyAdapter( Properties configurationProperties )
    {
        if ( configurationProperties != null )
        {
            for ( PropertyAdapter p : propertyAdapterList )
                p.applyProperties( configurationProperties );
        }
    }
    
    public void notifyPropertyAdapter( Properties configurationProperties, Object webDriver )
    {
        if ( configurationProperties != null )
        {
            for ( PropertyAdapter p : propertyAdapterList )
                p.applyInstanceProperties( configurationProperties, webDriver );
        }
    }
    
    public void addArtifactRecord( ArtifactType aType, Object value )
    {
        if ( artifactMap.get() == null )
            artifactMap.set( new HashMap<ArtifactType,List<Object>>( 10 ) );
        
        List<Object> artifactList = artifactMap.get().get( aType );
        if ( artifactList == null )
        {
            artifactList = new ArrayList<Object>(10);
            artifactMap.get().put( aType, artifactList );
        }
        
        artifactList.add( value );        
    }
    
    public List<Object> getArtifacts( ArtifactType aType )
    {
        if ( artifactMap.get() == null )
            return null;
        
        return artifactMap.get().get( aType );
    }
    
    public void clearArtifacts( ArtifactType aType )
    {
        if ( artifactMap.get() == null )
            return;
        
        if ( artifactMap.get().get( aType ) != null )
            artifactMap.get().get( aType ).clear(); 
    }
    
    public void clearAllArtifacts()
    {
        artifactMap.set( new HashMap<ArtifactType,List<Object>>( 10 ) );
    }
    
    public void addLog( String logMessage )
    {
        if ( logHandler.get() == null )
            logHandler.set( new StringBuffer() );
        
        logHandler.get().append( logMessage );
    }
    
    public void writeLog( File fileName )
    {
        try
        {
            fileName.getParentFile().mkdirs();
            FileWriter outputStream = new FileWriter( fileName );
            outputStream.write( logHandler.get().toString() );
            logHandler.set( new StringBuffer() );
            outputStream.close();
        }
        catch( Exception e )
        {
            log.error( "Error writing console log", e );
        }
    }
    
    public String getLog()
    {
        if ( logHandler.get() != null )
            return logHandler.get().toString();
        else
            return "";
    }
    
    public void clearLog()
    {
        logHandler.set( new StringBuffer() );
    }

    /**
     * Instance.
     *
     * @return the device manager
     */
    public static DeviceManager instance()
    {
        return singleton;
    }

    /**
     * Instantiates a new device manager.
     */
    private DeviceManager()
    {
        ArtifactManager.instance().addArtifactListener( this );
    }
	
    /** The log. */
    private Log log = LogFactory.getLog( DeviceManager.class );
	
    /** The manager lock. */
    private Lock managerLock = new ReentrantLock();
	
    /** The device map. */
    private Map<String, Device> deviceMap = new HashMap<String, Device>( 20 );
	
    /** The device list. */
    private List<Device> deviceList = new LinkedList<Device>();
	
    /** The device comparator. */
    private Comparator<Device> deviceComparator = new WeightedDeviceComparator();
	
    /** The retry count. */
    private int retryCount = 25;

    /** The analytics map. */
    private Map<String, DeviceAnalytics> analyticsMap = new HashMap<String, DeviceAnalytics>( 20 );
	
    /** The active runs. */
    private Map<String,Boolean> activeRuns = new HashMap<String,Boolean>( 20 );

    /** The run listeners. */
    private List<RunListener> runListeners = new ArrayList<RunListener>( 20 );
	
    /** The caching enabled. */
    private boolean cachingEnabled = false;
	
    /** The dry run. */
    private boolean dryRun = false;

	
	
    /**
     * Checks if is dry run.
     *
     * @return true, if is dry run
     */
    public boolean isDryRun()
    {
        return dryRun;
    }

    /**
     * Sets the dry run.
     *
     * @param dryRun the new dry run
     */
    public void setDryRun( boolean dryRun )
    {
        this.dryRun = dryRun;
    }

    /**
     * Sets the execution id.
     *
     * @param executionId the new execution id
     */
    public void setExecutionId( String executionId )
    {
        this.executionId.set( executionId );
    }
	
    /**
     * Gets the execution id.
     *
     * @return the execution id
     */
    public String getExecutionId()
    {
        return executionId.get();
    }
	
    /**
     * Adds the run listener.
     *
     * @param runListener the run listener
     */
    public void addRunListener( RunListener runListener )
    {
        runListeners.add( runListener );
    }
	
    /**
     * Removes the run listener.
     *
     * @param runListener the run listener
     */
    public void removeRunListener( RunListener runListener )
    {
        runListeners.remove( runListener );
    }
	
	
    /**
     * Notify before run.
     *
     * @param currentDevice the current device
     * @param runKey the run key
     * @return true, if successful
     */
    private boolean notifyBeforeRun( Device currentDevice, String runKey )
    {
        for ( RunListener runListener : runListeners )
        {
            try
            {
                if ( !runListener.beforeRun( currentDevice, runKey ) )
                    return false;
            }
            catch( Exception e )
            {
                log.error( "Error executing run listener", e );
            }
        }
		
        return true;
    }
	
    /**
     * Notify after run.
     *
     * @param currentDevice the current device
     * @param runKey the run key
     * @param successful the successful
     */
    private void notifyAfterRun( Device currentDevice, String runKey, boolean successful )
    {
        for ( RunListener runListener : runListeners )
        {
            try
            {
                runListener.afterRun( currentDevice, runKey, successful );
            }
            catch( Exception e )
            {
                log.error( "Error executing run listener", e );
            }
        }
    }
	
    /**
     * Sets the retry count.
     *
     * @param retryCount the new retry count
     */
    public void setRetryCount( int retryCount )
    {
        this.retryCount = retryCount;
    }
	
    /**
     * Sets the device comparator.
     *
     * @param deviceComparator the new device comparator
     */
    public void setDeviceComparator( Comparator<Device> deviceComparator )
    {
        this.deviceComparator = deviceComparator;
    }

    /**
     * Gets the device comparator.
     *
     * @return the device comparator
     */
    public Comparator<Device> getDeviceComparator()
    {
        return deviceComparator;
    }

    /**
     * Gets the device.
     *
     * @param deviceKey the device key
     * @return the device
     */
    public Device getDevice( String deviceKey )
    {
        return deviceMap.get( deviceKey );

    }

    /**
     * This will cycle through the available devices, by weight, to locate a
     * device that has not run this method yet.
     *
     * @param currentMethod            The current test method
     * @param testContext the test context
     * @param attachDevice the attach device
     * @param personaName the persona name
     * @return The next available device or null if no device are available
     */
    public ConnectedDevice getDevice( Method currentMethod, String testContext, boolean attachDevice, String personaName )
    {
		
        for ( int i=0; i<retryCount; i++ )
        {
            boolean deviceLocked = true;
			
            //
            // If the lock is a local lock then we will not increment the retry counter.  If it failed 
            //
            while( deviceLocked )
            {
                if (log.isDebugEnabled())
                    log.debug( Thread.currentThread().getName() + ": Acquiring Device Manager Lock (Iteration #" + i + ")" );
                managerLock.lock();
		
                try
                {
                    String runKey = currentMethod.getDeclaringClass().getSimpleName() + "." + currentMethod.getName() + ( testContext != null ? ( "." + testContext ) : "" );
                    if ( personaName != null && !personaName.isEmpty() )
                        runKey = runKey + "." + personaName;
					
                    for (Device currentDevice : deviceList)
                    {
                        //
                        // Attempt to acquire a lock for the device
                        //
                        if (log.isDebugEnabled())
                            log.debug( Thread.currentThread().getName() + ": Attempting to acquire semaphore for " + currentDevice );
                        if (currentDevice.getLock().tryAcquire())
                        {
                            //
                            // Now, make sure this test has not run on this device yet and that there are no active runs against it
                            //
                            if (log.isDebugEnabled())
                                log.debug( Thread.currentThread().getName() + ": Device Semaphore permitted for " + currentDevice );
                            if (!analyticsMap.get( currentDevice.getKey() ).hasRun( runKey ) && !activeRuns.containsKey( currentDevice.getKey() + "." + runKey ) )
                            {
                                if (log.isDebugEnabled())
                                    log.debug( Thread.currentThread().getName() + ": Selected " + currentDevice );
		
                                //
                                // Notify any listeners about this device acquisition and allow them to cancel it
                                //
                                if ( !notifyBeforeRun( currentDevice, runKey ) )
                                {
                                    if (log.isDebugEnabled())
                                        log.debug( Thread.currentThread().getName() + ": A registered RUN LISTENER cancelled this device request - Releasing Semaphore for " + currentDevice );
		
                                    currentDevice.getLock().release();
                                }
                                else
                                {
									
                                    //
                                    // If we made it here then we are not locally locking the device
                                    //
                                    deviceLocked = false;
									
                                    if ( attachDevice && !dryRun )
                                    {

                                        //
                                        // Create the WebDriver here if we are attaching this device
                                        //
                                        DeviceWebDriver webDriver = null;
                                        try
                                        {
                                            if ( log.isDebugEnabled() )
                                                log.debug( "Attempting to create WebDriver instance for " + currentDevice );
											
                                            if ( personaName != null && !personaName.isEmpty() )
                                                currentDevice.addCapability("windTunnelPersona", personaName);
											
                                            webDriver = DriverManager.instance().getDriverFactory( currentDevice.getDriverType() ).createDriver( currentDevice );
											
                                            if ( webDriver != null )
                                            {
                                                if ( log.isDebugEnabled() )
                                                    log.debug( "WebDriver Created - Creating Connected Device for " + currentDevice );
											
                                                DeviceManager.instance().notifyPropertyAdapter( configurationProperties, webDriver );

                                                activeRuns.put( currentDevice.getKey() + "." + runKey , true );
												
                                                return new ConnectedDevice( webDriver, currentDevice, personaName );
                                            }
                                        }
                                        catch( Exception e )
                                        {
                                            log.error( "Error creating factory instance", e );
                                            try { webDriver.close(); } catch( Exception e2 ) {}
                                            try { webDriver.quit(); } catch( Exception e2 ) {}
                                        }
										
                                        //
                                        // If we are here, the driver failed
                                        //
                                        if (log.isDebugEnabled())
                                            log.debug( Thread.currentThread().getName() + ": Releasing unused Device Semaphore for " + currentDevice );
                                        currentDevice.getLock().release();
										
                                    }
                                    else
                                    {
                                        activeRuns.put( currentDevice.getKey() + "." + runKey , true );
                                        return new ConnectedDevice( null, currentDevice, personaName );
                                    }
                                }
									
									
                            }
                            else
                            {
                                if (log.isDebugEnabled())
                                    log.debug( Thread.currentThread().getName() + ": Releasing unused Device Semaphore for " + currentDevice );
                                currentDevice.getLock().release();
                            }
                        }
                    }
                }
                finally
                {
                    if (log.isDebugEnabled())
                        log.debug( Thread.currentThread().getName() + ": Releasing Device Manager Lock" );
                    try { managerLock.unlock(); } catch( Exception e ) {}
                }
				
                //
                // Pause and wait to reload
                //
                try
                {
                    Thread.sleep( 2500 );
                }
                catch( Exception e )
                {}
            }
        }

        return null;

    }

    /**
     * This will cycle through the available devices, by weight, to locate a
     * device that has not run this method yet.
     *
     * @param currentMethod            The current test method
     * @param testContext the test context
     * @param attachDevice the attach device
     * @param personaName the persona name
     * @return The next available device or null if no device are available
     */
    public ConnectedDevice getUnconfiguredDevice( Method currentMethod,
                                                  String testContext,
                                                  String personaName,
                                                  String deviceId )
    {
        ConnectedDevice rtn = null;
        Device currentDevice = NamedDataProvider.lookupDeviceById( deviceId, driverType );

        String runKey = currentMethod.getDeclaringClass().getSimpleName() + "." +
            currentMethod.getName() + ( testContext != null ? ( "." + testContext ) : "" );
        if ( personaName != null && !personaName.isEmpty() )
            runKey = runKey + "." + personaName;

        if ((( analyticsMap.get( currentDevice.getKey() ) == null ) ||
             ( !analyticsMap.get( currentDevice.getKey() ).hasRun( runKey ))) &&
            ( !activeRuns.containsKey( currentDevice.getKey() + "." + runKey )))
        {
            DeviceWebDriver webDriver = null;
            try
            {
                if ( log.isDebugEnabled() )
                    log.debug( "Attempting to create WebDriver instance for " + currentDevice );
		
                if ( personaName != null && !personaName.isEmpty() )
                    currentDevice.addCapability("windTunnelPersona", personaName);
                
                webDriver = DriverManager.instance().getDriverFactory( currentDevice.getDriverType() ).createDriver( currentDevice );
		
                if ( webDriver != null )
                {
                    if ( log.isDebugEnabled() )
                        log.debug( "WebDriver Created - Creating Connected Device for " + currentDevice );
                    
                    DeviceManager.instance().notifyPropertyAdapter( configurationProperties, webDriver );
                    
                    activeRuns.put( currentDevice.getKey() + "." + runKey , true );
                    
                    rtn = new ConnectedDevice( webDriver, currentDevice, personaName );
                }
            }
            catch( Exception e )
            {
                log.error( "Error creating factory instance", e );
                try { webDriver.close(); } catch( Exception e2 ) {}
                try { webDriver.quit(); } catch( Exception e2 ) {}
            }
        }
        else
        {
            if (log.isDebugEnabled())
                log.debug( Thread.currentThread().getName() + ": Releasing unused Device Semaphore for " + currentDevice );
        }

        return rtn;
    }
	
    /**
     * Release device.
     *
     * @param currentDevice the current device
     */
    public void releaseDevice( Device currentDevice )
    {
        if (log.isDebugEnabled())
            log.debug( Thread.currentThread().getName() + ": Releasing Device Semaphore for " + currentDevice );
        currentDevice.getLock().release();
    }

    /**
     * Gets the usage.
     *
     * @param currentDevice the current device
     * @return the usage
     */
    public int getUsage( Device currentDevice )
    {
        return analyticsMap.get( currentDevice.getKey() ).getUsage();
    }

    public String getRunKey( Device currentDevice, Method currentMethod, String testContext, boolean success, String personaName )
    {
        String runKey = currentMethod.getDeclaringClass().getSimpleName() + "." + currentMethod.getName() + ( testContext != null ? ( "." + testContext ) : "" );
        if ( personaName != null && !personaName.isEmpty() && !runKey.endsWith( personaName ) )
        {
            runKey = runKey + "." + personaName;
        }
        
        return runKey;
    }
	
    /**
     * Adds the run.
     *
     * @param currentDevice the current device
     * @param currentMethod the current method
     * @param testContext the test context
     * @param success the success
     * @param personaName the persona name
     */
    public void addRun( Device currentDevice, Method currentMethod, String testContext, boolean success, String personaName )
    {
		
		
        if (log.isDebugEnabled())
            log.debug( Thread.currentThread().getName() + ": Acquiring Device Manager Lock" );
        managerLock.lock();
        try
        {
            String runKey = getRunKey( currentDevice, currentMethod, testContext, success, personaName );
			
            if (log.isInfoEnabled())
                log.info( Thread.currentThread().getName() + ": Adding run [" + runKey + "] to device " + currentDevice );
            analyticsMap.get( currentDevice.getKey() ).addRun( runKey );
			
            activeRuns.remove( currentDevice.getKey() + "." + runKey );
			
            notifyAfterRun( currentDevice, runKey, success );
            Collections.sort( deviceList, deviceComparator );
        }
        finally
        {
            if (log.isDebugEnabled())
                log.debug( Thread.currentThread().getName() + ": Releasing Device Manager Lock" );
            managerLock.unlock();
        }
    }

    /**
     * Gets the devices.
     *
     * @return the devices
     */
    public List<Device> getDevices()
    {
        return Collections.unmodifiableList( deviceList );
    }
	
	
	
    /**
     * Register device.
     *
     * @param currentDevice the current device
     */
    public void registerDevice( Device currentDevice )
    {
        if (log.isInfoEnabled())
            log.info( "Registering Device " + currentDevice );
        deviceMap.put( currentDevice.getKey(), currentDevice );
        analyticsMap.put( currentDevice.getKey(), new DeviceAnalytics( currentDevice.getKey() ) );
        deviceList.add( currentDevice );
    }

	/**
	 * Checks if is caching enabled.
	 *
	 * @return true, if is caching enabled
	 */
	public boolean isCachingEnabled()
	{
		return cachingEnabled;
	}

    /**
     * Sets the caching enabled.
     *
     * @param cachingEnabled the new caching enabled
     */
    public void setCachingEnabled( boolean cachingEnabled )
    {
        this.cachingEnabled = cachingEnabled;
    }  
	
}
