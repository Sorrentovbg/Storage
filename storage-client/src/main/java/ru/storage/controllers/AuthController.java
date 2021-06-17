package ru.storage.controllers;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.storage.StorageClientController;

import java.io.IOException;

public class AuthController {

    ObjectEncoderOutputStream ous = StorageClientController.getInstance().getOutputStream();
    ObjectDecoderInputStream ois = StorageClientController.getInstance().getInputStream();

@FXML
private Label errorMessageField;

@FXML
private TextField loginField;

@FXML
private PasswordField passField;

@FXML
private Button okButton;

@FXML
private Button cancelButton;

@FXML
private Button registrationButton;


    public void sendAuthMessage(MouseEvent mouseEvent) throws IOException {
        StringBuilder authMessage = new StringBuilder("auth_");
        authMessage.append(loginField.getText() + "_");
        authMessage.append(passField.getText());
        ous.writeObject(authMessage.toString());
    }

    public void openRegistrationForm(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ru.storage/regForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Registration");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
