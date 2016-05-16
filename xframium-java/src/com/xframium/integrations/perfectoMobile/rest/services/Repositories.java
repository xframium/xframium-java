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
package com.xframium.integrations.perfectoMobile.rest.services;

import com.xframium.integrations.perfectoMobile.rest.bean.ItemCollection;
import com.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Repositories.
 */
@ServiceDescriptor( serviceName="repositories" )
public interface Repositories extends PerfectoService
{
	
	/**
	 * The Enum RepositoryType.
	 */
	public enum RepositoryType
	{
		
		/** The media. */
		MEDIA,
		
		/** The datatables. */
		DATATABLES,
		
		/** The scripts. */
		SCRIPTS
	}
	
	
	/**
	 * Gets the repositorys.
	 *
	 * @param rType the r type
	 * @return the repositorys
	 */
	@Operation( operationName="list" )
	public ItemCollection getRepositorys( @ResourceID RepositoryType rType );

	/**
	 * Download.
	 *
	 * @param rType the r type
	 * @param fileKey the file key
	 * @return the byte[]
	 */
	@Operation( operationName="download" )
	public byte[] download( @ResourceID RepositoryType rType, @ResourceID String fileKey );
	
	
	
}
