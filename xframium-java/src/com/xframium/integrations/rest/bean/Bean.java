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
package com.xframium.integrations.rest.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


// TODO: Auto-generated Javadoc
/**
 * The Interface Bean.
 */
public interface Bean
{
	
	/**
	 * The Interface FieldDescriptor.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FieldDescriptor
	{
		
		/**
		 * Field path.
		 *
		 * @return the string
		 */
		String fieldPath() default "";
		
		/**
		 * Text content.
		 *
		 * @return true, if successful
		 */
		boolean textContent() default false;
	}
	
	/**
	 * The Interface FieldCollection.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FieldCollection
	{
		
		/**
		 * Field path.
		 *
		 * @return the string
		 */
		String fieldPath();
		
		/**
		 * Field element.
		 *
		 * @return the class
		 */
		Class fieldElement();
	}
	
	/**
	 * The Interface BeanDescriptor.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface BeanDescriptor
	{
		
		/**
		 * Bean name.
		 *
		 * @return the string
		 */
		String beanName() default "";
	}
	
}
