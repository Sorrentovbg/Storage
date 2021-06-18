package ru.storage.service;

import lombok.NoArgsConstructor;
import ru.storage.StorageAuthMessage;
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

    public User findUser(long id){
        return userRepository.findById(id);
    }

    public StorageAuthMessage findUserByUserName(String login, String password){
        User user = userRepository.findByUserName(login, password);
        if(user == null){
            return new StorageAuthMessage("error");
        }else {
            return new StorageAuthMessage(user.getUserName(), user.getUserSrc());
        }
    }

    public void save(String login, String password, String email, Path serverPath) throws IOException {
        File dir = new File(serverPath.toString());
        File[] arrFiles = dir.listFiles();
        String userSrc = serverPath.toString() + "/" + login;
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
}
