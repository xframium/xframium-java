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

import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractService.
 */
public abstract class AbstractService implements PerfectoService
{
	
	/** The perfecto mobile. */
	private PerfectoMobile perfectoMobile;
	
	/**
	 * Instantiates a new abstract service.
	 *
	 * @param perfectoMobile the perfecto mobile
	 */
	public AbstractService( PerfectoMobile perfectoMobile )
	{
		this.perfectoMobile = perfectoMobile;
	}
	
	/**
	 * Gets the base url.
	 *
	 * @return the base url
	 */
	protected String getBaseUrl()
	{
		return perfectoMobile.getBaseUrl();
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	protected String getUserName()
	{
		return perfectoMobile.getUserName();
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	protected String getPassword()
	{
		return perfectoMobile.getPassword();
	}

	
}
