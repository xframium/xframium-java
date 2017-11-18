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

import java.net.URL;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.page.keyWord.step.KeyWordStepFactory;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.utility.html.HTTPLinkCheck;
import com.deque.axe.AXE;
import com.deque.axe.AXE.Builder;

public class KWSAccessibility extends AbstractKeyWordStep
{
    private static final URL AXE_URL = KWSAccessibility.class.getResource("/org/xframium/resource/accessibility/axe/axe.min.js");
    
    public KWSAccessibility()
    {
        kwName = "Accessibilty Testing";
        kwDescription = "Accessibility testing on the current page";
        kwHelp = "https://www.xframium.org/keyword.html#kw-accessibility";
        category = "Utility";
    }
    
    private static final String[] IGNORED_EXTENSIONS = new String[] { ".pdf", ".jnlp", ".jar", ".exe", ".zip", ".hpi" };
    
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
	{
	    if ( getParameterList().size() == 0 )
	    {
    	    JSONObject responseJSON = new AXE.Builder( webDriver, AXE_URL ).analyze();
    	    
    	    JSONArray vA = responseJSON.getJSONArray( "violations" );
    	    if ( vA != null && vA.length() > 0 )
    	        throw new ScriptException( vA.toString() );
    
	    }
	    else
	    {
	        KeyWordParameter siteName = getParameter( "Site Name" );
	        KeyWordParameter url = getParameter( "URL" );
	        KeyWordParameter options = getParameter( "Options" );
	        
	        if ( url != null )
	        {
	            webDriver.get( getParameterValue( url, contextMap, dataMap, executionContext.getxFID() ) );
	            
	            Builder axeBuilder = new AXE.Builder( webDriver, AXE_URL );
	            
	            if ( options != null )
	                axeBuilder = axeBuilder.options( getParameterValue( options, contextMap, dataMap, executionContext.getxFID() ) );
	            
	            JSONObject responseJSON = axeBuilder.analyze();
	            
	            JSONArray vA = responseJSON.getJSONArray( "violations" );
	            if ( vA != null && vA.length() > 0 )
	                throw new ScriptException( vA.toString() );
	        }
	        else if ( siteName != null )
	        {
	            String rootSite = getParameterValue( siteName, contextMap, dataMap, executionContext.getxFID() );
	            HTTPLinkCheck lC = new HTTPLinkCheck();
	            try
	            {
	                lC.process( new URL( rootSite ), new URL( rootSite ) );
	            }
	            catch( Exception e )
	            {
	                throw new ScriptConfigurationException( "Could not parse URL: " + rootSite );
	            }
	            
	            boolean anyFailed = false;
	            for ( String currentPage : lC.getLinkMap().keySet() )
	            {
	                boolean processPage = true;
	                for ( String iE : IGNORED_EXTENSIONS )
	                {
	                    if ( currentPage.toLowerCase().endsWith( iE ) )
	                    {
	                        processPage = false;
	                        break;
	                    }
	                        
	                }
	                
	                if ( !processPage )
	                    continue;
	                    
	                KeyWordStep kW = KeyWordStepFactory.instance().createStep( this );
	                kW.addParameter( new KeyWordParameter( ParameterType.STATIC, currentPage, "URL", null ) );
	                kW.setFailure( StepFailure.ERROR );
	                kW.setName( currentPage );
	                try
	                {
	                    if ( !kW.executeStep( pageObject, webDriver, contextMap, dataMap, pageMap, sC, executionContext ) )
	                        anyFailed = true;
	                }
	                catch( Exception e )
	                {
	                    e.printStackTrace();
	                    anyFailed = true;
	                }
	            }
	            
	            if ( anyFailed )
	                throw new ScriptException( "One or more of the pages analyzed failed accessibility testing" );
	            
	        }
	        else
	        {
	            Builder axeBuilder = new AXE.Builder( webDriver, AXE_URL );
                
                if ( options != null )
                    axeBuilder = axeBuilder.options( getParameterValue( options, contextMap, dataMap, executionContext.getxFID() ) );
                
                JSONObject responseJSON = axeBuilder.analyze();
	            
	            JSONArray vA = responseJSON.getJSONArray( "violations" );
	            if ( vA != null && vA.length() > 0 )
	                throw new ScriptException( vA.toString() );
	        }
	    }

	    return true;
	}

}
