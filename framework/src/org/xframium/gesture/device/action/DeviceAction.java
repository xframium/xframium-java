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

import java.util.ArrayList;
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
		HIDE_KEYBOARD(1, "HIDE_KEYBOARD", "Hide the keyboard"),
		
		/** The back. */
		BACK(2, "BACK", "LEGACY Click Back Button"),
		
		/** The url. */
		URL(3, "URL", "Open a URL"),
		
		/** The install. */
		INSTALL( 4, "INSTALL", "Install a native application"),
		
		/** The uninstall. */
		UNINSTALL( 5, "UNINSTALL", "Remove a native application"),
		
		/** The reset apps. */
		RESET_APPS( 6, "RESET_APPS", "Reset applications"),
		
		/** The open app. */
		OPEN_APP( 7, "OPEN_APP", "Launch an application"),
		
		/** The close app. */
		CLOSE_APP( 8, "CLOSE_APP", "Close an application"),
		
		/** The reboot. */
		REBOOT( 9, "REBOOT", "Reboot the device"),
		
		/** The recover. */
		RECOVER( 10, "RECOVER", "Recover the connection"),
		
		/** The clean. */
		CLEAN( 11, "CLEAN", "Clean the device"),
		
		/**  switch the context. */
		CONTEXT( 12, "CONTEXT", "Switch the driver context"),
		
		/** The dump state. */
		DUMP_STATE( 13, "DUMP_STATE", "LEGACY dump state"),
		
		/** The dump state. */
        SEND_KEYS( 14, "SEND_KEYS", "Send keyword strokes"),
		
		/** The location. */
		LOCATION( 15, "LOCATION", "Change the device location"),
		/** Call this device */
		CALL( 16, "CALL", "Call the device"),
		
		/** send a text to this device */
		SEND_TEXT( 17, "SEND_TEXT", "Send SMS Message"),
		
		START_V_NET(18, "START_V_NET", "Start network virtualization"),
		
		STOP_V_NET(19, "STOP_V_NET", "Stop Network Virtualization"),
		
		DOWNLOAD_FILE(20, "DOWNLOAD_FILE", "Get file from device to local system"),
		
		CONFIGURE_NETWORK(21, "CONFIGURE_NETWORK", "To set network settings on the device (wifi, data, airplanemode)"),
		
		START_VITALS(22, "START_VITALS", "Start monitoring device vitals"),
        
		STOP_VITALS(23, "STOP_VITALS", "Stop Nmonitoring device vitals");
	    
	    public List<ActionType> getSupportedActions()
	    {
	        List<ActionType> supportedList = new ArrayList<ActionType>( 10 );
	        supportedList.add( ActionType.HIDE_KEYBOARD );
	        supportedList.add( ActionType.URL );
	        supportedList.add( ActionType.INSTALL );
	        supportedList.add( ActionType.UNINSTALL );
	        supportedList.add( ActionType.RESET_APPS );
	        supportedList.add( ActionType.OPEN_APP );
	        supportedList.add( ActionType.CLOSE_APP );
	        supportedList.add( ActionType.REBOOT );
	        supportedList.add( ActionType.RECOVER );
	        supportedList.add( ActionType.CLEAN );
	        supportedList.add( ActionType.CONTEXT );
	        supportedList.add( ActionType.SEND_KEYS );
	        supportedList.add( ActionType.LOCATION );
	        supportedList.add( ActionType.CALL );
	        supportedList.add( ActionType.SEND_TEXT );
	        supportedList.add( ActionType.START_V_NET );
	        supportedList.add( ActionType.STOP_V_NET );
	        supportedList.add( ActionType.DOWNLOAD_FILE );
	        supportedList.add( ActionType.CONFIGURE_NETWORK );
	        supportedList.add( ActionType.START_VITALS );
	        supportedList.add( ActionType.STOP_VITALS );
	        return supportedList;
	    }
	    
	    private ActionType( int id, String name, String description )
	    {
	        this.id = id;
	        this.name= name;
	        this.description = description;
	    }
	    
	    private int id;
	    private String name;
	    private String description;
	    
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
