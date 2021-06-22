package ru.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@Data
@AllArgsConstructor
public class StorageCommandMessage implements Message{
    private final String command;
    private final String login;
    private String path;
    private File[] fileList;

    public StorageCommandMessage(String command, String login){
        this.command = command;
        this.login = login;
    }
    public StorageCommandMessage(String command, String login, String path){
        this.command = command;
        this.login = login;
        this.path = path;
    }
}
