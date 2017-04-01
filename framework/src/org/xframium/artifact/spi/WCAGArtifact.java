package org.xframium.artifact.spi;

import java.io.File;
import java.io.InputStream;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;

public class WCAGArtifact extends AbstractArtifact
{
    private static final String FILE_NAME = "WCAG.html";
    public WCAGArtifact()
    {
        setArtifactType( ArtifactType.WCAG_REPORT.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver )
    {
        InputStream inputStream = null;
        try
        {
            inputStream = getTemplate( "wcag.html" );
            return writeToDisk( rootFolder, FILE_NAME, inputStream );
        }
        catch( Exception e )
        {
            log.error( "Error generating HTML", e );
        }
        finally
        {
            try { inputStream.close(); } catch( Exception e ) {}
        }

        
        
        return null;
    }
}
