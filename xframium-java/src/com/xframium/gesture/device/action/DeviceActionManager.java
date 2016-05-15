package com.xframium.gesture.device.action;

import java.util.HashMap;
import java.util.Map;
import com.xframium.gesture.device.action.DeviceAction.ActionType;
import com.xframium.gesture.device.action.spi.perfecto.PerfectoDeviceActionFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceActionManager.
 */
public class DeviceActionManager
{
	
	/** The singleton. */
	private static DeviceActionManager singleton = new DeviceActionManager();
	
	/**
	 * Instance.
	 *
	 * @return the device action manager
	 */
	public static DeviceActionManager instance()
	{
		return singleton;
	}

	/**
	 * Instantiates a new device action manager.
	 */
	private DeviceActionManager()
	{
		
	}
	
	/**
	 * Sets the device action factory.
	 *
	 * @param actionFactory the new device action factory
	 */
	public void setDeviceActionFactory( DeviceActionFactory actionFactory )
	{
		actionFactory.registerDeviceActions();
	}
	
	/** The action map. */
	private Map<ActionType,Class<DeviceAction>> actionMap = new HashMap<ActionType,Class<DeviceAction>>( 10 );
	
	
	/**
	 * Register action.
	 *
	 * @param actionType the action type
	 * @param actionClass the action class
	 */
	public void registerAction( ActionType actionType, Class actionClass )
	{
		actionMap.put( actionType, actionClass );
	}
	
	/**
	 * Gets the action.
	 *
	 * @param actionType the action type
	 * @return the action
	 */
	public DeviceAction getAction( ActionType actionType )
	{
		
		if ( actionMap.isEmpty() )
			setDeviceActionFactory( new PerfectoDeviceActionFactory() );
		try
		{
			Class<DeviceAction> actionClass = actionMap.get( actionType );
			if ( actionClass != null )
				return actionClass.newInstance();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
		
	}
}
