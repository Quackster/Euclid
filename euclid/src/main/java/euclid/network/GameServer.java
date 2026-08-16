package euclid.network;

import euclid.network.streams.GameAddress;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GameServer {

    private static final Logger log = LogManager.getLogger(GameServer.class);
    private static GameServer instance;

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final ScheduledExecutorService scheduledExecutorService;

    private final GameAddress mainServer;
    private final GameAddress privateServer;
    private final List<GameAddress> publicServers;

    public static GameServer getInstance() {
        return instance;
    }

    public static void createServer(GameAddress mainServer, GameAddress privateServer, List<GameAddress> publicServerAddresses) {
        instance = new GameServer(mainServer, privateServer, publicServerAddresses);
    }

    private GameServer(GameAddress mainServer, GameAddress privateServer, List<GameAddress> publicServerAddresses) {
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup(10);
        this.scheduledExecutorService = Executors.newScheduledThreadPool(4);
        this.mainServer = mainServer;
        this.privateServer = privateServer;
        this.publicServers = publicServerAddresses;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public GameAddress getMainServer() {
        return mainServer;
    }

    public GameAddress getPrivateServer() {
        return privateServer;
    }

    public List<GameAddress> getPublicServers() {
        return publicServers;
    }

    public void initialiseServer() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new GameChannelInitializer())
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_RCVBUF, 1024);

            bootstrap.bind(new InetSocketAddress(mainServer.getIpAddress(), mainServer.getPort()));
            bootstrap.bind(new InetSocketAddress(privateServer.getIpAddress(), privateServer.getPort()));

            for (GameAddress gameAddress : publicServers) {
                bootstrap.bind(new InetSocketAddress(gameAddress.getIpAddress(), gameAddress.getPort()));
            }

            log.info("Server listening on main port {}", mainServer.getPort());
            log.info("Server listening on private port {}", privateServer.getPort());
            for (GameAddress addr : publicServers) {
                log.info("Server listening on public port {} (room {})", addr.getPort(), addr.getRoomId());
            }
        } catch (Exception e) {
            log.error("Failed to setup network listener... {}", e.getMessage());
        }
    }
}
