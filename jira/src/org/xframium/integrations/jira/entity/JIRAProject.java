package org.xframium.integrations.jira.entity;

/**
 * Represents a Project in JIRA
 * @author rravs
 *
 */
public class JIRAProject extends JIRAEntity {
	
	private String projectKey;
	

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
	

}
