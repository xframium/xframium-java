package org.xframium.terminal.driver;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;

import org.openqa.selenium.*;
import org.xframium.terminal.driver.screen.model.*;

public class Tn3270TerminalDriver
    implements WebDriver
{
    private Dm3270Context context;
    private StartupDetails details;
    private Application application;
    private ConsumedApplication workingApp;
    private ConsumedScreen currentScreen;
    
    public Tn3270TerminalDriver( StartupDetails startup )
    {
        Dm3270Context.Dm3270Site site = new Dm3270Context.Dm3270Site( "Tn3270TerminalDriver",
                                                                      startup.getHost(),
                                                                      startup.getPort(),
                                                                      true,
                                                                      startup.getTerminalType(),
                                                                      false,
                                                                      "." );
        details = startup;

        context = new Dm3270Context( site );
        application = loadApplication( startup.getPathToAppFile() );
        workingApp = new ConsumedApplication( application );
        currentScreen = workingApp.getScreenByName( startup.getStartScreen() );
    }
    
    //
    // Implementation
    //
    
    public void get(String paramString)
    {

    }
  
    public String getCurrentUrl()
    {
        return null;
    }
  
    public String getTitle()
    {
        return null;
    }
  
    public List<WebElement> findElements(By paramBy)
    {
        reportUnsupportedUsage( "Only single element selection is available" );
        
        return null;
    }
  
    public WebElement findElement(By paramBy)
    {
        if ( !( paramBy instanceof By.ByName ))
        {
            reportUnsupportedUsage( "Only element selection by name is available" );
        }
        
        return null;
    }
  
    public String getPageSource()
    {
        return null;
    }
  
    public void close()
    {
    	
    }
  
    public void quit()
    {

    }
  
    public Set<String> getWindowHandles()
    {
        return null;
    }
  
    public String getWindowHandle()
    {
        return null;
    }
  
    public TargetLocator switchTo()
    {
        return null;
    }
  
    public Navigation navigate()
    {
        return null;
    }
  
    public Options manage()
    {
        return null;
    }

    //
    // Data Types
    //

    public class StartupDetails
    {
        private String host;
        private int port;
        private int terminalType;
        private String startScreen;
        private String pathToAppFile;

        public StartupDetails( String host, int port, int terminalType, String startScreen, String pathToAppFile )
        {
            this.host = host;
            this.port = port;
            this.terminalType = terminalType;
            this.startScreen = startScreen;
            this.pathToAppFile = pathToAppFile;
        }

        public String getHost() { return host; }
        public int getPort() { return port; }
        public int getTerminalType() { return terminalType; }
        public String getStartScreen() { return startScreen; }
        public String getPathToAppFile() { return pathToAppFile; }
    }

    private class MyWebElement
        implements WebElement
    {
        public MyWebElement()
        {

        }

        //
        // Impl
        //

        public void click()
        {

        }
  
        public void submit()
        {

        }
  
        public void sendKeys(CharSequence... paramVarArgs)
        {

        }
  
        public void clear()
        {

        }
  
        public String getTagName()
        {
            return null;
        }
  
        public String getAttribute(String paramString)
        {
            return null;
        }
  
        public boolean isSelected()
        {
            return false;
        }
  
        public boolean isEnabled()
        {
            return false;
        }
  
        public String getText()
        {
            return null;
        }
  
        public List<WebElement> findElements(By paramBy)
        {
            return null;
        }
  
        public WebElement findElement(By paramBy)
        {
            return null;
        }
  
        public boolean isDisplayed()
        {
            return false;
        }
  
        public Point getLocation()
        {
            return null;
        }
  
        public Dimension getSize()
        {
            return null;
        }
  
        public Rectangle getRect()
        {
            return null;
        }
  
        public String getCssValue(String paramString)
        {
            return null;
        }

        @Override
        public <X> X getScreenshotAs(OutputType<X> arg0) throws WebDriverException {
            // TODO Auto-generated method stub
            return null;
        }
    }

    private class ConsumedScreen
    {
        private Screen screen;
        private HashMap<String,Action> actionsByName = new HashMap<String,Action>();
        private HashMap<String,Field> fieldsByName = new HashMap<String,Field>();
        private HashMap<String,Link> linksByName = new HashMap<String,Link>();

        public ConsumedScreen( Screen screen )
        {
            this.screen = screen;

            Iterator<Action> actions = screen.getActions().iterator();
            while( actions.hasNext() )
            {
                Action action = actions.next();
                actionsByName.put( action.getName(), action );
            }

            Iterator<Field> fields = screen.getFields().iterator();
            while( fields.hasNext() )
            {
                Field field = fields.next();
                fieldsByName.put( field.getName(), field );
            }

            Iterator<Link> links = screen.getLinks().iterator();
            while( links.hasNext() )
            {
                Link link = links.next();
                linksByName.put( link.getName(), link );
            }
        }

        public Screen getScreen()
        {
            return screen;
        }

        public Action getActionByName( String name )
        {
            return actionsByName.get( name );
        }

        public Field getFieldByName( String name )
        {
            return fieldsByName.get( name );
        }

        public Link getLinkByName( String name )
        {
            return linksByName.get( name );
        }
    }

    private class ConsumedApplication
    {
        private Application application;
        private HashMap<String,ConsumedScreen> screensByName = new HashMap<String,ConsumedScreen>();
        
        public ConsumedApplication( Application application )
        {
            this.application = application;
            
            Iterator<Screen> screens = application.getScreens().iterator();
            while( screens.hasNext() )
            {
                ConsumedScreen cScreen = new ConsumedScreen( screens.next() );
                screensByName.put( cScreen.getScreen().getName(), cScreen );
            }
        }

        public Application getApplication()
        {
            return application;
        }

        public ConsumedScreen getScreenByName( String name )
        {
            return screensByName.get( name );
        }
    }

    //
    // Helpers
    //

    private Application loadApplication( String pathToFile )
    {
        Application rtn = null;
        FileReader reader = null;

        try
        {
            reader = new FileReader( pathToFile );
            
            JAXBContext context = JAXBContext.newInstance( ObjectFactory.class );
            Unmarshaller un = context.createUnmarshaller();

            JAXBElement<?> rootElement = (JAXBElement<?>)un.unmarshal( reader );
            RegistryRoot rRoot = (RegistryRoot)rootElement.getValue();

            rtn = rRoot.getApplication().get(0);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return rtn;
    }

    private void reportUnsupportedUsage( String msg )
    {
        throw new UnsupportedOperationException( msg );
    }
}

