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
package org.xframium.integrations.sauceLabs.rest.bean;

import org.xframium.integrations.rest.bean.AbstractBean;
import org.xframium.integrations.rest.bean.Bean.BeanDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class Action.
 */
@BeanDescriptor( beanName="timer" )
public class User extends AbstractBean
{

	/** The name. */
	@FieldDescriptor ( fieldPath = "username")
	private String userName;
	
	/** The elapsed. */
	@FieldDescriptor ( fieldPath = "vm_lockdown")
	private Boolean vmLockdown;
	
	/** The device. */
	@FieldDescriptor ( fieldPath = "monthly_minutes")
	private Allowance allowance;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    public Boolean getVmLockdown()
    {
        return vmLockdown;
    }

    public void setVmLockdown( Boolean vmLockdown )
    {
        this.vmLockdown = vmLockdown;
    }

    public Allowance getAllowance()
    {
        return allowance;
    }

    public void setAllowance( Allowance allowance )
    {
        this.allowance = allowance;
    }

    @Override
    public String toString()
    {
        return "User [userName=" + userName + ", vmLockdown=" + vmLockdown + ", allowance=" + allowance + "]";
    }


	

}
