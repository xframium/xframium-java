package org.xframium.artifact.spi;

import java.io.File;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;

public class CSVDataArtifact extends AbstractArtifact
{
    private static final String FILE_NAME = "outputData.csv";
    public CSVDataArtifact()
    {
        setArtifactType( ArtifactType.ADD_TO_CSV.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        
        if ( webDriver.getExecutionContext() == null )
            return null;

        return writeToDisk( rootFolder, FILE_NAME, webDriver.getExecutionContext().getCSVReport().getBytes() );
    }
}
