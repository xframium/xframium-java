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
