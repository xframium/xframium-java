package org.xframium.integrations.jira.entity;

import java.util.List;

/**
 * Represents a TestCycle
 * Project > Version > Test Cycle
 * @author rravs
 *
 */

public class JIRATestCycle extends JIRAEntity {
	
	private String cycleId;
	private String projectId;
	private String versionId;
	private String name;
	private List<String> issues;
	
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCycleId() {
		return cycleId;
	}
	public void setCycleId(String cycleId) {
		this.cycleId = cycleId;
	}
	public List<String> getIssues() {
		return issues;
	}
	public void setIssues(List<String> issues) {
		this.issues = issues;
	}
	@Override
	public String toString() {
		return "JIRATestCycle [cycleId=" + cycleId + ", projectId=" + projectId + ", versionId=" + versionId + ", name="
				+ name + ", issues=" + issues + ", getId()=" + getId() + "]";
	}
	
	
	
	

}
