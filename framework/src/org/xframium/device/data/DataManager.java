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
package org.xframium.device.data;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.artifact.ArtifactProducer;
import org.xframium.device.artifact.api.PerfectoArtifactProducer;

// TODO: Auto-generated Javadoc
/**
 * The Class DataManager.
 */
public class DataManager
{
	
	/** The singleton. */
	private static DataManager singleton = new DataManager();

	/**
	 * Instance.
	 *
	 * @return the data manager
	 */
	public static DataManager instance()
	{
		return singleton;
	}
	
	private String testTags;
	public void setTestTags( String testTags )
	{
	    this.testTags = testTags;
	}
	
	public String getTestTags()
	{
	    return testTags;
	}

	/**
	 * Instantiates a new data manager.
	 */
	private DataManager()
	{
	    setReportFolder( new File( "test-output" ) );
	}
	
	/** The log. */
	private Log log = LogFactory.getLog( DataManager.class );
	
	/** The test names. */
	private String[] testNames;
	
	/** The persona names. */
	private String[] personaNames;
	
	/** The artifact producer. */
	private ArtifactProducer artifactProducer = new PerfectoArtifactProducer();
	
	/** The report folder. */
	private File reportFolder;
	
	/** The automatic downloads. */
	private ArtifactType[] automaticDownloads = new ArtifactType[] { ArtifactType.EXECUTION_REPORT_HTML, ArtifactType.CONSOLE_LOG, ArtifactType.FAILURE_SOURCE, ArtifactType.EXECUTION_RECORD_HTML, ArtifactType.EXECUTION_RECORD_CSV };

	/**
	 * Sets the tests.
	 *
	 * @param testNames the new tests
	 */
	public void setTests( String[] testNames )
	{
		this.testNames = testNames;
	}
	
	/**
	 * Gets the tests.
	 *
	 * @return the tests
	 */
	public String[] getTests()
	{
		return testNames;
	}
	
	
	
	/**
	 * Gets the personas.
	 *
	 * @return the personas
	 */
	public String[] getPersonas() 
	{
		return personaNames;
	}

	/**
	 * Sets the personas.
	 *
	 * @param personaNames the new personas
	 */
	public void setPersonas(String[] personaNames) 
	{
		this.personaNames = personaNames;
	}
	
	/**
	 * Sets the personas.
	 *
	 * @param personaNames the new personas
	 */
	public void setPersonas(String personaNames) 
	{
		this.personaNames = personaNames.split( "," );
	}

	/**
	 * Read data.
	 *
	 * @param dataProvider the data provider
	 */
	public void readData( DataProvider dataProvider )
	{
		if ( log.isInfoEnabled() )
			log.info( "Reading Device Data from " + dataProvider );
		
		dataProvider.readData();
	}
	
	/**
	 * Sets the artifact producer.
	 *
	 * @param artifactProducer the new artifact producer
	 */
	public void setArtifactProducer( ArtifactProducer artifactProducer )
	{
		this.artifactProducer = artifactProducer;
	}

	/**
	 * Gets the artifact producer.
	 *
	 * @return the artifact producer
	 */
	public ArtifactProducer getArtifactProducer()
	{
		return artifactProducer;
	}

	/**
	 * Gets the report folder.
	 *
	 * @return the report folder
	 */
	public File getReportFolder()
	{
		return reportFolder;
	}

	/**
	 * Sets the report folder.
	 *
	 * @param reportFolder the new report folder
	 */
	public void setReportFolder( File reportFolder )
	{
		System.setProperty( "__outputFolder", reportFolder.getAbsolutePath() );
		this.reportFolder = reportFolder;
	}

	/**
	 * Gets the automatic downloads.
	 *
	 * @return the automatic downloads
	 */
	public ArtifactType[] getAutomaticDownloads()
	{
		return automaticDownloads;
	}
	
	public boolean isArtifactEnabled( ArtifactType isType )
	{
	    if ( automaticDownloads == null )
	        return false;
	    
	    for ( ArtifactType aType : automaticDownloads )
	    {
	        if ( aType.equals( isType ) )
	            return true;
	    }
	    
	    return false;
	}

	/**
	 * Sets the automatic downloads.
	 *
	 * @param automaticDownloads the new automatic downloads
	 */
	public void setAutomaticDownloads( ArtifactType[] automaticDownloads )
	{
		this.automaticDownloads = automaticDownloads;
	}
	
	

	
}
