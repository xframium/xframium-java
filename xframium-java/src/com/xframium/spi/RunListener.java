/*
 * 
 */
package com.xframium.spi;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving run events.
 * The class that is interested in processing a run
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addRunListener</code> method. When
 * the run event occurs, that object's appropriate
 * method is invoked.
 */
public interface RunListener
{
	
	/**
	 * Before run.
	 *
	 * @param currentDevice the current device
	 * @param runKey the run key
	 * @return true, if successful
	 */
	public boolean beforeRun( Device currentDevice, String runKey );
	
	/**
	 * After run.
	 *
	 * @param currentDevice the current device
	 * @param runKey the run key
	 * @param successful the successful
	 */
	public void afterRun( Device currentDevice, String runKey, boolean successful );
}
