package org.xframium.artifact.spi;

import java.io.File;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.spi.driver.ReportiumProvider;

public class PerfectoWindTunnel extends AbstractArtifact
{
    public PerfectoWindTunnel()
    {
        setArtifactType( ArtifactType.WIND_TUNNEL.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        
        if ( webDriver.getCloud().getProvider().equals( "PERFECTO" ) )
        {
            String wtUrl = ((DeviceWebDriver) webDriver).getWindTunnelReport();
            if ( webDriver.getExecutionContext() != null && wtUrl != null && !wtUrl.isEmpty() )
                webDriver.getExecutionContext().addExecutionParameter( "PERFECTO_WT", wtUrl );
        }
        
        return null;
    }
}
