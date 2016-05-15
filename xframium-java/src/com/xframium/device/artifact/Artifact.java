/*
 * 
 */
package com.xframium.device.artifact;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class Artifact.
 */
public class Artifact
{
	
	/** The log. */
	private Log log = LogFactory.getLog( Artifact.class );
	
	/** The artifact data. */
	private byte[] artifactData;
	
	/** The artifact name. */
	private String artifactName;
	
	/** The file name. */
	private File fileName;
	
	/**
	 * Instantiates a new artifact.
	 *
	 * @param artifactName the artifact name
	 * @param artifactData the artifact data
	 */
	public Artifact( String artifactName, byte[] artifactData )
	{
		this.artifactData = artifactData;
		this.artifactName = artifactName;
	}
	
	/**
	 * Gets the artifact data.
	 *
	 * @return the artifact data
	 */
	public byte[] getArtifactData()
	{
		return artifactData;
	}

	/**
	 * Sets the artifact data.
	 *
	 * @param artifactData the new artifact data
	 */
	public void setArtifactData( byte[] artifactData )
	{
		this.artifactData = artifactData;
	}

	/**
	 * Gets the artifact name.
	 *
	 * @return the artifact name
	 */
	public String getArtifactName()
	{
		return artifactName;
	}

	/**
	 * Sets the artifact name.
	 *
	 * @param artifactName the new artifact name
	 */
	public void setArtifactName( String artifactName )
	{
		this.artifactName = artifactName;
	}

	/**
	 * Write to disk.
	 *
	 * @param rootFolder the root folder
	 */
	public void writeToDisk( File rootFolder )
	{
		this.fileName = new File( rootFolder, artifactName );
		
		if ( !this.fileName.getParentFile().exists() )
			this.fileName.getParentFile().mkdirs();
	
		if ( artifactData == null )
		{
			log.warn( "Attempting to save empty data to [" + fileName.getAbsolutePath() + "]" );
			return;
		}
		
		if ( log.isInfoEnabled() )
			log.info( "Attempting to write [" + artifactData.length + "] bytes to [" + fileName.getAbsolutePath() + "]" );
		
		try
		{
			BufferedOutputStream oStream = new BufferedOutputStream( new FileOutputStream( fileName ) );
			oStream.write( artifactData );
			oStream.flush();
			oStream.close();
		}
		catch( Exception e )
		{
			log.error( "Could not save artifact to [" + fileName.getAbsolutePath() + "]", e );
		}
		
	}
	
	
}
