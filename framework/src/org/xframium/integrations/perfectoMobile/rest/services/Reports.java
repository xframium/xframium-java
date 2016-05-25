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
package org.xframium.integrations.perfectoMobile.rest.services;

import org.xframium.integrations.perfectoMobile.rest.bean.ExecutionReport;
import org.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Reports.
 */
@ServiceDescriptor( serviceName="reports" )
public interface Reports extends PerfectoService
{
	
	/**
	 * The Enum ReportFormat.
	 */
	public enum ReportFormat
	{
		
		/** The xml. */
		xml,
		
		/** The csv. */
		csv,
		
		/** The pdf. */
		pdf,
		
		/** The html. */
		html;
	}
	
	/**
	 * Download.
	 *
	 * @param reportKey the report key
	 * @param format the format
	 * @return the execution report
	 */
	@Operation( operationName="download" )
	public ExecutionReport download( @ResourceID String reportKey, @NameOverride( name="format" )ReportFormat format );
	
	@Operation( operationName="log" )
    public byte[] download( @ResourceID String reportKey, @NameOverride( name="attachment") String attachment, @NameOverride( name="admin") boolean admin );
}
