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
package org.xframium.integrations.perfectoMobile.rest.bean;

import org.xframium.integrations.rest.bean.AbstractBean;
import org.xframium.integrations.rest.bean.Bean.BeanDescriptor;
import org.xframium.integrations.rest.bean.Bean.FieldDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class Execution.
 */
@BeanDescriptor( beanName="response" )
public class Response extends AbstractBean
{
	
	/** The report key. */
	@FieldDescriptor ( )
	private String executionId;

	/** The status. */
	@FieldDescriptor ( )
	private String flowEndCode;
	
	/** The return value. */
	@FieldDescriptor ( )
	private String reason;
	
	@FieldDescriptor ( )
    private Long returnValue;


    public String getExecutionId()
    {
        return executionId;
    }

    public void setExecutionId( String executionId )
    {
        this.executionId = executionId;
    }

    public String getFlowEndCode()
    {
        return flowEndCode;
    }

    public void setFlowEndCode( String flowEndCode )
    {
        this.flowEndCode = flowEndCode;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason( String reason )
    {
        this.reason = reason;
    }

    public Long getReturnValue()
    {
        return returnValue;
    }

    public void setReturnValue( Long returnValue )
    {
        this.returnValue = returnValue;
    }

    @Override
    public String toString()
    {
        return "Response [executionId=" + executionId + ", flowEndCode=" + flowEndCode + ", reason=" + reason + ", returnValue=" + returnValue + "]";
    }

	
	
	
	

}
