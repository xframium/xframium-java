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
package com.xframium.integrations.rest.bean.factory;

import java.io.InputStream;
import com.xframium.integrations.rest.bean.Bean;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Bean objects.
 */
public interface BeanFactory
{
	
	/**
	 * Creates a new Bean object.
	 *
	 * @param returnType the return type
	 * @param inputData the input data
	 * @return the bean
	 * @throws Exception the exception
	 */
	public Bean createBean( Class returnType, String inputData ) throws Exception;
	
	/**
	 * Creates a new Bean object.
	 *
	 * @param returnType the return type
	 * @param inputStream the input stream
	 * @return the bean
	 * @throws Exception the exception
	 */
	public Bean createBean( Class returnType, InputStream inputStream ) throws Exception;
}
