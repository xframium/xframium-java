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
/*
 * 
 */
package org.xframium.device.cloud;

import java.io.ByteArrayInputStream;
import java.util.List;
import com.xframium.serialization.SerializationManager;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLApplicationProvider.
 */
public class EncryptedCloudProvider extends AbstractCloudProvider
{
	
	private CloudProvider baseProvider;
	
	/**
	 * Instantiates a new XML application provider.
	 *
	 * @param fileName the file name
	 */
	public EncryptedCloudProvider( CloudProvider baseProvider )
	{
		this.baseProvider = baseProvider;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.application.ApplicationProvider#readData()
	 */
	public List<CloudDescriptor> readData()
	{
		List<CloudDescriptor> cloudData = baseProvider.readData();
		
		for ( CloudDescriptor cD : cloudData )
		{
		    cD.setPassword( decryptValue( cD.getPassword() ) );
		    cD.setUserName( decryptValue( cD.getUserName() ) );
		}
		
		return cloudData;
	}
	
	public static String decryptValue( String currentValue )
	{
	    try
	    {
	        String fullValue = new StringBuilder( currentValue ).reverse().toString();
	        String version = fullValue.substring( 0, 2 );
	        String actualValue = fullValue.substring( 2 );
	        
	        if ( version.equals( "01" ) )
	        {
	            ByteArrayInputStream inputStream = new ByteArrayInputStream( actualValue.getBytes() );
	            return (String)SerializationManager.instance().readData( inputStream );
	        }
	        else
	        {
	            return currentValue;
	        }
	    }
	    catch( Exception e )
	    {
	        return currentValue;
	    }
	}
	
	public static String encryptValue(String currentValue )
	{
	   return new StringBuilder( new String( SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.EXTENDED_SERIALIZATION ), currentValue, 9 ) ) ).insert( 0, "01" ).reverse().toString();
	}
	
	public static void main( String[] args ) throws Exception
    {
        if ( args.length != 2 )
        {
            System.err.println( "[ENCRYPT/DECRYPT] [value]" );
            System.exit( -1 );
        }
        
        if ( args[ 0 ].equals( "ENCRYPT" ) )
            System.out.println( encryptValue( args[ 1 ] ) );
        else if ( args[ 0 ].equals( "DECRYPT" ) )
            System.out.println( decryptValue( args[ 1 ] ) );
        else
        {
            System.err.println( "[ENCRYPT/DECRYPT] [value]" );
            System.exit( -1 );
        }
    }
	

}
