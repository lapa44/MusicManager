package org.example.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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

  public MusicManagerController() {
    this.fileList = FXCollections.observableArrayList();
    this.manager = new MusicManager();
    this.fileHelper = new FileHelper();
  }

  @FXML
  void abortChanges() {
    manager.abortChanges(fileList);
  }

  @FXML
  void convertToMp3() {
    // Converter
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
      fileList.addAll(importedFiles);
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
    musicTable.setOnDragOver(dragEvent -> {
      if (dragEvent.getDragboard().hasFiles()) {
        dragEvent.acceptTransferModes(TransferMode.LINK);
      }
    });
    musicTable.setOnDragDropped(dragEvent -> {
      dragEvent.getDragboard().getFiles();
      fileList.addAll(fileHelper.createMyFilesFromFiles(dragEvent.getDragboard().getFiles()));
      dragEvent.setDropCompleted(true);
    });

    numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
    formatCol.setCellValueFactory(new PropertyValueFactory<>("format"));
    musicTable.setItems(fileList);

    musicTable.setOnKeyPressed(keyEvent -> {
      if (keyEvent.getCode().equals(KeyCode.DELETE)
          && musicTable.getSelectionModel().getSelectedItems() != null) {
        fileList.removeAll(musicTable.getSelectionModel().getSelectedItems());
      }
    });
  }
}
