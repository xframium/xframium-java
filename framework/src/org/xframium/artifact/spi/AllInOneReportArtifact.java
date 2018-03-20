package org.xframium.artifact.spi;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xframium.Initializable;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;

import com.xframium.serialization.SerializationManager;

public class AllInOneReportArtifact extends AbstractArtifact
{
    private static final String FILE_NAME = "allInOne.html";
    public AllInOneReportArtifact()
    {
        setArtifactType( ArtifactType.ALL_IN_ONE.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        InputStream inputStream = null;
        try
        {
        		if ( webDriver.getExecutionContext() == null )
                return null;
        		
        		webDriver.getExecutionContext().setFolderName( webDriver.getArtifactFolder().getPath() );
            webDriver.getExecutionContext().popupateSystemProperties();
            webDriver.getExecutionContext().setDevice( webDriver.getPopulatedDevice() );
        	
            inputStream = getTemplate( "Test.html" );
            
            Map<String,String> replacementMap = new HashMap<String,String>( 5 );
            replacementMap.put( "../../../assets", "http://xframium.org/reporting/" + Initializable.VERSION );
            replacementMap.put( "<script src=\"./Test.js\"></script>", ("<script>var testData = " + new String( SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), webDriver.getExecutionContext(), 0 ) ) + ";</script>" ) );
            replacementMap.put( "../../index.html", "#" );
            return writeToDisk( rootFolder, FILE_NAME, inputStream, replacementMap );
        }
        catch( Exception e )
        {
            log.error( "Error generating HTML", e );
            return null;
        }
        finally
        {
            try { inputStream.close(); } catch( Exception e ) {}
        }
    }
} 
