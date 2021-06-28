package ru.storage.controllers;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.storage.ErrorMessage;
import ru.storage.StorageClientController;
import ru.storage.StorageCommandMessage;
import ru.storage.StorageFileMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    ObjectEncoderOutputStream objectEncoderOutputStream = StorageClientController.getInstance().getOutputStream();
    ObjectDecoderInputStream objectDecoderInputStream = StorageClientController.getInstance().getInputStream();

    static String path = null;

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
            System.out.println("Main window UserLogin = " + StorageClientController.getInstance().getUserLogin());
            String command = "GETPATH";
            navigate(command);
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

    public void downloadFile(ActionEvent actionEvent) throws IOException {
        String command = "DOWN";
        String selectObject = fileList.getSelectionModel().getSelectedItem();
        String pathToFile = path + "\\" + selectObject;
        Stage stageDirectoryChooser = new Stage();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберете папку");
        File fileDirectory = directoryChooser.showDialog(stageDirectoryChooser);
        String pathToDirectory = fileDirectory.getPath() + "\\";
        System.out.println("Path to directory = " + pathToDirectory);

        objectEncoderOutputStream.writeObject(new StorageFileMessage(command,selectObject,pathToFile));
        objectEncoderOutputStream.flush();

//        new Thread(() -> {
            try {
                String fileName;
                String filePath;
                int packages = -1;
                long packagesCount = 0;
                long byteAvailable = -1;
                boolean lastPackage = true;
                while (byteAvailable != 0) {
                    Object object = objectDecoderInputStream.readObject();
                    if (object.getClass().getSimpleName().equals("StorageFileMessage")) {
                        StorageFileMessage inboundObject = (StorageFileMessage) object;
                        int arraySize = inboundObject.getFileByte().length;
                        packages = inboundObject.getPackageCount();
                        fileName = inboundObject.getFileName();
                        filePath = inboundObject.getFilePath();
                        byteAvailable = inboundObject.getFileSize();
//                        packagesCount = inboundObject.getFileSize();
                        File file = new File(pathToDirectory + "\\" + fileName);
                        byte[] fileByte = new byte[arraySize];
                        if (!file.exists()) {
                            System.out.println("First if filename = " + fileName);
                            System.out.println("First if filepath = " + filePath);
                            System.out.println("First if packages = " + inboundObject.getPackageCount());
                            System.out.println("First if packageCount = " + packagesCount);
                            System.out.println("First if fileByte size = " + fileByte.length);
                            OutputStream outputStream = new FileOutputStream(pathToDirectory + "\\" + fileName, true);
                            outputStream.write(inboundObject.getFileByte());
                            outputStream.close();
                            packagesCount++;
                        } else {
                            if (fileName.equals(inboundObject.getFileName()) && filePath.equals(inboundObject.getFilePath())) {
                                System.out.println("Second if filename = " + fileName);
                                System.out.println("Second if filePath = " + filePath);
                                System.out.println("Second if packages = " + inboundObject.getPackageCount());
                                System.out.println("Second if packageCount = " + packagesCount);
                                System.out.println("Second if fileByte size = " + fileByte.length);
//                            byte[] fileByte = new byte[fileSize];
                                OutputStream fos = new FileOutputStream(pathToDirectory + "\\" + fileName, true);
                                fos.write(inboundObject.getFileByte());
                                packagesCount++;
                                System.out.println("Package available = " + inboundObject.getFileSize());
                                fos.close();
//                                RandomAccessFile randomAccessFile = new RandomAccessFile(pathToDirectory + "\\" + fileName, "rw");
//                                File addByteToFile = new File(pathToDirectory + "\\" + fileName);
//                                System.out.println("File size addByteToFile.length before write byte = " + addByteToFile.length());
//                                System.out.println("File size randomAccessFile.length before write byte = " + randomAccessFile.length());
//                                System.out.println("File recived byte packageCount * arrayLength = " + (packagesCount * 1024));
//                                long positionToSeek = randomAccessFile.length();
//                                System.out.println("position seek = " + positionToSeek);
//                                randomAccessFile.seek(positionToSeek);
//                                randomAccessFile.write(inboundObject.getFileByte());

//                                System.out.println("File size randomAccessFile.length after write byte = " + randomAccessFile.length());
//                                randomAccessFile.close();
//                                if(packages == packagesCount){
//                                    lastPackage = false;
//                                    System.out.println("If packages == packagesCount");
//                                    break;
//                                }
                            }
                        }
                    }
                }
                } catch(ClassNotFoundException | IOException e){
                    e.printStackTrace();
                }
//        }).start();


    }

    public void delete(ActionEvent actionEvent) throws IOException {
        String comm = "DEL";
        String selectedFolder = fileList.getSelectionModel().getSelectedItem();
        String delDirectoryPath = path + "\\" + selectedFolder;
        objectEncoderOutputStream.writeObject(new StorageCommandMessage(comm,
                StorageClientController.getInstance().getUserLogin(),
                delDirectoryPath));
        navigate("LS");
    }

    public void refreshFolder(ActionEvent actionEvent) {
        System.out.println("Main window UserLogin LS = " + StorageClientController.getInstance().getUserLogin());
        String command = "LS";
        navigate(command);
    }


    public void createFolder(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ru.storage/createFolderForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Storage " + StorageClientController.getInstance().getUserLogin());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            navigate("LS");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void navigate(String command) {
        try {
            StorageCommandMessage outCommMess = null;
            if(command.equals("GETPATH")) {
                outCommMess = new StorageCommandMessage(command,
                        StorageClientController.getInstance().getUserLogin());
            }else if(command.equals("LS")){
                outCommMess = new StorageCommandMessage(command,
                        StorageClientController.getInstance().getUserLogin(),
                        path);
            }else if(command.startsWith("..")){
                String[] comeBackComm = command.split("_",2);
                outCommMess = new StorageCommandMessage(comeBackComm[0],
                        StorageClientController.getInstance().getUserLogin(),
                        comeBackComm[1]);
            }else if(command.startsWith("CD")){
                String[] forward = command.split("_",2);
                outCommMess = new StorageCommandMessage(forward[0],
                        StorageClientController.getInstance().getUserLogin(),
                        forward[1]);
            }
            objectEncoderOutputStream.writeObject(outCommMess);
            StorageCommandMessage scm = null;
            ErrorMessage errorMessage = null;
            while (scm == null) {
               Object inboundObject = objectDecoderInputStream.readObject();
               if(inboundObject.getClass().getSimpleName().equals("StorageCommandMessage")){
                   scm = (StorageCommandMessage) inboundObject;
                   path = scm.getPath();
                   System.out.println("scm getPath = " + scm.getPath());
               }else {
                   errorMessage = (ErrorMessage) inboundObject;
               }
            }
            fileList.getItems().clear();
            System.out.println("Path = " + path);
            if(errorMessage != null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText(errorMessage.getErrorMessage());
                alert.showAndWait();
            }else {
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
                    for (File file : scm.getFileList()) {
                        System.out.println(fileList.getItems().add(file.getName()));
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    private String getShortPath(String path, String login) {
        String[] splitPath = path.split(login,2);
        return "\\" + login + splitPath[1];
    }


    public void mouseClicked(MouseEvent mouseEvent) {
        int clickCount = mouseEvent.getClickCount();
        String delimiter = "\\\\";
        if(clickCount == 2){
            String listFiles = fileList.getSelectionModel().getSelectedItem();
            if(listFiles.equals("..")){
                String[] comaBack = path.split(delimiter);
                if(comaBack[comaBack.length-1].equals(StorageClientController.getInstance().getUserLogin())){
                    navigate("LS");
                }else {
                    StringBuilder newPath = new StringBuilder(".._");
                    for (int i = 0; i < comaBack.length-1; i++){
                        if(i == comaBack.length-2){
                            newPath.append(comaBack[i]);
                        }else{
                            newPath.append(comaBack[i] + "\\");
                        }

                    }
                    System.out.println("mouseClick path = " + path);
                    System.out.println("mouseClick out NewPath = " + newPath.toString());

                    navigate(newPath.toString());
                }
            }else{
                String newPath = path + "\\" + listFiles;
                navigate("CD_" + newPath);
                System.out.println("MouseClick (newPath) = " + newPath);
            }
        }
    }
}
