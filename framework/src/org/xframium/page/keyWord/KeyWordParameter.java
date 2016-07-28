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
package org.xframium.page.keyWord;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyWordParameter.
 */
public class KeyWordParameter
{

    /**
     * The Enum ParameterType.
     */
    public enum ParameterType
    {

        /** The context. */
        CONTEXT,
        /** The property. */
        PROPERTY,
        /** The static. */
        STATIC,
        /** The data. */
        DATA,

        /** The content. */
        CONTENT,

        FILE;
    }

    /** The type. */
    private ParameterType type;

    /** The value. */
    private String value;

    /** The file name */
    private String fileName;

    /** The name. */
    private String name;

    /** The usage. */
    private String usage;

    /** The token list. */
    private List<KeyWordToken> tokenList = new ArrayList<KeyWordToken>( 10 );

    /**
     * Instantiates a new key word parameter.
     *
     * @param type
     *            the type
     * @param value
     *            the value
     */
    public KeyWordParameter( ParameterType type, String value, String name, String usage )
    {
        super();
        this.type = type;
        this.value = value;
        this.name = name;
        this.usage = usage;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }
    
    public String getName()
    {
        return name;
    }

    public String getUsage()
    {
        return usage;
    }
    
    /**
     * Gets the token list.
     *
     * @return the token list
     */
    public List<KeyWordToken> getTokenList()
    {
        return tokenList;
    }
    
    public void addToken( KeyWordToken token )
    {
        tokenList.add( token );
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public ParameterType getType()
    {
        return type;
    }
}
