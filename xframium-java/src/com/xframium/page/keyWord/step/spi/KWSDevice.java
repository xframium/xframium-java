package com.xframium.page.keyWord.step.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import com.xframium.gesture.device.action.DeviceAction;
import com.xframium.gesture.device.action.DeviceActionManager;
import com.xframium.gesture.device.action.DeviceAction.ActionType;
import com.xframium.page.Page;
import com.xframium.page.PageManager;
import com.xframium.page.data.PageData;
import com.xframium.page.keyWord.KeyWordParameter;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;



// TODO: Auto-generated Javadoc
/**
 * The Class KWSDevice.
 */
public class KWSDevice extends AbstractKeyWordStep
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com
	 * .perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map,
	 * java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
		long startTime = System.currentTimeMillis();
		if ( log.isDebugEnabled() )
			log.debug( "Executing Device Action " + getName() + " using " + getParameterList() );
		
		DeviceAction deviceAction = DeviceActionManager.instance().getAction( ActionType.valueOf( getName().toUpperCase() ) );
		
		List<Object> actionParameters = new ArrayList<Object>(10);
		
		for ( KeyWordParameter param : getParameterList() )
		{
			actionParameters.add( getParameterValue( param, contextMap, dataMap ) );
		}
		
		boolean returnValue = deviceAction.executeAction( webDriver, actionParameters );
		
		

		return returnValue;
	}

}
