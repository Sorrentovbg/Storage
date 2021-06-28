package ru.storage.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.storage.StorageRegistrationMessage;
import ru.storage.service.UserService;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RegistrationMessageHandler extends SimpleChannelInboundHandler<StorageRegistrationMessage> {

    UserService userService = new UserService();

    private static final Path mainSrcPath = Paths.get("storage-server/src/main/resources");

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StorageRegistrationMessage regMessage) throws Exception {
        userService.save(regMessage.getLogin(), regMessage.getPassword(), regMessage.getEmail());
        System.out.println("Я попал в regHandler");
    }
}
