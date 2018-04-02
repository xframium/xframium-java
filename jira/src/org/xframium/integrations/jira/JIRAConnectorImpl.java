package org.xframium.integrations.jira;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xframium.integrations.jira.entity.JIRAAttachment;
import org.xframium.integrations.jira.entity.JIRAExecution;
import org.xframium.integrations.jira.entity.JIRAProject;
import org.xframium.integrations.jira.entity.JIRATestCycle;
import org.xframium.integrations.jira.entity.JIRAVersion;
import org.xframium.integrations.jira.util.JIRAConnectorUtil;

import com.xframium.serialization.SerializationManager;
import com.xframium.serialization.json.ReflectionSerializer;

/**
 * Class that implements various operations that can be performed in JIRA
 * through ZAPI
 * 
 * 
 *
 */

public class JIRAConnectorImpl implements JIRAConnector {

	private static Log log = LogFactory.getLog(JIRAConnectorImpl.class);

	/** Jira server URL */
	protected String serverUrl;

	/** The project. */
	protected String projectKey;

	/** use proxy? */
	private boolean useProxy;

	/** jira credentials */
	private String credentials;

	/** proxy */
	private Proxy proxy;
	
	/** proxy */
	private HttpHost proxyHost;

	/**
	 * Initialize connector with proxy
	 * 
	 * @param serverUrl
	 * @param credentials
	 *            (username:password)
	 * @param proxyServer
	 * @param proxyPort
	 */
	public JIRAConnectorImpl(String serverUrl, String credentials, String proxyServer, int proxyPort) {
		this.serverUrl = serverUrl;
		this.useProxy = true;
		this.credentials = credentials;

		if (useProxy){
			this.proxyHost = new HttpHost(proxyServer, proxyPort);
			this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyServer, proxyPort));
		}
			
	}

	/**
	 * Initialize connector without proxy
	 * 
	 * @param serverUrl
	 * @param credentials(username:password)
	 */
	public JIRAConnectorImpl(String serverUrl, String credentials) {
		this.serverUrl = serverUrl;
		this.credentials = credentials;

		SerializationManager.instance().setDefaultAdapter(SerializationManager.JSON_SERIALIZATION);
		SerializationManager.instance().getDefaultAdapter().addCustomMapping(JIRATestCycle.class,
				new ReflectionSerializer());
	}

	@Override
	public String getProjectIdWithKey(String projectKey) throws Exception {

		String projectURL = serverUrl.concat(PROJECT_PATH);

		JIRAProject projectEntity = new JIRAProject();
		projectEntity.setProjectKey(projectKey);
		
		JSONArray projects = null;
		
		HttpURLConnection httpUrlConn = JIRAConnector.getHttpConnection(projectURL, credentials, useProxy, proxy);
		projects = new JSONArray(JIRAConnectorUtil.httpGetJSONString(httpUrlConn.getInputStream()));
		

		Iterator<?> projectsIterator = projects.iterator();

		while (projectsIterator.hasNext()) {

			JSONObject projectRef = (JSONObject) projectsIterator.next();

			if (projectRef.get("key").equals(projectEntity.getProjectKey())) {
				projectEntity.setId(projectRef.getString(ID_TAG));
				break;
			}

		}

		if (projectEntity.getId() == null) {
			log.error("<JiraConnectorImpl> Invalid project Key");
			return null;
		}

		return projectEntity.getId();
	}

	@Override
	public String getVersionWithVersionNameAndProjectId(String projectId, String versionName) throws Exception {
		String versionURL = serverUrl.concat(PROJECT_PATH).concat("/").concat(projectId);

		JIRAVersion version = new JIRAVersion();
		version.setProjectId(projectId);
		version.setVersionName(versionName);
		
		JSONObject project  = null;
		
		HttpURLConnection httpUrlConn = JIRAConnector.getHttpConnection(versionURL, credentials, useProxy, proxy);
		project = new JSONObject(JIRAConnectorUtil.httpGetJSONString(httpUrlConn.getInputStream()));

		JSONArray versions = (JSONArray) project.get(VERSION_TAG);

		Iterator<?> versionIterator = versions.iterator();

		while (versionIterator.hasNext()) {
			JSONObject eachVersion = (JSONObject) versionIterator.next();
			if (eachVersion.get("name").equals(versionName)) {
				// use the first version with the given name
				version.setId(eachVersion.getString(ID_TAG));
				break;
			}

		}

		if (version.getId() == null) {
			log.error("<JiraConnectorImpl> Invalid ProjectId/Version");
			return null;
		}

		return version.getId();
	}

	@Override
	public String createTestCycle(String projectId, String versionId, String cycleName) throws Exception {
		String createCycleURL = serverUrl.concat(CYCLE_URL);

		HttpPost postRequest = JIRAConnectorUtil.getHttpPostRequest(createCycleURL, credentials);

		JIRATestCycle testCycle = new JIRATestCycle();
		testCycle.setProjectId(projectId);
		testCycle.setVersionId(versionId);
		testCycle.setName(cycleName);

		String jsonBody = JIRAConnectorUtil
				.removeNullValues(new String(SerializationManager.instance().toByteArray(testCycle)));

		StringEntity input = null;
		try {
			input = new StringEntity(jsonBody);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		input.setContentType(JSON_CONTENT_TYPE);

		/*
		 * Add JSON to request
		 */
		postRequest.setEntity(input);

		HttpClient client = JIRAConnector.getHttpClient(createCycleURL, useProxy, proxyHost);

		HttpResponse response = client.execute(postRequest);

		int status = response.getStatusLine().getStatusCode();
		String apiOutput = null;

		JIRATestCycle createCycleResponse = new JIRATestCycle();

		if (status != 200) {
			HttpEntity httpEntity = response.getEntity();
			apiOutput = EntityUtils.toString(httpEntity);

		} else {
			HttpEntity httpEntity = response.getEntity();
			apiOutput = EntityUtils.toString(httpEntity);
			JSONObject responseObject = new JSONObject(apiOutput);
			createCycleResponse.setId(responseObject.getString("id"));
			createCycleResponse.setName(testCycle.getName());
			createCycleResponse.setProjectId(testCycle.getProjectId());
			testCycle.setVersionId(testCycle.getVersionId());
		}

		if (createCycleResponse != null)
			return createCycleResponse.getId();
		else
			return null;
	}

	@Override
	public String getTestCyclesId(String projectId, String versionId, String cycleName) throws Exception {

		if (projectId == null || versionId == null || cycleName == null)
			return null;

		JIRATestCycle testCycle = new JIRATestCycle();
		testCycle.setProjectId(projectId);
		testCycle.setVersionId(versionId);
		testCycle.setName(cycleName);

		String projectURL = serverUrl.concat(GET_A_CYCLE_URL).concat(VERSION_ID_EQUALS)
				.concat(testCycle.getVersionId());

		BufferedReader br = null;
		HttpURLConnection httpUrlConn = JIRAConnector.getHttpConnection(projectURL, credentials, useProxy, proxy);
		br = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream()));	

		final StringBuffer httpResponse = new StringBuffer();
		String line = "";
		while (null != (line = br.readLine())) {
			httpResponse.append(line);
		}

		JSONObject cycles = new JSONObject(httpResponse.toString());
		
		if(cycles!=null){
		Set<String> cycleIds = cycles.keySet();
		
		for (String cycleId : cycleIds) {
			try {
				Integer.parseInt(cycleId); // to avoid the key "recordCount"
				JSONObject cycle = (JSONObject) cycles.get(cycleId);
				if (cycle.getString("name").equals(testCycle.getName().trim())) {
					testCycle.setId(cycleId);
					testCycle.setCycleId(cycleId);
					break;
				}
			} catch (NumberFormatException nfe) {
			}
		}
		}

		return testCycle.getId();
	}

	@Override
	public boolean addTestCasesToCycle(String projectId, String testCycleId, List<String> issues, String versionId)
			throws Exception {
		String addTestCaseURL = serverUrl.concat(ADD_TEST_CASE_URL);

		HttpPost postRequest = JIRAConnectorUtil.getHttpPostRequest(addTestCaseURL, credentials);

		JIRATestCycle cycle = new JIRATestCycle();
		cycle.setProjectId(projectId);
		cycle.setVersionId(versionId);
		cycle.setCycleId(testCycleId);
		cycle.setIssues(issues);

		String jsonBody = JIRAConnectorUtil
				.removeNullValues(new String(SerializationManager.instance().toByteArray(cycle)));

		StringEntity input = null;
		try {
			input = new StringEntity(jsonBody);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		input.setContentType(JSON_CONTENT_TYPE);

		/*
		 * Add JSON to request
		 */
		postRequest.setEntity(input);

		HttpClient client = JIRAConnector.getHttpClient(addTestCaseURL, useProxy, proxyHost);

		HttpResponse response = client.execute(postRequest);

		int status = response.getStatusLine().getStatusCode();

		if (status == STATUS_OK)
			return true;

		return false;
	}

	@Override
	public boolean changeExecutionStatus(String testCycleID, String issueKey, ExecutionStatus status, String comment)
			throws Exception {

		JIRAExecution execution = new JIRAExecution();
		execution.setCycleId(testCycleID);
		execution.setIssueKey(issueKey);
		execution.setStatus(status);
		
		StringBuilder commentBuilder = new StringBuilder();
		for(String commentPart: comment.split("\r\n")){
			commentBuilder.append(commentPart.trim()).append(",");
		}
		comment = commentBuilder.toString().substring(0,700);
		
		execution.setComment(comment);

		String executionId = getExecutionId(execution.getCycleId(), execution.getIssueKey());

		if (executionId == null)
			return false;

		execution.setId(executionId);

		String updateExecutionURL = serverUrl.concat(UPD_EXECUTION_URL).replace("{executionId}", execution.getId());

		HttpPut putRequest = JIRAConnectorUtil.getHttpPutRequest(updateExecutionURL, credentials);

		String payload = EXECUTION_PAYLOAD_JSON.replace("{status}", String.valueOf(execution.getStatus().getValue()))
				.replace("{comment}", String.valueOf(execution.getComment()));

		StringEntity content = new StringEntity(payload);
		content.setContentType(JSON_CONTENT_TYPE);
		putRequest.setEntity(content);

		HttpClient client = JIRAConnector.getHttpClient(updateExecutionURL, useProxy, proxyHost);

		HttpResponse response = client.execute(putRequest);

		if (response.getStatusLine().getStatusCode() == STATUS_OK) {
			return true;
		}

		return false;
	}

	@Override
	public boolean addAttachment(File fileToUpload, String testCycleID, String issueKey) throws Exception {
		JIRAExecution execution = new JIRAExecution();
		execution.setCycleId(testCycleID);
		execution.setIssueKey(issueKey);
		JIRAAttachment attachment = new JIRAAttachment();
		attachment.setAttachmentFile(fileToUpload);
		attachment.setAttachmentName("file");
		execution.setAttachment(attachment);

		String executionId = getExecutionId(execution.getCycleId(), execution.getIssueKey());

		if (executionId == null)
			return false;

		execution.setId(executionId);

		String addAttachmentURL = serverUrl.concat(ADD_ATTACHMENT_URL).replace("{executionId}", execution.getId());

		HttpPost postRequest = JIRAConnectorUtil.getHttpPostRequest(addAttachmentURL, credentials);
		postRequest.setHeader("X-Atlassian-Token", "nocheck");
		postRequest.setHeader("Accept", JSON_CONTENT_TYPE);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody(execution.getAttachment().getAttachmentName(),
				execution.getAttachment().getAttachmentFile(), ContentType.MULTIPART_FORM_DATA,
				execution.getAttachment().getAttachmentFile().getName());
		final HttpEntity multipart = builder.build();
		postRequest.setEntity(multipart);

		HttpClient client = JIRAConnector.getHttpClient(addAttachmentURL, useProxy, proxyHost);
		
		HttpResponse response = client.execute(postRequest);

		if (response.getStatusLine().getStatusCode() == STATUS_OK)
			return true;

		return false;
	}

	/**
	 * Retrieve execution id of the specified cycle & issue key
	 * 
	 * @param testCycleID
	 * @param issueKey
	 * @return
	 * @throws Exception
	 */
	public String getExecutionId(String testCycleID, String issueKey) throws Exception {
		JIRAExecution execution = new JIRAExecution();
		execution.setCycleId(testCycleID);
		execution.setIssueKey(issueKey);

		String findExecutionURL = serverUrl.concat(FIND_EXECUTION_URL).concat(execution.getCycleId());
		
		BufferedReader br = null;
		HttpURLConnection httpUrlConn = JIRAConnector.getHttpConnection(findExecutionURL, credentials, useProxy, proxy);
		br = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream()));


		StringBuffer httpResponse = new StringBuffer();

		String line = null;
		while (null != (line = br.readLine())) {
			httpResponse.append(line);
		}

		JSONObject cycleData = new JSONObject(httpResponse.toString());

		JSONArray executions = (JSONArray) cycleData.get(EXECUTIONS_TAG);

		for (int i = 0; i < executions.length(); i++) {
			JSONObject eachExecution = (JSONObject) executions.get(i);
			if (eachExecution.get(ISSUE_KEY_TAG).toString().equals(execution.getIssueKey())) {
				execution.setId(String.valueOf(eachExecution.get(ID_TAG)));
				break;
			}
		}

		return execution.getId();
	}

}
