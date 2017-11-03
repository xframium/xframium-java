package org.xframium.artifact.spi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.integrations.jira.ExecutionStatus;
import org.xframium.integrations.jira.JIRAConnector;
import org.xframium.integrations.jira.JIRAConnectorImpl;
import org.xframium.integrations.jira.util.JIRAEncryptorUtil;
import org.xframium.integrations.jira.util.ZIPFileUtil;
import org.xframium.reporting.ExecutionContext;
import org.xframium.reporting.ExecutionContextStep;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.reporting.ExecutionContextTest.TestStatus;

/**
 * Artifact class used to update the execution status
 * 
 * 
 *
 */
public class JIRAUpdateExecutionArtifact extends AbstractArtifact {

	protected Log log = LogFactory.getLog(JIRAUpdateExecutionArtifact.class);

	private String projectKey;
	private String versionName;
	private String cycleName;
	private String issueKey;
	private boolean attachmentsRequired;

	private String jiraServer;
	private String credentials;
	private boolean isSecuredCredentials;

	private String userName;
	private String password;

	@Override
	protected File _generateArtifact(File rootFolder, DeviceWebDriver webDriver, String xFID) {

		try {

			if ((webDriver != null) && (webDriver.getExecutionContext() != null)
					&& (webDriver.getExecutionContext().getTestStatus() != null)) {

				if (!initialize(webDriver.getExecutionContext(), rootFolder.getParentFile().getParentFile().getName(),
						xFID))
					return null;

				TestStatus executionStatus = webDriver.getExecutionContext().getTestStatus();

				JIRAConnector connector = new JIRAConnectorImpl(jiraServer, credentials);
				String projectId = connector.getProjectIdWithKey(projectKey);
				log.info("<JIRAUpdateExecutionArtifact> Project Id:" + projectId);

				if (projectId == null)
					return null;

				String versionId = connector.getVersionWithVersionNameAndProjectId(projectId, versionName);
				log.info("<JIRAUpdateExecutionArtifact> Version Id:" + versionId);

				if (versionId == null)
					return null;

				String cycleId = connector.getTestCyclesId(projectId, versionId, cycleName);

				cycleId = (cycleId != null) ? cycleId : connector.createTestCycle(projectId, versionId, cycleName);

				if (cycleId == null) {
					log.info("<JIRAUpdateExecutionArtifact> Cycle creation failed !!");
					return null;
				}

				String comment = "";
				if (executionStatus == TestStatus.FAILED) {

					for (ExecutionContextStep step : webDriver.getExecutionContext().getStepList()) {

						if ((step.getMessage() != null))
							comment = step.getMessage();
					}
				}

				String executionId = connector.getExecutionId(cycleId, issueKey);

				if (executionId != null) { // if issue already exists under the
											// cycle
					if (updateExecution(executionStatus, connector, cycleId, issueKey, comment) == true) {
						// Adding attachments
						if (attachmentsRequired)
							addAttachment(rootFolder, cycleId);
					}
				} else {
					List<String> issueList = new ArrayList<String>();
					issueList.add(issueKey);

					if (connector.addTestCasesToCycle(projectId, cycleId, issueList, versionId)) {
						if (updateExecution(executionStatus, connector, cycleId, issueKey, comment) == true) {
							// Adding attachments
							if (attachmentsRequired)
								addAttachment(rootFolder, cycleId);
						}

					} else {
						log.error("<JIRAUpdateExecutionArtifact> Adding test case to cycle failed !!");
						return null;
					}
				}

			} else {
				log.error("<JIRAAddAttachmentArtifact> Execution context is null!!");
			}

		} catch (Exception exception) {
			log.error("<JIRAUpdateExecutionArtifact> Exception.." + exception);
		}

		return null;
	}

