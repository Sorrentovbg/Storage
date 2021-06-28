package ru.storage.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.storage.StorageFileMessage;

import java.io.*;

public class FileMessageHandler extends SimpleChannelInboundHandler<StorageFileMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StorageFileMessage fileMessage) throws Exception {
        System.out.println("Я попал в fileHandler");
        switch (fileMessage.getCommand()){
            case "DOWN":
                int arraySize;
                int packages;
                String pathToFile = fileMessage.getFilePath();
                File file = new File(pathToFile);
                int count = 0;
                if(file.isFile()){
                    System.out.println("isFile");
                    arraySize = getArraySize(file.length());
                    packages = getPackageCount(file.length(),arraySize);
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] fileByte = new byte[arraySize];
                    while(fileInputStream.available() > 0){
                        fileInputStream.read(fileByte);
                        long i = fileInputStream.available();
                        System.out.println("Packages = " + packages);
                        System.out.println("Count = " + count);
                        System.out.println("Read available = " + i);
                        channelHandlerContext.write(new StorageFileMessage("DOWN",
                                            file.getName(),
                                            pathToFile,
                                            packages,
                                            fileByte,
                                            i));
                                    channelHandlerContext.flush();
                        count++;
                    }
                    fileInputStream.close();
                }
            case "UPLOAD":
                try {
                    System.out.println("UPLOAD command");
                    String fileName;
                    String filePath;
                    int inboundPackages = -1;
                    long packagesCount = 0;
                    long byteAvailable = -1;
                    while (byteAvailable != 0) {
//                        fileMessage = (StorageFileMessage) channelHandlerContext.read();
                        int inboundArraySize = fileMessage.getFileByte().length;
                        packages = fileMessage.getPackageCount();
                        fileName = fileMessage.getFileName();
                        filePath = fileMessage.getFilePath();
                        byteAvailable = fileMessage.getFileSize();
                        File inboundFile = new File(fileMessage.getFilePath());
                        byte[] fileByte = new byte[inboundArraySize];
                        if (!inboundFile.exists()) {
                            OutputStream outputStream = new FileOutputStream(fileMessage.getFilePath(), true);
                            outputStream.write(fileMessage.getFileByte());
                            outputStream.close();
                            packagesCount++;
                        } else {
                            if (fileName.equals(fileMessage.getFileName()) && filePath.equals(fileMessage.getFilePath())) {
                                OutputStream fos = new FileOutputStream(fileMessage.getFilePath(), true);
                                fos.write(fileMessage.getFileByte());
                                packagesCount++;
                                fos.close();
                            }
                        }

                    }
                } catch(IOException e){
                    e.printStackTrace();
                }
        }
    }

    private int getLastPackage(int arraySize, int packages, long length) {
        int arraySizeLast;
        int sendBytes = arraySize * (packages - 1);
        arraySizeLast = (int) (length - sendBytes);
        System.out.println("LastPack size = " + arraySizeLast);
        return arraySizeLast;
    }

    private int getPackageCount(long length, int arraySize) {
        int count;
        count = (int) (length / arraySize);
        if(length%arraySize != 0){
            count++;
        }
        return count;
    }

    private int getArraySize(long length) {
        int sizeMB = 1_000_000;
        int sizeOnePackage = 1_024;
        int arrayLength;
        if(length > sizeMB){
            arrayLength = sizeOnePackage;
        }else {
            arrayLength = (int) length;
        }

        return arrayLength;
    }
}
