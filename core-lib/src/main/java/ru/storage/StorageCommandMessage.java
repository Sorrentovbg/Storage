package ru.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@Data
@AllArgsConstructor
public class StorageCommandMessage implements Message{
    private final String command;
    private File[] fileList;

    public StorageCommandMessage(String command){
        this.command = command;
    }
}
