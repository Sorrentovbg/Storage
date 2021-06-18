package ru.storage;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import ru.storage.handler.AuthMessageHandler;
import ru.storage.handler.CommandMessageHandler;
import ru.storage.handler.FileMessageHandler;
import ru.storage.handler.RegistrationMessageHandler;

public class NettyServer {

    int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(5);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                new ObjectEncoder(),
                                new RegistrationMessageHandler(),
                                new AuthMessageHandler(),
                                new CommandMessageHandler(),
                                new FileMessageHandler(),
                                new ServerHandler()

                            );
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("Server start");
            future.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
