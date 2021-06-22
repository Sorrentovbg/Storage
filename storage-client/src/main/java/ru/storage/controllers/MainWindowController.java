package ru.storage.controllers;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.storage.StorageClientController;
import ru.storage.StorageCommandMessage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    ObjectEncoderOutputStream ous = StorageClientController.getInstance().getOutputStream();
    ObjectDecoderInputStream ois = StorageClientController.getInstance().getInputStream();

    String path = null;

    @FXML
    public Label pathLabel;
    @FXML
    public ListView<String> fileList;
    @FXML
    public Button uploadButton;
    @FXML
    public Button downloadButton;
    @FXML
    public Button createFolderButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button refreshButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        try {
            System.out.println("Main window UserLogin = " + StorageClientController.getInstance().getUserLogin());
            String command = "GETPATH";
            navigate(command);
//            ous.writeObject(new StorageCommandMessage("GETPATH", StorageClientController.getInstance().getUserLogin()));
//            StorageCommandMessage scm = null;
//            while(scm == null) {
//                scm = (StorageCommandMessage) ois.readObject();
//                path = scm.getPath();
//            }
//            fileList.getItems().clear();
//            System.out.println("Path = " + path.toString());
//            if(scm.getFileList() == null){
//                pathLabel.setText(getShortPath(path, scm.getLogin()));
//                fileList.getItems().add("..");
//                pathLabel.setText(path.toString());
//                pathLabel.setVisible(true);
//                System.out.println("If in file list");
//
//            }else {
//                pathLabel.setText(getShortPath(path, scm.getLogin()));
//                System.out.println("else in file list");
//                System.out.println("scm get list = " + scm.getFileList().length);
//                fileList.getItems().add("..");
//                for(File file: scm.getFileList()){
//                    System.out.println(fileList.getItems().add(file.getName()));
//                }
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
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
        System.out.println(file.getName());

    }

    public void downloadFile(ActionEvent actionEvent) {
    }

    public void delete(ActionEvent actionEvent) {

    }

    public void refreshFolder(ActionEvent actionEvent) {
        System.out.println("Main window UserLogin LS = " + StorageClientController.getInstance().getUserLogin());
        String command = "LS";
        navigate(command);
    }


    public void createFolder(ActionEvent actionEvent) {
    }

    private void navigate(String command) {
        try {
            StorageCommandMessage stCommMess = null;
            if(command.equals("LS") || command.equals("GETPATH")){
                stCommMess = new StorageCommandMessage(command, StorageClientController.getInstance().getUserLogin());
            }
            ous.writeObject(stCommMess);
//            ous.writeObject(new StorageCommandMessage(command, StorageClientController.getInstance().getUserLogin()));
            StorageCommandMessage scm = null;
            while (scm == null) {
                scm = (StorageCommandMessage) ois.readObject();
                path = scm.getPath();
            }
            fileList.getItems().clear();
            System.out.println("Path = " + path.toString());
            if (scm.getFileList() == null) {
                pathLabel.setText(getShortPath(path, scm.getLogin()));
                fileList.getItems().add("..");
                pathLabel.setText(path.toString());
                pathLabel.setVisible(true);
                System.out.println("If in file list");

            } else {
                pathLabel.setText(getShortPath(path, scm.getLogin()));
                System.out.println("else in file list");
                System.out.println("scm get list = " + scm.getFileList().length);
                fileList.getItems().add("..");
                for(File file: scm.getFileList()){
                    System.out.println(fileList.getItems().add(file.getName()));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getShortPath(String path, String login) {
        String[] splitPath = path.split(login,2);
        return "\\\\" + login + splitPath[1];
    }


    public void mouseClicked(MouseEvent mouseEvent) {
        int clickCount = mouseEvent.getClickCount();
        if(clickCount == 2){
            String s = fileList.getSelectionModel().getSelectedItem();
            if(s.equals("..")){
                try {
                    ous.writeObject(new StorageCommandMessage("..", StorageClientController.getInstance().getUserLogin()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    ous.writeObject(new StorageCommandMessage("CD", StorageClientController.getInstance().getUserLogin()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String newPath = path + "\\" + s;
                System.out.println(newPath);
//                Path p = Paths.get();
                File file = new File(newPath);
                boolean bool = file.isDirectory();
                System.out.println(bool);
            }
        }
    }
}