	/**
	 * Initialize the class variables
	 * 
	 * @param executionContext
	 */
	private boolean initialize(ExecutionContextTest executionContextTest, String outputFolderName, String xFID) {
	

		projectKey = ExecutionContext.instance(xFID).getConfigProperties().get("jira.projectkey");
		versionName = ExecutionContext.instance(xFID).getConfigProperties().get("jira.versionname");
		cycleName = ExecutionContext.instance(xFID).getConfigProperties().get("jira.cyclename");
		issueKey = executionContextTest.getTest().getLinkId();

		jiraServer = ExecutionContext.instance(xFID).getConfigProperties().get("jira.server");

		userName = ExecutionContext.instance(xFID).getConfigProperties().get("jira.username");
		password = ExecutionContext.instance(xFID).getConfigProperties().get("jira.password");

		String credentialSecurity = ExecutionContext.instance(xFID).getConfigProperties().get("jira.credentialSecurity");
		if (credentialSecurity != null && !credentialSecurity.isEmpty())

			isSecuredCredentials = (credentialSecurity.equals("true") || credentialSecurity.equals("false"))
					? Boolean.valueOf(credentialSecurity) : false;

		if (isSecuredCredentials) {
			password = JIRAEncryptorUtil.decryptValue(password);
			userName = JIRAEncryptorUtil.decryptValue(userName);
		}

		if (projectKey == null || projectKey.isEmpty()) {
			log.error("<JIRAUpdateExecutionArtifact> projectKey is null");
			return false;
		}
		if (versionName == null || versionName.isEmpty()) {
			log.error("<JIRAUpdateExecutionArtifact> versionName is null");
			return false;
		}
		if (cycleName == null || cycleName.isEmpty()) {
			cycleName = outputFolderName;
			log.error("<JIRAUpdateExecutionArtifact> cycleName is null; creating one with name :" + cycleName);
		}
		if (jiraServer == null || jiraServer.isEmpty()) {
			log.error("<JIRAUpdateExecutionArtifact> jiraServer is null");
			return false;
		}
		if (userName == null || userName.isEmpty()) {
			log.error("<JIRAUpdateExecutionArtifact> userName is null");
			return false;
		}
		if (password == null || password.isEmpty()) {
			log.error("<JIRAUpdateExecutionArtifact> password is null");
			return false;
		}
			
		if (issueKey == null || issueKey.isEmpty()) {
			log.error("<JIRAUpdateExecutionArtifact> issueKey is null");
			return false;
		}

		String attachment = ExecutionContext.instance(xFID).getConfigProperties().get("jira.attachment");
		if (attachment != null && !attachment.isEmpty())
			attachmentsRequired = (attachment.equals("true") || attachment.equals("false"))
					? Boolean.valueOf(attachment) : false;

		projectKey = projectKey.trim();
		versionName = versionName.trim();
		cycleName = cycleName.trim();
		issueKey = issueKey.trim();
		jiraServer = jiraServer.trim();
		credentials = (userName.trim()).concat(":").concat(password.trim());

		return true;
	}

	/**
	 * Updates the execution status
	 * 
	 * @param executionStatus
	 * @param connector
	 * @param cycleId
	 * @param issueKey
	 * @throws Exception
	 */
	private boolean updateExecution(TestStatus executionStatus, JIRAConnector connector, String cycleId,
			String issueKey, String comment) throws Exception {
		boolean updateStatus = false;
		if (executionStatus == TestStatus.PASSED) {
			if (connector.changeExecutionStatus(cycleId, issueKey, ExecutionStatus.PASS, comment)) {
				log.info("<JIRAUpdateExecutionArtifact> Execution updated successfully");
				updateStatus = true;
			} else {
				log.error("<JIRAUpdateExecutionArtifact> Failed to update execution");
			}
		} else if (executionStatus == TestStatus.FAILED) {
			if (connector.changeExecutionStatus(cycleId, issueKey, ExecutionStatus.FAIL, comment)) {
				log.info("<JIRAUpdateExecutionArtifact> Execution updated successfully");
				updateStatus = true;
			} else {
				log.error("<JIRAUpdateExecutionArtifact> Failed to update execution");
			}

		}

		return updateStatus;
	}

	/**
	 * Add attachments
	 * 
	 * @param rootFolder
	 * @throws Exception
	 */
	private void addAttachment(File rootFolder, String cycleId) throws Exception {
		JIRAConnector connector = new JIRAConnectorImpl(jiraServer, credentials);

		String zipFilePath = ZIPFileUtil.createZipFile(rootFolder);

		log.info("<JIRAUpdateExecutionArtifact-AddAttachment> Zip file creatred :" + zipFilePath);

		if (connector.addAttachment(new File(zipFilePath), cycleId, issueKey)) {
			log.info("<JIRAUpdateExecutionArtifact-AddAttachment> Attachment added sucessfully");
			if (new File(zipFilePath).delete()) {
				log.info("<JIRAUpdateExecutionArtifact-AddAttachment> Zip file successfully deleted from local");
			}
		} else {
			log.error("<JIRAUpdateExecutionArtifact-AddAttachment> Adding attachment to execution failed !!");
		}

	}

}
