package org.xframium.device.cloud.action;

import java.util.Date;
import java.util.Map;

import org.openqa.selenium.Point;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationRegistry;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.gesture.GestureManager;
import org.xframium.integrations.common.PercentagePoint;
import org.xframium.page.BY;
import org.xframium.page.element.Element;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class APPIUMCloudActionProvider extends AbstractCloudActionProvider
{
	/** The Constant PLATFORM_NAME. */
	public static final String PLATFORM_NAME = "platformName";
    
	
	
	@Override
    public boolean startApp( DeviceWebDriver webDriver, String executionId, String deviceId, String appName, String appIdentifier )
    {
		AppiumDriver aD = (AppiumDriver) webDriver.getNativeDriver();
		aD.launchApp();
		return true;
    }
	
	@Override
	public void tap( DeviceWebDriver webDriver, PercentagePoint location, int lengthInMillis )
	{
	    GestureManager.instance(webDriver).createPress( webDriver.getxFID(), new Point( location.x, location.y ), lengthInMillis ).executeGesture(webDriver);
	}
	
	@Override
	public Point translatePoint(DeviceWebDriver webDriver, Point currentPoint) {
		// TODO Auto-generated method stub
		return currentPoint;
	}
	
	@Override
	public String getLog( DeviceWebDriver webDriver )
	{
	    try
        {
            LogEntries logEntries = webDriver.manage().logs().get( "driver" );
            if ( logEntries != null )
            {
                StringBuilder logBuilder = new StringBuilder();
                for ( LogEntry logEntry : logEntries )
                    logBuilder.append( dateFormat.format( new Date( logEntry.getTimestamp() ) ) ).append( ": " ).append( logEntry.getMessage() ).append( "\r\n" );

                logBuilder.toString();
            }
            return null;
        }
        catch ( Exception e )
        {
            log.info( "Could not generate device logs" );
            return null;
        }
	    
	}
	
	@Override
    public boolean isDescriptorSupported( BY descriptorType )
    {
	    switch( descriptorType )
	    {
	        case V_IMAGE:
	        case V_TEXT:
	            return false;
            default:
                return true;
	    }
    }
    
    @Override
    public boolean popuplateDevice( DeviceWebDriver webDriver, String deviceId, Device device, String xFID )
    {
    	AppiumDriver aD = (AppiumDriver) webDriver.getNativeDriver();
    	
    	Map<String,?> cM = webDriver.getCapabilities().asMap();
    	
    	device.setOs( cM.get( "platform") + "" );
    	device.setOsVersion( cM.get( "platformVersion") + "" );

        return true;
    }
    
    @Override
    public String getExecutionId( DeviceWebDriver webDriver )
    {
        return ( (RemoteWebDriver) webDriver.getNativeDriver() ).getSessionId().toString();
    }
    
    @Override
    public void disableLogging( DeviceWebDriver webDriver )
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void enabledLogging( DeviceWebDriver webDriver )
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
	public String getCloudPlatformName(Device device) {
		// TODO Auto-generated method stub
		return PLATFORM_NAME;
	}

	
	@Override
	public String getCloudBrowserName(String currBrowserName) {		
		
		if(currBrowserName.equalsIgnoreCase("Chrome")){
			return "chrome";
		}else if (currBrowserName.equalsIgnoreCase("internet explorer")||currBrowserName.equalsIgnoreCase("internetexplorer")){
			return "internet explorer";
		}else if(currBrowserName.equalsIgnoreCase("firefox")){
			return "firefox";
		}else{
			return currBrowserName;
		}
	}

    @Override
    public boolean installApplication( String applicationName, DeviceWebDriver webDriver, boolean instrumentApp, boolean instrumentSensor, boolean dataReset )
    {

    	ApplicationDescriptor aD = ApplicationRegistry.instance(webDriver.getxFID() ).getApplication( applicationName );
    	
    	if ( webDriver.getDevice().getOs().equalsIgnoreCase( "IOS" ) )
    		( (IOSDriver) webDriver.getNativeDriver() ).installApp( aD.getIosInstallation() );
    	else if ( webDriver.getDevice().getOs().equalsIgnoreCase( "ANDROID" ) )
    		( (AndroidDriver) webDriver.getNativeDriver() ).installApp( aD.getAndroidInstallation() );
    	else
    		return false;
    	
    	return true;
    }

    @Override
    public boolean uninstallApplication( String applicationName, DeviceWebDriver webDriver )
    {
    	ApplicationDescriptor aD = ApplicationRegistry.instance(webDriver.getxFID() ).getApplication( applicationName );
    	
    	if ( webDriver.getDevice().getOs().equalsIgnoreCase( "IOS" ) )
    		( (IOSDriver) webDriver.getNativeDriver() ).removeApp( aD.getAppleIdentifier() );
    	else if ( webDriver.getDevice().getOs().equalsIgnoreCase( "ANDROID" ) )
    		( (AndroidDriver) webDriver.getNativeDriver() ).removeApp( aD.getAndroidIdentifier() );
    	else
    		return false;
    	
    	return true;
    }

    @Override
    public boolean openApplication( String applicationName, DeviceWebDriver webDriver, String xFID )
    {
    	AppiumDriver aD = (AppiumDriver) webDriver.getNativeDriver();
		aD.launchApp();
		return true;
    }

    @Override
    public boolean closeApplication( String applicationName, DeviceWebDriver webDriver )
    {
    	//AppiumDriver aD = (AppiumDriver) webDriver.getNativeDriver();
		//aD.closeApp();
		return true;
    }

    @Override
    public String startTimer( DeviceWebDriver webDriver, Element element, ExecutionContextTest executionContext )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getSupportedTimers( DeviceWebDriver webDriver, String timerId, ExecutionContextTest executionContext, String type )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void stopTimer( DeviceWebDriver webDriver, String timerId, ExecutionContextTest executionContext )
    {
        // TODO Auto-generated method stub
        
    }


    
	
    
}
