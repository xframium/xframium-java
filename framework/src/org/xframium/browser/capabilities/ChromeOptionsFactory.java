package org.xframium.browser.capabilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author user
 * Class for adding Chrome options.
 */
public class ChromeOptionsFactory extends AbstractBrowserCapability{

	private Log log = LogFactory.getLog(ChromeOptionsFactory.class);
	
	/* (non-Javadoc)
	 * @see org.xframium.browser.capabilities.AbstractBrowserCapability#_createBrowserOptions(org.openqa.selenium.remote.DesiredCapabilities, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected DesiredCapabilities _createBrowserOptions(DesiredCapabilities dc, Map <String,Object> options) {
		
		if ( log.isDebugEnabled() )
			log.debug( "Creating chromeBrowserOptions " );
		
		List<String> extensionList = null;
		List<File> extensionsList = new ArrayList<File>();
		List<String> binaryList = null;
//		dc = dc.chrome();
		ChromeOptions chromeOptions = new ChromeOptions();
		
		for ( String name : options.keySet() ) {
			
			if(name.equalsIgnoreCase("arguments")) {
				chromeOptions.addArguments((List<String>)options.get(name));
			}
			
			else if(name.equalsIgnoreCase("extensions")) {
				
				extensionList = (List<String>)options.get(name);
				
				for(String filePath : extensionList) {
					extensionsList.add(new File(filePath));
				}
				chromeOptions.addExtensions(extensionsList);
			}
			
			else if(name.equalsIgnoreCase("encodedextensions")) {
				chromeOptions.addEncodedExtensions((List<String>)options.get(name));
			}
			
			else if(name.equalsIgnoreCase("binary")) {
				binaryList = (List<String>)options.get(name);
				chromeOptions.setBinary(binaryList.get(0));
			}
			
			else if(name.equalsIgnoreCase("experimentaloption")) {
//				chromeOptions.setExperimentalOption("excludeSwitches", options.get(name));
				Map<String, Object> optionsMap = (Map<String, Object>) options.get(name);

				for (String preferenceKey : optionsMap.keySet()) {
					
					if (preferenceKey.equalsIgnoreCase("excludeSwitches")) {
						List<Object> exSwitchList = new ArrayList<Object>();
						exSwitchList.add(optionsMap.get(preferenceKey));
						chromeOptions.setExperimentalOption(preferenceKey, exSwitchList);
					}
					else 
					{
						Object preferenceValue = optionsMap.get(preferenceKey);
						chromeOptions.setExperimentalOption(preferenceKey, preferenceValue);
					}
				}
			}
			
			else if(name.equalsIgnoreCase("prefs")) {
				Map<String, Object> optionsMap = (Map<String, Object>) options.get(name);
				chromeOptions.setExperimentalOption(name, optionsMap);
			}
		}
		dc.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		
		if ( log.isDebugEnabled() )
			log.debug( "Chrome Options are set to Device Capability " );
		
		return dc;
	}
}
