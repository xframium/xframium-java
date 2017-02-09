package org.xframium.device.cloud.action;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationRegistry;
import org.xframium.device.artifact.ArtifactProducer;
import org.xframium.device.artifact.api.SeleniumArtifactProducer;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.page.BY;
import org.xframium.page.element.Element;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;
import eu.bitwalker.useragentutils.UserAgent;

public class SELENIUMCloudActionProvider extends AbstractCloudActionProvider
{
	/** The Constant PLATFORM_NAME. */
	public static final String PLATFORM_NAME = "platformName";
	
	
	@Override
    public boolean startApp( String executionId, String deviceId, String appName, String appIdentifier )
    {
        
        return true;
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
    public boolean popuplateDevice( DeviceWebDriver webDriver, String deviceId, Device device )
    {
        String uAgent = (String) webDriver.executeScript("return navigator.userAgent;");
        UserAgent userAgent = new UserAgent( uAgent );
        device.setBrowserName( userAgent.getBrowser().getName() );
        device.setManufacturer( userAgent.getOperatingSystem().getManufacturer().getName() );
        device.setOs( userAgent.getOperatingSystem().getName().split( " " )[ 0 ].toUpperCase() );
        Dimension winDim = webDriver.manage().window().getSize();
        if ( winDim != null )
            device.setResolution( winDim.getWidth() + " x " + winDim.height );
        else
            device.setResolution( null );
        
        return true;
    }
    
    @Override
    public String getExecutionId( DeviceWebDriver webDriver )
    {
        return ( (RemoteWebDriver) webDriver.getNativeDriver() ).getSessionId().toString();
    }
    
    @Override
    public ArtifactProducer getArtifactProducer()
    {
        return new SeleniumArtifactProducer();
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
    public boolean installApplication( String applicationName, DeviceWebDriver webDriver, boolean instrumentApp )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean uninstallApplication( String applicationName, DeviceWebDriver webDriver )
    {
        // TODO Auto-generated method stub
        return false;
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
        
        return true;
    }

    @Override
    public boolean closeApplication( String applicationName, DeviceWebDriver webDriver )
    {
        // TODO Auto-generated method stub
        return false;
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
