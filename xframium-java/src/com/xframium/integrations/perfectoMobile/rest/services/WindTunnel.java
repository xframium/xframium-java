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

import com.xframium.integrations.perfectoMobile.rest.bean.Execution;
import com.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface WindTunnel.
 */
@ServiceDescriptor( serviceName="executions" )
public interface WindTunnel extends PerfectoService
{
	
	/**
	 * The Enum Network.
	 */
	public enum Network
	{
		
		/** The wifi. */
		wifi,
		
		/** The data. */
		data,
		
		/** The airplanemode. */
		airplanemode;
	}
	
	/**
	 * The Enum TimerPolicy.
	 */
	public enum TimerPolicy
	{
		
		/** The reset. */
		reset,
		
		/** The noreset. */
		noreset;
	}
	
	/**
	 * The Enum TimerPolicy.
	 */
	public enum Status
	{
		
		/** The success. */
		success,
		
		/** The failure. */
		failure;
	}
	
	
	/**
	 * Adds the point of interest.
	 *
	 * @param executionId the execution id
	 * @param description the description
	 * @param status the status
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="status", subCommandName = "event" )
	public Execution addPointOfInterest( @ResourceID String executionId, @Parameter( name="description" ) String description, @Parameter( name="status" ) Status status );
	
	/**
	 * Adds the timer report.
	 *
	 * @param executionId the execution id
	 * @param name the name
	 * @param result the result
	 * @param status the status
	 * @param description the description
	 * @param threshold the threshold
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="status", subCommandName = "timer" )
	public Execution addTimerReport( @ResourceID String executionId, @Parameter( name="name" ) String name, @Parameter( name="result" ) int result, @Parameter( name="status" ) Status status, @Parameter( name="description" ) String description, @Parameter( name="threshold" ) int threshold );
	
	/**
	 * Gets the network setting.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param property the property
	 * @return the network setting
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="status", subCommandName = "event" )
	public Execution getNetworkSetting( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="property" ) Network property );
	
	/**
	 * Configure network.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param wifi the wifi
	 * @param data the data
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="status", subCommandName = "event" )
	public Execution configureNetwork( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="wifi" ) boolean wifi, @Parameter( name="data" ) boolean data );
	
	/**
	 * Start network virtualization.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param latency the latency
	 * @param packetLoss the packet loss
	 * @param packetCorruption the packet corruption
	 * @param packetReordering the packet reordering
	 * @param packetDuplication the packet duplication
	 * @param delayJitter the delay jitter
	 * @param correlation the correlation
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="status", subCommandName = "event" )
	public Execution startNetworkVirtualization( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="latency" ) int latency, @Parameter( name="packetLoss" ) int packetLoss, @Parameter( name="packetCorruption" ) int packetCorruption, @Parameter( name="packetReordering" ) int packetReordering, @Parameter( name="packetDuplication" ) int packetDuplication, @Parameter( name="delayJitter" ) int delayJitter, @Parameter( name="correlation" ) int correlation  );
	
	/**
	 * Stop network virtualization.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="status", subCommandName = "event" )
	public Execution stopNetworkVirtualization( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId );
	
	/**
	 * Start timer.
	 *
	 * @param executionId the execution id
	 * @param timerId the timer id
	 * @param initPolicy the init policy
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="timer", subCommandName = "start" )
	public Execution startTimer( @ResourceID String executionId, @Parameter( name="timerId" ) String timerId, @Parameter( name="initPolicy" ) TimerPolicy initPolicy );
	
	/**
	 * Stop timer.
	 *
	 * @param executionId the execution id
	 * @param timerId the timer id
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="timer", subCommandName = "stop" )
	public Execution stopTimer( @ResourceID String executionId, @Parameter( name="timerId" ) String timerId );
	
	/**
	 * Start vitals.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param timerId the timer id
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="monitor", subCommandName = "start" )
	public Execution startVitals( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="timerId" ) String timerId );
}
