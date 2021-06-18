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
import ru.storage.StorageAuthMessage;
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
        StorageAuthMessage authMessage = new StorageAuthMessage(loginField.getText(),passField.getText());
        ous.writeObject(authMessage);
        while (true){
            try {
                System.out.println("Ожидаю authResult");
                StorageAuthMessage authResult = (StorageAuthMessage) ois.readObject();
                if(authResult.getError() == null){
                    System.out.println("if в authResult");
                    openMainWindow(authResult.getLogin());
                    System.out.println("authResult.getLogin = " + authResult.getLogin());
                    break;
                }
                else if(authResult.getError().equals("error")){
                    System.out.println("Else в authResult");
                    errorMessageField.setText("Неверный Логин или Пароль");
                    errorMessageField.setVisible(true);
                    break;
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
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

    private void openMainWindow(String login) {
        cancelButton.getScene().getWindow().hide();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ru.storage/mainWindow.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Storage " + login);
            stage.setScene(new Scene(root));
//            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
