package org.xframium.integrations.jira.entity;

/**
 * Represents a version in JIRA
 * Project > Version
 *
 */
public class JIRAVersion extends JIRAEntity {
	
	private String projectId;
	private String versionName;
	
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	

}
