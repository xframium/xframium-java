package org.xframium.artifact.spi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.reporting.ExecutionContext;
import com.xframium.serialization.SerializationManager;

public class JSONHistoryArtifact extends AbstractArtifact
{
    private static final int MAX_HISTORY = 20;
    private static final String FILE_NAME = "History.js";
    public JSONHistoryArtifact()
    {
        setArtifactType( ArtifactType.EXECUTION_HISTORY_JSON.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        File[] suiteList = rootFolder.listFiles( new FolderFilter() );
        
        List<File> fileList = new LinkedList<File>(  );
        for ( File currentFile : suiteList )
        {
            fileList.add( new File( currentFile, "Suite.js" ) );
        }
        
        Collections.sort( fileList, new FileComparator() );
        
        int currentIndex = 0;
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( "var suiteData = [" );
        for ( File currentFile : fileList )
        {
            if ( log.isInfoEnabled() )
                log.info( "Reading: " + currentFile.getAbsolutePath() );
            
            String fileData = readFile( currentFile );
            if ( fileData != null )
            {
                String mapData = fileData.substring( 15, fileData.length() - 1 );
                stringBuilder.append( mapData ).append( ", " );
            }
            
            if ( currentIndex++ > MAX_HISTORY )
                break;
        }
        
        stringBuilder.append( " ];" );

        return writeToDisk( rootFolder, FILE_NAME, stringBuilder.toString().getBytes() );
        
    }
    
    private String readFile( File currentFile )
    {
        try
        {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedInputStream inputStream = new BufferedInputStream( new FileInputStream( currentFile ) );
            
            int bytesRead = 0;
            byte[] buffer = new byte[ 512 ];
            
            while ( ( bytesRead = inputStream.read( buffer ) ) != -1 )
            {
                stringBuilder.append( new String( buffer, 0, bytesRead ) );
            }
            
            inputStream.close();
            
            return stringBuilder.toString();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public class FileComparator implements Comparator<File>
    {
        @Override
        public int compare( File fileOne, File fileTwo )
        {
            return Long.compare( fileTwo.lastModified(), fileOne.lastModified() );
        }
        
    }
    
    public class FolderFilter implements FileFilter
    {

        @Override
        public boolean accept( File pathname )
        {
            if ( pathname.isDirectory() )
            {
                File[] fileList = pathname.listFiles( new SuiteFilter() );
                if ( fileList != null && fileList.length > 0 )
                    return true;
            }
            
            return false;
        }
    }
    
    public class SuiteFilter implements FileFilter
    {
        @Override
        public boolean accept( File pathname )
        {
            if ( pathname.getName().equals( "Suite.js" ) )
                return true;
            else
                return false;
        }
    }
}
