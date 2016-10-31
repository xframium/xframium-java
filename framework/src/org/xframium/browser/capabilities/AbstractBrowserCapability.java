package org.xframium.browser.capabilities;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author user
 * A abstract class for defining the Browser capabilities.
 */
public abstract class AbstractBrowserCapability implements BrowserCapabilityFactory {
	
	/**
	 * This method will be overridden by the class which extends AbstractBrowserCapability for implementing different browser options.
	 * @param DesiredCapabilities - desired capabilities objectdc
	 * @param Map <String,List<String>> - browser options with Key and values
	 * @return DesiredCapabilities
	 */

	protected abstract DesiredCapabilities _createBrowserOptions(DesiredCapabilities dc, Map <String,Object> options);
	
	@Override
	public DesiredCapabilities createBrowserOptions(DesiredCapabilities dc, Map <String,Object> options) {
		return _createBrowserOptions(dc, options);
	}

}
