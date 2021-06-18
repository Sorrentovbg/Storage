package ru.storage.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.storage.StorageFileMessage;

public class FileMessageHandler extends SimpleChannelInboundHandler<StorageFileMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StorageFileMessage fileMessage) throws Exception {
        System.out.println("Я попал в fileHandler");
    }
}
