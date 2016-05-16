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
package com.xframium.utility.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLFunction
{
    private static final String HTML_TAG = "html tag";
    private String name;
    private Map<String,HTMLParameter> parameterMap = new HashMap<String,HTMLParameter>( 10 );
    private List<HTMLParameter> parameterList = new ArrayList<HTMLParameter>( 10 );
    private static final Pattern PARAMETERS = Pattern.compile( "(?:[\"']([^:]*):=([^\"']*)[\"'])" );
    
    public HTMLFunction( String name, String parameterDefinition )
    {
        this.name = name;

        Matcher paramMatcher = PARAMETERS.matcher( parameterDefinition );
        
        while( paramMatcher.find() )
        {
            HTMLParameter currentParameter = new HTMLParameter( paramMatcher.group( 1 ), paramMatcher.group( 2 ) );
            parameterList.add( currentParameter );
            parameterMap.put( currentParameter.getName(), currentParameter );
        }
    }

    public String getName()
    {
        return name;
    }
    
    public String getTagName()
    {
        if ( parameterMap.containsKey( HTML_TAG ) )
            return parameterMap.get( HTML_TAG ).getValue();
        else
            return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public List<HTMLParameter> getParameterList()
    {
        return parameterList;
    }

    public void setParameterList( List<HTMLParameter> parameterList )
    {
        this.parameterList = parameterList;
    }

    @Override
    public String toString()
    {
        return "HTMLFunction [name=" + name + ", parameterList=" + parameterList + "]";
    }
    
    public String toXPath( boolean relativePath )
    {
        StringBuilder stringBuilder = new StringBuilder();
        if ( relativePath )
            stringBuilder.append( "." );
        stringBuilder.append( "//" ).append( getTagName() );
        for ( HTMLParameter p : parameterList )
        {
            if ( !p.getName().equals( HTML_TAG ) )
                stringBuilder.append( p.toXPath() );
        }
        
        return stringBuilder.toString();
    }
    
    
    
    
}

