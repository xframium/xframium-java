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
