/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
package org.xframium.page.keyWord.step.spi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.page.keyWord.step.SyntheticStep;
import org.xframium.reporting.ExecutionContextTest;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSExecJS.
 */
public class KWSScript extends AbstractKeyWordStep
{
    public KWSScript()
    {
        kwName = "Execute OS Script";
        kwDescription = "Allows the test step to execute and record a predefined OS script";
        kwHelp = "https://www.xframium.org/keyword.html#kw-execjs";
        orMapping = false;
        category = "Utility";
    }
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
    {
      String scriptDetail = getParameterValue( getParameter( "Script" ), contextMap, dataMap, executionContext.getxFID() );
      if ( scriptDetail == null || scriptDetail.isEmpty() )
        throw new ScriptConfigurationException( "The Script parameter was not defined" );
      
      String scriptType = getParameterValue( getParameter( "Script Type" ), contextMap, dataMap, executionContext.getxFID() );
      if ( scriptType == null || scriptType.isEmpty() )
        throw new ScriptConfigurationException( "The Script Type parameter was not defined - It should be VBS or JS" );
      
      File tempFile = null;
      FileWriter fW = null;
      try
      {
        
        tempFile = File.createTempFile( "xf-script", "." + scriptType.toLowerCase() );
        if ( log.isInfoEnabled() )
          log.info( "Writing Script to " + tempFile.getAbsolutePath() );
        fW = new FileWriter( tempFile );
        fW.write( scriptDetail );
        fW.flush();
      }
      catch( IOException e )
      {
        log.error( "Could not create temporary file", e );
        throw new ScriptConfigurationException( "Could not create temporary script file" );
      }
      finally
      {
        try { fW.close(); } catch( Exception e ) {}
      }
     
      List<String> commandList = new ArrayList<String>( 10 );
      commandList.add( "cscript.exe" );
      commandList.add( tempFile.getAbsolutePath() );
      
      for ( int i=1; i<100; i++ )
      {
        String scriptArgument = getParameterValue( getParameter( "Script Argument " + i ), contextMap, dataMap, executionContext.getxFID() );
        
        if ( scriptArgument != null )
          commandList.add( scriptArgument );
        else
          break;
      }
      
      if ( log.isInfoEnabled() )
        log.info( "Executing Script" );
      
      try
      {
        Process p = new ProcessBuilder( commandList ).start();
        p.waitFor();
        
        if ( p.exitValue() != 0 )
          throw new IllegalStateException( "Script Execution has failed with [" + getStreamAsString( p.getErrorStream() ) );
        
        LineNumberReader lineReader = new LineNumberReader( new InputStreamReader( p.getInputStream() ) );
        
        String currentLine = null;
        
        while( ( currentLine = lineReader.readLine() ) != null )
        {
          LineDefinition lD = new LineDefinition( currentLine );
          
          if ( lD.validLine() )
          {
            if ( log.isInfoEnabled() )
              log.info( lD.toString() );
            
            switch( lD.getLineType() )
            {
              case "step":
                SyntheticStep sS = new SyntheticStep( lD.getLineValues().get( "name" ), "SCRIPT_STEP", lD.getLineValues().get( "successMessage" ), lD.getLineValues().get( "failureMessage" ) );
                sS.setKWDescription( "Script defined steps written to the output console");
                executionContext.startStep( sS, contextMap, dataMap );
                
                if ( lD.getLineValues().get( "status" ) != null && lD.getLineValues().get( "status" ).equalsIgnoreCase( "failure" ) )
                  executionContext.completeStep( StepStatus.FAILURE, new ScriptException( "Failed to execute OS script"), sS.getReportMessage( StepStatus.FAILURE, contextMap, dataMap, executionContext.getxFID() ) );
                else
                  executionContext.completeStep( StepStatus.SUCCESS, null, sS.getReportMessage( StepStatus.SUCCESS, contextMap, dataMap, executionContext.getxFID() ) );
                  
                break;
                
                
              case "context":
                if ( getContext() != null && !getContext().isEmpty() )
                {
                  for ( String keyName : lD.getLineValues().keySet() )
                    addContext( keyName + "." + keyName, lD.getLineValues().get( keyName ), contextMap, executionContext );
                }
                break;
                
              case "status":
                if ( lD.getLineValues().get( "status" ).equalsIgnoreCase( "success" ) )
                  return true;
                else
                  return false;
            }
            System.out.println( lD.toString() );
          }
        }
        
      }
      catch( Exception e )
      {
        
      }
      
      
      
      
        return false;
    }

	
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
     */
    public boolean isRecordable()
    {
        return false;
    }
    

    
    private static Pattern LINE_TYPE = Pattern.compile( "([^:]*):" );
    private static Pattern LINE_VALUES = Pattern.compile( "(\\w*)\\s*=\\s*\'([^\'\\\\]*(?:\\\\.[^\'\\\\]*)*)\'" );
    
    private class LineDefinition
    {
     
      private String lineDetail;
      private String lineType;
      private Map<String,String> lineValues = new HashMap<String,String>( 10 );
      public LineDefinition( String lineDetail )
      {
        this.lineDetail = lineDetail;
        
        Matcher typeMatcher = LINE_TYPE.matcher( lineDetail );
        
        if ( typeMatcher.find() )
        {
          lineType = typeMatcher.group( 1 ).toLowerCase();
          if ( !lineType.equals( "step") && !lineType.equals( "context" ) && !lineType.equals( "status" ) )
          {
            lineType = null;
            return;
          }
        
          Matcher detailMatcher = LINE_VALUES.matcher( lineDetail );
          while ( detailMatcher.find() )
          {
            lineValues.put( detailMatcher.group( 1 ), detailMatcher.group( 2 ) );
          }
        }
        else
        {
          if ( log.isDebugEnabled() )
            log.debug( "Ignoring: " + lineDetail );
        }
      }
      
      
      
      public String getLineDetail()
      {
        return lineDetail;
      }



      public void setLineDetail( String lineDetail )
      {
        this.lineDetail = lineDetail;
      }



      public String getLineType()
      {
        return lineType;
      }



      public void setLineType( String lineType )
      {
        this.lineType = lineType;
      }



      public Map<String, String> getLineValues()
      {
        return lineValues;
      }



      public void setLineValues( Map<String, String> lineValues )
      {
        this.lineValues = lineValues;
      }



      public boolean validLine()
      {
        return lineType != null;
      }
      
      @Override
      public String toString()
      {
        return "LineDefinition [lineDetail=" + lineDetail + ", lineType=" + lineType + ", lineValues=" + lineValues + "]";
      }
      
      
    }
    
    private static String getStreamAsString( InputStream is )
    {
        return new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
    } 

}
