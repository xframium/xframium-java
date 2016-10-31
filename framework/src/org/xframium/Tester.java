package org.xframium;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.Capabilities;
import org.xframium.debugger.StepContainer;
import org.xframium.debugger.TestContainer;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.data.PageData;
import org.xframium.page.element.SeleniumElement;
import org.xframium.page.element.provider.XMLElementProvider;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.KeyWordToken;
import org.xframium.page.keyWord.spi.KeyWordPageImpl;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.page.keyWord.step.spi.KWSAlign;
import org.xframium.page.keyWord.step.spi.KWSClick;
import org.xframium.page.keyWord.step.spi.KWSExists;
import org.xframium.page.keyWord.step.spi.KWSVisible;
import org.xframium.page.keyWord.step.spi.KWSWait;
import org.xframium.page.keyWord.step.spi.KWSWaitFor;
import com.xframium.serialization.SerializationManager;
import com.xframium.serialization.json.ReflectionSerializer;
import com.xframium.serialization.xml.MapSerializer;

public class Tester
{
    public static void main( String[] args )
    {
        SerializationManager.instance().setDefaultAdapter( SerializationManager.JSON_SERIALIZATION );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( TestContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( StepContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( KeyWordStep.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( KWSClick.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( AbstractKeyWordStep.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( KeyWordTest.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( KeyWordPageImpl.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( Page.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( PageData.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( KeyWordParameter.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( KeyWordToken.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( SeleniumElement.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( Capabilities.class, new MapSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( XMLElementProvider.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( XMLElementProvider.class, new ReflectionSerializer() );
        
        List x = new ArrayList();
        x.add( new KWSClick() );
        x.add( new KWSAlign() );
        x.add( new KWSExists() );
        x.add( new KWSVisible() );
        x.add( new KWSWaitFor() );
        x.add( new KWSWait() );
        //x.add( new KeyWordTest( "testOne", true, "dpOne, dpTwo", "dDriver", false, "linkId", "IOS", 0, "This is a sample test case", "tag one, tagTwo", "english, french" ) );
        //x.add( new KeyWordTest( "testTwo", true, "dpOne", null, false, "linkId", "IOS", 0, "This is a sample test case with a much longer description to test how far this will stretch", "tag one, tagTwo", "english, french" ) );
        
        PageManager.instance().setSiteName( "VZW" );
        PageManager.instance().setElementProvider( new XMLElementProvider( new File( "C:\\Projects\\Git\\morelandLabs\\customerProjects\\xmlDriven\\verizon\\pageElements.xml" ) ) );
        

    }
}
