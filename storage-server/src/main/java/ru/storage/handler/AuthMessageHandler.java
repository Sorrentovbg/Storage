package ru.storage.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.storage.StorageAuthMessage;
import ru.storage.service.UserService;

import java.util.concurrent.ConcurrentLinkedDeque;

public class AuthMessageHandler extends SimpleChannelInboundHandler<StorageAuthMessage> {

    private static final ConcurrentLinkedDeque<ChannelHandlerContext> clients = new ConcurrentLinkedDeque<>();

    UserService userService = new UserService();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx);
        System.out.println("Client accept");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StorageAuthMessage authMessage) throws Exception {
        System.out.println("Я попал в authHandler");
//        User user = userService.findUserByUserName(authMessage.getLogin(), authMessage.getPassword());
        StorageAuthMessage authMessageOut = userService.findUserByUserName(authMessage.getLogin(), authMessage.getPassword());
        channelHandlerContext.write(authMessageOut);
        channelHandlerContext.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        clients.remove(ctx);
        System.out.println("Client disconnected");
    }
}
