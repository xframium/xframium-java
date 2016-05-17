/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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
package com.xframium.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.xframium.spi.RunDetails;

public class ExecutionDefinitionMap 
{
	private Map<String,ExecutionDefinition> definitionMap = new HashMap<String,ExecutionDefinition>( 10 );
	
	public ExecutionDefinitionMap ( File mapFile )
	{
		try
		{
			Properties props = new Properties();
			props.load( new FileInputStream( mapFile ) );
			
			for ( Object keyName : props.keySet() )
			{
				ExecutionDefinition ed = new ExecutionDefinition( new File( mapFile.getAbsoluteFile().getParentFile(), props.getProperty( keyName + "") ) );
				if ( ed.filesExist() )
					definitionMap.put( keyName + "", ed );

			}
			
		}
		catch( Exception e )
		{
			
		}
	}
	
	public Collection<ExecutionDefinition> getExecutions()	
	{
		return definitionMap.values();
	}
}
