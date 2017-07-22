package org.xframium.artifact.spi;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.reporting.ExecutionContext;
import com.xframium.serialization.SerializationManager;

public class HTMLGridArtifact extends AbstractArtifact
{
    private static final String FILE_NAME = "Grid.html";
    public HTMLGridArtifact()
    {
        setArtifactType( ArtifactType.GRID_HTML.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        InputStream inputStream = null;
        try
        {
            ExecutionContext.instance( xFID ).setGridUrl( "Grid.html" );
            inputStream = getTemplate( "Grid.html" );
            
            return writeToDisk( rootFolder, FILE_NAME, inputStream );
        }
        catch( Exception e )
        {
            log.error( "Error generating GRID HTML Report", e );
            return null;
        }
        finally
        {
            try { inputStream.close(); } catch( Exception e ) {}
        }
    }
}
