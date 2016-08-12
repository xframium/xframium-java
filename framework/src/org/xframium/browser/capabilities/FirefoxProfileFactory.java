package org.xframium.browser.capabilities;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author user Class for creating FireFox profile options.
 */
public class FirefoxProfileFactory extends AbstractBrowserCapability {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xframium.browser.capabilities.AbstractBrowserCapability#
	 * _createBrowserOptions(org.openqa.selenium.remote.DesiredCapabilities,
	 * java.util.Map)
	 */
	private Log log = LogFactory.getLog(FirefoxProfileFactory.class);

	@SuppressWarnings("unchecked")
	@Override
	protected DesiredCapabilities _createBrowserOptions(DesiredCapabilities desiredCapabilities,
			Map<String, Object> options) {
		Map<String, String> optionsMap = null;
		FirefoxProfile profile = new FirefoxProfile();
		if (log.isDebugEnabled())
			log.debug("Creating chromeBrowserOptions ");
		for (String name : options.keySet()) {
			try {
				if (name.contains("preference")) {
					optionsMap = (Map<String, String>) options.get(name);

					for (String preferenceKey : optionsMap.keySet()) {
						String preferenceValue = optionsMap.get(preferenceKey);
						profile.setPreference(preferenceKey, preferenceValue);
					}

				} else if (name.equalsIgnoreCase("extension")) {
					List<String> extensions = (List<String>) options.get(name);
					for (String filePath : extensions) {
						try {
							profile.addExtension(new File(filePath));
						} catch (IOException exception) {
							exception.printStackTrace();
						}

					}
				} else if (name.equalsIgnoreCase("clean")) {
					List<String> extensions = (List<String>) options.get(name);
					for (String filePath : extensions) {
						profile.clean(new File(filePath));
					}
				} else if (name.equalsIgnoreCase("AssumeUntrustedCertificateIssuer")) {
					List<String> CertificateIssuer = (List<String>) options.get(name);
					for (String str : CertificateIssuer) {
						Boolean CertificateIssuer_flag = Boolean.parseBoolean(str);
						profile.setAssumeUntrustedCertificateIssuer(CertificateIssuer_flag);
					}
				}
				desiredCapabilities.setCapability(FirefoxDriver.PROFILE, profile);
			}

			catch (Exception exception) {
				log.error("Error in Firefox profile options:" + name + "exception:" + exception);
			}
		}
		return desiredCapabilities;
	}


}