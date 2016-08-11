package org.xframium.debugger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.KeyWordStep;

public class StepContainer
{
    private WebDriver webDriver;
    private KeyWordStep step;
    private StepStatus status;
    private Map<String, Object> contextMap;
    private Page pageObject;
    private String stepType;
    private List<String> parameterList;
    private List<String[]> tokenList;
    private boolean selected;
    private Element pageElement;
    private String fullElement;
    
    public StepContainer( WebDriver webDriver, KeyWordStep step, Map<String, Object> contextMap, Page pageObject, List<String> parameterList, List<String[]> tokenList )
    {
        this.webDriver = webDriver;
        this.step = step;
        
        if ( contextMap instanceof HashMap )
            this.contextMap = (Map<String,Object>)( (HashMap) contextMap ).clone();
        else
            this.contextMap = contextMap;
        this.pageObject = pageObject;
        this.stepType = step.getClass().getSimpleName();
        this.parameterList = parameterList;
        this.tokenList = tokenList;
        
        this.pageElement = this.pageObject.getElement( step.getPageName(), step.getName() );
        
        if ( this.pageElement != null )
        {
            Map<String,String> tokenMap = new HashMap<String,String>( 20 );

            for ( String[] tokens : tokenList )
                tokenMap.put( tokens[ 0 ], tokens[ 1 ] );

            if ( tokenMap != null && !tokenMap.isEmpty() )
            {
                fullElement = pageElement.getKey();
                for ( String tokenName : tokenMap.keySet() )
                {
                    if ( tokenMap.get( tokenName ) != null)
                        fullElement = fullElement.replace( "{" + tokenName + "}", tokenMap.get( tokenName ) );
                }
            }
        }
        
        
    }
    
    
    
    public Element getPageElement()
    {
        return pageElement;
    }



    public void setPageElement( Element pageElement )
    {
        this.pageElement = pageElement;
    }



    public List<String[]> getTokenList()
    {
        return tokenList;
    }

    public void setTokenList( List<String[]> tokenList )
    {
        this.tokenList = tokenList;
    }

    public List<String> getParameterList()
    {
        return parameterList;
    }

    public void setParameterList( List<String> parameterList )
    {
        this.parameterList = parameterList;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected( boolean selected )
    {
        this.selected = selected;
    }

    public String getStepType()
    {
        return stepType;
    }

    public void setStepType( String stepType )
    {
        this.stepType = stepType;
    }

    public Map<String, Object> getContextMap()
    {
        return contextMap;
    }
    public void setContextMap( Map<String, Object> contextMap )
    {
        this.contextMap = contextMap;
    }
    public Page getPageObject()
    {
        return pageObject;
    }
    public void setPageObject( Page pageObject )
    {
        this.pageObject = pageObject;
    }
    public WebDriver getWebDriver()
    {
        return webDriver;
    }
    public void setWebDriver( WebDriver webDriver )
    {
        this.webDriver = webDriver;
    }
    public KeyWordStep getStep()
    {
        return step;
    }
    public void setStep( KeyWordStep step )
    {
        this.step = step;
    }
    public StepStatus getStatus()
    {
        return status;
    }
    public void setStatus( StepStatus status )
    {
        this.status = status;
    }
}
