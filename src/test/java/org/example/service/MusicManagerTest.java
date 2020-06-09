package org.example.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.model.MyFile;
import org.junit.jupiter.api.Test;

class MusicManagerTest {

  private final String TEST_RESOURCES_DIR = "src/test/resources/";

  @Test
  public void shouldRandomizePrefixes() {
    MusicManager manager = new MusicManager();
    ObservableList<MyFile> myFiles = FXCollections.observableArrayList(Arrays.asList(
        new MyFile(TEST_RESOURCES_DIR + "1.file1.mp3"),
        new MyFile(TEST_RESOURCES_DIR + "2.file2.mp3"),
        new MyFile(TEST_RESOURCES_DIR + "3.file3.mp3")
    ));

    ObservableList<MyFile> changedFiles = manager.randomizePrefixes(myFiles);

    assertNotNull(changedFiles);
    assertEquals(myFiles.size(), changedFiles.size());
    assertTrue(verifyInequalityOfPrefixes(changedFiles));
  }

  @Test
  public void shouldResetPrefixes() {
    MusicManager manager = new MusicManager();
    ObservableList<MyFile> myFiles = FXCollections.observableArrayList(Arrays.asList(
        new MyFile(TEST_RESOURCES_DIR + "file1.mp3"),
        new MyFile(TEST_RESOURCES_DIR + "file2.mp3"),
        new MyFile(TEST_RESOURCES_DIR + "3.file3.mp3")
    ));

    ObservableList<MyFile> changedFiles = manager.randomizePrefixes(myFiles);
    assertTrue(verifyInequalityOfPrefixes(changedFiles));
    changedFiles = manager.abortChanges(changedFiles);

    assertNotNull(changedFiles);
    assertEquals(myFiles.size(), changedFiles.size());
    assertFalse(verifyInequalityOfPrefixes(changedFiles));
  }

  private boolean verifyInequalityOfPrefixes(ObservableList<MyFile> files) {
    String[] decodedName;
    for (MyFile file : files) {
      decodedName = file.getName().split("\\.");
      if (decodedName.length > 2) {
        if (file.getNumber() != Integer.parseInt(decodedName[0])) {
          return true;
        }
      }
    }
    return false;
  }
}
