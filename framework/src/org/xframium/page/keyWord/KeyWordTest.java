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
package org.xframium.page.keyWord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyWordTest.
 */
public class KeyWordTest
{
	
	/** The log. */
	private Log log = LogFactory.getLog( KeyWordTest.class );
	
	/** The name. */
	private String name;
	
	/** The active. */
	private boolean active;
	
	/** The data providers. */
	private String[] dataProviders;
	
	/** The data driver. */
	private String dataDriver;
	
	/** The timed. */
	private boolean timed;
	
	/** The link id. */
	private String linkId;
	
	/** The os. */
	private String os;
	
	/** The description. */
	private String description;
	
	/** The threshold. */
	private int threshold;
	
	/** The test tags. */
	private String[] testTags;
	
	/** The step list. */
	private List<KeyWordStep> stepList = new ArrayList<KeyWordStep>( 10 );
	
	/**
	 * Instantiates a new key word test.
	 *
	 * @param name the name
	 * @param active the active
	 * @param dataProviders the data providers
	 * @param dataDriver the data driver
	 * @param timed the timed
	 * @param linkId the link id
	 * @param os the os
	 * @param threshold the threshold
	 * @param description the description
	 * @param testTags the test tags
	 */
	public KeyWordTest( String name, boolean active, String dataProviders, String dataDriver, boolean timed, String linkId, String os, int threshold, String description, String testTags )
	{
		this.name = name;
		this.active = active;
		if ( dataProviders != null )
			this.dataProviders = dataProviders.split( "," );
		this.dataDriver = dataDriver;
		this.timed = timed;
		this.linkId = linkId;
		this.os = os;
		this.threshold = threshold;
		this.description = description;
		
		if ( testTags != null )
		    this.testTags = testTags.split( "," );
		else
		    this.testTags = new String[] { "" };
	}

	/**
	 * Copy test.
	 *
	 * @param testName the test name
	 * @return the key word test
	 */
	public KeyWordTest copyTest( String testName )
	{
		KeyWordTest newTest = new KeyWordTest( testName, active, null, dataDriver, timed, linkId, os, threshold, description, null );
		newTest.dataProviders = dataProviders;
		newTest.stepList = stepList;
		newTest.testTags = testTags;
		return newTest;
	}
	
	/**
	 * Gets the data driver.
	 *
	 * @return the data driver
	 */
	public String getDataDriver()
	{
		return dataDriver;
	}

	/**
	 * Adds the step.
	 *
	 * @param step the step
	 */
	public void addStep( KeyWordStep step )
	{
		if ( step.isActive() )
		{
			if ( log.isDebugEnabled() )
				log.debug( "Adding Step [" + step.getName() + "] to [" + name + "]"  );
			stepList.add( step );
		}
	}

        /**
	 * Get step at offset.
	 *
	 * @param step the step
	 */
	public KeyWordStep getStepAt( int offset )
	{
            return (KeyWordStep) stepList.get( offset );
	}
	
	/**
	 * Gets the data providers.
	 *
	 * @return the data providers
	 */
	public String[] getDataProviders()
	{
		return dataProviders;
	}
	
	/**
	 * Gets the data providers.
	 *
	 * @return the data providers
	 */
	public String getDataProvidersAsString()
	{
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(" [");
		for ( String provider : dataProviders )
			sBuilder.append( provider ).append(", " );
		sBuilder.append( "]" );
		return sBuilder.toString();
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName( String name )
	{
		this.name = name;
	}
	
	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public boolean isActive()
	{
		return active;
	}
	
	/**
	 * Checks if is timed.
	 *
	 * @return true, if is timed
	 */
	public boolean isTimed()
	{
		return timed;
	}

	/**
	 * Execute test.
	 *
	 * @param webDriver the web driver
	 * @param contextMap the context map
	 * @param dataMap the data map
	 * @param pageMap the page map
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean executeTest( WebDriver webDriver, Map<String,Object> contextMap, Map<String,PageData> dataMap, Map<String,Page> pageMap ) throws Exception
	{
		if ( log.isInfoEnabled() )
			log.info( "*** Executing Test " + name + ( linkId != null ? " linked to " + linkId : "" ) );
		
		long startTime = System.currentTimeMillis();
		
		boolean stepSuccess = true;

		String executionId = PageManager.instance().getExecutionId(webDriver);
		String deviceName = PageManager.instance().getDeviceName(webDriver);
		
		for( KeyWordStep step : stepList )
		{
			if ( log.isDebugEnabled() )
				log.debug( "Executing Step [" + step.getName() + "]" );
			
			Page page = null;
			if ( step.getPageName() != null )
			{
			    page = pageMap.get( step.getPageName() );
			    if ( page == null )
			    {
			        if ( log.isInfoEnabled() )
			            log.info( "Creating Page [" + step.getPageName() + "]" );
    				page = PageManager.instance().createPage( KeyWordDriver.instance().getPage( step.getPageName() ), webDriver );
    				pageMap.put( step.getPageName(), page );
			    }
			}
			
			stepSuccess = step.executeStep( page, webDriver, contextMap, dataMap, pageMap );
			
			if ( !stepSuccess )
			{
				if ( log.isWarnEnabled() )
					log.warn( "***** Step [" + step.getName() + "] Failed" );
				
				PageManager.instance().addExecutionLog( executionId, deviceName, "", this.getName(), "Test", startTime, System.currentTimeMillis() - startTime, StepStatus.FAILURE, "", null, 0, "", false );
				
				if ( timed )
					PageManager.instance().addExecutionTiming( executionId, deviceName, getName(), System.currentTimeMillis() - startTime, StepStatus.FAILURE, description, threshold );
				
				if ( PageManager.instance().getThrowable() == null )
					PageManager.instance().setThrowable( new IllegalStateException( step.toError() ) );
				
				return false;

			}	
			
		}
		
		PageManager.instance().addExecutionLog( executionId, deviceName, "", this.getName(), "Test", startTime, System.currentTimeMillis() - startTime, StepStatus.SUCCESS, "", null, 0, "", false );
		
		if ( timed )
			PageManager.instance().addExecutionTiming( executionId, deviceName, getName(), System.currentTimeMillis() - startTime, StepStatus.SUCCESS, description, threshold );
		
		return stepSuccess;
	}

	/**
	 * Gets the os.
	 *
	 * @return the os
	 */
	public String getOs()
	{
		return os;
	}

	/**
	 * Sets the os.
	 *
	 * @param os the new os
	 */
	public void setOs( String os )
	{
		this.os = os;
	}
	
	/**
	 * Checks if is tagged.
	 *
	 * @param tagName the tag name
	 * @return true, if is tagged
	 */
	public boolean isTagged( String tagName )
	{
	    if ( testTags == null )
	        return false;
	    
	    for ( String testTag : testTags )
	    {
	        if ( tagName.equalsIgnoreCase( testTag ) )
	            return true;
	    }
	    
	    return false;
	}
	
	/**
	 * Gets the tags.
	 *
	 * @return the tags
	 */
	public String[] getTags()
	{
	    if ( testTags == null )
	        return new String[] { "" };
	    else 
	        return testTags;
	}
	
	
}
