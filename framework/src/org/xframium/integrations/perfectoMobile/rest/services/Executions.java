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

import org.xframium.integrations.perfectoMobile.rest.bean.Execution;
import org.xframium.integrations.perfectoMobile.rest.bean.ExecutionCollection;
import org.xframium.integrations.perfectoMobile.rest.bean.ExecutionResult;
import org.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Executions.
 */
@ServiceDescriptor( serviceName="executions" )
public interface Executions extends PerfectoService
{
	
	/**
	 * The Enum FlowEndCode.
	 */
	public enum FlowEndCode
	{
		
		/** The Success. */
		Success,
		
		/** The Failed. */
		Failed,
		
		/** The User aborted. */
		UserAborted;
	}
	
	/**
	 * The Enum TimeType.
	 */
	public enum TimeType
	{
		
		/** The started. */
		started,
		
		/** The completed. */
		completed;
	}
	
	/**
	 * Execute.
	 *
	 * @param scriptKey the script key
	 * @param DUT the dut
	 * @return the execution
	 */
	@Operation( operationName="execute" )
	public Execution executeScript( @NameOverride( name="scriptKey" ) String scriptKey, @Parameter( name="DUT" ) String DUT );
	
	@Operation( operationName="execute" )
	@PerfectoCommand( commandName="execute", subCommandName = "script" )
    public Execution execute( @ResourceID String executionId, @NameOverride( name="script" ) String script, @Parameter( name="DUT" ) String DUT );
	
	/**
	 * Status.
	 *
	 * @param executionId the execution id
	 * @return the execution result
	 */
	@Operation( operationName="status" )
	public ExecutionResult status( @ResourceID String executionId );
	
	/**
	 * End execution.
	 *
	 * @param executionId the execution id
	 * @return the execution
	 */
	@Operation( operationName="end" )
	public Execution endExecution( @ResourceID String executionId );
	
	/**
	 * Start execution.
	 *
	 * @return the execution
	 */
	@Operation( operationName="start" )
	public Execution startExecution();
	
	/**
	 * Gets the executions.
	 *
	 * @param completed the completed
	 * @param admin the admin
	 * @param flowEndCode the flow end code
	 * @param timeType the time type
	 * @param timeAnchor the time anchor
	 * @param timeOffset the time offset
	 * @return the executions
	 */
	@Operation( operationName="list" )
	public ExecutionCollection getExecutions( @NameOverride( name="completed" )Boolean completed, @NameOverride( name="admin" )Boolean admin, @NameOverride( name="flowEndCode" ) FlowEndCode flowEndCode, @NameOverride( name="time.type" ) TimeType timeType, @NameOverride( name="time.anchor") Long timeAnchor, @NameOverride( name="time.offset" ) Short timeOffset );
	
}
