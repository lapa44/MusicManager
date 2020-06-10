package org.example.model;

import java.io.File;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MyFile extends File {

  private SimpleIntegerProperty number;
  private SimpleStringProperty fileName;
  private SimpleStringProperty format;

  private final String NUMBER_REGEX = "^\\d+\\.";


  public MyFile(String pathname) {
    super(pathname);
    String fullName = getName();
    String[] splitFullName = fullName.split("\\.");

    if (pathname.matches(NUMBER_REGEX)) {
      this.number = new SimpleIntegerProperty(Integer.parseInt(splitFullName[0]));
      this.fileName = new SimpleStringProperty(fullName.substring(splitFullName[0].length(),
          fullName.length() - splitFullName[splitFullName.length - 1].length() - 1));
    } else {
      this.number = new SimpleIntegerProperty();
      this.fileName = new SimpleStringProperty(fullName.substring(0,
          fullName.length() - splitFullName[splitFullName.length - 1].length() - 1));
    }
    this.format = new SimpleStringProperty(splitFullName[splitFullName.length - 1]);
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
