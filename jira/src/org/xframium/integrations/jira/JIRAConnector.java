package org.xframium.integrations.jira;

import java.io.File;
import java.util.List;

/**
 * Connector interface used for ZAPI(JIRA) connection
 * 
 *
 */
public interface JIRAConnector {
	
	
	public static final String PROJECT_PATH = "rest/api/2/project";
	public static final String CYCLE_URL = "rest/zapi/latest/cycle/";
	public static final String GET_A_CYCLE_URL = "rest/zapi/latest/cycle?";
	public static final String ADD_TEST_CASE_URL = "rest/zapi/latest/execution/addTestsToCycle/";
	public static final String FIND_EXECUTION_URL = "rest/zapi/latest/execution?cycleId=";
	public static final String UPD_EXECUTION_URL = "rest/zapi/latest/execution/{executionId}/execute";
	public static final String ADD_ATTACHMENT_URL = "/rest/zapi/latest/attachment?entityId={executionId}&entityType=EXECUTION";
	
	public static final String EXECUTION_PAYLOAD_JSON = "{ \"status\": \"{status}\", \"comment\":\"{comment}\"}"; 
	
	
	public static final String VERSION_TAG = "versions";
	public static final String ID_TAG = "id";
	public static final String EXECUTIONS_TAG = "executions";
	public static final String ISSUE_KEY_TAG = "issueKey";
	public static final String PROJECT_ID_EQUALS = "projectId=";
	public static final String VERSION_ID_EQUALS="versionId=";
	
	public static final String JSON_CONTENT_TYPE = "application/json";
	public static final int STATUS_OK = 200;
	
	
	
	/**
	 * Retrieve the project id with project key
	 * @param projectKey
	 * @return
	 * @throws Exception
	 */
	String getProjectIdWithKey(String projectKey) throws Exception;
	
	/**
	 * retrieve the version id with project id & version name
	 * @param projectId
	 * @param versionName
	 * @return
	 */
	String getVersionWithVersionNameAndProjectId(String projectId, String versionName) throws Exception;
	
	
	/**
	 * Create a new test cycle
	 * @param projectId
	 * @param versionId
	 * @param cycleName
	 * @return cycleId
	 */
	String createTestCycle(String projectId, String versionId, String cycleName) throws Exception;
	



	/**
	 * Get the cycleId
	 * @param projectId
	 * @param versionId
	 * @param cycleName
	 * @return
	 */
	String getTestCyclesId(String projectId, String versionId, String cycleName) throws Exception;
	
	/**
	 * Add existing test cases to cycle
	 * @param projectID
	 * @param testCycleID
	 * @param issues [Test Case ID from Jira]
	 * @param versionId
	 * @return
	 */
	boolean addTestCasesToCycle(String projectID, String testCycleID, List<String> issues, String versionId) throws Exception;
	
	
	/**
	 * Change execution status of specified execution
	 * @param testCycleIDd
	 * @param executionId
	 * @param status
	 * @param comment 
	 * @return
	 * @throws Exception
	 */
	boolean changeExecutionStatus(String testCycleID, String issueKey, ExecutionStatus status, String comment) throws Exception;
	
	
	/**
	 * Adds the specified file as attachment to the execution inside specified test cycle
	 * @param fileToUpload
	 * @param executionId
	 * @return
	 */
	boolean addAttachment(final File fileToUpload, String testCycleID, final String issueKey) throws Exception;
	
	
	/**
	 * Retrieves execution id of the specified issue key from given cycle
	 * @param testCycleID
	 * @param issueKey
	 * @return
	 * @throws Exception
	 */
	String getExecutionId(String testCycleID, String issueKey) throws Exception;
	

}
