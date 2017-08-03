package org.xframium.integrations.jira.entity;

import java.io.File;

/**
 * Represents an attachment inside execution
 * Test Cycle > Execution > Attachment
 * @author rravs
 *
 */

public class JIRAAttachment extends JIRAEntity {

	private String attachmentName;
	private File attachmentFile;
	
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public File getAttachmentFile() {
		return attachmentFile;
	}
	public void setAttachmentFile(File attachmentFile) {
		this.attachmentFile = attachmentFile;
	}
}
