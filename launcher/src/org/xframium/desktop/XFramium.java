package org.xframium.desktop;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import org.xframium.application.ApplicationRegistry;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.factory.DriverManager;
import org.xframium.driver.ConfigurationReader;
import org.xframium.driver.TXTConfigurationReader;
import org.xframium.driver.TestDriver;
import org.xframium.driver.XMLConfigurationReader;
import org.xframium.spi.Device;
import org.xframium.spi.RunListener;


public class XFramium extends JFrame implements RunListener, ActionListener
{
    private ImageIcon failIcon = null;
    private ImageIcon successIcon = null;
    
    private JTextField driverFile;
    private JButton browseDriver;
    private JButton executeTest;
    private JLabel cloud;
    private JLabel application;
    private JLabel device;
    private JLabel test;
    private JLabel testType;

    private JList<XFramiumEntry> activeList;
    private DefaultListModel<XFramiumEntry> activeModel = new DefaultListModel<XFramiumEntry>();

    private JList<ListEntry> completeList;
    private DefaultListModel<ListEntry> completeModel = new DefaultListModel<ListEntry>();

    private ConfigurationReader configReader = null;
    /**
     * 
     */
    private static final long serialVersionUID = -7819774710004501717L;

    public XFramium()
    {
       
        System.setProperty( "X_DEBUGGER", "true" );
        try
        {
            failIcon = new ImageIcon( new URL( "http://www.xframium.org/fail.png" ) );
            successIcon = new ImageIcon( new URL( "http://www.xframium.org/success.png" ) );
            
            setTitle( "xFramium Test Execution" );
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
            DeviceManager.instance().addRunListener( this );
            setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            initPanel();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public static void main( String[] args )
    {
        new XFramium().setVisible( true );
    }

    private JPanel createExecutionPanel()
    {
        JPanel executionPanel = new JPanel( new GridBagLayout() );

        activeList = new JList<XFramiumEntry>( activeModel );
        completeList = new JList<ListEntry>( completeModel );
        completeList.setCellRenderer( new ListEntryCellRenderer() );

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;

        c.insets = new Insets( 10, 10, 10, 10 );
        executionPanel.add( new JScrollPane( activeList ), c );

        c.gridx++;
        executionPanel.add( new JScrollPane( completeList ), c );
        return executionPanel;
    }

    private void initPanel()
    {
        setSize( 1024, 768 );
        driverFile = new JTextField();
        browseDriver = new JButton( "Browse..." );
        browseDriver.addActionListener( this );
        cloud = new JLabel( "" );
        application = new JLabel( "" );
        device = new JLabel( "" );
        test = new JLabel( "" );
        testType = new JLabel( "" );
        executeTest = new JButton( "Execute Tests" );
        executeTest.addActionListener( this );
        executeTest.setEnabled( false );

        setLayout( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;

        c.insets = new Insets( 2, 2, 2, 2 );
        c.anchor = GridBagConstraints.EAST;
        add( new JLabel( "Driver File: " ), c );

        c.gridx++;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        add( driverFile, c );

        c.gridx++;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        add( browseDriver, c );

        c.gridx = 1;
        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        add( new JLabel( "Cloud: " ), c );
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        add( cloud, c );

        c.gridx = 1;
        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        add( new JLabel( "Application: " ), c );
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        add( application, c );

        c.gridx = 1;
        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        add( new JLabel( "Application Type: " ), c );
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        add( testType, c );

        c.gridx = 1;
        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        add( new JLabel( "Device Method: " ), c );
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        add( device, c );

        c.gridx = 1;
        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        add( new JLabel( "Test Case Definition: " ), c );
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        add( test, c );

        c.gridy++;
        c.gridwidth = 3;
        c.gridx = 1;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        add( createExecutionPanel(), c );

        c.gridy++;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.weighty = 0.0;
        c.insets = new Insets( 2, 2, 10, 2 );
        add( executeTest, c );
    }

    @Override
    public boolean validateDevice( Device currentDevice, String runKey )
    {
        // TODO Auto-generated method stub
        return true;
    }
    
    @Override
    public boolean beforeRun( final Device currentDevice, final String runKey )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                activeModel.addElement( new XFramiumEntry( currentDevice, runKey, false ) );
            }
        } );
        
