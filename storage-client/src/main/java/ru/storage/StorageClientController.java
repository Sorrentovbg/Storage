package ru.storage;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.Socket;

@Getter
@Setter
public class StorageClientController {

    private static StorageClientController instance;
    ObjectEncoderOutputStream outputStream;
    ObjectDecoderInputStream inputStream;

    private StorageClientController(){
        try {
            Socket socket = new Socket("localhost", 8199);
            outputStream = new ObjectEncoderOutputStream(socket.getOutputStream());
            inputStream = new ObjectDecoderInputStream(socket.getInputStream());
        }catch (RuntimeException | IOException e){

        }
    }

  public static StorageClientController getInstance(){
        if(instance == null){
            instance = new StorageClientController();
        }
        return instance;
  }
}
