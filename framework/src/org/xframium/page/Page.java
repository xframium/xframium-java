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
package org.xframium.page;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xframium.page.element.Element;
import org.xframium.reporting.ExecutionContextTest;


// TODO: Auto-generated Javadoc
/**
 * The Interface Page.
 *
 * @author ageary
 */
public interface Page
{
	
	/**
	 * A tag indicating that this element is a page object element.  If the name cannot be derived from the site, class and element name
	 * then you may optional specify those.  By default, the system will use the page dat site name, the page object class simple name
	 * and the element variable name to locate the element from your element provider
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ElementDefinition
	{
		
		/**
		 * Site name.
		 *
		 * @return the string
		 */
		String siteName() default "";
		
		/**
		 * Page name.
		 *
		 * @return the string
		 */
		String pageName() default "";
		
		/**
		 * Element name.
		 *
		 * @return the string
		 */
		String elementName() default "";
	}
	
	/**
	 * A flag indicating that this method call should be timed.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface TimeMethod
	{
		
		/**
		 * Key name.
		 *
		 * @return the string
		 */
		String keyName() default "";
	}
	
	/**
	 * A flag indicating if a screen shot should be taken at the end of a method call - pass or fail.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ScreenShot
	{
	}

	
	/**
	 * Gets the element.
	 *
	 * @param elementName the element name
	 * @return the element
	 */
	public Element getElement( String elementName );
    
    /**
     * Gets the element.
     *
     * @param pageName the page name
     * @param elementName the element name
     * @return the element
     */
    public Element getElement( String pageName, String elementName );
    
    public Element getElement( ElementDescriptor elementDescriptor );
    
    /**
     * Initialize page.
     */
    public void initializePage();
    
    /**
     * Sets the driver.
     *
     * @param webDriver the new driver
     */
    public void setDriver( Object webDriver );
    
    public void setExecutionContext( ExecutionContextTest executionContext );
    public ExecutionContextTest getExecutionContext();
}
