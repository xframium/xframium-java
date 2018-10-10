package org.xframium.page.keyWord.step;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.reporting.ExecutionContextTest;

public class SyntheticStep extends AbstractKeyWordStep implements KeyWordStep
{

    public SyntheticStep( String name, String kwName, String description )
    {
        this.setName( name );
        this.kw = kwName;
        this.setDescription( description );
        this.orMapping = false;
    }
    
    public SyntheticStep( String name, String kwName )
    {
        this( name, kwName, null, null );
    }
    
    public SyntheticStep( String name, String kwName, String successMessage, String failMessage )
    {
        this.setName( name );
        this.kw = kwName;
        setSuccessReport( successMessage );
        setFailureReport( failMessage );
    }
    
    public void setKWDescription( String kwDescription )
    {
      this.kwDescription = kwDescription;
    }
    
    @Override
    protected boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext ) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public String getReportMessage( StepStatus stepStatus, Map<String, Object> contextMap, Map<String, PageData> dataMap, String xFID )
    {
      // TODO Auto-generated method stub
      return super.getReportMessage(stepStatus, contextMap, dataMap, xFID);
    }

}
