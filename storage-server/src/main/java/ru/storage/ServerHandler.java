package ru.storage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.storage.service.UserService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static final ConcurrentLinkedDeque<ChannelHandlerContext> clients = new ConcurrentLinkedDeque<>();

    private static final Path mainSrcPath = Paths.get("storage-server/src/main/resources");

    UserService userService = new UserService();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx);
        System.out.println("Client accept");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String str) throws Exception {
        if(str.startsWith("auth")){
            String[] authArray = str.split("_",3);
            System.out.println(authArray[0]);
            System.out.println(authArray[1]);
            System.out.println(authArray[2]);


        }
        if(str.startsWith("reg")){
            String[] regArray = str.split("_", 4);
            System.out.println(regArray[0]);
            System.out.println(regArray[1]);
            System.out.println(regArray[2]);
            System.out.println(regArray[3]);
//            userService.save(regArray[1],regArray[2],regArray[3],mainSrcPath);
        }
        System.out.println("Что получил: " + str);
//        User user = userService.findUser(1L);
//        System.out.println(user.getUserName());
    }
}
