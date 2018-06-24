package org.xframium.debugger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.openqa.selenium.WebDriver;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.spi.Device;

public class TestContainer
{
    private WebDriver webDriver;
    private KeyWordTest keyWordTest;
    private Map<String, Object> contextMap;
    private Map<String, PageData> dataMap;
    private Map<String, Page> pageMap;
    private Boolean stepPass;
    private int stepsSent = 0;
    private CloudDescriptor cD;
    private Device d;
    
    private ReentrantLock tLock = new ReentrantLock();
    private boolean stepAhead = false;
    
    
    public boolean isStepAhead()
    {
        return stepAhead;
    }

    public void pause()
    {
        waitFor( true );
        
    }
    
    public void resume()
    {
    	stepAhead=false;
        tLock.unlock();
    }

    public void stepAhead()
    {
        stepAhead = true;
    }
    
    public void waitFor( boolean acquire )
    {
        while ( tLock.tryLock() == false )
        {
        	if ( stepAhead == true )
        	{
        		stepAhead = false;
        		return;
        	}
            try { Thread.sleep( 1000 ); } catch( Exception e ) {}
        }
        
        
        if ( !acquire )
        {
            tLock.unlock();
        }
    }

    private List<StepContainer> stepList = new ArrayList<StepContainer>( 50 );
    
    public TestContainer( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, CloudDescriptor cD, Device d )
    {
        this.webDriver = webDriver;
        this.keyWordTest = keyWordTest;
        this.contextMap = contextMap;
        this.dataMap = dataMap;
        this.pageMap = pageMap;
        this.cD = cD;
        this.d = d;
    }
    
    public int getStepsSent()
    {
        return stepsSent;
    }

    public void setStepsSent( int stepsSent )
    {
        this.stepsSent = stepsSent;
    }

    public void addStep( StepContainer stepContainer )
    {
        stepList.add( stepContainer );
    }
    
    public List<StepContainer> getSteps()
    {
        return Collections.unmodifiableList( stepList );
    }
    
    public WebDriver getWebDriver()
    {
        return webDriver;
    }
    public void setWebDriver( WebDriver webDriver )
    {
        this.webDriver = webDriver;
    }
    public KeyWordTest getKeyWordTest()
    {
        return keyWordTest;
    }
    public void setKeyWordTest( KeyWordTest keyWordTest )
    {
        this.keyWordTest = keyWordTest;
    }
    public Map<String, Object> getContextMap()
    {
        return contextMap;
    }
    public void setContextMap( Map<String, Object> contextMap )
    {
        this.contextMap = contextMap;
    }
    public Map<String, PageData> getDataMap()
    {
        return dataMap;
    }
    public void setDataMap( Map<String, PageData> dataMap )
    {
        this.dataMap = dataMap;
    }
    public Map<String, Page> getPageMap()
    {
        return pageMap;
    }
    public void setPageMap( Map<String, Page> pageMap )
    {
        this.pageMap = pageMap;
    }
    public Boolean getStepPass()
    {
        return stepPass;
    }
    public void setStepPass( Boolean stepPass )
    {
        this.stepPass = stepPass;
    }
    
    
}
