/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xframium.page.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xframium.page.Page;

// TODO: Auto-generated Javadoc
/**
 * A abstract factory for creating page objects.
 *
 * @author ageary
 */
public abstract class AbstractPageFactory implements PageFactory
{
    
    /** The log. */
    protected Log log = LogFactory.getLog( getClass() );

    /** The web driver. */
    protected Object webDriver;
    
    /**
     * Instantiates a new abstract page factory.
     */
    public AbstractPageFactory()
    {
    	
    }
    
    /**
     * Service created.
     *
     * @param serviceImpl the service impl
     */
    protected void serviceCreated( Page serviceImpl )
    {

    }

    /* (non-Javadoc)
     * @see com.perfectoMobile.page.factory.PageFactory#createPage(java.lang.Class, java.lang.Object)
     */
    public final Page createPage( Class<Page> pageInterface, Object webDriver )
    {
    	this.webDriver = webDriver;
        Page serviceImpl = _createPage( pageInterface, webDriver );
        serviceCreated( serviceImpl );
        return serviceImpl;
    }

    /**
     * _create page.
     *
     * @param serviceInterface the service interface
     * @param webDriver the web driver
     * @return the page
     */
    protected abstract Page _createPage( Class<Page> serviceInterface, Object webDriver );

}
