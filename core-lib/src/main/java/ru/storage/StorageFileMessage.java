package ru.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StorageFileMessage implements Message{

    private final String fileName;
    private final String filePath;
    private final byte[] fileByte;
    private final Long fileSize;
}
