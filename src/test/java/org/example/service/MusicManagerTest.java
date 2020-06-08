package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import org.example.model.MyFile;
import org.junit.jupiter.api.Test;

class MusicManagerTest {

  private final String TEST_RESOURCES_DIR = "src/test/resources/";

  @Test
  public void shouldRandomizePrefixes() {
    MusicManager manager = new MusicManager();
    List<MyFile> myFiles = Arrays.asList(
        new MyFile(TEST_RESOURCES_DIR + "file1.mp3"),
        new MyFile(TEST_RESOURCES_DIR + "file2.mp3")
    );

    List<MyFile> changedFiles = manager.randomizePrefixes(myFiles);

    assertNotNull(changedFiles);
    assertEquals(myFiles.size(), changedFiles.size());
    assertFalse(verifyEqualityOfPrefixes(myFiles, changedFiles));
  }

  @Test
  public void shouldResetPrefixes() {
    MusicManager manager = new MusicManager();
    List<MyFile> myFiles = Arrays.asList(
        new MyFile(TEST_RESOURCES_DIR + "file1.mp3"),
        new MyFile(TEST_RESOURCES_DIR + "file2.mp3")
    );

    List<MyFile> changedFiles = manager.randomizePrefixes(myFiles);
    assertTrue(verifyEqualityOfPrefixes(myFiles, changedFiles));
    changedFiles = manager.abortChanges(changedFiles);

    assertNotNull(changedFiles);
    assertEquals(myFiles.size(), changedFiles.size());
    assertTrue(verifyEqualityOfPrefixes(myFiles, changedFiles));
  }

  private boolean verifyEqualityOfPrefixes(List<MyFile> list1, List<MyFile> list2) {
    for (int i = 0; i < list1.size(); i++) {
      if (list1.get(i).getNumber() != list2.get(i).getNumber()) {
        return false;
      }
    }
    return true;
  }

}