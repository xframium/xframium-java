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
/*
 * 
 */
package org.xframium.device.comparator;

import java.util.Comparator;
import org.xframium.device.DeviceManager;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class WeightedDeviceComparator.
 */
public class WeightedDeviceComparator implements Comparator<Device>
{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare( Device o1, Device o2 )
	{
		int usageOne = DeviceManager.instance().getUsage( o1 );
		int usageTwo = DeviceManager.instance().getUsage( o2 );
		
		
		if ( usageOne < usageTwo )
			return -1;
		else if ( usageOne > usageTwo )
			return 1;
		else
			return 0;
	}
}
