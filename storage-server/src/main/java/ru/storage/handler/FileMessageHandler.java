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
//                    new Thread(() -> {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] fileByte = new byte[arraySize];
                    while(fis.available() > 0){
                        fis.read(fileByte);
                        long i = fis.available();
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
//                        System.out.println("in New Thread");
//                        System.out.println("File.length = " + file.length());
//                        int packageCount = 1;
//                        byte[] fileByte = new byte[arraySize];
//                        while (true) {
//                            System.out.println("Цикл while, packages = " + packages);
//                            System.out.println("Цикл while, packagesCount = " + packageCount);
//                            try {
//                                if (packageCount == 1) {
//                                    System.out.println("First if, packageCount == 1, packageCount = " + packageCount);
//                                    System.out.println("First if, fileByte[] length = " + fileByte.length);
////                                    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
//                                    FileInputStream fileInputStream = new FileInputStream(file);
//                                    fileInputStream.read(fileByte);
////                                    randomAccessFile.seek(0);
////                                    randomAccessFile.read(fileByte);
//                                    System.out.println("fileByte = " + fileByte.length);
//                                    fileInputStream.close();
////                                    randomAccessFile.close();
//                                    channelHandlerContext.write(new StorageFileMessage("DOWN",
//                                            file.getName(),
//                                            pathToFile,
//                                            packages,
//                                            fileByte,
//                                            packageCount));
//                                    channelHandlerContext.flush();
//                                    packageCount++;
//                                } else if (packageCount > 1 && packageCount < packages) {
//                                    System.out.println("Second if, packageCount > 0 && packageCount < packages , packageCount = " + packageCount);
//                                    System.out.println("Second if, fileByte[] length = " + fileByte.length);
//                                    RandomAccessFile randomAccessFile = new RandomAccessFile(pathToFile, "r");
//                                    long positionToSeek = (long) arraySize * packageCount;
//                                    System.out.println("position seek = " + positionToSeek);
//                                    randomAccessFile.seek(positionToSeek);
//                                    randomAccessFile.read(fileByte);
//                                    randomAccessFile.close();
//                                    channelHandlerContext.write(new StorageFileMessage("DOWN",
//                                            file.getName(),
//                                            pathToFile,
//                                            packages,
//                                            fileByte,
//                                            packageCount));
//                                    channelHandlerContext.flush();
//                                    packageCount++;
//                                } else if (packageCount == packages) {
//                                    packages = -1;
//                                    int arraySizeLast = getLastPackage(arraySize, packageCount , file.length());
//                                    byte[] lastPackage = new byte[arraySizeLast];
//                                    System.out.println("Third if, packageCount == packages , packageCount = " + packageCount);
//                                    System.out.println("Third if, fileByte[] length = " + lastPackage.length);
//                                    System.out.println("Third if, file length = " + file.length());
//                                    long positionToSeek = (long) arraySize * (packageCount - 1);
//                                    System.out.println("position seek = " + positionToSeek);
//                                    RandomAccessFile randomAccessFile = new RandomAccessFile(pathToFile, "r");
//                                    randomAccessFile.seek(positionToSeek);
//                                    randomAccessFile.read(lastPackage);
//                                    randomAccessFile.close();
//                                    channelHandlerContext.write(new StorageFileMessage("DOWN",
//                                            file.getName(),
//                                            pathToFile,
//                                            packages,
//                                            lastPackage,
//                                            packageCount));
//                                    channelHandlerContext.flush();
//                                    break;
//                                }
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
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
