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
package org.xframium.page.listener;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving execution events.
 * The class that is interested in processing a execution
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addExecutionListener</code> method. When
 * the execution event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ExecutionEvent
 */
public interface ExecutionListener
{
	
	/**
	 * Before execution.
	 *
	 * @param methodKey the method key
	 */
	public void beforeExecution( String methodKey );
	
	/**
	 * After execution.
	 *
	 * @param methodKey the method key
	 * @param runLength the run length
	 */
	public void afterExecution( String methodKey, long runLength );
}
