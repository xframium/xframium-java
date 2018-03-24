package org.xframium.artifact;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.device.factory.DeviceWebDriver;

public abstract class AbstractArtifact implements Artifact
{
    protected Log log = LogFactory.getLog( Artifact.class );
    
    
    protected abstract File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID ) throws Exception;
    
    private String artifactType;
    private File fileName;
    
    protected void setArtifactType( String artifactType )
    {
        this.artifactType = artifactType;
    }
    
    protected String getArtifactType()
    {
        return artifactType;
    }
    
    protected InputStream getTemplate( String templateName ) throws FileNotFoundException
    {
        if ( System.getProperty( "reportTemplateFolder" ) == null )
        {
            if ( System.getProperty( "reportTemplate" ) == null )
                return getClass().getClassLoader().getResourceAsStream( "org/xframium/reporting/html/light/" + templateName );
            else
                return getClass().getClassLoader().getResourceAsStream( "org/xframium/reporting/html/" + System.getProperty( "reportTemplate" ) + "/" + templateName );
        }
        else
            return new FileInputStream( new File(  System.getProperty( "reportTemplateFolder" ), templateName ) );
    }
    
    public File generateArtifact( String rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        if ( log.isInfoEnabled() )
            log.info( Thread.currentThread().getName() + ": Generating artifact for " + getArtifactType() );
        
        try 
        {
            File rootFile = new File( rootFolder );
            if ( !rootFile.exists() )
                rootFile.mkdirs();
            
            File returnFile =  _generateArtifact( rootFile, webDriver, xFID );
            
            if ( returnFile == null && (webDriver == null || webDriver.getExecutionContext().getElementParameter( artifactType + "_" + URL ) == null ) )
                return null;
            
            if ( webDriver != null)
            {
                if ( returnFile != null )
                {
                    webDriver.getExecutionContext().addExecutionParameter( artifactType + "_FILE", returnFile.getName() );
                    webDriver.getExecutionContext().addExecutionParameter( artifactType + "_ABS", returnFile.getAbsolutePath() );
                    webDriver.getExecutionContext().addExecutionParameter( artifactType + "_PATH", returnFile.getPath() );
                }
                
                webDriver.getExecutionContext().addExecutionParameter( artifactType, "enabled" );
            }
            
            if ( artifactType.equals( ArtifactManager.instance( xFID ).getDisplayArtifact() ) )
            {
                try
                {
                    if ( returnFile != null )
                        Desktop.getDesktop().open( returnFile );
                    else if ( webDriver.getExecutionContext().getExecutionParameter( artifactType + "_" + URL ) != null )
                        Desktop.getDesktop().browse( URI.create( webDriver.getExecutionContext().getExecutionParameter( artifactType + "_" + URL ) ) );
                }
                catch( Exception e )
                {
                    log.warn( "Could not display configured artifact" );
                }
            }
            
            
            
        }
        catch( Exception e )
        {
            log.warn( "Error generating artifact for " + artifactType + "(" + e.getMessage() + ")" );
        }
        
        return null;
    }
    
    protected File writeToDisk( File rootFolder, String useName, byte[] artifactData )
    {
        return writeToDisk( rootFolder, useName, new ByteArrayInputStream( artifactData ) );
    }
    
    protected File writeToDisk( File rootFolder, String useName, InputStream inputStream )
    {
    		return writeToDisk( rootFolder, useName, inputStream, null );
    }
    
    protected File writeToDisk( File rootFolder, String useName, InputStream inputStream, Map<String,String> replacementMap )
    {
        if ( inputStream == null )
            return null;
        
        this.fileName = new File( rootFolder, useName );
        
        if ( !this.fileName.getParentFile().exists() )
            this.fileName.getParentFile().mkdirs();
    
        
        byte[] buffer = new byte[ 2048 ];
        int bytesRead = 0;

        if ( log.isInfoEnabled() )
            log.info( "Writing stream to " + fileName.getAbsolutePath() );
        
        try
        {
            BufferedOutputStream oStream = new BufferedOutputStream( new FileOutputStream( fileName ) );
            
            
            if ( replacementMap == null )
            {
	            while ( ( bytesRead = inputStream.read( buffer ) ) > 0 )
	                oStream.write( buffer, 0, bytesRead );
	            
	            
            }
            else
            {
            		StringBuilder sBuilder = new StringBuilder();
            		while ( ( bytesRead = inputStream.read( buffer ) ) > 0 )
            			sBuilder.append( new String( buffer, 0, bytesRead ) );
            		
            		String fileData = sBuilder.toString();
            		for ( String keyName : replacementMap.keySet() )
            			fileData = fileData.replace( keyName, replacementMap.get( keyName ) );
            		
            		oStream.write( fileData.getBytes() );
            }
            
            oStream.flush();
            oStream.close();
            return this.fileName;
        }
        catch( Exception e )
        {
            log.error( "Could not save artifact to [" + fileName.getAbsolutePath() + "]", e );
        }
        
        return null;
    }
}
