package ru.storage;

public class StartServer {

    public static void main(String[] args) {

        final int PORT = 8199;

        new NettyServer(PORT).start();

    }
}
