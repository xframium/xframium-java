package org.xframium.terminal.driver;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;

import org.openqa.selenium.*;
import org.apache.commons.jxpath.*;

import org.xframium.terminal.driver.screen.model.*;
import org.xframium.terminal.driver.util.*;

public class Tn3270TerminalDriver
    implements WebDriver,
               SearchContext,
               org.openqa.selenium.internal.FindsByXPath
{
    //
    // Class Data
    //

    private static final String RETURN = "\n";
    
    //
    // Instance Data
    //
    
    private StartupDetails startup;
    private Dm3270Context context;
    private StartupDetails details;
    private Application application;
    private JXPathContext applicationRdeader;
    private ConsumedApplication workingApp;
    private ConsumedScreen currentScreen;
    
    public Tn3270TerminalDriver( StartupDetails startup )
    {
    	this.startup = startup;
        Dm3270Context.Dm3270Site site = new Dm3270Context.Dm3270Site( "Tn3270TerminalDriver",
                                                                      startup.getHost(),
                                                                      startup.getPort(),
                                                                      true,
                                                                      startup.getTerminalType(),
                                                                      false,
                                                                      "." );
        details = startup;

        context = new Dm3270Context( site );
        Utilities.setscreenDimensions( context.getScreenDimensions() );
        
        application = loadApplication( startup.getPathToAppFile() );
        applicationRdeader = JXPathContext.newContext( application );
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
        File appFileLocation = new File( startup.getPathToAppFile() );

        return appFileLocation.toURI().toString();
    }
  
    public String getTitle()
    {
        return null;
    }
  
    public List<WebElement> findElements(By paramBy)
    {
        if ( !( paramBy instanceof By.ByXPath ))
        {
            reportUnsupportedUsage( "Only element selection by XPath is available" );
        }
        
        return ((By.ByXPath) paramBy).findElements( this );
    }

    public List<WebElement> findElementsByXPath(String paramString)
    {
        List<WebElement> rtn = new ArrayList<WebElement>();

        try
        {
            Iterator results = applicationRdeader.iterate( paramString );
            while( results.hasNext() )
            {
                rtn.add( new MyWebElement( results.next() ));
            }
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Find element by xpath: " + paramString + " failed with: ", e );
        }

        return rtn;
    }
  
    public WebElement findElement(By paramBy)
    {
        if ( !( paramBy instanceof By.ByXPath ))
        {
            reportUnsupportedUsage( "Only element selection by XPath is available" );
        }

        return ((By.ByXPath) paramBy).findElement( this );
    }

    public WebElement findElementByXPath(String paramString)
    {
        WebElement rtn = null;

        try
        {
            Object result = applicationRdeader.getValue( paramString );

            rtn = new MyWebElement( result );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Find element by xpath: " + paramString + " failed with: ", e );
        }

        return rtn;
    }
  
    public String getPageSource()
    {
        return null;
    }
  
    public void close()
    {
    	quit();
    }
  
    public void quit()
    {
        System.exit(0);
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
        reportUnsupportedUsage( "Alternate frame movement is not supported" );
        
        return null;
    }
  
    public Navigation navigate()
    {
        reportUnsupportedUsage( "Navigation is not supported" );
        
        return null;
    }
  
    public Options manage()
    {
        reportUnsupportedUsage( "Cookie operations are not supported" );
        
        return null;
    }

    //
    // Data Types
    //

    public static class StartupDetails
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
        private Field field = null;
        private Link link = null;
        private Action action = null;
        private Location location = null;
        
        public MyWebElement( Object context )
        {
            if ( context instanceof Action )
            {
                action = (Action) context;
                location = action.getLocation();
            }
            else if ( context instanceof Field )
            {
                field = (Field) context;
                location = field.getLocation();
            }
            else if ( context instanceof Link )
            {
                link = (Link) context;
                location = link.getLocation();
            }
            else
            {
                throw new java.lang.reflect.MalformedParametersException( "Element must be of type Action, Field or Link" );
            }
        }

        //
        // Impl
        //

        public void click()
        {
            context.setLocation( Utilities.asTerminalLocation( location ));
            context.sendKey( javafx.scene.input.KeyCode.ENTER );
        }
  
        public void submit()
        {
            click();
        }
  
        public void sendKeys(CharSequence... paramVarArgs)
        {
            context.setLocation( Utilities.asTerminalLocation( location ));

            for( CharSequence seq : paramVarArgs )
            {
                context.sendChars( seq );
            }
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
            return context.getTextFromField( Utilities.asTerminalLocation( location ));
        }
  
        public List<WebElement> findElements(By paramBy)
        {
            return Tn3270TerminalDriver.this.findElements( paramBy );
        }
  
        public WebElement findElement(By paramBy)
        {
            return Tn3270TerminalDriver.this.findElement( paramBy );
        }
  
        public boolean isDisplayed()
        {
            return false;
        }
  
        public Point getLocation()
        {
            return Utilities.asApplicationLocation( context.getCurrentLocation() );
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

            Iterator<Action> actions = screen.getAction().iterator();
            while( actions.hasNext() )
            {
                Action action = actions.next();
                actionsByName.put( action.getName(), action );
            }

            Iterator<Field> fields = screen.getField().iterator();
            while( fields.hasNext() )
            {
                Field field = fields.next();
                fieldsByName.put( field.getName(), field );
            }

            Iterator<Link> links = screen.getLink().iterator();
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
            
            Iterator<Screen> screens = application.getScreen().iterator();
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

            reader.close();
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

