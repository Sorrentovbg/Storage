package ru.storage.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.storage.StorageCommandMessage;

public class CommandMessageHandler extends SimpleChannelInboundHandler<StorageCommandMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StorageCommandMessage commandMessage) throws Exception {
        System.out.println("Я попал в commHandler");
    }
}
