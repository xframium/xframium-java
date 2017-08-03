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
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.page.keyWord.KeyWordDriver;

// TODO: Auto-generated Javadoc
/**
 * The Class DataManager.
 */
public class DataManager
{
	
	/** The singleton. */
	private static Map<String,DataManager> singleton = new HashMap<String,DataManager>(3);

	/**
	 * Instance.
	 *
	 * @return the data manager
	 */
	public static DataManager instance( String xFID )
	{
	    if ( singleton.containsKey( xFID ) )
            return singleton.get( xFID );
        else
        {
            singleton.put( xFID, new DataManager() );
            return singleton.get( xFID );
        }
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
	
	/** The report folder. */
	private File reportFolder;
	

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
	public void readData( DataProvider dataProvider, String xFID )
	{
		if ( log.isInfoEnabled() )
			log.info( "Reading Device Data from " + dataProvider );
		
		dataProvider.readData(xFID);
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
}
