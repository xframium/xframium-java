/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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
package com.xframium.gesture.device.action;

import java.util.List;

import org.openqa.selenium.WebDriver;

// TODO: Auto-generated Javadoc
/**
 * The Interface DeviceAction.
 */
public interface DeviceAction
{
	
	/**
	 * The Enum ActionType.
	 */
	public enum ActionType
	{
		
		/** The hide keyboard. */
		HIDE_KEYBOARD,
		
		/** The back. */
		BACK,
		
		/** The url. */
		URL,
		
		/** The install. */
		INSTALL,
		
		/** The uninstall. */
		UNINSTALL,
		
		/** The reset apps. */
		RESET_APPS,
		
		/** The open app. */
		OPEN_APP,
		
		/** The close app. */
		CLOSE_APP,
		
		/** The reboot. */
		REBOOT,
		
		/** The recover. */
		RECOVER,
		
		/** The clean. */
		CLEAN,
		
		/**  switch the context. */
		CONTEXT,
		
		/** The dump state. */
		DUMP_STATE,
		
		/** The dump state. */
        SEND_KEYS,
		
		/** The location. */
		LOCATION,
		/** Call this device */
		CALL,
		
		/** send a text to this device */
		SEND_TEXT;
	}
	
	/**
	 * Execute action.
	 *
	 * @param webDriver the web driver
	 * @return true, if successful
	 */
	public boolean executeAction( WebDriver webDriver );
	
	/**
	 * Execute action.
	 *
	 * @param webDriver the web driver
	 * @param parameterList the parameter list
	 * @return true, if successful
	 */
	public boolean executeAction( WebDriver webDriver, List<Object> parameterList );
}
