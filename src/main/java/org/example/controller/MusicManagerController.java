package org.example.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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

  public MusicManagerController() {
    this.fileList = FXCollections.observableArrayList();
    this.manager = new MusicManager();
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
  void executeChanges() {
    // FileHelper change names
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
      fileList.addAll(createMyFiles(dragEvent.getDragboard().getFiles()));
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

  public List<MyFile> createMyFiles(List<File> list) {
    List<MyFile> myFiles = new ArrayList<>();
    for (File file : list) {
      myFiles.add(new MyFile(file.getAbsolutePath()));
    }
    return myFiles;
  }

}
