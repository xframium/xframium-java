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
package org.xframium.gesture.device.action.spi.perfecto;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.gesture.device.action.AbstractDefaultAction;
import org.xframium.gesture.device.action.DeviceAction;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.perfectoMobile.rest.services.Repositories.RepositoryType;
import org.xframium.reporting.ExecutionContext;


// TODO: Auto-generated Javadoc
/**
 * The Class DownloadFileAction.
 */
public class DownloadFileAction extends AbstractDefaultAction implements DeviceAction
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.device.action.AbstractDefaultAction#_executeAction(org.openqa.selenium.WebDriver, java.util.List)
	 */
	@Override
	public boolean _executeAction( WebDriver webDriver, List<Object> parameterList )
	{
		String executionId = getExecutionId( webDriver );
		String deviceName = getDeviceName( webDriver );
		
		String deviceFilePath = (String) parameterList.get( 0 ); 
		
		String repFileName = deviceFilePath.substring(deviceFilePath.lastIndexOf("/"), deviceFilePath.length());
		String repFilePath = "PRIVATE:" + repFileName;
		
		// Copy file from device to Perfecto Repository
		PerfectoMobile.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).device().getFile( executionId, deviceName, deviceFilePath, repFilePath );
		
		String destinationFolderPath;
		
		if ( parameterList.size() > 1 )
			destinationFolderPath = System.getProperty("user.dir") + File.separator + (String) parameterList.get( 1 );
		else
			destinationFolderPath = ExecutionContext.instance(( (DeviceWebDriver) webDriver ).getxFID()).getReportFolder(( (DeviceWebDriver) webDriver ).getxFID()).getAbsolutePath();
				
		// Download the file from repository to local
		byte[] imageData;
		try{
			imageData = PerfectoMobile.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).repositories().download( RepositoryType.MEDIA, repFilePath );
		}catch(Exception ex){
			throw new IllegalArgumentException( "Could not download the file " + repFileName + "from Perfecto repository path", ex );
		}
				
		try
		{
			File destinationFolder = new File(destinationFolderPath);	
			
			if (!destinationFolder.exists()){
				destinationFolder.mkdirs();
			}
			
			FileOutputStream outputStream = new FileOutputStream( destinationFolderPath + File.separator + repFileName );
			outputStream.write( imageData );
			outputStream.flush();
			outputStream.close();			
		}
		catch( Exception e )
		{
			throw new IllegalArgumentException( "Could not write to file", e );
		}	
		
		// Delete file from repository
		PerfectoMobile.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).repositories().delete(RepositoryType.MEDIA, repFilePath );
		
		return true;
	}

}
