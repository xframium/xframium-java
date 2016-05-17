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
package com.xframium.integrations.perfectoMobile.rest.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.xframium.integrations.perfectoMobile.rest.services.Application;
import com.xframium.integrations.rest.bean.AbstractBean;
import com.xframium.integrations.rest.bean.Bean.BeanDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationCollection.
 */
@BeanDescriptor( beanName="response" )
public class ApplicationCollection extends AbstractBean
{
	
	/** The execution id. */
	@FieldDescriptor ( )
	private String executionId;
	
	/** The report key. */
	@FieldDescriptor ( )
	private String reportKey;
	
	/** The status. */
	@FieldDescriptor ( )
	private String status;
	
	/** The return value. */
	@FieldDescriptor ( )
	private String returnValue;
	
	/** The application map. */
	private Map<String,String> applicationMap;

	/**
	 * Gets the execution id.
	 *
	 * @return the execution id
	 */
	public String getExecutionId()
	{
		return executionId;
	}

	/**
	 * Sets the execution id.
	 *
	 * @param executionId the new execution id
	 */
	public void setExecutionId( String executionId )
	{
		this.executionId = executionId;
	}

	/**
	 * Gets the report key.
	 *
	 * @return the report key
	 */
	public String getReportKey()
	{
		return reportKey;
	}

	/**
	 * Sets the report key.
	 *
	 * @param reportKey the new report key
	 */
	public void setReportKey( String reportKey )
	{
		this.reportKey = reportKey;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus( String status )
	{
		this.status = status;
	}

	/**
	 * Gets the return value.
	 *
	 * @return the return value
	 */
	public String getReturnValue()
	{
		return returnValue;
	}

	/**
	 * Sets the return value.
	 *
	 * @param returnValue the new return value
	 */
	public void setReturnValue( String returnValue )
	{
		this.returnValue = returnValue;
	}
	
	/**
	 * Gets the applications.
	 *
	 * @return the applications
	 */
	public Collection<String> getApplications()
	{
		if ( applicationMap == null )
			parseApplications();
		return applicationMap.values();
	}
	
	
	/**
	 * Parses the applications.
	 */
	private void parseApplications()
	{
		applicationMap = new HashMap<String,String>( 20 );
		
		String workingValue = returnValue;
		workingValue = workingValue.replace( '[', ' ' ).replace( ']', ' ' ).trim();
		String[] allApps = workingValue.split( "," );
		
		for ( String app : allApps )
		{
			String trimmedApp = app.trim();
			applicationMap.put( trimmedApp.toLowerCase(), trimmedApp );
		}
	}
	
	/**
	 * Gets the application.
	 *
	 * @param applicationName the application name
	 * @return the application
	 */
	public String getApplication( String applicationName )
	{
		if ( applicationMap == null )
			parseApplications();
		
		return applicationMap.get( applicationName.toLowerCase() );
	}

	
	
	
	

}
