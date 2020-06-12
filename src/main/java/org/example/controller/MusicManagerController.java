package org.example.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import org.example.model.MyFile;
import org.example.service.FileHelper;
import org.example.service.MusicManager;
import org.example.service.VideoConverter;
import ws.schild.jave.EncoderException;

public class MusicManagerController {

  @FXML
  private TableView<MyFile> musicTable;

  @FXML
  private TableColumn<MyFile, Integer> numberCol;

  @FXML
  private TableColumn<MyFile, String> nameCol;

  @FXML
  private TableColumn<MyFile, String> formatCol;

  private ObservableList<MyFile> fileList;
  private final MusicManager manager;
  private final FileHelper fileHelper;
  private final VideoConverter videoConverter;
  private final List<String> acceptedFormats = Arrays.asList(
      ".mp3", ".wav", ".aac", ".mp4", ".wmv", ".avi");

  public MusicManagerController() {
    this.fileList = FXCollections.observableArrayList();
    this.manager = new MusicManager();
    this.fileHelper = new FileHelper();
    this.videoConverter =  new VideoConverter();
  }

  @FXML
  void abortChanges() {
    manager.abortChanges(fileList);
  }

  @FXML
  void convertToMp3() throws EncoderException {
    List<MyFile> filesToConvert = musicTable.getSelectionModel().getSelectedItems()
        .filtered(myFile -> !myFile.getFormat().equals("mp3"));
    if (!filesToConvert.isEmpty()) {
      List<MyFile> convertedFiles = videoConverter.convertSelectedToMp3s(filesToConvert);
      fileList.removeAll(filesToConvert);
      fileList.addAll(convertedFiles);
    }
  }

  @FXML
  void credits() throws URISyntaxException, IOException {
    Desktop.getDesktop().browse(new URI("https://github.com/lapa44/MusicManager"));
  }

  @FXML
  void executeChanges() throws IOException {
    ObservableList<MyFile> newList = FXCollections.observableArrayList();
    for (MyFile file : fileList) {
      newList.add(fileHelper.renameFile(file));
    }
    fileList = newList;
    musicTable.setItems(fileList);
  }

  @FXML
  void importFiles() {
    List<MyFile> importedFiles = fileHelper.importMyFiles();
    if (!importedFiles.isEmpty()) {
      fileList.addAll(importedFiles.stream()
          .filter(this::doesFileNotExistInList).collect(Collectors.toList()));
    }
  }

  @FXML
  void quit() {
    Platform.exit();
  }

  @FXML
  void randomizePrefixes() {
    manager.randomizePrefixes(fileList);
  }

  @FXML
  public void initialize() {
    musicTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    tableEventsConfig();
    numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
    formatCol.setCellValueFactory(new PropertyValueFactory<>("format"));
    musicTable.setItems(fileList);
  }

  private void tableEventsConfig() {
    musicTable.setOnDragOver(dragEvent -> {
      if (dragEvent.getDragboard().hasFiles()) {
        dragEvent.acceptTransferModes(TransferMode.LINK);
      }
    });
    musicTable.setOnDragDropped(dragEvent -> {
      fileList.addAll(fileHelper.createMyFilesFromFiles(
          filterDroppedFiles(dragEvent.getDragboard().getFiles())));
      dragEvent.setDropCompleted(true);
    });
    musicTable.setOnKeyPressed(keyEvent -> {
      if (keyEvent.getCode().equals(KeyCode.DELETE)
          && musicTable.getSelectionModel().getSelectedItems() != null) {
        fileList.removeAll(musicTable.getSelectionModel().getSelectedItems());
      }
    });
  }

  private List<File> filterDroppedFiles(List<File> list) {
    return list.stream().filter(this::isFileFormatValid)
        .filter(this::doesFileNotExistInList)
        .collect(Collectors.toList());
  }

  private boolean isFileFormatValid(File file) {
    for (String format : acceptedFormats) {
      if (file.getName().contains(format)) {
        return true;
      }
    }
    return false;
  }

  private boolean doesFileNotExistInList(File file) {
    return !fileList.contains(file);
  }

}
