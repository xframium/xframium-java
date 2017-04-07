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

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.gesture.device.action.AbstractDefaultAction;
import org.xframium.gesture.device.action.DeviceAction;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;

import com.beust.jcommander.ParameterException;

// TODO: Auto-generated Javadoc
/**
 * The Class CopyFileAction.
 */
public class CopyFileAction extends AbstractDefaultAction implements DeviceAction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.perfectoMobile.gesture.device.action.AbstractDefaultAction#
	 * _executeAction(org.openqa.selenium.WebDriver, java.util.List)
	 */
	@Override
	public boolean _executeAction(WebDriver webDriver, List<Object> parameterList) {
		
		String executionId = getExecutionId(webDriver);
		String deviceName = getDeviceName(webDriver);

		if (parameterList.size() < 2)
			throw new ParameterException("Repository path and Device path should be given as parameters");

		String repFilePath = ((String) parameterList.get(0)).trim();

		String deviceFilePath = ((String) parameterList.get(1)).trim();

		// Copy File from repository to device
		try {
			PerfectoMobile.instance().device().putFile(executionId, deviceName, repFilePath, deviceFilePath);
		} catch (Exception e) {
			throw new ScriptConfigurationException("Unable to copy the file from repository path - " + repFilePath
					+ " to the device path - " + deviceFilePath);
		}

		return true;
	}

}
