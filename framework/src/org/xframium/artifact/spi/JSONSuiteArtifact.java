package org.xframium.artifact.spi;

import java.io.File;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.reporting.ExecutionContext;
import com.xframium.serialization.SerializationManager;

public class JSONSuiteArtifact extends AbstractArtifact
{
    private static final String FILE_NAME = "Suite.js";
    
    public JSONSuiteArtifact()
    {
        setArtifactType( ArtifactType.EXECUTION_SUITE_JSON.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        return writeToDisk( rootFolder, FILE_NAME, ("var testData = " + new String( SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), ExecutionContext.instance( xFID ), 0 ) ) + ";").getBytes() );
    }
    
    
}
