package euclid.network;

import euclid.network.codec.NetworkDecoder;
import euclid.network.codec.NetworkEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class GameChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) {
        channel.pipeline().addLast("gameEncoder", new NetworkEncoder());
        channel.pipeline().addLast("gameDecoder", new NetworkDecoder());
        channel.pipeline().addLast("clientHandler", new GameNetworkHandler());
    }
}
