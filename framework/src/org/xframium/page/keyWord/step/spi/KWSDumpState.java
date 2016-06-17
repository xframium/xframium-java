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
import java.io.OutputStream;
import java.util.Map;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.xframium.device.data.DataManager;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.spi.RunDetails;
import org.xframium.utility.XMLEscape;

public class KWSDumpState extends AbstractKeyWordStep
{

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public synchronized boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
        long startTime = System.currentTimeMillis();
        File rootFolder = new File( DataManager.instance().getReportFolder(), RunDetails.instance().getRootFolder() );
        File useFolder = new File( rootFolder, "artifacts" );
        useFolder.mkdirs();
        
        
        
        if ( webDriver instanceof TakesScreenshot )
        {
            OutputStream os = null;
            try
            {
                byte[] screenShot = ( ( TakesScreenshot ) webDriver ).getScreenshotAs( OutputType.BYTES );
                File randomFile = File.createTempFile( "screenshot", ".png", useFolder );
                randomFile.getParentFile().mkdirs();
                os = new BufferedOutputStream( new FileOutputStream( randomFile ) );
                os.write( screenShot );
                os.flush();
                os.close();
                String data = "<a hRef=\"../../artifacts/" + randomFile.getName() + "\" class=\"thumbnail\"><img class=\"img-rounded img-responsive\" src=\"../../artifacts/" + randomFile.getName() + "\" style=\"height: 200px;\"/></a>"; 

                PageManager.instance().addExecutionLog( getExecutionId( webDriver ), getDeviceName( webDriver ), getPageName(), data, "SCREENSHOT", startTime, System.currentTimeMillis() - startTime, StepStatus.SUCCESS, "", null, getThreshold(), getDescription(), false );
            }
            catch( Exception e )
            {
                log.error( "Error taking screenshot", e );
                try { os.close(); } catch( Exception e2 ) {}
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
            File randomFile = File.createTempFile( "dom-", ".xml", useFolder );
            outputStream = new FileOutputStream( randomFile );
            outputStream.write( XMLEscape.toXML( webDriver.getPageSource() ).getBytes() );
            outputStream.flush();
            outputStream.close();
            PageManager.instance().addExecutionLog( getExecutionId( webDriver ), getDeviceName( webDriver ), getPageName(), "<a target=\"_blank\" class=\"btn btn-primary\" hRef='../../artifacts/" + randomFile.getName() + "' >Device State</a>", "STATE", startTime, System.currentTimeMillis() - startTime, StepStatus.SUCCESS, "", null, getThreshold(), getDescription(), false );            
        }
        catch( Exception e )
        {
            throw new IllegalArgumentException( "Could not write to file", e );
        }
        finally
        {
            if ( outputStream != null )
                try{ outputStream.close(); } catch( Exception e ) {}
        }
        
        return true;
	}
	
	@Override
	public boolean isRecordable()
	{
	    return false;
	}

	
}
