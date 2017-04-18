package org.xframium.artifact.spi;

import java.io.File;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;

public class VitalsArtifact extends AbstractArtifact
{
    private static final String FILE_NAME = "statistics.csv";
    public VitalsArtifact()
    {
        setArtifactType( ArtifactType.STATISTICS.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver )
    {
        return writeToDisk( rootFolder, FILE_NAME, webDriver.getCloud().getCloudActionProvider().getVitals( webDriver ).getBytes() );
    }
}
