package ru.storage.controllers;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.storage.StorageClientController;
import ru.storage.StorageCommandMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainWindowController {

    ObjectEncoderOutputStream ous = StorageClientController.getInstance().getOutputStream();
    ObjectDecoderInputStream ois = StorageClientController.getInstance().getInputStream();

    Path path;

    @FXML
    public ListView<String> fileList;
    @FXML
    public Button downloadButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Button uploadButton;

    public MainWindowController(){
        try {
            ous.writeObject(new StorageCommandMessage("GETPATH"));
            StorageCommandMessage scm = (StorageCommandMessage) ois.readObject();
            path = Paths.get(scm.getCommand());
            fileList.getItems().clear();
            fileList.getItems().add("..");
            for(File file: scm.getFileList()){
                fileList.getItems().add(file.getName());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png","*.jpg","*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file.getPath());
        if (file != null) {
            file.getName();
        } else{
            file.length();
        }

    }

    public void downloadFile(ActionEvent actionEvent) {
    }

    public void delete(ActionEvent actionEvent) {
    }

    public void refreshFolder(ActionEvent actionEvent) {
    }
}
