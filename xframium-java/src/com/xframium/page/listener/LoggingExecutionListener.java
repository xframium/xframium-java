package com.xframium.page.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving loggingExecution events.
 * The class that is interested in processing a loggingExecution
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addLoggingExecutionListener</code> method. When
 * the loggingExecution event occurs, that object's appropriate
 * method is invoked.
 *
 * @see LoggingExecutionEvent
 */
public class LoggingExecutionListener implements ExecutionListener
{
	
	/** The log. */
	private Log log = LogFactory.getLog( getClass() );
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.listener.ExecutionListener#beforeExecution(java.lang.String)
	 */
	@Override
	public void beforeExecution( String methodKey )
	{
		log.debug( "Executing " + methodKey );

	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.listener.ExecutionListener#afterExecution(java.lang.String, long)
	 */
	@Override
	public void afterExecution( String methodKey, long runLength )
	{
		log.debug( "Executed " + methodKey + " for " + runLength );

	}

}
