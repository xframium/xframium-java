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
package org.xframium.page.keyWord.step.spi;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSValue.
 */
public class KWSString extends AbstractKeyWordStep
{
    public KWSString()
    {
        kwName = "String Operations";
        kwDescription = "Allows the script to perform some basic string and formatting operations";
        kwHelp = "https://www.xframium.org/keyword.html#kw-string";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com
     * .perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map,
     * java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC )
    {
        boolean rtn = true;
        String originalValue = null;
        String operationName = null;
        int paramCount = getParameterList().size();

        if ( getParameterList().size() == 1 )
        {
            originalValue = getElement( pageObject, contextMap, webDriver, dataMap ).getValue();
            operationName = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
        }
        else if ( getParameterList().size() >= 2 )
        {
            originalValue = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
            operationName = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "";
        }

        String newValue = null;

        switch ( operationName.toLowerCase() )
        {
            case "trim":
                newValue = originalValue.trim();
                break;

            case "lower":
                newValue = originalValue.toLowerCase();
                break;

            case "decimal":
                DecimalFormat decimalFormat = new DecimalFormat( getParameterValue( getParameterList().get( 2 ), contextMap, dataMap ) + "" );
                decimalFormat.setRoundingMode( RoundingMode.DOWN );
                newValue = decimalFormat.format( Double.parseDouble( originalValue ) );
                break;

            case "upper":
                newValue = originalValue.toUpperCase();
                break;

            case "contains":
                String expectedValue = getParameterValue( getParameterList().get( 2 ), contextMap, dataMap ) + "";

                if ( expectedValue.isEmpty() )
                {
                    throw new ScriptConfigurationException( "STRING Expected format is: [" + expectedValue + "]" );
                }

                if ( !originalValue.contains( expectedValue ) && !(expectedValue == null) && !(expectedValue.equals( null )) )
                {
                    throw new ScriptException( "STRING Expected [" + expectedValue + "] and original[" + originalValue + "] format is not matching " );
                }
                else
                {
                    newValue = expectedValue;
                }
                break;

            case "concat":
                StringBuilder buff = new StringBuilder( originalValue );
                for( int i = 2; i < paramCount; ++i )
                {
                    buff.append( getParameterValue( getParameterList().get( i ), contextMap, dataMap ) + "" );
                }
                newValue = buff.toString();
                break;

            case "substr":
                if ( paramCount < 4 )
                {
                    throw new ScriptConfigurationException( "STRING operation substr requires four parameters" );
                }

                int beginIndex = getInt( getParameterValue( getParameterList().get( 2 ), contextMap, dataMap ) + "" );
                int endIndex = getInt( getParameterValue( getParameterList().get( 3 ), contextMap, dataMap ) + "" );

                newValue = originalValue.substring( beginIndex, endIndex );

                if ( paramCount > 4 )
                {
                    String compareTo = getParameterValue( getParameterList().get( 4 ), contextMap, dataMap ) + "";

                    rtn = newValue.equals( compareTo );
                }
                
                break;
        }

        if ( !validateData( newValue + "" ) )
            throw new IllegalStateException( "STRING Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + newValue + "]" );

        if ( getContext() != null )
        {
            if ( log.isDebugEnabled() )
                log.debug( "Setting Context Data to [" + newValue + "] for [" + getContext() + "]" );
            contextMap.put( getContext(), newValue );
        }

        return rtn;
    }

    //
    // Helpers
    //

    private int getInt( String value )
    {
        int rtn = 0;
        
        try
        {
            rtn = Integer.parseInt( value );
        }
        catch( Exception e )
        {
            throw new IllegalStateException( e );
        }

        return rtn;
    }
}
