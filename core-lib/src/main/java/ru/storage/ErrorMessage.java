package ru.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage implements Message{
    private String errorMessage;
}
