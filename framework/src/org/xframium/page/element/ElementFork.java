/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
package org.xframium.page.element;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class ElementFork.
 */
public class ElementFork
{
	
	/** The log. */
	private Log log = LogFactory.getLog( ElementFork.class );
	
	/** The thread pool. */
	private static ExecutorService threadPool = Executors.newCachedThreadPool();
	
	/** The element index. */
	private int elementIndex = -1;
	
	/** The element found. */
	private Element elementFound;
	
	/** The time out. */
	private long timeOut;
	
	/**
	 * Instantiates a new element fork.
	 *
	 * @param elementArray the element array
	 * @param timeOut the time out
	 */
	public ElementFork( Element[] elementArray, int timeOut )
	{
		if ( log.isInfoEnabled() )
			log.info(  "Element fork created with " + elementArray.length + " elements with a timeout of " + timeOut );
		this.timeOut = timeOut;
		
		for ( int i=0; i<elementArray.length; i++ )
			threadPool.submit( new ElementThread( elementArray[ i ], i, timeOut ) );
	}
	
	
	
	/**
	 * Gets the element index.
	 *
	 * @return the element index
	 */
	public int getElementIndex()
	{
		return elementIndex;
	}



	/**
	 * Gets the element found.
	 *
	 * @return the element found
	 */
	public Element getElementFound()
	{
		return elementFound;
	}



	/**
	 * Find element.
	 *
	 * @return true, if successful
	 */
	public boolean findElement()
	{
		long startTime = System.currentTimeMillis();
		
		while( elementIndex < 0 && ( (System.currentTimeMillis() - startTime) / 1000 ) < timeOut )
		{
			try
			{
				Thread.sleep( 250 );
			}
			catch( Exception e )
			{
				
			}
		}
		
		return elementFound != null;		
	}
	
	/**
	 * Checks if is element found.
	 *
	 * @return true, if is element found
	 */
	protected boolean isElementFound()
	{
		return elementIndex >= 0;
	}
	
	/**
	 * Sets the element found.
	 *
	 * @param elementFound the element found
	 * @param elementIndex the element index
	 */
	public synchronized void setElementFound( Element elementFound, int elementIndex )
	{
		if ( this.elementFound == null )
		{
			if ( log.isInfoEnabled() )
				log.info( "Element found! " + elementFound );
			this.elementFound = elementFound;
			this.elementIndex = elementIndex;
		}
	}
	
	/**
	 * The Class ElementThread.
	 */
	private class ElementThread implements Runnable
	{
		
		/** The current element. */
		private Element currentElement;
		
		/** The index. */
		private int index;
		
		/** The time out. */
		private int timeOut;
		
		/**
		 * Instantiates a new element thread.
		 *
		 * @param currentElement the current element
		 * @param index the index
		 * @param timeOut the time out
		 */
		public ElementThread( Element currentElement, int index, int timeOut )
		{
			this.currentElement = currentElement;
			this.index = index;
			this.timeOut = timeOut;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run()
		{
			
			if ( log.isInfoEnabled() )
				log.info( "Forked thread searching for " + currentElement );
			long startTime = System.currentTimeMillis();
			
			while( !isElementFound() && ( (System.currentTimeMillis() - startTime) / 1000 ) < timeOut )
			{
				try
				{
					if ( currentElement.isPresent() )
					{
						setElementFound( currentElement, index );
						break;
					}
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}
		}
	}
}
