package org.xframium.driver;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.step.SyntheticStep;
import org.xframium.page.listener.KeyWordListener;
import org.xframium.reporting.ExecutionContextTest;

public class GlobalListener implements KeyWordListener
{
    private String beforeTest;
    private String afterTest;
    private String beforeStep;
    private String afterStep;
    private String xFID;
    
    public GlobalListener(String beforeTest, String afterTest, String beforeStep, String afterStep, String xFID)
    {
        this.beforeTest = beforeTest;
        this.afterTest = afterTest;
        this.beforeStep = beforeStep;
        this.afterStep = afterStep;
        this.xFID = xFID;
    }

    @Override
    public boolean beforeStep(WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC)
    {
        if  ( beforeStep == null || beforeStep.trim().isEmpty() || currentStep instanceof SyntheticStep || eC.isInStepListener() )
            return true;
        eC.setInStepListener( true );
        
        boolean returnValue = false;
        try
        {
            eC.startStep( new SyntheticStep( beforeStep, "CALL2" ), contextMap, dataMap );
            
            KeyWordTest kTest = KeyWordDriver.instance( xFID ).getTest( beforeStep );
            if ( kTest != null )
            {
                 returnValue = kTest.executeTest( webDriver, contextMap, dataMap, pageMap, sC, eC );
                 eC.completeStep( returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, null, null );
            }
            else
            {
                returnValue = false;
                eC.completeStep( StepStatus.FAILURE, new ScriptConfigurationException( "Could not locate pre-step function [" + beforeStep+ "]" ), null );
            }
        }
        catch( Exception e )
        {
            eC.completeStep( StepStatus.FAILURE, e, null );
            return false;
        }
        finally
        {
            eC.setInStepListener( false );
        }
        
        return returnValue;
    }

    @Override
    public void afterStep(WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, StepStatus stepStatus, SuiteContainer sC, ExecutionContextTest eC)
    {
        if  ( afterStep == null || afterStep.trim().isEmpty() || currentStep instanceof SyntheticStep || eC.isInStepListener() )
            return;
        eC.setInStepListener( true );
        
        boolean returnValue = false;
        try
        {
            eC.startStep( new SyntheticStep( afterStep, "CALL2" ), contextMap, dataMap );
            
            KeyWordTest kTest = KeyWordDriver.instance( xFID ).getTest( afterStep );
            if ( kTest != null )
            {
                 returnValue = kTest.executeTest( webDriver, contextMap, dataMap, pageMap, sC, eC );
                 eC.completeStep( returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, null, null );
            }
            else
            {
                eC.completeStep( StepStatus.FAILURE, new ScriptConfigurationException( "Could not locate post-step function [" + afterStep + "]" ), null );
            }
        }
        catch( Exception e )
        {
            eC.completeStep( StepStatus.FAILURE, e, null );
        }
        finally
        {
            eC.setInStepListener( false );
        }
        
    }

    @Override
    public boolean beforeTest(WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC)
    {
        if  ( beforeTest == null || beforeTest.trim().isEmpty() )
            return true;
        boolean returnValue = false;
        try
        {
            eC.startStep( new SyntheticStep( beforeTest, "CALL2" ), contextMap, dataMap );
            
            KeyWordTest kTest = KeyWordDriver.instance( xFID ).getTest( beforeTest );
            if ( kTest != null )
            {
                 returnValue = kTest.executeTest( webDriver, contextMap, dataMap, pageMap, sC, eC );
                 eC.completeStep( returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, null, null );
            }
            else
            {
                returnValue = false;
                eC.completeStep( StepStatus.FAILURE, new ScriptConfigurationException( "Could not locate pre-test function [" + beforeTest+ "]" ), null );
            }
        }
        catch( Exception e )
        {
            eC.completeStep( StepStatus.FAILURE, e, null );
            return false;
        }
        
        return returnValue;
    }

    @Override
    public void afterTest(WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC)
    {
        if  ( afterTest == null || afterTest.trim().isEmpty() )
            return;
        
        boolean returnValue = false;
        try
        {
            eC.startStep( new SyntheticStep( afterTest, "CALL2" ), contextMap, dataMap );
            
            KeyWordTest kTest = KeyWordDriver.instance( xFID ).getTest( afterTest );
            if ( kTest != null )
            {
                 returnValue = kTest.executeTest( webDriver, contextMap, dataMap, pageMap, sC, eC );
                 eC.completeStep( returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, null, null );
            }
            else
            {
                eC.completeStep( StepStatus.FAILURE, new ScriptConfigurationException( "Could not locate pre-test function [" + afterTest + "]" ), null );
            }
        }
        catch( Exception e )
        {
            eC.completeStep( StepStatus.FAILURE, e, null );
        }

    }

    @Override
    public void afterArtifacts(WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC)
    {
        // TODO Auto-generated method stub

    }

}
