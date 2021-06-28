package ru.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.file.Path;

@Data
@AllArgsConstructor
public class StorageAuthMessage implements Message{

    private String error = null;

    private String login;
    private String password;
    private Path src;

    public StorageAuthMessage(String error){
        this.error = error;
    }

    public StorageAuthMessage(String login, String pass){
        this.login = login;
        this.password = pass;
    }

}
