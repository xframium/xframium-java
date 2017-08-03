package org.xframium.artifact.spi;

import java.io.File;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;

public class PerfectoCSVReport extends AbstractArtifact
{
    private static final String FILE_NAME = ArtifactType.EXECUTION_REPORT_CSV.name() + ".csv";
    public PerfectoCSVReport()
    {
        setArtifactType( ArtifactType.EXECUTION_REPORT_CSV.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        
        if ( webDriver.getCloud().getProvider().equals( "PERFECTO" ) )
        {
        
            if ( webDriver.getExecutionContext() == null )
                return null;
    
            return writeToDisk( rootFolder, FILE_NAME, webDriver.getCloud().getCloudActionProvider().getReport( webDriver, "csv" ) );
        }
        
        return null;
    }
}
