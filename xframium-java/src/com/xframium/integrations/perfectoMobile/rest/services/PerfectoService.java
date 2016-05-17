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
