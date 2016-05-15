package com.xframium.gesture.device.action.spi.perfecto;

import com.xframium.gesture.device.action.DeviceActionFactory;
import com.xframium.gesture.device.action.DeviceActionManager;
import com.xframium.gesture.device.action.DeviceAction.ActionType;

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
	}

}
