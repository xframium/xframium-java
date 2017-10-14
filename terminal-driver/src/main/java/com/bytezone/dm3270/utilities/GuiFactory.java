package com.bytezone.dm3270.utilities;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GuiFactory
{
  public HBox getHBox()
  {
    HBox hbox = new HBox(15.0D);
    
    hbox.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
    hbox.setAlignment(Pos.CENTER_LEFT);
    return hbox;
  }
  
  public VBox getVBox()
  {
    VBox vbox = new VBox(15.0D);
    
    vbox.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
    return vbox;
  }
  
  public Button getButton(String name, VBox vbox, int width)
  {
    Button button = new Button(name);
    button.setPrefWidth(width);
    vbox.getChildren().add(button);
    button.setDisable(true);
    return button;
  }
  
  public Button getButton(String name, HBox hbox, int width)
  {
    Button button = new Button(name);
    button.setPrefWidth(width);
    hbox.getChildren().add(button);
    button.setDisable(true);
    return button;
  }
  
  public TextArea getTextArea(int width)
  {
    TextArea textArea = new TextArea();
    textArea.setEditable(false);
    textArea.setFont(javafx.scene.text.Font.font("Monospaced", 12.0D));
    textArea.setPrefWidth(width);
    return textArea;
  }
  
  public Tab getTab(String name, TextArea textArea)
  {
    Tab tab = new Tab();
    tab.setText(name);
    tab.setContent(textArea);
    return tab;
  }
  
  public RadioButton getRadioButton(String text, HBox hbox, ToggleGroup group)
  {
    RadioButton button = new RadioButton(text);
    hbox.getChildren().add(button);
    button.setToggleGroup(group);
    button.setDisable(true);
    return button;
  }
  
  public RadioButton getRadioButton(String text, VBox vbox, ToggleGroup group)
  {
    RadioButton button = new RadioButton(text);
    vbox.getChildren().add(button);
    button.setToggleGroup(group);
    button.setDisable(true);
    return button;
  }
}
