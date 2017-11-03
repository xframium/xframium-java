package com.bytezone.dm3270.utilities;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public abstract class DefaultTable<T> extends javafx.scene.control.TableView<T>
{
  public static enum Justification
  {
    LEFT,  CENTER,  RIGHT;
  }
  
  public DefaultTable()
  {
    setStyle("-fx-font-size: 12; -fx-font-family: Monospaced");
    setFixedCellSize(20.0D);
  }
  


  protected void addColumnString(String heading, int width, Justification justification, String propertyName)
  {
    TableColumn<T, String> column = new TableColumn(heading);
    column.setPrefWidth(width);
    column.setCellValueFactory(new PropertyValueFactory(propertyName));
    getColumns().add(column);
    
    if (justification == Justification.CENTER) {
      column.setStyle("-fx-alignment: CENTER;");
    }
  }
  
  protected void addColumnNumber(String heading, int width, String propertyName)
  {
    TableColumn<T, Number> column = new TableColumn(heading);
    column.setPrefWidth(width);
    column.setCellValueFactory(new PropertyValueFactory(propertyName));
    getColumns().add(column);
    column.setStyle("-fx-alignment: CENTER-RIGHT;");
  }
}
