package org.xframium.browser.capabilities;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author user
 * Interface for creating browser specific options.
 */
public interface BrowserCapabilityFactory {
	
	/**
	 * Interface method for creating browser options.
	 * @param DesiredCapabilities - desired capabilities object dc
	 * @param Map <String,List<String>> - browser specific options
	 * @return DesiredCapabilities
	 */
	public DesiredCapabilities createBrowserOptions(DesiredCapabilities dc, Map <String,List<String>> options);


}
