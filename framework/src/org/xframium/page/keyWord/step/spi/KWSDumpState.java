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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.xframium.application.ApplicationRegistry;
import org.xframium.container.SuiteContainer;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.ScriptException;
import org.xframium.exception.XFramiumException;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.spi.RunDetails;
import org.xframium.utility.ImageUtility;
import org.xframium.utility.XMLEscape;

public class KWSDumpState extends AbstractKeyWordStep
{
    private static NumberFormat numberFormat = new DecimalFormat( "0000" );
    public KWSDumpState()
    {
        kwName = "Platform Checkpoint";
        kwDescription = "Allows the script to get a platform checkpoint containing a snapshot image and platform state";
        kwHelp = "https://www.xframium.org/keyword.html#kw-state";
        orMapping = false;
    }
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public synchronized boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC )
	{
	    String checkPointName = null;
	    int historicalCount = -1;
	    int deviationPercentage = -1;
	    
	    File checkPointFolder = null;
	    
	    if ( getParameterList().size() >= 1 )
	    {
	        checkPointFolder = new File( DataManager.instance().getReportFolder(), "historicalComparison" );
	        checkPointFolder = new File( checkPointFolder, ( (DeviceWebDriver) webDriver ).getDevice().getKey() );

	        checkPointName = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
	        if ( getParameterList().size() >= 2 )
	        {
	            historicalCount = Integer.parseInt( getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "" );
	            if ( getParameterList().size() >= 3 )
	            {
	                deviationPercentage = Integer.parseInt( getParameterValue( getParameterList().get( 2 ), contextMap, dataMap ) + "" );
	            }
	        }
	    }
	    
        long startTime = System.currentTimeMillis();
        File rootFolder = new File( DataManager.instance().getReportFolder(), RunDetails.instance().getRootFolder() );
        File useFolder = new File( rootFolder, "artifacts" );
        useFolder.mkdirs();
        
        File screenFile = null;
        File domFile = null;;
        
        if ( webDriver instanceof TakesScreenshot )
        {
            OutputStream os = null;
            try
            {
                byte[] screenShot = ( ( TakesScreenshot ) webDriver ).getScreenshotAs( OutputType.BYTES );
                if ( checkPointName != null )
                    screenFile = new File( useFolder, "grid-" + checkPointName.replace("-", "_" ) + "-" + ( (DeviceWebDriver) webDriver ).getDevice().getKey() + ".png" );
                else
                    screenFile = File.createTempFile( "state", ".png", useFolder );
                
                contextMap.put( "_SCREENSHOT", screenFile.getAbsolutePath() );
                screenFile.getParentFile().mkdirs();
                os = new BufferedOutputStream( new FileOutputStream( screenFile ) );
                os.write( screenShot );
                os.flush();
                os.close();
                
                if ( checkPointFolder != null )
                {
                    //
                    // Dump state as historical 
                    //
                    checkPointFolder.mkdirs();
                    
                    if ( historicalCount >= 0 )
                    {
                        List<File> historicalFiles = Arrays.asList( checkPointFolder.listFiles( new CheckPointFiles( checkPointName + "-" ) ) );
                        Collections.sort( historicalFiles );
                        Collections.reverse( historicalFiles );
                        
                        if ( historicalFiles.size() >= historicalCount )
                            historicalFiles.get( 0 ).delete();
                        
                        for ( File file : historicalFiles )
                        {
                            String[] fileParts = file.getName().split( "\\." );
                            int fileNumber = Integer.parseInt( fileParts[ 0 ].substring( checkPointName.length() + 1 ) );
                            
                            file.renameTo( new File( file.getParentFile(), checkPointName + "-" + numberFormat.format( fileNumber+1 ) + ".png" ) );
                        }
                        
                        os = new BufferedOutputStream( new FileOutputStream( new File( checkPointFolder, checkPointName + "-0000.png" ) ) );
                    }
                    else
                        os = new BufferedOutputStream( new FileOutputStream( new File( checkPointFolder, checkPointName + ".png" ) ) );

                    os.write( screenShot );
                    os.flush();
                    os.close();
                    
                    if ( deviationPercentage >= 0 )
                    {
                        //
                        // Compare with the last image
                        //
                        File newFile = new File( checkPointFolder, checkPointName + "-0000.png" );
                        File previousFile = new File( checkPointFolder, checkPointName + "-0001.png" );
                        
                        if ( newFile.exists() && previousFile.exists() )
                        {
                            double computedDeviation = ( ImageUtility.compareImages( ImageIO.read( newFile ), ImageIO.read( previousFile ) ) * 100 );
                            
                            if ( computedDeviation > deviationPercentage )
                                throw new ScriptException( "Historical image comparison failed.  Expected a maximum difference of [" + deviationPercentage + "] but found [" + computedDeviation + "]" );
                        }
                    }
                }
            }
            catch( ScriptException xe )
            {
                throw xe;
            }
            catch( Exception e )
            {
                e.printStackTrace(  );
                throw new ScriptException( "Error taking screenshot" );
            }
            finally
            {
                if ( os != null )
                    try{ os.close(); } catch( Exception e ) {}
            }         
        }

        FileOutputStream outputStream = null;
        try
        {
            File xmlFile = File.createTempFile( "dom-", ".xml", useFolder );
            domFile = new File( xmlFile.getParentFile(), xmlFile.getName().replace( ".xml", ".html" ) );
            
            contextMap.put( "_DOM_XML", xmlFile.getAbsolutePath() );
            contextMap.put( "_DOM_HTML", domFile.getAbsolutePath() );
            
            String pageSource = webDriver.getPageSource();
            outputStream = new FileOutputStream( xmlFile );
            if ( ApplicationRegistry.instance().getAUT() == null || ApplicationRegistry.instance().getAUT().isWeb() )
            	outputStream.write( XMLEscape.toXML( pageSource ).getBytes() );
            else
            	outputStream.write( XMLEscape.toHTML( pageSource ).getBytes() );
            
            outputStream.flush();
            outputStream.close();
            
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append( "<html><head><link href=\"http://www.xframium.org/output/assets/css/prism.css\" rel=\"stylesheet\"><script src=\"http://www.xframium.org/output/assets/js/prism.js\"></script>" );
            stringBuilder.append( "</script></head><body><pre class\"line-numbers\"><code class=\"language-markup\">" + pageSource.replace( "<", "&lt;" ).replace( ">", "&gt;" ).replace( "\t", "  ") + "</code></pre></body></html>" );

            outputStream = new FileOutputStream( domFile );
            outputStream.write( stringBuilder.toString().getBytes() );
            outputStream.flush();
            outputStream.close();                        
        }
        catch( Exception e )
        {
            log.warn( "Could not write to output file", e );
            throw new ScriptException( "Error capturing device source" );
        }
        finally
        {
            if ( outputStream != null )
                try{ outputStream.close(); } catch( Exception e ) {}
        }
        
        String files = domFile.getName() + ( screenFile != null ? ( "," + screenFile.getName() ) : "" );
        
        PageManager.instance().addExecutionLog( getExecutionId( webDriver ), getDeviceName( webDriver ), files, getName(), "KWSDumpState", startTime, System.currentTimeMillis() - startTime, StepStatus.SUCCESS, "", null, getThreshold(), getDescription(), false, null );
        
        return true;
	}
	
	@Override
	public boolean isRecordable()
	{
	    return false;
	}
	
	private class CheckPointFiles implements FilenameFilter
	{
	    private String checkPointName;
	    
	    public CheckPointFiles( String checkPointName )
	    {
	        this.checkPointName = checkPointName;
	    }
	    
        @Override
        public boolean accept( File dir, String name )
        {
            return ( name.startsWith( checkPointName ) );
        }
	}

}
