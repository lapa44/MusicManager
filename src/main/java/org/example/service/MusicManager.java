package org.example.service;

import java.io.File;
import javafx.collections.ObservableList;
import org.example.model.MyFile;

public class MusicManager {

  public ObservableList<MyFile> randomizePrefixes(ObservableList<MyFile> files) {
    return null;
  }

  public ObservableList<MyFile> abortChanges(ObservableList<MyFile> files) {
    for (MyFile file : files) {
      String[] decodedName = decodeName(file);
      if (decodedName.length > 2) {
        file.setNumber(Integer.parseInt(decodedName[0]));
        file.setFileName(decodedName[1]);
      } else {
        file.setNumber(0);
        file.setFileName(decodedName[0]);
      }
    }
    return files;
  }

  private String[] decodeName(File file) {
    return file.getName().split("\\.");
  }
}
