/*
 * 
 */
package com.xframium.device.artifact;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import com.xframium.artifact.ArtifactType;
import com.xframium.device.ConnectedDevice;

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
