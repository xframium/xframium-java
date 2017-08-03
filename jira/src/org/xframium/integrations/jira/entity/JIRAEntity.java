package org.xframium.integrations.jira.entity;

/**
 * Common entity class extended by other entity classes.
 * @author rravs
 *
 */
public abstract class JIRAEntity {
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

}
