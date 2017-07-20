package org.xframium.artifact.spi;

import java.io.File;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.spi.driver.ReportiumProvider;

public class PerfectoReportingServices extends AbstractArtifact
{
    public PerfectoReportingServices()
    {
        setArtifactType( ArtifactType.REPORTIUM.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        
        if ( webDriver.getCloud().getProvider().equals( "PERFECTO" ) )
        {
            if ( ((ReportiumProvider) webDriver).getReportiumClient() != null )
            {
                webDriver.getExecutionContext().addExecutionParameter( getArtifactType() + "_" + URL, webDriver.getReportiumClient().getReportUrl() );
            }
        }
        
        return null;
    }
}
