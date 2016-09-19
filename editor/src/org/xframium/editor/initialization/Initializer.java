package org.xframium.editor.initialization;

import org.openqa.selenium.Capabilities;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.driver.XMLConfigurationReader;
import org.xframium.driver.container.ApplicationContainer;
import org.xframium.driver.container.CloudContainer;
import org.xframium.driver.container.DeviceContainer;
import org.xframium.page.BY;
import org.xframium.page.Page;
import org.xframium.page.PageContainer;
import org.xframium.page.data.PageData;
import org.xframium.page.data.provider.XMLPageDataProvider;
import org.xframium.page.element.SeleniumElement;
import org.xframium.page.element.provider.XMLElementProvider;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.KeyWordToken;
import org.xframium.page.keyWord.provider.SuiteContainer;
import org.xframium.page.keyWord.spi.KeyWordPageImpl;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.page.keyWord.step.spi.KWSClick;
import org.xframium.spi.Device;
import com.xframium.serialization.SerializationManager;
import com.xframium.serialization.json.ReflectionSerializer;
import com.xframium.serialization.xml.MapSerializer;

public class Initializer 
{
	private static final Initializer singleton = new Initializer();
	private boolean init = false;
	
	private Initializer()
	{
		
	}
	
	public static Initializer instance()
	{
		return singleton;
	}
	
	public void initialize()
	{
		if ( init )
			return;
		
		SerializationManager.instance().setDefaultAdapter( SerializationManager.JSON_SERIALIZATION );
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
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( XMLPageDataProvider.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( SuiteContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( ApplicationContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( CloudContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( DeviceContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( ApplicationDescriptor.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( Device.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( CloudDescriptor.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( XMLConfigurationReader.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( PageContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( BY.class, new ReflectionSerializer() );
        
	}
	
}
