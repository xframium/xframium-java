package org.xframium.terminal.driver;

import java.util.*;

import org.openqa.selenium.*;

public class Tn3270TerminalDriver
    implements WebDriver
{
    private Dm3270Context context;
    private StartupDetails details;
    
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
        return null;
    }
  
    public WebElement findElement(By paramBy)
    {
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
}

