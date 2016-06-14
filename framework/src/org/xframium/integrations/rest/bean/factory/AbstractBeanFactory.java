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
package org.xframium.integrations.rest.bean.factory;

import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.integrations.rest.bean.Bean;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating AbstractBean objects.
 */
public abstract class AbstractBeanFactory implements BeanFactory
{
	
	/** The log. */
	protected Log log = LogFactory.getLog( BeanFactory.class );
	
	/**
	 * _create bean.
	 *
	 * @param returnType the return type
	 * @param inputData the input data
	 * @return the bean
	 * @throws Exception the exception
	 */
	protected abstract Bean _createBean( Class returnType, String inputData ) throws Exception;
	
	/**
	 * _create bean.
	 *
	 * @param returnType the return type
	 * @param inputStream the input stream
	 * @return the bean
	 * @throws Exception the exception
	 */
	protected abstract Bean _createBean( Class returnType, InputStream inputStream ) throws Exception;
	
	/* (non-Javadoc)
	 * @see com.morelandLabs.integrations.rest.bean.factory.BeanFactory#createBean(java.lang.Class, java.lang.String)
	 */
	public Bean createBean( Class returnType, String inputData ) throws Exception
	{
		return _createBean( returnType, inputData );
	}

	/* (non-Javadoc)
	 * @see com.morelandLabs.integrations.rest.bean.factory.BeanFactory#createBean(java.lang.Class, java.io.InputStream)
	 */
	public Bean createBean( Class returnType, InputStream inputStream ) throws Exception
	{
		return _createBean( returnType, inputStream );
	}

}
