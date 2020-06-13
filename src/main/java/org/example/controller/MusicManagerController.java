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
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import org.example.model.MyFile;
import org.example.service.FileHelper;
import org.example.service.MusicManager;
import org.example.service.VideoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private final Logger logger;
  private ObservableList<MyFile> fileList;
  private final MusicManager manager;
  private final FileHelper fileHelper;
  private final VideoConverter videoConverter;
  private final List<String> acceptedFormats = Arrays.asList(
      ".mp3", ".wav", ".aac", ".mp4", ".wmv", ".avi");

  public MusicManagerController() {
    this.logger = LoggerFactory.getLogger(MusicManagerController.class);
    this.fileList = FXCollections.observableArrayList();
    this.manager = new MusicManager();
    this.fileHelper = new FileHelper();
    this.videoConverter =  new VideoConverter();
  }

  @FXML
  void abortChanges() {
    manager.abortChanges(fileList);
    logger.info("Reverted randomized prefixes");
  }

  @FXML
  void convertToMp3() {
    List<MyFile> filesToConvert = musicTable.getSelectionModel().getSelectedItems()
        .filtered(myFile -> !myFile.getFormat().equals("mp3"));
    if (!filesToConvert.isEmpty()) {
      try {
        logger.info("Trying to convert selected files.");
        List<MyFile> convertedFiles = videoConverter.convertSelectedToMp3s(filesToConvert);
        fileList.removeAll(filesToConvert);
        fileList.addAll(convertedFiles);
        logger.info("Successfully converted " + convertedFiles.size() + " files.");
      }
      catch (EncoderException exception) {
        logger.error(String.valueOf(exception));
      }
    }
  }

  @FXML
  void credits() {
    logger.info("Trying to open credits in browser.");
    try {
      Desktop.getDesktop().browse(new URI("https://github.com/lapa44/MusicManager"));
    }
    catch (IOException | URISyntaxException exception) {
      logger.warn(String.valueOf(exception));
    }
  }

  @FXML
  void executeChanges() {
    ObservableList<MyFile> newList = FXCollections.observableArrayList();
    logger.info("Trying to rename imported files.");
    for (MyFile file : fileList) {
      try {
        newList.add(fileHelper.renameFile(file));
      } catch (IOException exception) {
        logger.error(String.valueOf(exception));
      }
    }
    fileList = newList;
    musicTable.setItems(fileList);
    logger.info("Finished executing changes.");
  }

  @FXML
  void importFiles() {
    List<MyFile> importedFiles = fileHelper.importMyFiles();
    if (!importedFiles.isEmpty()) {
      fileList.addAll(importedFiles.stream()
          .filter(this::doesFileNotExistInList).collect(Collectors.toList()));
      logger.info("Successfully imported " + importedFiles.size() + " files.");
    } else {
      logger.info("No file were imported.");
    }
  }

  @FXML
  void quit() {
    logger.info("Exiting application");
    Platform.exit();
  }

  @FXML
  void randomizePrefixes() {
    manager.randomizePrefixes(fileList);
    logger.info("Successfully randomized " + fileList.size() + " prefixes.");
  }

  @FXML
  public void initialize() {
    logger.info("Initializing file table.");
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
      logger.info("Finished importing files by drag and drop.");
      dragEvent.setDropCompleted(true);
    });
    musicTable.setOnKeyPressed(keyEvent -> {
      if (keyEvent.getCode().equals(KeyCode.DELETE)
          && musicTable.getSelectionModel().getSelectedItems() != null) {
        fileList.removeAll(musicTable.getSelectionModel().getSelectedItems());
        logger.info("Removed selected files.");
      }
    });
    musicTable.setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
        musicTable.getSelectionModel().clearSelection();
      }
    });
  }

  private List<File> filterDroppedFiles(List<File> list) {
    return list.stream().filter(this::isFileFormatValid)
        .filter(this::doesFileNotExistInList)
        .collect(Collectors.toList());
  }

  private boolean isFileFormatValid(File file) {
    logger.info("Validating file " + file.getName());
    for (String format : acceptedFormats) {
      if (file.getName().contains(format)) {
        return true;
      }
    }
    logger.info("File " + file.getName() + " has invalid format.");
    return false;
  }

  private boolean doesFileNotExistInList(File file) {
    logger.info("Checking for duplicates of " + file.getName());
    return fileList.stream().noneMatch(myFile -> myFile.getAbsolutePath().equals(file.getAbsolutePath()));
  }
}
