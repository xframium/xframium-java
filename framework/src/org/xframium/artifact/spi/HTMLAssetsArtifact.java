package org.xframium.artifact.spi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.reporting.ExecutionContext;

public class HTMLAssetsArtifact extends AbstractArtifact
{
    private static final String FILE_NAME = "assets.zip";

    public HTMLAssetsArtifact()
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
            inputStream = getTemplate( "assets.zip" );

            ZipInputStream zipStream = new ZipInputStream( inputStream );
            ZipEntry zipEntry = null;

            File assetsFolder = new File( rootFolder, "assets" );
            assetsFolder.mkdirs();

            while ( (zipEntry = zipStream.getNextEntry()) != null )
            {
                File entryDestination = new File( assetsFolder, zipEntry.getName() );

                OutputStream out = new FileOutputStream(entryDestination);
                IOUtils.copy(zipStream, out);
                zipStream.closeEntry();
                out.close();
                
            }

            return null;
        }
        catch ( Exception e )
        {
            log.error( "Error generating GRID HTML Report", e );
            return null;
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch ( Exception e )
            {
            }
        }
    }
}
