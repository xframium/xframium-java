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
import org.xframium.integrations.perfectoMobile.rest.services.Imaging.ImageFormat;
import org.xframium.integrations.perfectoMobile.rest.services.Imaging.Resolution;
import org.xframium.integrations.perfectoMobile.rest.services.Imaging.Screen;
import org.xframium.integrations.perfectoMobile.rest.services.Repositories.RepositoryType;
import org.xframium.utility.XMLEscape;

// TODO: Auto-generated Javadoc
/**
 * The Class InstallApplicationAction.
 */
public class DumpStateAction extends AbstractDefaultAction implements DeviceAction
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.device.action.AbstractDefaultAction#_executeAction(org.openqa.selenium.WebDriver, java.util.List)
	 */
	@Override
	public boolean _executeAction( WebDriver webDriver, List<Object> parameterList )
	{
		String executionId = getExecutionId( webDriver );
		String deviceName = getDeviceName( webDriver );
		
		long timeKey = System.currentTimeMillis();
		
		Resolution resolution = Resolution.valueOf( ( (String) parameterList.get( 0 )  ).toLowerCase() );
		String rootFolder = System.getProperty( "__outputFolder" );
		
		if ( parameterList.size() > 1 )
			rootFolder = (String) parameterList.get( 1 ); 
		
		File outputFile = new File( new File( rootFolder, executionId ), timeKey + ".png" );
		File DOMFile = new File( new File( rootFolder, executionId ), timeKey + ".xml" );
		String fileKey = "PRIVATE:" + deviceName + ".png";
		
		PerfectoMobile.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).imaging().screenShot( executionId, deviceName, fileKey, Screen.primary, ImageFormat.png, resolution );
		byte[] imageData = PerfectoMobile.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).repositories().download( RepositoryType.MEDIA, fileKey );
		
		try
		{
			FileOutputStream outputStream = new FileOutputStream( outputFile );
			outputStream.write( imageData );
			outputStream.flush();
			outputStream.close();
			
			outputStream = new FileOutputStream( DOMFile );
			outputStream.write( XMLEscape.toXML( webDriver.getPageSource() ).getBytes() );
			outputStream.flush();
			outputStream.close();
			return true;
		}
		catch( Exception e )
		{
			throw new IllegalArgumentException( "Could not write to file", e );
		}
		
		
		
	}

}
