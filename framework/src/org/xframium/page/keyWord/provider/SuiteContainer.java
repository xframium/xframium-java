package org.xframium.page.keyWord.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.element.provider.ElementProvider;
import org.xframium.page.keyWord.KeyWordTest;

public class SuiteContainer
{
    public List<KeyWordTest> getTestList()
    {
        return testList;
    }
    
    public List<KeyWordTest> getActiveTestList()
    {
        return activeTestList;
    }

    public List<KeyWordTest> getInactiveTestList()
    {
        return inactiveTestList;
    }

    public List<KeyWordTest> getFunctionList()
    {
        return functionList;
    }

    public Map<String, KeyWordTest> getTestMap()
    {
        return testMap;
    }

    public Map<String, Class> getModelMap()
    {
        return modelMap;
    }
    
    public ElementProvider getElementProvider()
    {
        return elementProvider;
    }
    
    public void setElementProvider( ElementProvider elementProvider )
    {
        this.elementProvider = elementProvider;
    }
    
    public PageDataProvider getDataProvider()
    {
        return dataProvider;
    }

    public void setDataProvider( PageDataProvider dataProvider )
    {
        this.dataProvider = dataProvider;
    }

    private List<KeyWordTest> activeTestList = new ArrayList<KeyWordTest>(10);
    private List<KeyWordTest> testList = new ArrayList<KeyWordTest>(10);
    private List<KeyWordTest> inactiveTestList = new ArrayList<KeyWordTest>(10);
    private List<KeyWordTest> functionList = new ArrayList<KeyWordTest>(10);
    private ElementProvider elementProvider;
    private PageDataProvider dataProvider;
    private String siteName;
    
    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName( String siteName )
    {
        this.siteName = siteName;
    }

    private Map<String,KeyWordTest> testMap = new HashMap<String,KeyWordTest>( 20 );
    private Map<String,Class> modelMap = new HashMap<String,Class>(20);
    
    public boolean testExists( String testName )
    {
        return testMap.get( testName ) != null;
    }
    
    public void addActiveTest( KeyWordTest t )
    {
        testMap.put( t.getName(), t );
        testList.add( t );
        activeTestList.add( t );
    }
    
    public void addInactiveTest( KeyWordTest t )
    {
        testMap.put( t.getName(), t );
        testList.add( t );
        inactiveTestList.add( t );
    }
    
    public void addFunction( KeyWordTest t )
    {
        testMap.put( t.getName(), t );
        functionList.add( t );
    }
    
    public void addPageModel( String pageName, Class pageImpl )
    {
        modelMap.put( pageName, pageImpl );
    }
    
}
