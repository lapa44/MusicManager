package org.example.model;

import java.io.File;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MyFile extends File {

  private SimpleIntegerProperty number;
  private SimpleStringProperty fileName;
  private SimpleStringProperty format;


  public MyFile(String pathname) {
    super(pathname);
    String[] fullName = getName().split("\\.");
    if (fullName.length > 2) {
      this.number = new SimpleIntegerProperty(Integer.parseInt(fullName[0]));
      this.fileName = new SimpleStringProperty(fullName[1]);
      this.format = new SimpleStringProperty(fullName[2]);
    } else {
      this.number = new SimpleIntegerProperty();
      this.fileName = new SimpleStringProperty(fullName[0]);
      this.format = new SimpleStringProperty(fullName[1]);
    }

  }

  public int getNumber() {
    return number.get();
  }

  public SimpleIntegerProperty numberProperty() {
    return number;
  }

  public void setNumber(int number) {
    this.number.set(number);
  }

  public String getFileName() {
    return fileName.get();
  }

  public SimpleStringProperty fileNameProperty() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName.set(fileName);
  }

  public String getFormat() {
    return format.get();
  }

  public SimpleStringProperty formatProperty() {
    return format;
  }

  public void setFormat(String format) {
    this.format.set(format);
  }
}
