package org.xframium.page.keyWord;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class GherkinContainer 
{
	private String value;
	private Pattern regex;
	private Method method;
	
	public GherkinContainer(String value, Pattern regex, Method method) {
		super();
		this.value = value;
		this.regex = regex;
		this.method = method;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Pattern getRegex() {
		return regex;
	}
	public void setRegex(Pattern regex) {
		this.regex = regex;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	
	
	
}
