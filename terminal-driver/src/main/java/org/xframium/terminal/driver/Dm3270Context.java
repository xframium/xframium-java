package org.xframium.terminal.driver;

import java.util.*;

import com.bytezone.dm3270.application.*;
import com.bytezone.dm3270.display.*;
import com.bytezone.dm3270.utilities.Dm3270Utility;

public class Dm3270Context
{
    private Dm3270Site theSite;
    private Dm3270Console console;

    //
    // CTOR
    //

    public Dm3270Context( Dm3270Site site )
    {
        theSite = site;

        init();
    }

    //
    // WebDriver Support Methods
    //

    public void setConsole( Dm3270Console console )
    {
        this.console = console;
    }

    public int getCurrentLocation()
    {
        return console.getScreen().getScreenCursor().getLocation();
    }

    public void setLocation( int location )
    {
        console.getScreen().getScreenCursor().moveTo( location );
    }

    public void sendChars( String charSequence )
    {
        for( char ch : charSequence.toCharArray() )
        {
            console.getScreen().getScreenCursor().typeChar ((byte) Dm3270Utility.asc2ebc[ch]);
        }
    }

    public String getText()
    {
        int location = getCurrentLocation();

        return getTextFromField( location );
    }

    public String getTextFromField( int location )
    {
        String rtn = null;
        Optional<Field> possibleField = console.getScreen().getFieldManager().getFieldAt( location );

        if ( possibleField.isPresent() )
        {
            rtn = possibleField.get().getText();
        }
        
        return rtn;
    }

    public ScreenDimensions getScreenDimensions()
    {
        return console.getScreen().getScreenDimensions();
    }

    //
    // Data Types
    //

    public static class Dm3270Site
        implements com.bytezone.dm3270.utilities.ISite
    {
        private String name;
        private String url;
        private int port;
        private boolean extended;
        private int model;
        private boolean plugins;
        private String folder;

        public Dm3270Site( String name, String url, int port, boolean extended,
                       	   int model, boolean plugins, String folder )
        {
            this.name = name;
            this.url = url;
            this.port = port;
            this.extended = extended;
            this.model = model;
            this.plugins = plugins;
            this.folder = folder;
        }

        public String getName()
        {
            return  this.name;
        }

        public String getURL()
        {
            return this.url;
        }

        public int getPort()
        {
            return this.port;
        }

        public boolean getExtended()
        {
            return this.extended;
        }

        public int getModel()
        {
            return this.model;
        }

        public boolean getPlugins()
        {
            return this.plugins;
        }

        public String getFolder()
        {
            return this.folder;
        }
    }

    //
    // Helpers
    //

    private void init()
    {
        final Dm3270Context _this = this;
        
        Runnable launcher = new Runnable()
            {
                public void run()
                {
                    Dm3270Console.doIt( _this, theSite );
                }
            };
        
        new Thread( launcher ).run();
    }
}
