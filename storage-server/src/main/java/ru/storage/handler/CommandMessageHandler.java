package ru.storage.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.storage.StorageCommandMessage;
import ru.storage.service.UserService;


public class CommandMessageHandler extends SimpleChannelInboundHandler<StorageCommandMessage> {

    private final UserService userService = new UserService();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StorageCommandMessage commandMessage) throws Exception {
        System.out.println("Я попал в commHandler");
        switch (commandMessage.getCommand()){
            case "GETPATH":
                System.out.println("Попал в GETTPATH");
                System.out.println("CommandMessage getLogin = " + commandMessage.getLogin());
               String login = commandMessage.getLogin();
               StorageCommandMessage strCommMess = userService.getPath(login);
               channelHandlerContext.write(strCommMess);
               channelHandlerContext.flush();
               break;
            case "LS":
                System.out.println("Попал в LS");
                System.out.println("CommandMessage getLogin = " + commandMessage.getLogin());
                String loginLs = commandMessage.getLogin();
                StorageCommandMessage strCommMessLs = userService.getPath(loginLs);
                channelHandlerContext.write(strCommMessLs);
                channelHandlerContext.flush();
        }

    }
}
