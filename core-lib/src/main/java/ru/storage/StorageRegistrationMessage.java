package ru.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StorageRegistrationMessage implements Message{

    private final String login;
    private final String password;
    private final String email;
}
