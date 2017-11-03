package org.xframium.integrations.jira;

/**
 * 
 *
 */
public enum HTTPMethod {
	GET("GET"), POST("POST"), DELETE("DELETE"), PUT("PUT");
	
	private String value;
	
	private HTTPMethod(String method){
		this.value = method;
	}
	
	public String getValue() {
		return value;
	}
}
