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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.KeyWordPage;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.spi.KeyWordPageImpl;
import org.xframium.page.keyWord.step.KeyWordStepFactory;
import org.xframium.reporting.ExecutionContextTest;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractPage.
 *
 * @author ageary
 */
public abstract class AbstractPage implements Page
{
    
    /** The log. */
    protected Log log = LogFactory.getLog( Page.class );

    /** The element cache. */
    private HashMap<String,Element> elementCache = new HashMap<String,Element>( 20 );
    
    /** The element list. */
    private List<Element> elementList = new ArrayList<Element>( 10 );
    
    /** The web driver. */
    protected Object webDriver;
    
    private ExecutionContextTest executionContext;
    
    @Override
    public ExecutionContextTest getExecutionContext()
    {
        return executionContext;
    }
    
    @Override
    public void setExecutionContext( ExecutionContextTest executionContext )
    {
        this.executionContext = executionContext;
    }
    
    /**
     * Register element.
     *
     * @param elementDescriptor the element descriptor
     * @param currentElement the current element
     */
    public void registerElement( ElementDescriptor elementDescriptor, Element currentElement )
    {
    	if ( log.isDebugEnabled() )
    		log.debug( "Registering CACHED element using [" + elementDescriptor.toString() + "] as " + currentElement + " on " + getClass().getName() );
    	elementCache.put( elementDescriptor.toString(), currentElement );
    	elementList.add( currentElement );
    	currentElement.setDriver( webDriver );
    }
    
    /**
     * Gets the element.
     *
     * @param siteName the site name
     * @param pageName the page name
     * @param elementName the element name
     * @return the element
     */
    public Element getElement( String siteName, String pageName, String elementName )
    {
    	ElementDescriptor elementDescriptor = new ElementDescriptor( siteName, pageName, elementName );
    	return getElement( elementDescriptor );
    	
    }
    
    public Element getElement( ElementDescriptor elementDescriptor )
    {
        if ( log.isDebugEnabled() )
            log.debug( "Attempting to locate element using [" + elementDescriptor.toString() + "]" );
        
        return elementCache.get( elementDescriptor.toString() );
    }
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.Page#getElement(java.lang.String, java.lang.String)
     */
    public Element getElement( String pageName, String elementName )
    {
    	return getElement( PageManager.instance(( (DeviceWebDriver) webDriver ).getxFID()).getSiteName(), pageName, elementName );
    }
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.Page#getElement(java.lang.String)
     */
    public Element getElement( String elementName )
    {
    	return getElement( PageManager.instance(( (DeviceWebDriver) webDriver ).getxFID()).getSiteName(), this.getClass().getInterfaces()[ 0 ].getSimpleName(), elementName );
    }
    
    
    /**
     * Gets the page name.  This will extract the Class simple name
     *
     * @return the page name
     */
    public String getPageName()
    {
    	return getClass().getInterfaces()[ 0 ].getSimpleName();
    }
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.Page#setDriver(java.lang.Object)
     */
    public void setDriver( Object webDriver )
    {
    	this.webDriver = webDriver;
    }
    
    protected Object getDriver()
    {
        return this.webDriver;
    }
    
    /**
     * Creates a KeyWord step to be used in a Java test
     *
     * @param keyword The name of the keyword
     * @param pageName the page name
     * @param elementName the element name
     * @param parameterList A list of parameter to pass to the keyword.  User name==value for named parameters
     * @return The created step
     */
    protected KeyWordStep createStep( String keyword, String pageName, String elementName, String[] parameterList )
    {
        KeyWordStep step = KeyWordStepFactory.instance().createStep( elementName, pageName, true, keyword, null, false, StepFailure.ERROR, false, null, null, null, 0, null, 0, keyword, null, null, null, null, false, false, null, null, null, null );
        
        if ( parameterList != null )
        {
            for ( String s : parameterList )
            {
                String[] sArray = s.split( "==" );
                if ( s.contains( "==" ) )
                    step.addParameter( new KeyWordParameter( ParameterType.STATIC, sArray.length > 1 ? sArray[1] : "{EMPTY}", sArray[0], null ) );
                else
                    step.addParameter( new KeyWordParameter( ParameterType.STATIC, s, null, null ) );
            }
        }
        
        return step;
        
    }
    
    /**
     * Execute a keyword step
     *
     * @param step the step
     * @param webDriver the web driver
     * @return true, if successful
     * @throws Exception the exception
     */
    protected Map<String,Object> executeStep( KeyWordStep step ) throws Exception
    {
        KeyWordPage p = new KeyWordPageImpl();
        p.setPageName( step.getPageName() );
        Map<String,Object> contextMap = new HashMap<String,Object>( 10 );
        contextMap.put( "RESULT", step.executeStep( p, ( (DeviceWebDriver) webDriver ), contextMap, null, null, null, ( (DeviceWebDriver) webDriver ).getExecutionContext() ) );
        return contextMap;
    }
    
    /**
     * Creates and executes a KeyWord step
     *
     * @param keyword The name of the keyword
     * @param pageName the page name
     * @param elementName the element name
     * @param parameterList A list of parameter to pass to the keyword.  User name==value for named parameters
     * @param webDriver The webDriver for the active run
     * @return The created step
     */
    protected Map<String,Object> executeStep( String keyword, String pageName, String elementName, String[] parameterList ) throws Exception
    {
        KeyWordPage p = new KeyWordPageImpl();
        p.setPageName( pageName );
        Map<String,Object> contextMap = new HashMap<String,Object>( 10 );
        contextMap.put( "RESULT", createStep( keyword, pageName, elementName, parameterList ).executeStep( p, ( (DeviceWebDriver) webDriver ), contextMap, null, null, null, ( (DeviceWebDriver) webDriver ).getExecutionContext() ) );
        return contextMap;
    }

    
    
}
