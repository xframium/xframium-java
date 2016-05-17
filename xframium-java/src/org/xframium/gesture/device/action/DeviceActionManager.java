/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
package org.xframium.gesture.device.action;

import java.util.HashMap;
import java.util.Map;
import org.xframium.gesture.device.action.DeviceAction.ActionType;
import org.xframium.gesture.device.action.spi.perfecto.PerfectoDeviceActionFactory;

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
