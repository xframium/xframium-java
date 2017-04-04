package org.xframium.artifact.spi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.XFramiumException.ExceptionType;
import org.xframium.integrations.alm.ALMRESTConnection;
import org.xframium.integrations.alm.entity.ALMAttachment;
import org.xframium.integrations.alm.entity.ALMDefect;
import org.xframium.reporting.ExecutionContext;

public class ALMDefectArtifact extends AbstractArtifact
{
    public ALMDefectArtifact()
    {
        setArtifactType( ArtifactType.ALM_DEFECT.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver ) throws Exception
    {
        if ( webDriver.isConnected() && webDriver.getExecutionContext().getExceptionType() != null && webDriver.getExecutionContext().getExceptionType().equals( ExceptionType.SCRIPT ) )
        {
            //
            // ALM Integration
            //
            ALMDefect almDefect = new ALMDefect();
            almDefect.setAssignedTo( ExecutionContext.instance().getConfigProperties().get( "alm.assignedTo" ) );
            almDefect.setDescription( webDriver.getExecutionContext().getMessageDetail() );
            almDefect.setDetectedBy( ExecutionContext.instance().getConfigProperties().get( "alm.userName" ) );
            almDefect.setDetectedInCycle( ExecutionContext.instance().getPhase() );
            almDefect.setDetectedInEnvironment( ExecutionContext.instance().getAut().getEnvironment() );
            almDefect.setDetectedInRelease( ((int) ExecutionContext.instance().getAut().getVersion()) + "" );

            almDefect.setPriority( webDriver.getExecutionContext().getTest().getPriority() );
            almDefect.setSeverity( webDriver.getExecutionContext().getTest().getSeverity() );
            almDefect.setStatus( ExecutionContext.instance().getConfigProperties().get( "alm.defectStatus" ) );
            almDefect.setSummary( webDriver.getExecutionContext().getMessage() );
            List<ALMAttachment> artifactList = new ArrayList<ALMAttachment>( 10 );
            for ( ArtifactType a : ArtifactType.CONSOLE_LOG.getSupported() )
            {
                if ( webDriver.getExecutionContext().getExecutionParameter( a.name() + "_FILE" ) != null )
                {
                    artifactList.add( new ALMAttachment( new File( rootFolder, webDriver.getExecutionContext().getTestName() + System.getProperty( "file.separator" ) + webDriver.getExecutionContext().getDevice().getKey() + System.getProperty( "file.separator" ) + webDriver.getExecutionContext().getExecutionParameter( a.name() + "_FILE" ) ), null, "", a.getDescription() ) );
                }
            }
            
            String screenShot = webDriver.getExecutionContext().getScreenShotLocation();
            if ( screenShot != null )
            {
                artifactList.add( new ALMAttachment( new File( screenShot ), null, "", "SCREENSHOT" ) );
            }
            
            almDefect.setAttachments( artifactList.toArray( new ALMAttachment[ 0 ] ) );
            ALMRESTConnection arc = new ALMRESTConnection( ExecutionContext.instance().getConfigProperties().get( "alm.serverUrl" ), ExecutionContext.instance().getDomain(), ExecutionContext.instance().getSuiteName() );
            arc.login( ExecutionContext.instance().getConfigProperties().get( "alm.userName" ), ExecutionContext.instance().getConfigProperties().get( "alm.password" ) );
            if ( log.isInfoEnabled() )
                log.info( "ALM: " + almDefect.toXML() );
            String almDefectUrl = arc.addDefect( almDefect );
            
            
            webDriver.getExecutionContext().addExecutionParameter( getArtifactType() + "_" + URL, almDefectUrl + "?login-form-required=y" );
            arc.logout();
            
            
        }
        
        return null;
    }
}
