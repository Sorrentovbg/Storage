package ru.storage.controllers;

import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.storage.StorageClientController;
import ru.storage.StorageCommandMessage;

import java.io.IOException;

public class CreateFolderController {

    ObjectEncoderOutputStream ous = StorageClientController.getInstance().getOutputStream();

    @FXML
    public Label messageErrorLabel;

    @FXML
    public Button closeButton;

    @FXML
    public TextField newFolderName;

    public void createNewFolder(ActionEvent actionEvent) throws IOException {
        String path = MainWindowController.path;
        String folderName = newFolderName.getText();
        if(checkFolderName(folderName)){
            String newPath = path + "\\" + folderName;
            ous.writeObject(new StorageCommandMessage("MKDIR",
                    StorageClientController.getInstance().getUserLogin(),
                    newPath));
            closeButton.getScene().getWindow().hide();
        }else{
            messageErrorLabel.setText("Неверное имя папки");
        }
    }

    public void closeDialog(ActionEvent actionEvent) {
        closeButton.getScene().getWindow().hide();
    }

    public boolean checkFolderName(String folderName) {
        char[] literal = folderName.toCharArray();
        boolean check = false;
        for (char ch : literal){
            if((ch == '/' || ch == '\\' || ch == '|' || ch == '"') && literal.length < 255){
                check = false;
            }else {
                check = true;
            }
        }
        return check;
    }
}
