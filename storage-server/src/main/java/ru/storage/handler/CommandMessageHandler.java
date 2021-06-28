package ru.storage.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.storage.ErrorMessage;
import ru.storage.StorageCommandMessage;
import ru.storage.service.UserService;

import java.io.File;


public class CommandMessageHandler extends SimpleChannelInboundHandler<StorageCommandMessage> {

    private final UserService userService = new UserService();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StorageCommandMessage commandMessage) throws Exception {
        System.out.println("Я попал в commHandler");
        switch (commandMessage.getCommand()){
            case "GETPATH":
                System.out.println("Попал в GETTPATH");
                System.out.println("CommandMessage getLogin = " + commandMessage.getLogin());
               String loginGetPath = commandMessage.getLogin();
               StorageCommandMessage strCommMess = userService.getMainPath(loginGetPath);
               channelHandlerContext.write(strCommMess);
               channelHandlerContext.flush();
               break;
            case "LS":
            case "..":
                System.out.println("Попал в LS или ..");
                System.out.println("CommandMessage getLogin = " + commandMessage.getLogin());
                System.out.println("CommandMessage getPath = " + commandMessage.getPath());
                String login = commandMessage.getLogin();
                StorageCommandMessage strCommMessLs = userService.getPath(login, commandMessage.getPath());
                System.out.println("out LS or .. path = " + strCommMessLs.getPath());
                channelHandlerContext.write(strCommMessLs);
                channelHandlerContext.flush();
                break;
            case "CD":
                System.out.println("Попал в CD");
                System.out.println("CommandMessage getLogin = " + commandMessage.getLogin());
                String loginCd = commandMessage.getLogin();
                if(checkFolder(commandMessage.getPath())){
                    StorageCommandMessage strCommMessCd = userService.getPath(loginCd, commandMessage.getPath());
                    System.out.println("strCommMessCd getPath = " + commandMessage.getPath());
                    channelHandlerContext.write(strCommMessCd);
                    channelHandlerContext.flush();
                }else{
                    channelHandlerContext.write(new ErrorMessage("Невозможно перейти в указанную директорию"));
                }
                break;
            case "MKDIR":
                System.out.println("Попал в MKDIR");
                System.out.println("CommandMessage getLogin = " + commandMessage.getLogin());
                String loginMK = commandMessage.getLogin();
                if(checkFolderExist(commandMessage.getPath())){
                    userService.createFolder(commandMessage.getPath());

                }else{
                    channelHandlerContext.write(new ErrorMessage("Папка уже существует"));
                }
                break;
            case "DEL":
                System.out.println("Попал в DEL");
                System.out.println("CommandMessage getLogin = " + commandMessage.getLogin());
                userService.delDirectory(commandMessage.getPath());
                break;
        }

    }
    private boolean checkFolderExist(String path) {
        boolean check;
        File file = new File(path);
        if(file.exists()){
            System.out.println("checkFolder path = " + path);
            check = false;
        }else{
            check = true;
        }
        System.out.println("checkFolder check = " + check);
        return check;
    }

    private boolean checkFolder(String path) {
        boolean check = false;
        File file = new File(path);
        if(file.isDirectory()){
            System.out.println("checkFolder path = " + path);
            check = true;
        }
        System.out.println("checkFolder check = " + check);
        return check;
    }
}
