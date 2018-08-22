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
package org.xframium.page.keyWord;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.xframium.application.ApplicationVersion;
import org.xframium.container.SuiteContainer;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Interface KeyWordStep.
 */
public interface KeyWordStep {

	/** The Constant TYPE. */
	public static final String TYPE = "TYPE";

	/**
	 * The Enum StepFailure.
	 */
	public enum StepFailure {

		/** The error. */
		ERROR,
		/** The ignore. */
		IGNORE,
		/** The log ignore. */
		LOG_IGNORE,

		LOG_ERROR,
		
		FAILURE
	}

	/**
	 * The Enum StepFailure.
	 */
	public enum ValidationType {

		/** The regex. */
		REGEX,

		/** The empty. */
		EMPTY,

		/** The not empty. */
		NOT_EMPTY;
	}

	public void setTagNames(String tagNames);

	public void setDeviceTags(String tagNames);

	public Object getParameterValue(KeyWordParameter param, Map<String, Object> contextMap,
			Map<String, PageData> dataMap, String xFID);

	public String getTokenValue(KeyWordToken token, Map<String, Object> contextMap, Map<String, PageData> dataMap,
			String xFID);

	public ApplicationVersion getVersion();

	public void setVersion(String appVersion);

	public boolean isOrMapping();

	public String dumpState(WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap,
			ExecutionContextTest executionContext);

	/**
	 * Gets the link id.
	 *
	 * @return the link id
	 */
	public String getOs();

	/**
	 * Sets the link id.
	 *
	 * @param os
	 *            the new os
	 */
	public void setOs(String os);

	/**
	 * Gets the link id.
	 *
	 * @return the link id
	 */
	public String getBrowser();

	public String getWaitFor();

	public void setWaitFor(String waitFor);
	
	public boolean isAllowMultiple();
	public void setAllowMultiple(boolean allowMultiple); 

	/**
	 * Sets the link id.
	 *
	 * @param browser
	 *            the new os
	 */
	public void setBrowser(String browser);

	public List<KeyWordParameter> getParameterList();

	public List<KeyWordToken> getTokenList();
	
	public void addReportingToken( KeyWordToken t );

	public String[] getTagNames();

	public String[] getDeviceTags();

	public void setTagNames(String[] tagNames);

	public void setDeviceTags(String[] deviceTags);

	public void setImage(String imageName);

	public String getImage();

	public boolean isTrace();

	public void setTrace(boolean trace);

	public String getKw();

	/**
	 * Gets the link id.
	 *
	 * @return the link id
	 */
	public String getLinkId();
	
	public void setSuccessReport( String message );
	public void setFailureReport( String message );
	
	public String getSuccessReport();
	public String getFailureReport();
	
	public List<KeyWordToken> getReportTokenList();
	
	
	/**
	 * Sets the link id.
	 *
	 * @param linkId
	 *            the new link id
	 */
	public void setLinkId(String linkId);

	/**
	 * Gets the failure.
	 *
	 * @return the failure
	 */
	public StepFailure getFailure();

	/**
	 * Sets the failure.
	 *
	 * @param sf
	 *            the new failure
	 */
	public void setFailure(StepFailure sf);

