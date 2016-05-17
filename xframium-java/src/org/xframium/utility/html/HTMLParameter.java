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
package org.xframium.utility.html;

public class HTMLParameter
{
    private String name;
    private String value;
    private String functionName = null;

    @Override
    public String toString()
    {
        return "HTMLParameter [name=" + name + ", value=" + value + "]";
    }

    public HTMLParameter( String name, String value )
    {
        this.name = name;
        this.value = value;
        
        if ( value.contains( ".*" ) )
        {
            if ( value.startsWith( ".*" ) && value.endsWith( ".*" ) )
                functionName = "contains";
            else if ( value.startsWith( ".*" ) )
                functionName = "starts-with";
            else if ( value.endsWith( ".*" ) )
                functionName = "ends-with";
            
            this.value = value.replace( ".*", "" );
        }
        
    }
    
    public String getName()
    {
        return name;
    }
    public void setName( String name )
    {
        this.name = name;
    }
    public String getValue()
    {
        return value;
    }
    public void setValue( String value )
    {
        this.value = value;
    }
    
    public String toXPath()
    {
        if ( name.equals( "innerText" ) )
        {
            if ( functionName != null )
                return "[" + functionName + "( text(), '" + value + "')]";
            else
                return "[text()='" + value + "']";
        }
        else
        {
            if ( functionName != null )
                return "[" + functionName + "( @" + name + ", '" + value + "' )]";
            else
                return "[@" + name + "='" + value + "']";
        }
    }
    
    
}
