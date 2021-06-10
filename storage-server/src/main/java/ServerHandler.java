import entity.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import service.UserService;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    UserService userService = new UserService();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String str) throws Exception {
        System.out.println("Что получил: " + str);
        User user = userService.findUser(1L);
        System.out.println(user.getUserName());
    }
}
