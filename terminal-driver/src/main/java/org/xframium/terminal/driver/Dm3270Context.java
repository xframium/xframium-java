package org.xframium.terminal.driver;

import java.awt.Robot;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.util.*;
import java.io.*;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import com.sun.javafx.scene.input.KeyCodeMap;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

import org.apache.commons.logging.*;

import com.bytezone.dm3270.application.*;
import com.bytezone.dm3270.display.*;
import com.bytezone.dm3270.utilities.Dm3270Utility;
import org.xframium.terminal.driver.util.SceneRobot;

public class Dm3270Context
{
    //
    // Class Data
    //

    private static Log log = LogFactory.getLog( Tn3270TerminalDriver.class );
    private static Dm3270Console console;
    private static SceneRobot robot = null;

    //
    // Instance Data
    //
    
    private Dm3270Site theSite;
    private boolean running = true;
    private static Hashtable KEY_CODES = new Hashtable();

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
        JavaFxRunnable worker = new JavaFxRunnable()
            {
                public void myWork()
                    throws Exception
                {
                    setValue( new Integer(console.getScreen().getScreenCursor().getLocation()) );
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
                public void myWork()
                    throws Exception
                {
                    console.getScreen().getScreenCursor().moveTo( location );
                }
            };

        Platform.runLater( worker );
        worker.doWait();
    }

    public void closeTerminal()
    {
        JavaFxRunnable worker = new JavaFxRunnable()
            {
                public void myWork()
                    throws Exception
                {
                    console.getConsolePane().disconnect();
                }
            };

        Platform.runLater( worker );
        worker.doWait();
    }

    public void openTerminal( Dm3270Site theNewSite )
    {
        JavaFxRunnable worker = new JavaFxRunnable()
            {
                public void myWork()
                    throws Exception
                {
                    theSite = theNewSite;
                    console.setModel( theSite );
                    console.getConsolePane().setSite( theSite );
                    console.getConsolePane().connect();
                }
            };

        Platform.runLater( worker );
        worker.doWait();
    }

    public void sendChars( CharSequence charSequence )
    {
        JavaFxRunnable worker = new JavaFxRunnable()
            {
                public void myWork()
                    throws Exception
                {
                    for( int i = 0; i < charSequence.length(); ++i  )
                    {
                        char ch = charSequence.charAt(i);
                        
                        if (Character.isUpperCase(ch)) {
                            robot.keyPress(KeyCode.SHIFT);
                        }

                        KeyCode code = KeyCodeMap.valueOf(KeyEvent.getExtendedKeyCodeForChar( (int) ch ));

                        robot.delay(40);

                        console.getScreen().getScreenCursor().typeChar( (byte) Dm3270Utility.asc2ebc[ch] );
                        
                        if (Character.isUpperCase(ch)) {
                            robot.keyRelease(KeyCode.SHIFT);
                        }
                    }
                }
            };

        Platform.runLater( worker );
        worker.doWait();
    }

    public void sendKey( KeyCode keyCodePressed )
    {
        JavaFxRunnable worker = new JavaFxRunnable()
            {
                public void myWork()
                    throws Exception
                {
                    robot.delay(40);
                    robot.keyPress(keyCodePressed);
                    robot.keyRelease(keyCodePressed);
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
                public void myWork()
                    throws Exception
                {
                    Optional<Field> possibleField = console.getScreen().getFieldManager().getFieldAt( location );
                    
                    if ( possibleField.isPresent() )
                    {
                        setValue( possibleField.get().getText() );
                    }
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
                public void myWork()
                    throws Exception
                {
                    setValue( console.getScreen().getScreenDimensions() );
                }
            };

        Platform.runLater( worker );
        worker.doWait();
        
        return (ScreenDimensions) worker.getValue();
    }

    public void takeSnapShot(OutputStream output)
    {
        JavaFxRunnable worker = new JavaFxRunnable()
            {
                public void myWork()
                    throws Exception
                {
                    Scene scene = console.getConsoleScene();
                    
                    WritableImage writableImage = 
                        new WritableImage((int)scene.getWidth(), (int)scene.getHeight());
                    scene.snapshot(writableImage);
         
                    try
                    {
                        ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", output);
                    }
                    catch (IOException ex)
                    {
                        log.error( "Snapshot failed with: ", ex );
                    }
                }
            };

        Platform.runLater( worker );
        worker.doWait();
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
                    //
                    // if we have no console reference, we need to initialize the UI
                    //

                    if ( console == null )
                    {
                        Dm3270Console.doIt( _this, theSite );
                    }

                    //
                    // otherwise, we'll disconnect from the cirrent site and connect to a new one
                    //

                    else
                    {
                        if ( console.getConsolePane().isConnected() )
                        {
                            closeTerminal();
                        }
                        
                        openTerminal( theSite );
                    }
                }
            };
        
        new Thread( launcher, "Launch Emulator" ).start();

        Dm3270Console.waitForStartup();

        if ( robot == null )
        {
            try
            {
                robot = console.getRobot();
            }
            catch( Exception e)
            {
                log.error( "Robot init failed with: ", e );
            }
        }
    }

    private abstract class JavaFxRunnable
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
                    try { monitor.wait(5000); }
                    catch( Exception e) {}
                }
            }
        }

        private void doNotify()
        {
            synchronized( monitor )
            {
                notified = true;
                monitor.notify();
            }
        }

        public void run()
        {
            try
            {
                myWork();
            }
            catch( Exception e )
            {
                log.error( "JavaFX action failed with: ", e );
            }
            finally
            {
                doNotify();
            }
        }

        public abstract void myWork()
            throws Exception;

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
