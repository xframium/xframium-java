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

import org.xframium.integrations.perfectoMobile.rest.bean.ImageExecution;
import org.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Imaging.
 */
@ServiceDescriptor( serviceName="executions" )
public interface Imaging extends PerfectoService
{
	
	/**
	 * The Enum MatchMode.
	 */
	public enum MatchMode
	{
		
		/** The identical. */
		identical,
		
		/** The similar. */
		similar,
		
		/** The fine. */
		fine,
		
		/** The bounded. */
		bounded;
	}
	
	/**
	 * The Enum Screen.
	 */
	public enum Screen
	{
		
		/** The primary. */
		primary,
		
		/** The camera. */
		camera,
		
		/** The best. */
		best;
	}
	
	/**
	 * The Enum ImageFormat.
	 */
	public enum ImageFormat
	{
		
		/** The jpg. */
		jpg,
		
		/** The bmp. */
		bmp,
		
		/** The png. */
		png;
	}
	
	/**
	 * The Enum Resolution.
	 */
	public enum Resolution
	{
		
		/** The high. */
		high,
		
		/** The medium. */
		medium,
		
		/** The low. */
		low;
	}
	
	/**
	 * Text exists.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param content the content
	 * @param timeout the timeout
	 * @return the image execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="checkpoint", subCommandName = "text" )
	public ImageExecution textExists( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="content" ) String content, @Parameter( name="timeout" ) Short timeout, @Parameter( name="threshold" ) Integer threshold   );
	
	
	/**
	 * Image exists.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param content the content
	 * @param timeout the timeout
	 * @param match the match
	 * @return the image execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="checkpoint", subCommandName = "image" )
	public ImageExecution imageExists( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="content" ) String content, @Parameter( name="timeout" ) Short timeout, @Parameter( name="match" ) MatchMode match );
	
	

	/**
	 * Screen shot.
	 *
	 * @param executionId the execution id
	 * @param handsetId the handset id
	 * @param key the key
	 * @param source the source
	 * @param format the format
	 * @param resolution the resolution
	 * @return the image execution
	 */
	@Operation( operationName="command" )
	@PerfectoCommand( commandName="screen", subCommandName="image" )
	public ImageExecution screenShot( @ResourceID String executionId, @Parameter( name="handsetId" ) String handsetId, @Parameter( name="key" ) String key, @Parameter( name="source" ) Screen source, @Parameter( name="format" ) ImageFormat format, @Parameter( name="report.resolution") Resolution resolution );
	
}
