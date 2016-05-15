package com.xframium.gesture.factory.spi.perfecto;

import java.io.InputStream;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import com.xframium.gesture.AbstractRotateGesture;
import com.xframium.integrations.common.PercentagePoint;
import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import com.xframium.integrations.perfectoMobile.rest.services.Device.ScreenOrientation;

// TODO: Auto-generated Javadoc
/**
 * The Class RotateGesture.
 */
public class RotateGesture extends AbstractRotateGesture
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.AbstractGesture#_executeGesture(org.openqa.selenium.WebDriver)
	 */
	@Override
	protected boolean _executeGesture( WebDriver webDriver )
	{
		String executionId = getExecutionId( webDriver );
		String deviceName = getDeviceName( webDriver );
		if ( executionId != null )
		{
			PerfectoMobile.instance().device().rotate( executionId, deviceName, ScreenOrientation.valueOf( getOrientation().name().toLowerCase() ) );
			return true;
		}
		else
			return false;
	}

}
