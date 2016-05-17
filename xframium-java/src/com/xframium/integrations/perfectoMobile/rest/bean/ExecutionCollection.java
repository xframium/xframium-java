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
package com.xframium.integrations.perfectoMobile.rest.bean;

import java.util.ArrayList;
import java.util.List;
import com.xframium.integrations.rest.bean.AbstractBean;
import com.xframium.integrations.rest.bean.Bean.BeanDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class ExecutionCollection.
 */
@BeanDescriptor( beanName="response" )
public class ExecutionCollection extends AbstractBean
{
	
	/** The handset list. */
	@FieldCollection( fieldElement=Handset.class, fieldPath="handsets" )
	private List<Handset> handsetList = new ArrayList<Handset>( 10 );
	
	/**
	 * Gets the handset list.
	 *
	 * @return the handset list
	 */
	public List<Handset> getHandsetList()
	{
		return handsetList;
	}
}
