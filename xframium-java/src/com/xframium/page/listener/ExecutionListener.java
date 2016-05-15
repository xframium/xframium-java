package com.xframium.page.listener;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving execution events.
 * The class that is interested in processing a execution
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addExecutionListener</code> method. When
 * the execution event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ExecutionEvent
 */
public interface ExecutionListener
{
	
	/**
	 * Before execution.
	 *
	 * @param methodKey the method key
	 */
	public void beforeExecution( String methodKey );
	
	/**
	 * After execution.
	 *
	 * @param methodKey the method key
	 * @param runLength the run length
	 */
	public void afterExecution( String methodKey, long runLength );
}
