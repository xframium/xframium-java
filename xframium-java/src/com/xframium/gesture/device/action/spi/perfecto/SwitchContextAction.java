package com.xframium.gesture.device.action.spi.perfecto;

import java.util.List;

import org.openqa.selenium.ContextAware;
import org.openqa.selenium.WebDriver;
import com.xframium.gesture.device.action.AbstractDefaultAction;
import com.xframium.gesture.device.action.DeviceAction;

// TODO: Auto-generated Javadoc
/**
 * The Class ResetApplicationsAction.
 */
public class SwitchContextAction extends AbstractDefaultAction implements DeviceAction
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.device.action.AbstractDefaultAction#_executeAction(org.openqa.selenium.WebDriver, java.util.List)
	 */
	@Override
	public boolean _executeAction( WebDriver webDriver, List<Object> parameterList )
	{
		String contextName = (String) parameterList.get( 0 );

		
		
		if ( webDriver instanceof ContextAware )
		{
			( (ContextAware) webDriver ).context( contextName );
		}
		
		return true;
	}

}
