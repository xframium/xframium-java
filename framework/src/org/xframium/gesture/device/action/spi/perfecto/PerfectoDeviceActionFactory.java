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
package org.xframium.gesture.device.action.spi.perfecto;

import org.xframium.gesture.device.action.DeviceAction.ActionType;
import org.xframium.gesture.device.action.DeviceActionFactory;
import org.xframium.gesture.device.action.DeviceActionManager;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating PerfectoDeviceAction objects.
 */
public class PerfectoDeviceActionFactory implements DeviceActionFactory
{

	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.device.action.DeviceActionFactory#registerDeviceActions()
	 */
	@Override
	public void registerDeviceActions()
	{
		DeviceActionManager.instance().registerAction( ActionType.HIDE_KEYBOARD, HideKeyboardAction.class );
		DeviceActionManager.instance().registerAction( ActionType.BACK, BackAction.class );
		DeviceActionManager.instance().registerAction( ActionType.URL, URLAction.class );
		DeviceActionManager.instance().registerAction( ActionType.CLOSE_APP, CloseApplicationAction.class );
		DeviceActionManager.instance().registerAction( ActionType.OPEN_APP, OpenApplicationAction.class );
		DeviceActionManager.instance().registerAction( ActionType.INSTALL, InstallApplicationAction.class );
		DeviceActionManager.instance().registerAction( ActionType.UNINSTALL, UninstallApplicationAction.class );
		DeviceActionManager.instance().registerAction( ActionType.REBOOT, RebootAction.class );
		DeviceActionManager.instance().registerAction( ActionType.RECOVER, RecoverAction.class );
		DeviceActionManager.instance().registerAction( ActionType.RESET_APPS, ResetApplicationsAction.class );
		DeviceActionManager.instance().registerAction( ActionType.CLEAN, CleanApplicationAction.class );
		DeviceActionManager.instance().registerAction( ActionType.LOCATION, LocationAction.class );
		DeviceActionManager.instance().registerAction( ActionType.CONTEXT, SwitchContextAction.class );
		DeviceActionManager.instance().registerAction( ActionType.DUMP_STATE, DumpStateAction.class );
		DeviceActionManager.instance().registerAction( ActionType.SEND_KEYS, SendKeysAction.class );
		DeviceActionManager.instance().registerAction( ActionType.SEND_TEXT, SendTextAction.class );
		DeviceActionManager.instance().registerAction( ActionType.CALL, CallAction.class );
		DeviceActionManager.instance().registerAction( ActionType.START_V_NET, StartNetworkVirtualization.class );
		DeviceActionManager.instance().registerAction( ActionType.STOP_V_NET, StopNetworkVirtualization.class );
		DeviceActionManager.instance().registerAction( ActionType.DOWNLOAD_FILE, DownloadFileAction.class );
		DeviceActionManager.instance().registerAction( ActionType.CONFIGURE_NETWORK, ConfigureNetworkAction.class );
	}

}
