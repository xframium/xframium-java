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

public class JSONGridArtifact extends AbstractArtifact
{
    private static final String FILE_NAME = "Grid.js";
    public JSONGridArtifact()
    {
        setArtifactType( ArtifactType.GRID_REPORT.name() );
    }
    
    private static class GRIDFileFilter implements FileFilter
    {

        @Override
        public boolean accept( File pathname )
        {
            return pathname.getName().startsWith( "grid" );
        }
        
    }
    
    protected File findFile( File rootFolder, File useFile )
    {
        if ( useFile.exists() || useFile.isAbsolute() )
            return useFile;

        File myFile = new File( rootFolder, useFile.getPath() );
        if ( myFile.exists() )
            return myFile;

        throw new IllegalArgumentException( "Could not find " + useFile.getName() + " at " + useFile.getPath() + " or " + myFile.getAbsolutePath() );

    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        
        
        File artifactFolder = new File( rootFolder, "artifacts" );
        
        File[] fileList = artifactFolder.listFiles( new GRIDFileFilter() );
        
        if ( fileList == null )
            return null;
        
        Map<String,List<String>> fileMap = new HashMap<String,List<String>>( 20 );
        
        for ( File file : fileList )
        {
            String[] fileParts = file.getName().split( "-" );
            List<String> gridList = fileMap.get( fileParts[ 1 ] );
            if ( gridList == null )
            {
                gridList = new ArrayList<String>( 10 );
                fileMap.put( fileParts[ 1 ], gridList );
            }
            
            gridList.add( file.getName() );
        }
        
        String outputData = "var testData = " + new String( SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), fileMap, 0 ) ) + ";";
        
        return writeToDisk( rootFolder, FILE_NAME, outputData.getBytes() );
    }
}
