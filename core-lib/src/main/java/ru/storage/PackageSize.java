package ru.storage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PackageSize {
    public static void main(String[] args) throws IOException {
        int fileSize = 3_238_914;
        int packageSize = 1024;
        int packageCount = fileSize / packageSize;
        System.out.println("packageCount без добавления остатков = " + packageCount);
        double lastPackage = fileSize % packageSize;
        System.out.println("lastPackage через % = " + lastPackage);
        double anotherTry = fileSize - (packageCount * packageSize);
        System.out.println("fileSize - (packageCount * packageSize) = " + anotherTry);
        double anotherTryStepOne = packageCount * packageSize;
        double anotherTryTStepTwo = fileSize - anotherTryStepOne;
        System.out.println("anotherTryStepOne = " + anotherTryStepOne);
        System.out.println("anotherTryTStepTwo = " + anotherTryTStepTwo);


        File file = new File("D:\\DownloadTest\\test.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\DownloadTest\\test.txt", "rw");
        System.out.println("File length  = " + file.length());
//        byte bytes = randomAccessFile.read();
//        int str =  randomAccessFile.
        randomAccessFile.writeBytes("123");
        randomAccessFile.seek(4);
        randomAccessFile.writeBytes("123");
//        randomAccessFile.seek(7);
//        randomAccessFile.writeInt(123);
        String str = randomAccessFile.readLine();
        System.out.println("str = " + str);
//        System.out.println("Char = " + str);


    }
}
