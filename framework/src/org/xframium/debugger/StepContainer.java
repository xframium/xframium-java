package org.xframium.debugger;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.keyWord.KeyWordStep;

public class StepContainer
{
    private WebDriver webDriver;
    private KeyWordStep step;
    private StepStatus status;
    private Map<String, Object> contextMap;
    private Page pageObject;
    private String stepType;
    
    public StepContainer( WebDriver webDriver, KeyWordStep step, Map<String, Object> contextMap, Page pageObject )
    {
        this.webDriver = webDriver;
        this.step = step;
        
        if ( contextMap instanceof HashMap )
            this.contextMap = (Map<String,Object>)( (HashMap) contextMap ).clone();
        else
            this.contextMap = contextMap;
        this.pageObject = pageObject;
        this.stepType = step.getClass().getSimpleName();
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
