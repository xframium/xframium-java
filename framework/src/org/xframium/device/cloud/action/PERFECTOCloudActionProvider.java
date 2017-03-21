package org.xframium.device.cloud.action;

import org.openqa.selenium.ContextAware;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationRegistry;
import org.xframium.device.SimpleDevice;
import org.xframium.device.artifact.ArtifactProducer;
import org.xframium.device.artifact.api.PerfectoArtifactProducer;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.DeviceConfigurationException;
import org.xframium.exception.DeviceException;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.integrations.common.PercentagePoint;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.perfectoMobile.rest.bean.Execution;
import org.xframium.integrations.perfectoMobile.rest.bean.Handset;
import org.xframium.integrations.perfectoMobile.rest.services.WindTunnel.TimerPolicy;
import org.xframium.page.BY;
import org.xframium.page.element.Element;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;

public class PERFECTOCloudActionProvider extends AbstractCloudActionProvider
{
	/** The Constant PLATFORM_NAME. */
	public static final String PLATFORM_NAME = "platformName";
	
	private String createTimerId( Element element )
	{
	    StringBuilder stringBuilder = new StringBuilder();
	    if ( element != null )
	        stringBuilder.append( element.getPageName() ).append( "." ).append( element.getName() );
	    else
	        stringBuilder.append( "Unknown Action" );
	    
	    return stringBuilder.toString();
	}

	@Override
	public void tap( DeviceWebDriver webDriver, PercentagePoint location, int lengthInMillis )
	{
	    PerfectoMobile.instance().gestures().tap( webDriver.getExecutionId(), webDriver.getDeviceName(), location, lengthInMillis / 1000 );
	}
	
	@Override
	public boolean getSupportedTimers( DeviceWebDriver webDriver, String timerId, ExecutionContextTest executionContext, String type )
	{
	    if( timerId == null )
	        return false;
	    
	    executionContext.getStep().addTiming( (type != null && !type.isEmpty()) ? "ux." + type : "ux", PerfectoMobile.instance().windTunnel().getTimer( webDriver.getExecutionId(), timerId, "ux", "milliseconds" ).getReturnValue() );
	    executionContext.getStep().addTiming( (type != null && !type.isEmpty()) ? "device." + type : "device", PerfectoMobile.instance().windTunnel().getTimer( webDriver.getExecutionId(), timerId, "device", "milliseconds" ).getReturnValue() );
	    executionContext.getStep().addTiming( (type != null && !type.isEmpty()) ? "system." + type : "system", PerfectoMobile.instance().windTunnel().getTimer( webDriver.getExecutionId(), timerId, "system", "milliseconds" ).getReturnValue() );
	    executionContext.getStep().addTiming( (type != null && !type.isEmpty()) ? "elapsed." + type : "elapsed", PerfectoMobile.instance().windTunnel().getTimer( webDriver.getExecutionId(), timerId, "elapsed", "milliseconds" ).getReturnValue() );
	    stopTimer( webDriver, timerId, executionContext );
	    return true;
	}
	
	@Override
	public String startTimer( DeviceWebDriver webDriver, Element element, ExecutionContextTest executionContext )
	{
	    String timerId = createTimerId( element );
	    PerfectoMobile.instance().windTunnel().startTimer( webDriver.getExecutionId(), timerId, TimerPolicy.reset ).getReturnValue();
	    executionContext.setTimerName( timerId );
	    return timerId;
	}
	
	@Override
	public void stopTimer( DeviceWebDriver webDriver, String timerId, ExecutionContextTest executionContext )
	{
	    PerfectoMobile.instance().windTunnel().stopTimer( webDriver.getExecutionId(), timerId );
	    executionContext.clearTimer();

	}
	
	
	@Override
    public boolean startApp( String executionId, String deviceId, String appName, String appIdentifier )
    {
        PerfectoMobile.instance().application().open( executionId, deviceId, appName, appIdentifier );
        return true;
    }

