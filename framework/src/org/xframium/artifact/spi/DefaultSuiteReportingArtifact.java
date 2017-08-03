package org.xframium.artifact.spi;

import java.io.File;
import java.io.InputStream;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;

public class DefaultSuiteReportingArtifact extends AbstractArtifact
{
    private static final String FILE_NAME = "index.html";
    public DefaultSuiteReportingArtifact()
    {
        setArtifactType( ArtifactType.EXECUTION_SUITE_HTML.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        InputStream inputStream = null;
        try
        {
            inputStream = getTemplate( "Suite.html" );
            
            return writeToDisk( rootFolder, FILE_NAME, inputStream );
        }
        catch( Exception e )
        {
            log.error( "Error generating HTML", e );
            return null;
        }
        finally
        {
            try { inputStream.close(); } catch( Exception e ) {}
        }
    }
}
