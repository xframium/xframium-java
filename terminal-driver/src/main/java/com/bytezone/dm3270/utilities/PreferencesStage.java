package com.bytezone.dm3270.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class PreferencesStage extends javafx.stage.Stage
{
  protected final Preferences prefs;
  protected Button cancelButton;
  protected Button saveButton;
  protected List<PreferenceField> fields = new ArrayList();
  
  public static enum Type
  {
    TEXT,  NUMBER,  BOOLEAN;
  }
  
  public PreferencesStage(Preferences prefs)
  {
    this.prefs = prefs;
  }
  
  protected Node buttons()
  {
    HBox box = new HBox(10.0D);
    this.saveButton = new Button("Save");
    this.saveButton.setDefaultButton(true);
    this.cancelButton = new Button("Cancel");
    this.cancelButton.setCancelButton(true);
    this.saveButton.setPrefWidth(80.0D);
    this.cancelButton.setPrefWidth(80.0D);
    box.getChildren().addAll(new Node[] { this.cancelButton, this.saveButton });
    box.setAlignment(Pos.BASELINE_CENTER);
    box.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
    return box;
  }
  
  public VBox getHeadings()
  {
    HBox hbox = new HBox();
    hbox.setSpacing(5.0D);
    hbox.setPadding(new Insets(10.0D, 5.0D, 0.0D, 5.0D));
    
    for (int i = 0; i < this.fields.size(); i++)
    {
      PreferenceField field = (PreferenceField)this.fields.get(i);
      Label heading = new Label(field.heading);
      hbox.getChildren().add(heading);
      heading.setPrefWidth(field.width);
      if (field.type == Type.BOOLEAN) {
        heading.setAlignment(Pos.CENTER);
      }
    }
    VBox vbox = new VBox();
    vbox.setSpacing(5.0D);
    vbox.setPadding(new Insets(0.0D, 15.0D, 0.0D, 15.0D));
    vbox.getChildren().add(hbox);
    
    return vbox;
  }
  
  public class PreferenceField
  {
    public final String heading;
    public final int width;
    public final PreferencesStage.Type type;
    
    public PreferenceField(String heading, int width, PreferencesStage.Type type)
    {
      this.heading = heading;
      this.width = width;
      this.type = type;
    }
  }
}
