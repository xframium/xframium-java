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
package com.xframium.integrations.perfectoMobile.rest.services;

import com.xframium.integrations.perfectoMobile.rest.bean.Handset;
import com.xframium.integrations.perfectoMobile.rest.bean.HandsetCollection;
import com.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Devices.
 */
@ServiceDescriptor( serviceName="handsets" )
public interface Devices extends PerfectoService
{
	
	/**
	 * Gets the device.
	 *
	 * @param handsetId the handset id
	 * @return the device
	 */
	@Operation( operationName="info" )
	public Handset getDevice( @ResourceID String handsetId );
	
	/**
	 * Gets the devices.
	 *
	 * @return the devices
	 */
	@Operation( operationName="list" )
	public HandsetCollection getDevices();
	
	/**
	 * Gets the devices.
	 *
	 * @param manufacturer the manufacturer
	 * @return the devices
	 */
	@Operation( operationName="list" )
	public HandsetCollection getDevices( @NameOverride( name="manufacturer" ) String manufacturer );
	
	/**
	 * Gets the my devices.
	 *
	 * @param reservedTo the reserved to
	 * @return the my devices
	 */
	@Operation( operationName="list" )
	public HandsetCollection getMyDevices( @NameOverride( name="reservedTo" ) String reservedTo );
	
	/**
	 * Gets the devices.
	 *
	 * @param inUse the in use
	 * @return the devices
	 */
	@Operation( operationName="list" )
	public HandsetCollection getDevices( @NameOverride( name="inUse" ) boolean inUse );
}
