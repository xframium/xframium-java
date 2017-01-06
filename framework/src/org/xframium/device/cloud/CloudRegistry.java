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
/*
 * 
 */
package org.xframium.device.cloud;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.grid.internal.utils.configuration.StandaloneConfiguration;
import org.openqa.selenium.remote.server.SeleniumServer;

// TODO: Auto-generated Javadoc
/**
 * The Class cloudRegistry.
 */
public class CloudRegistry
{
	
	/** The singleton. */
	private static CloudRegistry singleton = new CloudRegistry();

	/**
	 * Instance.
	 *
	 * @return the cloud registry
	 */
	public static CloudRegistry instance()
	{
		return singleton;
	}

	/**
	 * Instantiates a new cloud registry.
	 */
	private CloudRegistry()
	{

	}
	
	/** The log. */
	private Log log = LogFactory.getLog( CloudRegistry.class );
	
	/** The cloud map. */
	private Map<String,CloudDescriptor> cloudMap = new HashMap<String,CloudDescriptor>( 20 );
	
	
	/** The cut. */
	private CloudDescriptor cut = null;
	
	private boolean embeddedGrid = false;
	private boolean serverStarted = false;
	private SeleniumServer _server = null;
	
	public boolean isEmbeddedGrid()
    {
        return embeddedGrid;
    }

    public void setEmbeddedGrid( boolean embeddedGrid )
    {
        this.embeddedGrid = embeddedGrid;
    }

    /**
	 * Adds the cloud descriptor.
	 *
	 * @param cloudDescriptor the cloud descriptor
	 */
	public void addCloudDescriptor( CloudDescriptor cloudDescriptor )
	{
		cloudMap.put( cloudDescriptor.getName(), cloudDescriptor );
		
	}

	
	
	/**
	 * Gets the cloud descriptors.
	 *
	 * @return the cloud descriptors
	 */
	public Collection<CloudDescriptor> getCloudDescriptors()
	{
		return Collections.unmodifiableCollection( cloudMap.values() );
		
	}
	
	/**
	 * Sets the cut.
	 *
	 * @param cloudName the new cloud
	 */
	public void setCloud( String cloudName )
	{
		cut = cloudMap.get( cloudName );
		
		if ( cut == null )
			throw new IllegalArgumentException( "Unknown cloud Identifier " + cloudName );
		
		if ( log.isInfoEnabled() )
			log.info( "cloud Under Test set to " + cut );
		
		if ( cut.getGridInstance() != null && cut.getGridInstance().equals( "EMBEDDED" ) )
		{
		    startEmbeddedCloud();
		}
		
		System.setProperty( "__cloudUrl", "https://" + cut.getHostName() );
		System.setProperty( "__userName", cut.getUserName() );
		System.setProperty( "__password", cut.getPassword() );
	}
	
	public void startEmbeddedCloud()
	{
	    try
        {
	        if ( serverStarted )
	            return;

	        serverStarted = true;
            _server = new SeleniumServer( new StandaloneConfiguration() );
            _server.boot();
            CloudRegistry.instance().addCloudDescriptor( new CloudDescriptor( "EMBEDDED", "", "", "127.0.0.1:4444", "", "0", "", null, "SELENIUM", "SELENIUM", "SELENIUM" ) );
            embeddedGrid=true;
        }
        catch( Exception e )
        {
            log.fatal( "Could not start embedded grid", e );
        }
	}
	
	/**
	 * Gets the aut.
	 *
	 * @param cloudName the cloud name
	 * @return the aut
	 */
	public CloudDescriptor getCloud( String cloudName )
	{
		return cloudMap.get( cloudName );
	}
	
	/**
	 * Gets the aut.
	 *
	 * @return the aut
	 */
	public CloudDescriptor getCloud()
	{
		if ( cut == null && !cloudMap.isEmpty() )
			cut = cloudMap.values().toArray( new CloudDescriptor[ 0 ] )[ 0 ];
		return cut;
	}
	
	public void clear()
	{
	    cloudMap.clear();
	}
	
	public void shutdown()
	{
	    try
	    {
    	    if ( serverStarted && embeddedGrid && _server != null )
    	        _server.stop();
    	    
    	    serverStarted = false;
	    }
	    catch( Exception e )
	    {
	        log.fatal( "Error shutting down embedded grid", e );
	    }
	}
}
