package org.xframium.terminal.driver;

import java.awt.Robot;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.util.*;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import com.sun.javafx.scene.input.KeyCodeMap;

import com.bytezone.dm3270.application.*;
import com.bytezone.dm3270.display.*;
import com.bytezone.dm3270.utilities.Dm3270Utility;
import org.xframium.terminal.driver.util.SceneRobot;

public class Dm3270Context
{
    private Dm3270Site theSite;
    private Dm3270Console console;
    private boolean running = true;
    private SceneRobot robot = null;
    private static Hashtable KEY_CODES = new Hashtable();

    //
    // CTOR
    //

    public Dm3270Context( Dm3270Site site )
    {
        theSite = site;

        init();
        
        try
        {
            robot = console.getRobot();
        }
        catch( Exception e)
        {
            e.printStackTrace();
        }
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
        JavaFxRunnable worker = new JavaFxRunnable()
            {
                public void run()
                {
                    setValue( new Integer(console.getScreen().getScreenCursor().getLocation()) );
                    doNotify();
                }
            };

        Platform.runLater( worker );
        worker.doWait();

        return ((Integer) worker.getValue()).intValue();
    }

    public void setLocation( int location )
    {
        JavaFxRunnable worker = new JavaFxRunnable()
            {
                public void run()
                {
                    System.out.println( "\n Before Location: " + console.getScreen().getScreenCursor().getLocation());
                    console.getScreen().getScreenCursor().moveTo( location );
                    System.out.println( "\n After Location: " + console.getScreen().getScreenCursor().getLocation());
                    doNotify();
                }
            };

        Platform.runLater( worker );
        worker.doWait();
    }

    public void sendChars( CharSequence charSequence )
    {
        JavaFxRunnable worker = new JavaFxRunnable()
            {
                public void run()
                {
                    for( int i = 0; i < charSequence.length(); ++i  )
                    {
                        char ch = charSequence.charAt(i);
                        
                        if (Character.isUpperCase(ch)) {
                            robot.keyPress(KeyCode.SHIFT);
                        }

                        KeyCode code = KeyCodeMap.valueOf(KeyEvent.getExtendedKeyCodeForChar( (int) ch ));

                        robot.delay(40);
                        //robot.keyPress(code);
                        //robot.keyRelease(code);

                        console.getScreen().getScreenCursor().typeChar( (byte) Dm3270Utility.asc2ebc[ch] );
                        
                        if (Character.isUpperCase(ch)) {
                            robot.keyRelease(KeyCode.SHIFT);
                        }
                    }
                    
                    doNotify();
                }
            };

        Platform.runLater( worker );
        worker.doWait();
    }

    public void sendKey( KeyCode keyCodePressed )
    {
        JavaFxRunnable worker = new JavaFxRunnable()
            {
                public void run()
                {
                    robot.delay(40);
                    robot.keyPress(keyCodePressed);
                    robot.keyRelease(keyCodePressed);
                        
                    doNotify();
                }
            };

        Platform.runLater( worker );
        worker.doWait();
    }

    public String getText()
    {
        int location = getCurrentLocation();

        return getTextFromField( location );
    }

    public String getTextFromField( int location )
    {
        JavaFxRunnable worker = new JavaFxRunnable()
            {
                public void run()
                {
                    Optional<Field> possibleField = console.getScreen().getFieldManager().getFieldAt( location );
                    
                    if ( possibleField.isPresent() )
                    {
                        setValue( possibleField.get().getText() );
                    }
                    doNotify();
                }
            };

        Platform.runLater( worker );
        worker.doWait();
        
        return (String) worker.getValue();
    }

    public ScreenDimensions getScreenDimensions()
    {
        JavaFxRunnable worker = new JavaFxRunnable()
            {
                public void run()
                {
                    setValue( console.getScreen().getScreenDimensions() );
                    doNotify();
                }
            };

        Platform.runLater( worker );
        worker.doWait();
        
        return (ScreenDimensions) worker.getValue();
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
        
        new Thread( launcher, "Launch Emulator" ).start();

        Dm3270Console.waitForStartup();
    }

    private class JavaFxRunnable
        implements Runnable
    {
        private Object monitor = new Object();
        private boolean notified = false;
        private Object value = null;

        public void doWait()
        {
            synchronized( monitor )
            {
                if ( !notified )
                {
                    try { monitor.wait(); }
                    catch( Exception e) {}
                }
            }
        }

        public void doNotify()
        {
            synchronized( monitor )
            {
                monitor.notify();
                notified = true;
            }
        }

        public void run()
        {}

        public Object getValue() { return value; }
        public void setValue( Object v ) { value = v; }
    }

    
    private static KeyCode findKeyCode(char character)
    {
        if (KEY_CODES.containsKey(character))
        {
            return (KeyCode) KEY_CODES.get(character);
        }
        
        KeyCode keyCode = KeyCode.getKeyCode(String.valueOf(Character
                                                            .toUpperCase(character)));
        if (keyCode != null)
        {
            return keyCode;
        }
        
        for (KeyCode code : KeyCode.values())
        {
            if ((char) code.impl_getCode() == character)
            {
                return code;
            }
        }
        
        throw new IllegalArgumentException("No KeyCode found for character: " + character);
    }
}
