package org.xframium.artifact.spi;

import java.io.File;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.spi.driver.ReportiumProvider;

public class SauceLabsReportingServices extends AbstractArtifact
{
    public SauceLabsReportingServices()
    {
        setArtifactType( ArtifactType.SAUCE_LABS.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        if ( webDriver.getCloud().getProvider().equals( "SAUCELABS" ) )
        {
        	// SauceLabs has two different implementations at this time
        	
        	if ( webDriver.getCapabilities().getCapability("testobject_test_report_url") != null )
        		webDriver.getExecutionContext().addExecutionParameter( getArtifactType() + "_" + URL, webDriver.getCapabilities().getCapability("testobject_test_report_url") + "" );
        	else
        		webDriver.getExecutionContext().addExecutionParameter( getArtifactType() + "_" + URL, "https://saucelabs.com/beta/tests/" + webDriver.getExecutionContext().getSessionId() + "/commands#0" );
        }
        
        return null;
    }
}
