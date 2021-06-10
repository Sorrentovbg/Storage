import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class StorageClientController {

    public static void run() {
        try {
            Socket socket = new Socket("localhost", 8199);
            ObjectEncoderOutputStream outputStream = new ObjectEncoderOutputStream(socket.getOutputStream());
            ObjectDecoderInputStream decoderInputStream = new ObjectDecoderInputStream(socket.getInputStream());
            while (true){
                Scanner sc = new Scanner(System.in);
                String readSc = sc.next();
                outputStream.writeObject(readSc);
                System.out.println("Что напечал : " + readSc);
            }



        }catch (RuntimeException | IOException e){

        }

    }
}