    @Override
    public boolean popuplateDevice( DeviceWebDriver webDriver, String deviceId, Device device )
    {
        try
        {
            Handset handSet = PerfectoMobile.instance().devices().getDevice( deviceId );

            device.setOs( handSet.getOs() );
            if ( device.getOs().toLowerCase().equals( "android" ) || device.getOs().toLowerCase().equals( "ios" ) )
                device.setModel( handSet.getModel() );
            
            device.setOsVersion( handSet.getOsVersion() );
            device.setResolution( handSet.getResolution() );
            device.setManufacturer( handSet.getManufacturer() );
            device.setDeviceName( handSet.getDeviceId() );
            
            ((SimpleDevice) device).setDeviceName( deviceId );
            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }
    
    @Override
    public boolean isDescriptorSupported( BY descriptorType )
    {
        return true;
    }
    
    @Override
    public String getExecutionId( DeviceWebDriver webDriver )
    {
        return webDriver.getCapabilities().getCapability( "executionId" ) + "";
    }
    
    @Override
    public ArtifactProducer getArtifactProducer()
    {
        return new PerfectoArtifactProducer();
    }
    
    @Override
    public void disableLogging( DeviceWebDriver webDriver )
    {
        PerfectoMobile.instance().device().startDebug( webDriver.getExecutionId(), webDriver.getDeviceName() );
        
    }
    
    @Override
    public void enabledLogging( DeviceWebDriver webDriver )
    {
        PerfectoMobile.instance().device().stopDebug( webDriver.getExecutionId(), webDriver.getDeviceName() );
        
    }

    @Override
	public String getCloudPlatformName(Device device) {
		// TODO Auto-generated method stub
		return PLATFORM_NAME;
	}
	
	@Override
	public String getCloudBrowserName(String currBrowserName) {
		if(currBrowserName.equalsIgnoreCase("Chrome")){
			return "Chrome";
		}else if (currBrowserName.equalsIgnoreCase("internet explorer")||currBrowserName.equalsIgnoreCase("internetexplorer")){
			return "Internet Explorer";
		}else if(currBrowserName.equalsIgnoreCase("firefox")){
			return "Firefox";
		}else{
			return currBrowserName;
		}
	}

    @Override
    public boolean installApplication( String applicationName, DeviceWebDriver webDriver, boolean instrumentApp )
    {
        ApplicationDescriptor appDesc = ApplicationRegistry.instance().getApplication( applicationName );
    
        Handset localDevice = PerfectoMobile.instance().devices().getDevice( webDriver.getPopulatedDevice().getDeviceName() );
        
        Execution appExec = null;
        
        if ( localDevice.getOs().toLowerCase().equals( "ios" ) )                
            appExec = PerfectoMobile.instance().application().install( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getIosInstallation(), instrumentApp ? "instrument" : "noinstrument" );
        else if ( localDevice.getOs().toLowerCase().equals( "android" ) )
            appExec = PerfectoMobile.instance().application().install( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getAndroidInstallation(), instrumentApp ? "instrument" : "noinstrument" );
        else
            throw new DeviceConfigurationException( "Could not install application to " + webDriver.getPopulatedDevice().getEnvironment() + "(" + webDriver.getDevice().getDeviceName() + ")" );
        
        if ( appExec != null )
        {
            if ( appExec.getStatus().toLowerCase().equals( "success" ) )
                return true;
            else
                throw new DeviceConfigurationException( "Failed to install application " + appDesc.getName() );
        }
        else 
            throw new DeviceConfigurationException( "Failed to install application " + appDesc.getName() );
        
    }

    @Override
    public boolean uninstallApplication( String applicationName, DeviceWebDriver webDriver )
    {
        ApplicationDescriptor appDesc = ApplicationRegistry.instance().getApplication( applicationName );
    
        Handset localDevice = PerfectoMobile.instance().devices().getDevice( webDriver.getPopulatedDevice().getDeviceName() );
        
        if ( localDevice.getOs().toLowerCase().equals( "ios" ) )                
            PerfectoMobile.instance().application().uninstall( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getName(), appDesc.getAppleIdentifier() );
        else if ( localDevice.getOs().toLowerCase().equals( "android" ) )
            PerfectoMobile.instance().application().uninstall( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getName(), appDesc.getAndroidIdentifier() );
        else
            throw new DeviceException( "Could not uninstall application from " + localDevice.getOs() );
        return true;
    }

    @Override
    public boolean openApplication( String applicationName, DeviceWebDriver webDriver )
    {

        ApplicationDescriptor appDesc = ApplicationRegistry.instance().getApplication( applicationName );
        
        if ( appDesc == null )
            throw new ScriptConfigurationException( "The Application " + applicationName + " does not exist" );
    
        if ( appDesc.isWeb() )
        {
            //String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,"t");
            //if ( webDriver.getWindowHandles() != null && webDriver.getWindowHandles().size() > 0 )
            //    webDriver.findElement(By.tagName("body")).sendKeys(selectLinkOpeninNewTab);
            
            webDriver.get( appDesc.getUrl() );
                
        }
        else
        {
            Handset localDevice = PerfectoMobile.instance().devices().getDevice( webDriver.getPopulatedDevice().getDeviceName() );
            Execution appExec = null;
            if ( localDevice.getOs().toLowerCase().equals( "ios" ) )                
                appExec = PerfectoMobile.instance().application().open( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getName(), appDesc.getAppleIdentifier() );
            else if ( localDevice.getOs().toLowerCase().equals( "android" ) )
                appExec = PerfectoMobile.instance().application().open( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getName(), appDesc.getAndroidIdentifier() );
            else
                throw new IllegalArgumentException( "Could not install application to " + localDevice.getOs() );
            
            if ( appExec != null )
            {
                if ( appExec.getStatus().toLowerCase().equals( "success" ) )
                {
                    if ( webDriver instanceof ContextAware )
                        ( ( ContextAware ) webDriver ).context( "NATIVE_APP" );
                    return true;
                }
                else
                    throw new ScriptException( "Failed to launch application " + appDesc.getName() );
            }
            else 
                throw new ScriptException( "Failed to launch application " + appDesc.getName() );
            
            
        }
        return true;
    }

    @Override
    public boolean closeApplication( String applicationName, DeviceWebDriver webDriver )
    {

        ApplicationDescriptor appDesc = ApplicationRegistry.instance().getApplication( applicationName );
    
        Handset localDevice = PerfectoMobile.instance().devices().getDevice( webDriver.getPopulatedDevice().getDeviceName() );
        
        if ( localDevice.getOs().toLowerCase().equals( "ios" ) )                
            PerfectoMobile.instance().application().close( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getName(), appDesc.getAppleIdentifier() );
        else if ( localDevice.getOs().toLowerCase().equals( "android" ) )
            PerfectoMobile.instance().application().close( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getName(), appDesc.getAndroidIdentifier() );
        else
            log.warn( "Could not close application on " + localDevice.getOs() );
        return true;
    }
    
}
