package ru.storage.controllers;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.storage.StorageClientController;
import ru.storage.StorageRegistrationMessage;

import java.io.IOException;

public class RegistrationController {
    ObjectEncoderOutputStream ous = StorageClientController.getInstance().getOutputStream();
    ObjectDecoderInputStream ois = StorageClientController.getInstance().getInputStream();

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passField;
    @FXML
    public TextField emailField;
    @FXML
    public Button okButton;
    @FXML
    public Button cancelButton;


    public void sendRegForm(ActionEvent actionEvent) throws IOException {
        StringBuilder regForm = new StringBuilder("reg_");
        regForm.append(loginField.getText() + "_" + passField.getText() + "_" + emailField.getText());
        StorageRegistrationMessage regMessage = new StorageRegistrationMessage(loginField.getText(), passField.getText(),emailField.getText());
        ous.writeObject(regMessage);
        loginField.clear();
        passField.clear();
        emailField.clear();
    }

    public void closeRegForm(ActionEvent actionEvent) {
        cancelButton.getScene().getWindow().hide();
    }
}
