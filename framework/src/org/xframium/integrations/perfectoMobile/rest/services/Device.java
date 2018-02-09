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

import org.xframium.integrations.common.Location;
import org.xframium.integrations.perfectoMobile.rest.bean.Execution;
import org.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Device.
 */
@ServiceDescriptor( serviceName="executions" )
public interface Device extends PerfectoService
{
	
	/**
	 * The Enum ScreenOrientation.
	 */
	public enum ScreenOrientation
	{
		
		/** The portrait. */
		portrait,
		
		/** The landscape. */
		landscape;
	}
	
	/**
	 * Rotate.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param state the state
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="device", subCommandName = "rotate" )
	public Execution rotate( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="state" ) ScreenOrientation state );
	
	
	/**
	 * Reboot.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="device", subCommandName = "reboot" )
	public Execution reboot( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId );
	
	/**
	 * Recover.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="device", subCommandName = "recover" )
	public Execution recover( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId );
	
	/**
	 * Open.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="device", subCommandName = "open" )
	public Execution open( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId );
	
	/**
	 * Close.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="device", subCommandName = "close" )
	public Execution close( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId );
	
	/**
	 * Sets the location.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param coordinates the coordinates
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="location", subCommandName = "set" )
	public Execution setLocation( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="coordinates" ) Location coordinates );
	

    /**
     * Call.
     *
     * @param executionId the execution id
     * @param handsetId the handset id
     * @return the execution
     */
    @Operation( operationName="command" )
    @PerfectoCommand( commandName="gateway", subCommandName = "call" )
    public Execution call( @ResourceID String executionId, @Parameter( name="to.handset" ) String handsetId );
    
    /**
     * Call.
     *
     * @param executionId the execution id
     * @param handsetId the handset id
     * @return the execution
     */
    @Operation( operationName="command" )
    @PerfectoCommand( commandName="gateway", subCommandName = "sms" )
    public Execution sendText( @ResourceID String executionId, @NameOverride( name="body" )String body, @Parameter( name="to.handset" ) String handsetId );
    
    @Operation( operationName="command" )
    @PerfectoCommand( commandName="device", subCommandName = "ready" )
    public Execution home( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId  );
    
    
    @Operation( operationName="command" )
    @PerfectoCommand( commandName="browser", subCommandName = "execute" )
    public Execution clean( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId  );
	
    
    
    @Operation( operationName="command" )
    @PerfectoCommand( commandName="logs", subCommandName = "start" )
    public Execution startDebug( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId );
    
    @Operation( operationName="command" )
    @PerfectoCommand( commandName="logs", subCommandName = "stop" )
    public Execution stopDebug( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId );

    @Operation( operationName="command" )
    @PerfectoCommand( commandName="media", subCommandName = "get" )
    public Execution getFile( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="handsetFile" ) String handsetFile, @Parameter( name="repositoryFile" ) String repositoryFile );
    
    /**
     * 
     * @param executionId
     * @param handsetId
     * @param repositoryFile
     * @param handsetFile
     * @return
     */
    @Operation( operationName="command" )
    @PerfectoCommand( commandName="media", subCommandName = "put" )
    public Execution putFile( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="repositoryFile" ) String repositoryFile, @Parameter( name="handsetFile" ) String handsetFile );
    
    /**
   	 * Locks the screen for 10 seconds (default)
   	 *
   	 * @param executionId the execution id
   	 * @param handsetId the handset id
   	 * @param coordinates the coordinates
   	 * @return the execution
   	 */
   	@Operation( operationName="command" )
   	@PerfectoCommand( commandName="screen", subCommandName = "lock" )
   	public Execution lockScreen( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId );
   	

    
}
