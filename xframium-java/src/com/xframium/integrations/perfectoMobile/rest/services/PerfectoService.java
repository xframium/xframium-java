package com.xframium.integrations.perfectoMobile.rest.services;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// TODO: Auto-generated Javadoc
/**
 * The Interface PerfectoService.
 */
public interface PerfectoService
{
	
	/**
	 * The Interface ServiceDescriptor.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ServiceDescriptor
	{
		
		/**
		 * Service name.
		 *
		 * @return the string
		 */
		String serviceName();
	}
	
	/**
	 * The Interface Operation.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Operation
	{
		
		/**
		 * Operation name.
		 *
		 * @return the string
		 */
		String operationName();
	}
	
	/**
	 * The Interface PerfectoCommand.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface PerfectoCommand
	{
		
		/**
		 * Command name.
		 *
		 * @return the string
		 */
		String commandName();
		
		/**
		 * Sub command name.
		 *
		 * @return the string
		 */
		String subCommandName() default "";
	}
	
	/**
	 * The Interface ResourceID.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ResourceID
	{
	}
	
	/**
	 * The Interface ParameterMap.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ParameterMap
	{
	}
	
	/**
	 * The Interface Parameter.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Parameter
	{
		
		/**
		 * Name.
		 *
		 * @return the string
		 */
		String name() default "";
	}
	
	/**
	 * The Interface NameOverride.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface NameOverride
	{
		
		/**
		 * Name.
		 *
		 * @return the string
		 */
		String name() default "";
	}
}
