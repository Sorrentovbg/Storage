package ru.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StorageFileMessage implements Message{
    private String command;
    private String fileName;
    private String filePath;
    private int packageCount;
    private byte[] fileByte;
    private long fileSize;

    public StorageFileMessage(String command,String fileName, String filePath){
        this.command = command;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
