package org.example.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import org.example.model.MyFile;

public class FileHelper {

  public List<MyFile> createMyFilesFromFiles(List<File> list) {
    List<MyFile> myFiles = new ArrayList<>();
    for (File file : list) {
      myFiles.add(new MyFile(file.getAbsolutePath()));
    }
    return myFiles;
  }

  public List<MyFile> importMyFiles() {
    FileChooser fileChooser = new FileChooser();
    List<File> files = fileChooser.showOpenMultipleDialog(null);
    return createMyFilesFromFiles(files);
  }

  public MyFile renameFile(MyFile file) throws IOException {
    Path source = Paths.get(file.getAbsolutePath());
    Path target = Files.move(source, source.resolveSibling(
        file.getNumber() + "." + file.getFileName() + "." + file.getFormat()));
    return new MyFile(target.toString());
  }
}