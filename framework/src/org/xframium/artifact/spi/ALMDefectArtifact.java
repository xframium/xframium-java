package org.xframium.artifact.spi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.XFramiumException.ExceptionType;
import org.xframium.integrations.alm.ALMRESTConnection;
import org.xframium.integrations.alm.entity.ALMAttachment;
import org.xframium.integrations.alm.entity.ALMData;
import org.xframium.integrations.alm.entity.ALMDefect;
import org.xframium.reporting.ExecutionContext;

public class ALMDefectArtifact extends AbstractArtifact
{
    private static Pattern CUSTOM_PATTERN = Pattern.compile( "(\\w*)\\((\\w*)\\,(\\w*)\\)=(\\w*)" );
    private static Pattern OVERRIDE_PATTERN = Pattern.compile( "(\\w*)\\((\\w*)\\,(\\w*)\\)" ); 
    
    public ALMDefectArtifact()
    {
        setArtifactType( ArtifactType.ALM_DEFECT.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver ) throws Exception
    {
        if ( webDriver.isConnected() && !webDriver.getExecutionContext().getStatus() && webDriver.getExecutionContext().getExceptionType() != null && webDriver.getExecutionContext().getExceptionType().equals( ExceptionType.SCRIPT ) )
        {
            //
            // ALM Integration
            //
            ALMDefect almDefect = new ALMDefect();
            almDefect.setAssignedTo( ExecutionContext.instance().getConfigProperties().get( "alm.assignedTo" ) );
            
            String descriptionTemplate = ExecutionContext.instance().getConfigProperties().get( "alm.defect.template.BG_DESCRIPTION" );
            if ( descriptionTemplate != null )
                almDefect.setDescription( webDriver.toFormattedString( descriptionTemplate ) );
            else
                almDefect.setDescription( webDriver.getExecutionContext().getMessageDetail() );
            
            almDefect.setDetectedBy( ExecutionContext.instance().getConfigProperties().get( "alm.userName" ) );
            almDefect.setDetectedInCycle( ExecutionContext.instance().getPhase() );
            if ( ExecutionContext.instance().getAut().getEnvironment() != null )
                almDefect.setDetectedInEnvironment( ExecutionContext.instance().getAut().getEnvironment() );
            
            if ( ExecutionContext.instance().getAut().getVersion() > 0 )
                almDefect.setDetectedInRelease( ((int) ExecutionContext.instance().getAut().getVersion()) + "" );

            if ( webDriver.getExecutionContext().getTest().getPriority() > 0 )
                almDefect.setPriority( webDriver.getExecutionContext().getTest().getPriority() );
            
            if ( webDriver.getExecutionContext().getTest().getSeverity() > 0 )
                almDefect.setSeverity( webDriver.getExecutionContext().getTest().getSeverity() );
            
            almDefect.setStatus( ExecutionContext.instance().getConfigProperties().get( "alm.defectStatus" ) );
            
            String summaryTemplate = ExecutionContext.instance().getConfigProperties().get( "alm.defect.template.BG_SUMMARY" );
            if ( summaryTemplate != null )
                almDefect.setSummary( webDriver.toFormattedString( summaryTemplate ) );
            else
                almDefect.setSummary( webDriver.getExecutionContext().getMessage() );
            
            //
            // Add custom fields with static values
            //
            String almCustomFields = ExecutionContext.instance().getConfigProperties().get( "alm.defectCustomFields" );
            if ( almCustomFields != null && !almCustomFields.isEmpty() )
            {
                for ( String fieldDef : almCustomFields.split( ":" ) )
                {
                    Matcher fieldMatcher = CUSTOM_PATTERN.matcher( fieldDef );
                    
                    if ( fieldMatcher.find() )
                    {
                        ALMData almData = new ALMData( fieldMatcher.group( 1 ), fieldMatcher.group( 3 ), fieldMatcher.group( 2 ), fieldMatcher.group( 4 ) );    
                        almDefect.addCustomData( almData.getPhysicalName(), almData );
                    }
                }
            }
            
            // Override fields names
            //
            //
            String almOverrideFields = ExecutionContext.instance().getConfigProperties().get( "alm.defectOverrideFields" );
            if ( almOverrideFields != null && !almOverrideFields.isEmpty() )
            {
                for ( String fieldDef : almOverrideFields.split( ":" ) )
                {
                    Matcher fieldMatcher = OVERRIDE_PATTERN.matcher( fieldDef );
                    
                    if ( fieldMatcher.find() )
                    {
                        ALMData almData = new ALMData( fieldMatcher.group( 1 ), fieldMatcher.group( 3 ), fieldMatcher.group( 2 ), null );    
                        almDefect.addCustomData( almData.getPhysicalName(), almData );
                    }
                }
            }
            
            
            List<ALMAttachment> artifactList = new ArrayList<ALMAttachment>( 10 );
            for ( ArtifactType a : ArtifactType.CONSOLE_LOG.getSupported() )
            {
                if ( webDriver.getExecutionContext().getExecutionParameter( a.name() + "_ABS" ) != null )
                {
                    artifactList.add( new ALMAttachment( new File( webDriver.getExecutionContext().getExecutionParameter( a.name() + "_ABS" ) ), null, "", a.getDescription() ) );
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
