package com.bytezone.dm3270.utilities;

import java.util.prefs.Preferences;
import javafx.stage.Stage;


public class WindowSaver
{
  private final Preferences prefs;
  private final Stage stage;
  private final String key;
  
  public WindowSaver(Preferences prefs, Stage stage, String key)
  {
    this.prefs = prefs;
    this.stage = stage;
    this.key = key;
  }
  
  public void saveWindow()
  {
    this.prefs.putDouble(this.key + "X", this.stage.getX());
    this.prefs.putDouble(this.key + "Y", this.stage.getY());
    this.prefs.putDouble(this.key + "Height", this.stage.getHeight());
    this.prefs.putDouble(this.key + "Width", this.stage.getWidth());
  }
  
  public boolean restoreWindow()
  {
    Double x = Double.valueOf(this.prefs.getDouble(this.key + "X", -1.0D));
    Double y = Double.valueOf(this.prefs.getDouble(this.key + "Y", -1.0D));
    Double height = Double.valueOf(this.prefs.getDouble(this.key + "Height", -1.0D));
    Double width = Double.valueOf(this.prefs.getDouble(this.key + "Width", -1.0D));
    
    if (width.doubleValue() < 0.0D)
    {
      this.stage.centerOnScreen();
      return false;
    }
    
    this.stage.setX(x.doubleValue());
    this.stage.setY(y.doubleValue());
    this.stage.setHeight(height.doubleValue());
    this.stage.setWidth(width.doubleValue());
    
    return true;
  }
}
