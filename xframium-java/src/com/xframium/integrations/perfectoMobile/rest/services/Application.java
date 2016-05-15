package com.xframium.integrations.perfectoMobile.rest.services;

import com.xframium.integrations.perfectoMobile.rest.bean.ApplicationCollection;
import com.xframium.integrations.perfectoMobile.rest.bean.Execution;
import com.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Application.
 */
@ServiceDescriptor( serviceName="executions" )
public interface Application extends PerfectoService
{
	
	
	/**
	 * Install.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param file the file
	 * @param instrument the instrument
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="application", subCommandName = "install" )
	public Execution install( @ResourceID String executionId, @Parameter( name="deviceId" ) String handsetId, @Parameter( name="file" ) String file, @Parameter( name="instrument" ) String instrument  );
	
	/**
	 * Uninstall.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param name the name
	 * @param identifier the identifier
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="application", subCommandName = "uninstall" )
	public Execution uninstall( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="name" ) String name, @Parameter( name="identifier" ) String identifier  );

	/**
	 * Open.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param name the name
	 * @param identifier the identifier
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="application", subCommandName = "open" )
	public Execution open( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="name" ) String name, @Parameter( name="identifier" ) String identifier );
	
	/**
	 * Close.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param name the name
	 * @param identifier the identifier
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="application", subCommandName = "close" )
	public Execution close( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="name" ) String name, @Parameter( name="identifier" ) String identifier );
	
	/**
	 * Uninstall all.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @return the execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="application", subCommandName = "reset" )
	public Execution uninstallAll( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId  );
	
	/**
	 * Find.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @return the application collection
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="application", subCommandName = "find" )
	public ApplicationCollection find( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId  );
	
	/**
	 * Clean.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param name the name
	 * @param identifier the identifier
	 * @return the application collection
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="application", subCommandName = "clean" )
	public ApplicationCollection clean( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="name" ) String name, @Parameter( name="identifier" ) String identifier  );
	
}
