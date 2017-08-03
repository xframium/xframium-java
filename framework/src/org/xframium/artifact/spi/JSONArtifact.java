package org.xframium.artifact.spi;

import java.io.File;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;
import com.xframium.serialization.SerializationManager;

public class JSONArtifact extends AbstractArtifact
{
    private static final String FILE_NAME = "Test.js";
    public JSONArtifact()
    {
        setArtifactType( ArtifactType.EXECUTION_RECORD_JSON.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        if ( webDriver.getExecutionContext() == null )
            return null;
        
        webDriver.getExecutionContext().setFolderName( webDriver.getArtifactFolder().getPath() );
        webDriver.getExecutionContext().popupateSystemProperties();
        webDriver.getExecutionContext().setDevice( webDriver.getPopulatedDevice() );
        return writeToDisk( rootFolder, FILE_NAME, ("var testData = " + new String( SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), webDriver.getExecutionContext(), 0 ) ) + ";").getBytes() );
    }
}
