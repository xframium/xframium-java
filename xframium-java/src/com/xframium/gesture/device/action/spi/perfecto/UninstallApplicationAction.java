package com.xframium.gesture.device.action.spi.perfecto;

import java.util.List;

import org.openqa.selenium.WebDriver;
import com.xframium.application.ApplicationDescriptor;
import com.xframium.application.ApplicationRegistry;
import com.xframium.gesture.device.action.AbstractDefaultAction;
import com.xframium.gesture.device.action.DeviceAction;
import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import com.xframium.integrations.perfectoMobile.rest.bean.Handset;

// TODO: Auto-generated Javadoc
/**
 * The Class UninstallApplicationAction.
 */
public class UninstallApplicationAction extends AbstractDefaultAction implements DeviceAction
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.device.action.AbstractDefaultAction#_executeAction(org.openqa.selenium.WebDriver, java.util.List)
	 */
	@Override
	public boolean _executeAction( WebDriver webDriver, List<Object> parameterList )
	{
		String executionId = getExecutionId( webDriver );
		String deviceName = getDeviceName( webDriver );
		
		String applicationName = (String) parameterList.get( 0 );

		ApplicationDescriptor appDesc = ApplicationRegistry.instance().getApplication( applicationName );
	
		Handset localDevice = PerfectoMobile.instance().devices().getDevice( deviceName );
		
		if ( localDevice.getOs().toLowerCase().equals( "ios" ) )				
			PerfectoMobile.instance().application().uninstall( executionId, deviceName, appDesc.getName(), appDesc.getAppleIdentifier() );
		else if ( localDevice.getOs().toLowerCase().equals( "android" ) )
			PerfectoMobile.instance().application().uninstall( executionId, deviceName, appDesc.getName(), appDesc.getAndroidIdentifier() );
		else
			throw new IllegalArgumentException( "Could not uninstall application from " + localDevice.getOs() );
		return true;
	}

}
