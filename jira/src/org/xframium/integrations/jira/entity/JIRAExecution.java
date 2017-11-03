package org.xframium.integrations.jira.entity;

import org.xframium.integrations.jira.ExecutionStatus;

/**
 * Represents an Execution inside a test Cycle
 * Test Cycle > TestCase/Issue[Execution]
 *
 */
public class JIRAExecution extends JIRAEntity {
	
	private String issueKey;
	private String cycleId;
	private ExecutionStatus status;
	private JIRAAttachment  attachment;
	
	
	public String getIssueKey() {
		return issueKey;
	}
	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}
	public String getCycleId() {
		return cycleId;
	}
	public void setCycleId(String cycleId) {
		this.cycleId = cycleId;
	}
	public JIRAAttachment getAttachment() {
		return attachment;
	}
	public void setAttachment(JIRAAttachment attachment) {
		this.attachment = attachment;
	}
	public ExecutionStatus getStatus() {
		return status;
	}
	public void setStatus(ExecutionStatus status) {
		this.status = status;
	}
	
	
	
}