	/**
	 * Adds the parameter.
	 *
	 * @param param
	 *            the param
	 */
	public void addParameter(KeyWordParameter param);

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name);

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public boolean isActive();

	/**
	 * Sets the active.
	 *
	 * @param active
	 *            the new active
	 */
	public void setActive(boolean active);

	/**
	 * Checks if is inverse.
	 *
	 * @return true, if is inverse
	 */
	public boolean isInverse();

	/**
	 * Sets the inverse.
	 *
	 * @param inverse
	 *            the new inverse
	 */
	public void setInverse(boolean inverse);

	public boolean isStartAt();

	public void setStartAt(boolean startAt);

	public boolean isBreakpoint();

	public void setBreakpoint(boolean breakpoint);

	public void setAppContext(String appContext);

	public String getAppContext();

	/**
	 * Gets the page name.
	 *
	 * @return the page name
	 */
	public String getPageName();

	/**
	 * Sets the page name.
	 *
	 * @param pageName
	 *            the new page name
	 */
	public void setPageName(String pageName);

	public String getSiteName();

	/**
	 * Sets the page name.
	 *
	 * @param pageName
	 *            the new page name
	 */
	public void setSiteName(String siteName);

	/**
	 * Checks if is timed.
	 *
	 * @return true, if is timed
	 */
	public boolean isTimed();

	/**
	 * Sets the timed.
	 *
	 * @param timed
	 *            the new timed
	 */
	public void setTimed(boolean timed);

	/**
	 * Adds the all steps.
	 *
	 * @param step
	 *            the step
	 */
	public void addAllSteps(KeyWordStep[] step);

	/**
	 * Adds the step.
	 *
	 * @param step
	 *            the step
	 */
	public void addStep(KeyWordStep step);

	/**
	 * Gets the step list.
	 *
	 * @return the step list
	 */
	public List<KeyWordStep> getStepList();

	/**
	 * Checks if is fork.
	 *
	 * @return true, if is fork
	 */
	public boolean isFork();

	/**
	 * Sets the fork.
	 *
	 * @param fork
	 *            the new fork
	 */
	public void setFork(boolean fork);

	/**
	 * Adds the token.
	 *
	 * @param token
	 *            the token
	 */
	public void addToken(KeyWordToken token);

	/**
	 * Execute step.
	 *
	 * @param pageObject
	 *            the page object
	 * @param webDriver
	 *            the web driver
	 * @param contextMap
	 *            the context map
	 * @param dataMap
	 *            the data map
	 * @param pageMap
	 *            the page map
	 * @param exeuctionContext
	 *            TODO
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean executeStep(Page pageObject, WebDriver webDriver, Map<String, Object> contextMap,
			Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC,
			ExecutionContextTest exeuctionContext) throws Exception;

	/**
	 * To error.
	 *
	 * @return the string
	 */
	public String toError();

	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public String getContext();

	/**
	 * Gets the device name.
	 *
	 * @return the device
	 */
	public String getDevice();

	/**
	 * Gets the validation.
	 *
	 * @return the validation
	 */
	public String getValidation();

	/**
	 * Sets the validation.
	 *
	 * @param validation
	 *            the new validation
	 */
	public void setValidation(String validation);

	/**
	 * Gets the validation type.
	 *
	 * @return the validation type
	 */
	public ValidationType getValidationType();

	/**
	 * Sets the validation type.
	 *
	 * @param validationType
	 *            the new validation type
	 */
	public void setValidationType(ValidationType validationType);

	/**
	 * Sets the context.
	 *
	 * @param contextName
	 *            the new context
	 */
	public void setContext(String contextName);

	/**
	 * Sets the device name.
	 *
	 * @param deviceName
	 *            the altername device anme
	 */
	public void setDevice(String deviceName);

	/**
	 * Sets the threshold.
	 *
	 * @param threshold
	 *            the new threshold
	 */
	public void setThreshold(int threshold);

	/**
	 * Gets the threshold.
	 *
	 * @return the threshold
	 */
	public int getThreshold();

	/**
	 * Sets the description.
	 *
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description);

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription();

	/**
	 * Sets the poi.
	 *
	 * @param poi
	 *            the new poi
	 */
	public void setPoi(String poi);

	/**
	 * Gets the poi.
	 *
	 * @return the poi
	 */
	public String getPoi();

	/**
	 * Gets the wait.
	 *
	 * @return the wait
	 */
	public long getWait();

	/**
	 * Sets the wait.
	 *
	 * @param waitAfter
	 *            the new wait
	 */
	public void setWait(long waitAfter);
}
