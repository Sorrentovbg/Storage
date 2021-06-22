package ru.storage.service;

import lombok.NoArgsConstructor;
import ru.storage.StorageAuthMessage;
import ru.storage.StorageCommandMessage;
import ru.storage.entity.User;
import ru.storage.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor
public class UserService {

    UserRepository userRepository = new UserRepository();

    private static final Path mainPath = Paths.get("storage-server/src/main/resources");

    public User findUser(long id){
        return userRepository.findById(id);
    }

    public StorageAuthMessage findUserByUserNameAndPassword(String login, String password){
        User user = userRepository.findByUserNameAndPassword(login, password);
        if(user == null){
            return new StorageAuthMessage("error");
        }else {
            return new StorageAuthMessage(user.getUserName(), user.getUserSrc());
        }
    }
//    public StorageAuthMessage findUserByUserName(String login){
//        User user = userRepository.findByUserName(login);
//        if(user == null){
//            return new StorageAuthMessage("error");
//        }else {
//            return new StorageAuthMessage(user.getUserName(), user.getUserSrc());
//        }
//    }

    public void save(String login, String password, String email) throws IOException {
        File dir = new File(mainPath.toString());

        File[] arrFiles = dir.listFiles();
        String userSrc = mainPath.toString() + "\\" + login;
        if(!folderCheck(arrFiles, login)){
            Files.createDirectory(Paths.get(userSrc));
        }
        User newUser = new User();
        newUser.setUserName(login);
        newUser.setPassword(password);
        newUser.setUserEmail(email);
        newUser.setUserSrc(userSrc);
        userRepository.save(newUser);
    }

    private boolean folderCheck(File[] arrFiles, String login) {
        boolean check = false;
        for(int i = 0; i < arrFiles.length; i++){
            if(arrFiles[0].getName().equals(login)){
                check = true;
            }
        }
        return check;
    }

    public StorageCommandMessage getPath(String login){
        String userSrc = mainPath.toString() + "\\" + login;
        File[] arrFile = new File(userSrc).listFiles();
        if(arrFile == null){
            System.out.println("getPath methode File[] == null");
        }else {
            System.out.println("getPath methode File[] != null");
        }
        return new StorageCommandMessage("GETPATH",login,userSrc, arrFile);
    }



}
