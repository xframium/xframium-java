package org.xframium.artifact.spi;

import java.io.File;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.Artifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;

public class XMLSourceArtifact extends AbstractArtifact implements Artifact
{
    private static final String FILE_NAME = "failureDOM.xml";
    public XMLSourceArtifact()
    {
        setArtifactType( ArtifactType.FAILURE_SOURCE.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        return writeToDisk( rootFolder, FILE_NAME, webDriver.getPageSource().getBytes() );
    }
}
