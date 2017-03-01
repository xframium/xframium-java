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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.page.keyWord.step.spi.KWSBrowser.SwitchType;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.utility.DateUtility;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSValue.
 */
public class KWSCompare2 extends AbstractKeyWordStep
{
    public KWSCompare2()
    {
        kwName = "Compare";
        kwDescription = "Allows the script to compare to parameter values";
        kwHelp = "https://www.xframium.org/keyword.html#kw-compare";
        orMapping = false;
        category = "Verification";
    }
    
    public enum CompareType
    {
        STRING( 1, "STRING", "Compare two Strings" ),
        INTEGER( 2, "INTEGER", "Compare two whole numbers" ),
        DECIMAL( 3, "DECIMAL", "Compare two decimals" ),
        DATE( 4, "DATE", "Compare two dates" );
        

        public List<CompareType> getSupported()
        {
            List<CompareType> supportedList = new ArrayList<CompareType>( 10 );
            supportedList.add( CompareType.STRING );
            supportedList.add( CompareType.INTEGER );
            supportedList.add( CompareType.DECIMAL );
            supportedList.add( CompareType.DATE );

            return supportedList;
        }

        private CompareType( int id, String name, String description )
        {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        private int id;
        private String name;
        private String description;
    }
    
    private enum OperatorType
    {
        GREATER,LESS,EQUALS;
    }
    
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
	{

	    OperatorType operatorType = OperatorType.EQUALS;
	    if ( getParameter( "Operator" ) != null )
	        operatorType = OperatorType.valueOf( getParameterValue( getParameter( "Operator" ), contextMap, dataMap ).toUpperCase() );
	    
	    String valueOne = getParameterValue( getParameter( "Value One" ), contextMap, dataMap );
	    String valueTwo = getParameterValue( getParameter( "Value Two" ), contextMap, dataMap );

	    switch( CompareType.valueOf( getName().toUpperCase() ) )
	    {
	        case DATE:
	            int dateCompare = Long.compare( (DateUtility.instance().parseDate( valueOne ).getTime() / 1000 ) * 1000, ( DateUtility.instance().parseDate( valueTwo ).getTime() / 1000 ) * 1000 );
	            
                switch( operatorType )
                {
                    case EQUALS:
                        if ( dateCompare != 0 )
                            throw new ScriptException( "COMPARE Expected [" + DateUtility.instance().parseDate( valueOne ) + "] but found [" + DateUtility.instance().parseDate( valueTwo ) + "]" );
                        break;
                    case GREATER:
                        if ( dateCompare <= 0 )
                            throw new ScriptException( "COMPARE Expected [" + DateUtility.instance().parseDate( valueOne ) + "] greater than [" + DateUtility.instance().parseDate( valueTwo ) + "]" );
                        break;
                    case LESS:
                        if ( dateCompare >= 0 )
                            throw new ScriptException( "COMPARE Expected [" + DateUtility.instance().parseDate( valueOne ) + "] less than [" + DateUtility.instance().parseDate( valueTwo ) + "]" );
                        break;
                }
                break;
	        case DECIMAL:
	            float decCompare = Float.compare( Float.parseFloat( valueOne ), Float.parseFloat( valueTwo ) );
                switch( operatorType )
                {
                    case EQUALS:
                        if ( decCompare != 0 )
                            throw new ScriptException( "COMPARE Expected [" + valueOne + "] but found [" + valueTwo + "]" );
                        break;
                    case GREATER:
                        if ( decCompare <= 0 )
                            throw new ScriptException( "COMPARE Expected [" + valueOne + "] greater than [" + valueTwo + "]" );
                        break;
                    case LESS:
                        if ( decCompare >= 0 )
                            throw new ScriptException( "COMPARE Expected [" + valueOne + "] less than [" + valueTwo + "]" );
                        break;
                }
                break;
	        case INTEGER:
	            int wholeCompare = Integer.compare( Integer.parseInt( valueOne ), Integer.parseInt( valueTwo ) );
                switch( operatorType )
                {
                    case EQUALS:
                        if ( wholeCompare != 0 )
                            throw new ScriptException( "COMPARE Expected [" + valueOne + "] but found [" + valueTwo + "]" );
                        break;
                    case GREATER:
                        if ( wholeCompare <= 0 )
                            throw new ScriptException( "COMPARE Expected [" + valueOne + "] greater than [" + valueTwo + "]" );
                        break;
                    case LESS:
                        if ( wholeCompare >= 0 )
                            throw new ScriptException( "COMPARE Expected [" + valueOne + "] less than [" + valueTwo + "]" );
                        break;
                }
                break;
                
	        case STRING:
	            int stringCompare = valueOne.compareTo( valueTwo );
	            switch( operatorType )
	            {
	                case EQUALS:
	                    if ( stringCompare != 0 )
	                        throw new ScriptException( "COMPARE Expected [" + valueOne + "] but found [" + valueTwo + "]" );
	                    break;
	                case GREATER:
	                    if ( stringCompare <= 0 )
                            throw new ScriptException( "COMPARE Expected [" + valueOne + "] greater than [" + valueTwo + "]" );
                        break;
	                case LESS:
	                    if ( stringCompare >= 0 )
                            throw new ScriptException( "COMPARE Expected [" + valueOne + "] less than [" + valueTwo + "]" );
                        break;
	            }
	            break;
	    }
	    
	    
		Object compareFrom = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap );
		Object compareTo = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap );
		

		
		return true;
	}


}
