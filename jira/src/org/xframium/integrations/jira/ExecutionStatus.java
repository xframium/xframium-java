package org.xframium.integrations.jira;
/**
 * Enum that stored the execution status values
 * @author rravs
 *
 */
public enum ExecutionStatus {
	PASS(1), FAIL(2), WIP(3);

	private int value;

	private ExecutionStatus(int value){
		 this.value = value;
	 }

	public int getValue() {
		return value;
	}
}
