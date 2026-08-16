package euclid.network;

import euclid.messages.IMessageComposer;
import euclid.messages.MessageHandler;
import euclid.network.session.ConnectionSession;
import euclid.network.streams.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameNetworkHandler extends SimpleChannelInboundHandler<Object> {

    public static final AttributeKey<ConnectionSession> CONNECTION_KEY =
            AttributeKey.valueOf("CONNECTION_KEY");

    private static final Logger log = LogManager.getLogger(GameNetworkHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ConnectionSession connection = new ConnectionSession(ctx.channel());

        connection.getPlayer().getLog().debug("Client connected to server: {}", connection.getIpAddress());
        ctx.channel().attr(CONNECTION_KEY).setIfAbsent(connection);

        ctx.channel().writeAndFlush(new euclid.messages.outgoing.HELLO());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ConnectionSession connection = ctx.channel().attr(CONNECTION_KEY).get();

        if (connection == null) {
            return;
        }

        connection.onDisconnect();
        connection.getPlayer().getLog().debug("Client disconnected from server: {}", connection.getIpAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        ConnectionSession connectionSession = ctx.channel().attr(CONNECTION_KEY).get();

        if (connectionSession == null) {
            return;
        }

        if (msg instanceof Request request) {
            MessageHandler.getInstance().handleMessage(connectionSession.getPlayer(), request);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.toString());
    }
}
