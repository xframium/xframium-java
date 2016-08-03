package org.xframium.page.listener;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;

public interface KeyWordListener
{
    public boolean beforeStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap );
    public void afterStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, StepStatus stepStatus );
    
    public boolean beforeTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String,Object> contextMap, Map<String,PageData> dataMap, Map<String,Page> pageMap );
    public void afterTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String,Object> contextMap, Map<String,PageData> dataMap, Map<String,Page> pageMap, boolean stepPass );
}
