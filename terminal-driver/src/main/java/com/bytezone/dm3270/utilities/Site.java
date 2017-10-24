package com.bytezone.dm3270.utilities;

import java.io.PrintStream;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class Site
{
  public final TextField name = new TextField();
  public final TextField url = new TextField();
  public final TextField port = new TextField();
  public final CheckBox extended = new CheckBox();
  public final TextField model = new TextField();
  public final CheckBox plugins = new CheckBox();
  public final TextField folder = new TextField();
  
  private final TextField[] textFieldList = { this.name, this.url, this.port, null, this.model, null, this.folder };
  
  private final CheckBox[] checkBoxFieldList = { null, null, null, this.extended, null, this.plugins, null };
  
    public Site(String name, String url, int port, boolean extended, int model, boolean plugins, String folder)
    {
        this.name.setText(name);
        this.url.setText(url);
        this.port.setText("" + port);
        this.extended.setSelected(extended);
        this.model.setText("" + model);
        this.plugins.setSelected(plugins);
        this.folder.setText(folder);
    }

    public Site()
    {}
  
  public String getName()
  {
    return this.name.getText();
  }
  
  public String getURL()
  {
    return this.url.getText();
  }
  
  public int getPort()
  {
      int portValue = 23;
      
      try
      {
          portValue = Integer.parseInt(this.port.getText());
          if (portValue <= 0)
          {
              System.out.println("Invalid port value: " + this.port.getText());
              this.port.setText("23");
              portValue = 23;
          }
      }
      catch (NumberFormatException e)
      {
          System.out.println("Invalid port value: " + this.port.getText());
          this.port.setText("23");
          portValue = 23;
      }
      
    return portValue;
  }
  

  public boolean getExtended()
  {
    return this.extended.isSelected();
  }
  
  public int getModel()
  {
      int modelValue = 2;
          
      try
      {
          modelValue = Integer.parseInt(this.model.getText());
          if ((modelValue < 2) || (modelValue > 5))
          {
              System.out.println("Invalid model value: " + this.model.getText());
              this.model.setText("2");
              modelValue = 2;
          }
      }
      catch (NumberFormatException e)
      {
          System.out.println("Invalid model value: " + this.model.getText());
          this.model.setText("2");
          modelValue = 2;
      }
      
      return modelValue;
  }
  

  public boolean getPlugins()
  {
    return this.plugins.isSelected();
  }
  
  public String getFolder()
  {
    return this.folder.getText();
  }
  
  public TextField getTextField(int index)
  {
    return this.textFieldList[index];
  }
  
  public CheckBox getCheckBoxField(int index)
  {
    return this.checkBoxFieldList[index];
  }
  

  public String toString()
  {
    return String.format("Site [name=%s, url=%s, port=%d, folder=%s]", new Object[] { getName(), 
      getURL(), Integer.valueOf(getPort()), getFolder() });
  }
}
