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
/*
 * 
 */
package org.xframium.device.artifact;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.ConnectedDevice;

// TODO: Auto-generated Javadoc
/**
 * The Interface ArtifactProducer.
 */
public interface ArtifactProducer
{
	
	
	
	/**
	 * Gets the artifact.
	 *
	 * @param webDriver the web driver
	 * @param aType the a type
	 * @param connectedDevice the connected device
	 * @return the artifact
	 */
	public Artifact getArtifact( WebDriver webDriver, ArtifactType aType, ConnectedDevice connectedDevice, String testName, boolean success );
	
	/**
	 * Gets the artifact.
	 *
	 * @param webDriver the web driver
	 * @param aType the a type
	 * @param parameterMap the parameter map
	 * @param connectedDevice the connected device
	 * @return the artifact
	 */
	public Artifact getArtifact( WebDriver webDriver, ArtifactType aType, Map<String,String> parameterMap, ConnectedDevice connectedDevice, String testName, boolean success );
}
