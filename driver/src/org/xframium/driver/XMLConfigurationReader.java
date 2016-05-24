package org.xframium.driver;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import org.openqa.selenium.WebDriver;
import org.xframium.Initializable;
import org.xframium.application.ApplicationRegistry;
import org.xframium.application.CSVApplicationProvider;
import org.xframium.application.ExcelApplicationProvider;
import org.xframium.application.XMLApplicationProvider;
import org.xframium.application.xsd.ObjectFactory;
import org.xframium.artifact.ArtifactType;
import org.xframium.content.ContentManager;
import org.xframium.content.provider.ExcelContentProvider;
import org.xframium.content.provider.SQLContentProvider;
import org.xframium.content.provider.XMLContentProvider;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CSVCloudProvider;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.cloud.ExcelCloudProvider;
import org.xframium.device.cloud.SQLCloudProvider;
import org.xframium.device.cloud.XMLCloudProvider;
import org.xframium.device.data.CSVDataProvider;
import org.xframium.device.data.DataManager;
import org.xframium.device.data.DataProvider.DriverType;
import org.xframium.device.data.ExcelDataProvider;
import org.xframium.device.data.NamedDataProvider;
import org.xframium.device.data.SQLDataProvider;
import org.xframium.device.data.XMLDataProvider;
import org.xframium.device.data.perfectoMobile.AvailableHandsetValidator;
import org.xframium.device.data.perfectoMobile.PerfectoMobileDataProvider;
import org.xframium.device.data.perfectoMobile.ReservedHandsetValidator;
import org.xframium.device.logging.ThreadedFileHandler;
import org.xframium.device.ng.AbstractSeleniumTest;
import org.xframium.device.property.PropertyAdapter;
import org.xframium.driver.xsd.XFramiumRoot;
import org.xframium.gesture.GestureManager;
import org.xframium.gesture.device.action.DeviceActionManager;
import org.xframium.gesture.device.action.spi.perfecto.PerfectoDeviceActionFactory;
import org.xframium.gesture.factory.spi.PerfectoGestureFactory;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.rest.bean.factory.BeanManager;
import org.xframium.integrations.rest.bean.factory.XMLBeanFactory;
import org.xframium.page.PageManager;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.data.provider.ExcelPageDataProvider;
import org.xframium.page.data.provider.SQLPageDataProvider;
import org.xframium.page.data.provider.XMLPageDataProvider;
import org.xframium.page.element.provider.CSVElementProvider;
import org.xframium.page.element.provider.ExcelElementProvider;
import org.xframium.page.element.provider.SQLElementProvider;
import org.xframium.page.element.provider.XMLElementProvider;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.provider.XMLKeyWordProvider;
import org.xframium.spi.CSVRunListener;
import org.xframium.spi.RunDetails;
import org.xframium.utility.SeleniumSessionManager;

public class XMLConfigurationReader extends AbstractConfigurationReader
{
    private static final String[] CLOUD = new String[] { "cloudRegistry.provider", "cloudRegistry.fileName", "cloudRegistry.cloudUnderTest" };
    private static final String[] OPT_CLOUD = new String[] { "cloudRegistry.query" };
    private static final String[] APP = new String[] { "applicationRegistry.provider", "applicationRegistry.fileName", "applicationRegistry.applicationUnderTest" };
    private static final String[] ARTIFACT = new String[] { "artifactProducer.parentFolder" };
    private static final String[] PAGE = new String[] { "pageManagement.siteName", "pageManagement.provider", "pageManagement.fileName" };
    private static final String[] OPT_PAGE = new String[] { "pageManagement.query" };
    private static final String[] DATA = new String[] { "pageManagement.pageData.provider", "pageManagement.pageData.fileName" };
    private static final String[] OPT_DATA = new String[] { "pageManagement.pageData.query" };
    private static final String[] CONTENT = new String[] { "pageManagement.content.provider", "pageManagement.content.fileName" };
    private static final String[] OPT_CONTENT = new String[] { "pageManagement.content.query" };
    private static final String[] DEVICE = new String[] { "deviceManagement.provider", "deviceManagement.driverType" };
    private static final String[] OPT_DEVICE = new String[] { "deviceManagement.device.query", "deviceManagement.capability.query" };
    private static final String[] DRIVER = new String[] { "driver.frameworkType", "driver.configName" };
    private static final String[] JDBC = new String[] { "jdbc.username", "jdbc.password", "jdbc.url", "jdbc.driverClassName" };
    
    private XFramiumRoot xRoot;

    @Override
    protected boolean readFile( File configFile )
    {
        try
        {
            configFolder = configFile.getParentFile();
            JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class );
            Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<?> rootElement = (JAXBElement<?>) u.unmarshal( new FileInputStream( configFile ) );

            xRoot = (XFramiumRoot) rootElement.getValue();
            return true;
        }
        catch ( Exception e )
        {
            log.fatal( "Error reading CSV Element File", e );
            return false;
        }
    }

    @Override
    protected boolean configureCloud()
    {
        if ( xRoot.getCloud().getFileName() != null && !xRoot.getCloud().getFileName().isEmpty() )
        {
            if ( xRoot.getCloud().getFileName().toLowerCase().endsWith( ".xml" ) )
                CloudRegistry.instance().setCloudProvider( new XMLCloudProvider( findFile( configFolder, new File( xRoot.getCloud().getFileName() ) ) ) );
            else if ( xRoot.getCloud().getFileName().toLowerCase().endsWith( ".xml" ) )
        }
    }

    @Override
    protected boolean configureApplication()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean configureThirdParty()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean configureArtifacts()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean configurePageManagement()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean configureData()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean configureContent()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean configureDevice()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean configurePropertyAdapters()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean configureDriver()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean executeTest() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

}
