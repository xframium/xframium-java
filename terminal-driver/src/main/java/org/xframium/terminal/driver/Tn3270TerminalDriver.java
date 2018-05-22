package org.xframium.terminal.driver;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;

import org.openqa.selenium.*;
import org.apache.commons.jxpath.*;
import org.apache.commons.logging.*;

import org.xframium.terminal.driver.screen.model.*;
import org.xframium.terminal.driver.util.*;

public class Tn3270TerminalDriver
    implements WebDriver,
               SearchContext,
               TakesScreenshot,
               org.openqa.selenium.internal.FindsByXPath
{
    //
    // Class Data
    //

    private static final String RETURN = "\n";
    private static Log log = LogFactory.getLog( Tn3270TerminalDriver.class );
    
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
        Utilities.setTerminalVisibility( startup.getDisplayEmulator() );

        context = new Dm3270Context( site );
        Utilities.setscreenDimensions( context.getScreenDimensions() );
        
        application = loadApplication( startup.getPathToAppFile(), startup.getAppName() );
        applicationRdeader = JXPathContext.newContext( application );
        workingApp = new ConsumedApplication( application );
        currentScreen = workingApp.getScreenByName( startup.getStartScreen() );
    }
    
    //
    // WebDriver Implementation
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

    //
    // SearchContext Implementation
    //
  
    public List<WebElement> findElements(By paramBy)
    {
        if ( !( paramBy instanceof By.ByXPath ))
        {
            reportUnsupportedUsage( "Only element selection by XPath is available" );
        }
        
        return ((By.ByXPath) paramBy).findElements( this );
    }

    public WebElement findElement(By paramBy)
    {
        if ( !( paramBy instanceof By.ByXPath ))
        {
            reportUnsupportedUsage( "Only element selection by XPath is available" );
        }

        return ((By.ByXPath) paramBy).findElement( this );
    }

    //
    // TakesScreenshot Implementation
    //

    public <X> X getScreenshotAs(OutputType<X> target)
        throws WebDriverException
    {
        X rtn = null;
        OutputStream ostream = null;
        File theFile = null;
        String theFileName = null;
        ByteArrayOutputStream baos = null;
        
        try
        {
            if ( OutputType.FILE == target )
            {
                theFile = File.createTempFile(application.getName() + "-",
                                              ".png",
                                              new File( startup.getPathToImageFolder()));
                theFileName = theFile.getCanonicalPath();
                ostream = new FileOutputStream( theFile );
            }
            else if ( OutputType.BYTES == target )
            {
                baos = new ByteArrayOutputStream();
                ostream = baos;
            }
            else if ( OutputType.BASE64 == target )
            {
                baos = new ByteArrayOutputStream();
                ostream = Base64.getEncoder().wrap( baos );
            }

            context.takeSnapShot( ostream );

            ostream.flush();
            ostream.close();

            if ( OutputType.FILE == target )
            {
                rtn = (X) theFile;
            }
            else if ( OutputType.BYTES == target )
            {
                rtn = (X) baos.toByteArray();
            }
            else if ( OutputType.BASE64 == target )
            {
                rtn = (X) new String( baos.toByteArray() );
            }
        }
        catch( Exception e )
        {
            log.error( "Screenshot failed with: ", e );
        }

        if ( log.isDebugEnabled() )
            log.debug( "Screenshot saved to: " + (( theFile != null ) ? theFileName : "output buffer" ));
            
        return rtn;
    }

    //
    // FindsByXPath Implementation
    //

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
        context.closeTerminal();
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
        private String appName;
        private String pathToImageFolder;
        private boolean displayEmulator;

        public StartupDetails( String host,
                               int port,
                               int terminalType,
                               String startScreen,
                               String pathToAppFile,
                               String appName,
                               String pathToImageFolder,
                               boolean displayEmulator )
        {
            this.host = host;
            this.port = port;
            this.terminalType = terminalType;
            this.startScreen = startScreen;
            this.pathToAppFile = pathToAppFile;
            this.appName = appName;
            this.pathToImageFolder = pathToImageFolder;
            this.displayEmulator = displayEmulator;
        }

        public String getHost() { return host; }
        public int getPort() { return port; }
        public int getTerminalType() { return terminalType; }
        public String getStartScreen() { return startScreen; }
        public String getPathToAppFile() { return pathToAppFile; }
        public String getPathToImageFolder() { return pathToImageFolder; }
        public String getAppName() { return appName; }
        public boolean getDisplayEmulator() { return displayEmulator; }
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

            if ( log.isDebugEnabled() )
                log.debug( "Click on: " + dumpContext() );
        }
  
        public void submit()
        {
            context.sendKey( javafx.scene.input.KeyCode.ENTER );

            if ( log.isDebugEnabled() )
                log.debug( "Submit on: " + dumpContext() );
        }
  
        public void sendKeys(CharSequence... paramVarArgs)
        {
            context.setLocation( Utilities.asTerminalLocation( location ));

            for( CharSequence seq : paramVarArgs )
            {
                if ( !specialKeysHandled( seq ))
                {
                    context.sendChars( seq );
                }
            }

            if ( log.isDebugEnabled() )
                log.debug( "Typed in: " + dumpContext() );
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
            return Tn3270TerminalDriver.this.getScreenshotAs( arg0 );
        }

        //
        // Helpers
        //

        private String dumpContext()
        {
            StringBuilder buffer = new StringBuilder();

            buffer.append( (( field != null ) ? "Field: " + field.getName() :
                            (( link != null ) ? "Link: " + link.getName() :
                             (( action != null ) ? "Action: " + action.getName() : "??? " ))));
            buffer.append( "[ " ).append( location.getLine() ).append("/").append( location.getColumn() ).append( " ]" );

            return buffer.toString();
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

    private Application loadApplication( String pathToFile, String name )
    {
        Application rtn = null;
        Reader reader = null;

        try
        {
            File theFile = new File( pathToFile );
            reader = (( theFile.exists() ) ?
                      new FileReader( pathToFile ) :
                      new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream( pathToFile )) );
            
            JAXBContext context = JAXBContext.newInstance( ObjectFactory.class );
            Unmarshaller un = context.createUnmarshaller();

            JAXBElement<?> rootElement = (JAXBElement<?>)un.unmarshal( reader );
            RegistryRoot rRoot = (RegistryRoot)rootElement.getValue();

            Iterator<Application> apps = rRoot.getApplication().iterator();
            while(( rtn == null ) && ( apps.hasNext() ))
            {
                Application app = apps.next();

                if ( app.getName().equals( name ))
                {
                    rtn = app;
                }
            }

            reader.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        if ( rtn == null )
        {
            throw new IllegalStateException( "Application: " + name + " is not found" );
        }

        return rtn;
    }

    private void reportUnsupportedUsage( String msg )
    {
        throw new UnsupportedOperationException( msg );
    }

    private boolean specialKeysHandled( CharSequence seq )
    {
        boolean rtn = false;
        String value = seq.toString();

        switch( value )
        {
            case Tn3270SpecialKeyCodes.CTRL_C:
            {
                context.sendKeyCombination(  javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.C );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.PA1:
            {
                context.sendKeyCombination(  javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.F1 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.PA2:
            {
                context.sendKeyCombination(  javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.F2 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.PA3:
            {
                context.sendKeyCombination(  javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.F3 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F1:
            {
                context.sendKey( javafx.scene.input.KeyCode.F1 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F2:
            {
                context.sendKey( javafx.scene.input.KeyCode.F2 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F3:
            {
                context.sendKey( javafx.scene.input.KeyCode.F3 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F4:
            {
                context.sendKey( javafx.scene.input.KeyCode.F4 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F5:
            {
                context.sendKey( javafx.scene.input.KeyCode.F5 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F6:
            {
                context.sendKey( javafx.scene.input.KeyCode.F6 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F7:
            {
                context.sendKey( javafx.scene.input.KeyCode.F7 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F8:
            {
                context.sendKey( javafx.scene.input.KeyCode.F8 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F9:
            {
                context.sendKey( javafx.scene.input.KeyCode.F9 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F10:
            {
                context.sendKey( javafx.scene.input.KeyCode.F10 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F11:
            {
                context.sendKey( javafx.scene.input.KeyCode.F11 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F12:
            {
                context.sendKey( javafx.scene.input.KeyCode.F12 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F13:
            {
                context.sendKey( javafx.scene.input.KeyCode.F13 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F14:
            {
                context.sendKey( javafx.scene.input.KeyCode.F14 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F15:
            {
                context.sendKey( javafx.scene.input.KeyCode.F15 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F16:
            {
                context.sendKey( javafx.scene.input.KeyCode.F16 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F17:
            {
                context.sendKey( javafx.scene.input.KeyCode.F17 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F18:
            {
                context.sendKey( javafx.scene.input.KeyCode.F18 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F19:
            {
                context.sendKey( javafx.scene.input.KeyCode.F19 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F20:
            {
                context.sendKey( javafx.scene.input.KeyCode.F20 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F21:
            {
                context.sendKey( javafx.scene.input.KeyCode.F21 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F22:
            {
                context.sendKey( javafx.scene.input.KeyCode.F22 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F23:
            {
                context.sendKey( javafx.scene.input.KeyCode.F23 );
                rtn = true;
                break;
            }

            case Tn3270SpecialKeyCodes.F24:
            {
                context.sendKey( javafx.scene.input.KeyCode.F24 );
                rtn = true;
                break;
            }
        }

        return rtn;
    }
}

