package org.example.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import org.example.model.MyFile;

public class MusicManager {

  public ObservableList<MyFile> randomizePrefixes(ObservableList<MyFile> files) {
    List<Integer> numbersPool = new ArrayList<>();
    for (int i = 1; i < files.size() + 1; i++) {
      numbersPool.add(i);
    }
    Collections.shuffle(numbersPool);
    for (int i = 0; i < files.size(); i++) {
      files.get(i).setNumber(numbersPool.get(i));
    }
    return files;
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
