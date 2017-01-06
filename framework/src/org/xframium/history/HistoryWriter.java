package org.xframium.history;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HistoryWriter
{
    private static final int MAX_HISTORY = 15;
    private File rootFolder;
    private Log log = LogFactory.getLog(HistoryWriter.class);
    
    
    public HistoryWriter( File rootFolder )
    {
        this.rootFolder = rootFolder;
    }
    
    public static void main( String[] args )
    {
        new HistoryWriter( new File( "C:\\Users\\Allen\\git\\xframium-java\\testing\\test-output" ) ).updateHistory();
    }
    
    public void updateHistory()
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

        BufferedOutputStream outputStream = null;

        try
        {
            outputStream = new BufferedOutputStream( new FileOutputStream( new File( rootFolder, "History.js" ) ) );
            outputStream.write( stringBuilder.toString().getBytes() );
            
        }
        catch( Exception e )
        {
            
        }
        finally
        {
            try { outputStream.flush(); } catch( Exception e ){}
            try { outputStream.close(); } catch( Exception e ){}
        }
        
        outputStream = null;

        File templateFile = new File( rootFolder, "index.html" );
        if ( !templateFile.exists() )
        {
            StringBuilder stringBuffer = new StringBuilder();
            InputStream inputStream = null;
            try
            {
                if ( System.getProperty( "reportTemplateFolder" ) == null )
                {
                    if ( System.getProperty( "reportTemplate" ) == null )
                        inputStream = this.getClass().getClassLoader().getResourceAsStream( "org/xframium/reporting/html/dark/History.html" );
                    else
                        inputStream = this.getClass().getClassLoader().getResourceAsStream( "org/xframium/reporting/html/" + System.getProperty( "reportTemplate" ) + "/History.html" );
                }
                else
                    inputStream = new FileInputStream( new File(  System.getProperty( "reportTemplateFolder" ), "History.html" ) );
                
                File fileName = new File( rootFolder, "index.html" );
                
                if ( !fileName.getParentFile().exists() )
                    fileName.getParentFile().mkdirs();
                
                int bytesRead = 0;
                byte[] buffer = new byte[ 512 ];
                
                while ( ( bytesRead = inputStream.read( buffer ) ) > 0 )
                {
                    stringBuffer.append( new String( buffer, 0, bytesRead ) );
                }
                
                outputStream = new BufferedOutputStream( new FileOutputStream( fileName ) );
                outputStream.write( stringBuffer.toString().getBytes() );
            }
            catch( Exception e )
            {
                log.error( "Error generating History Index", e );
            }
            finally
            {
                try { inputStream.close(); } catch( Exception e ) {}
                try { outputStream.flush(); } catch( Exception e ){}
                try { outputStream.close(); } catch( Exception e ){}
            }
        }
        
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
