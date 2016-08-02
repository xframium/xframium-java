package org.xframium.debugger;

import org.openqa.selenium.WebDriver;
import org.xframium.page.StepStatus;
import org.xframium.page.keyWord.KeyWordStep;

public class StepContainer
{
    private WebDriver webDriver;
    private KeyWordStep step;
    private StepStatus status;
    
    public StepContainer( WebDriver webDriver, KeyWordStep step )
    {
        this.webDriver = webDriver;
        this.step = step;
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
