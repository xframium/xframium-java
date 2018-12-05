package org.xframium.artifact.spi;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;

public class StatisticsArtifact extends AbstractArtifact
{
    public StatisticsArtifact()
    {
        setArtifactType( ArtifactType.USAGE_STATS.name() );
    }
    
    private static boolean errorDisabled = false;
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver, String xFID )
    {
        InputStream inputStream = null;
        try
        {
           if ( errorDisabled )
             return null;
           
           URL usageUrl = new URL( "http://usage.morelandlabs.com/services/feature/updateStepUsage?featureIdList=" + webDriver.getExecutionContext().getFeatures() );
           URLConnection urlConnection = usageUrl.openConnection();
           urlConnection.setConnectTimeout( 3500 );
           urlConnection.setReadTimeout( 1500 );
           urlConnection.getInputStream();
           
           inputStream = urlConnection.getInputStream();
           return null;
        }
        catch( Exception e )
        {
           errorDisabled = true; 
        }
        finally
        {
            try { inputStream.close(); } catch( Exception e ) {}
        }

        
        
        return null;
    }
}