        return true;
    }

    @Override
    public void afterRun(Device currentDevice, String runKey, boolean successful, int stepsPassed, int stepsFailed, int stepsIgnored, long startTime, long stopTime, int scriptFailures, int configFailures, int applicationFailures, int cloudFailures)
    {
        for ( int i = 0; i < activeModel.getSize(); i++ )
        {
            if ( activeModel.getElementAt( i ).getCurrentDevice().getKey().equals( currentDevice.getKey() ) && activeModel.getElementAt( i ).getRunKey().equals( runKey ) )
            {
                activeModel.removeElementAt( i );
                break;
            }
        }

        completeModel.addElement( new ListEntry( new XFramiumEntry( currentDevice, runKey, successful ), successful ? successIcon : failIcon ) );
    }

    class XFramiumEntry
    {
        public XFramiumEntry( Device currentDevice, String runKey, boolean success )
        {
            this.currentDevice = currentDevice;
            this.runKey = runKey;
            this.success = success;
        }

        public Device getCurrentDevice()
        {
            return currentDevice;
        }

        public void setCurrentDevice( Device currentDevice )
        {
            this.currentDevice = currentDevice;
        }

        public String getRunKey()
        {
            return runKey;
        }

        public void setRunKey( String runKey )
        {
            this.runKey = runKey;
        }

        public boolean isSuccess()
        {
            return success;
        }

        public void setSuccess( boolean success )
        {
            this.success = success;
        }

        public String toString()
        {
            return currentDevice.getManufacturer() + " " + currentDevice.getModel() + "(" + currentDevice.getKey() + ") - " + runKey;
        }

        private Device currentDevice;
        private String runKey;
        private boolean success;

    }

    class ListEntry
    {
        private XFramiumEntry value;
        private ImageIcon icon;

        public ListEntry( XFramiumEntry value, ImageIcon icon )
        {
            this.value = value;
            this.icon = icon;
        }

        public XFramiumEntry getValue()
        {
            return value;
        }

        public ImageIcon getIcon()
        {
            return icon;
        }

        public String toString()
        {
            return value.toString();
        }
    }

    class ListEntryCellRenderer extends JLabel implements ListCellRenderer<ListEntry>
    {
        /**
         * 
         */
        private static final long serialVersionUID = 676205314421651620L;

        @Override
        public Component getListCellRendererComponent( JList<? extends ListEntry> list, ListEntry value, int index, boolean isSelected, boolean cellHasFocus )
        {
            ListEntry entry = (ListEntry) value;

            setText( value.toString() );
            setIcon( entry.getIcon() );

            if ( isSelected )
            {
                setBackground( list.getSelectionBackground() );
                setForeground( list.getSelectionForeground() );
            }
            else
            {
                setBackground( list.getBackground() );
                setForeground( list.getForeground() );
            }

            setEnabled( list.isEnabled() );
            setFont( list.getFont() );
            setOpaque( true );

            return this;
        }
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        if ( e.getSource().equals( executeTest ) )
        {
            new Thread( new Runnable()
            {
                @Override
                public void run()
                {
                    if ( configReader != null )
                        configReader.executeTest();
                }
            }).start();
            
        }
        else if ( e.getSource().equals( browseDriver ) )
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
            fileChooser.setFileFilter( new FileFilter()
            {

                @Override
                public String getDescription()
                {
                    return "Locate your XFramium Driver Configuration";
                }

                @Override
                public boolean accept( File f )
                {
                    if ( f.getName().endsWith( ".xml" ) || f.getName().endsWith( ".txt" ) || f.isDirectory() )
                        return true;

                    return false;
                }
            } );

            int fileResponse = fileChooser.showOpenDialog( this );

            if ( fileResponse == JFileChooser.APPROVE_OPTION )
            {
                driverFile.setText( fileChooser.getSelectedFile().getAbsolutePath() );
                try
                {
                    
                    if ( driverFile.getText().toLowerCase().endsWith( ".txt" ) )
                    {
                        configReader = new TXTConfigurationReader();
                    }
                    else if (driverFile.getText().toLowerCase().endsWith( ".xml" ) )
                    {
                        configReader = new XMLConfigurationReader();
                    }
                    
                    configReader.readConfiguration( fileChooser.getSelectedFile(), false );
                    
                    
                    cloud.setText( CloudRegistry.instance().getCloud().getHostName() + "  (" + CloudRegistry.instance().getCloud().getName() + ")" );
                    application.setText( ApplicationRegistry.instance().getAUT().getName() );
                    switch ( DeviceManager.instance().getDriverType() )
                    {
                        case APPIUM:
                            testType.setText( "NATIVE (Appium)" );
                            break;

                        case WEB:
                            testType.setText( "Website" );
                            break;

                        default:
                            testType.setText( DeviceManager.instance().getDriverType().toString() );
                    }

                    test.setText( "XML" );
                    device.setText( DeviceManager.instance().getDevices().size() > 1 ? "Multiple" : DeviceManager.instance().getDevices().get( 0 ).getDeviceName() );
                    executeTest.setEnabled( true );

                }
                
                catch ( Exception ex )
                {
                    ex.printStackTrace();
                }
            }
            else
                executeTest.setEnabled( false );
        }
    }

    @Override
    public void skipRun( Device currentDevice, String runKey )
    {
        // TODO Auto-generated method stub
        
    }
}
