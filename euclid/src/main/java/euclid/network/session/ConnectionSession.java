package euclid.network.session;

import euclid.Euclid;
import euclid.game.Player;
import euclid.messages.IMessageComposer;
import euclid.network.GameServer;
import euclid.network.streams.GameAddress;
import euclid.util.encryption.RC4;
import euclid.util.encryption.SecretKey;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

public class ConnectionSession {

    private boolean disconnected;

    private final Channel channel;
    private final String ipAddress;
    private final int localPort;
    private final String publicKey;
    private final Player player;
    private final ConnectionMode mode;

    private RC4 encryption;

    public ConnectionSession(Channel channel) {
        this.channel = channel;
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
        this.ipAddress = remoteAddress != null ? remoteAddress.getAddress().getHostAddress() : "unknown";
        this.localPort = ((InetSocketAddress) channel.localAddress()).getPort();
        this.publicKey = UUID.randomUUID().toString().replace("-", "").toLowerCase();

        GameServer server = GameServer.getInstance();
        ConnectionMode resolvedMode = ConnectionMode.MAIN;

        if (server != null) {
            if (localPort == server.getMainServer().getPort()) {
                resolvedMode = ConnectionMode.MAIN;
            } else if (localPort == server.getPrivateServer().getPort()) {
                resolvedMode = ConnectionMode.PRIVATE;
            } else {
                List<GameAddress> publicServers = server.getPublicServers();
                for (GameAddress addr : publicServers) {
                    if (addr.getPort() == localPort) {
                        resolvedMode = ConnectionMode.PUBLIC;
                        break;
                    }
                }
            }
        }

        this.mode = resolvedMode;
        this.player = new Player(this);
    }

    public Channel getChannel() {
        return channel;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Player getPlayer() {
        return player;
    }

    public RC4 getEncryption() {
        return encryption;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public int getLocalPort() {
        return localPort;
    }

    public ConnectionMode getMode() {
        return mode;
    }

    public void send(IMessageComposer composer) {
        if (!composer.isComposed()) {
            composer.setComposed(true);
            composer.write();
        }

        try {
            channel.writeAndFlush(composer);
        } catch (Exception ignored) {
        }
    }

    public void initialiseEncryption() {
        if (encryption != null) {
            return;
        }

        encryption = new RC4();
        encryption.setKey(SecretKey.secretDecode(publicKey));

        if (Euclid.ENCRYPTION) {
            channel.pipeline().addFirst("encryption", new euclid.network.codec.EncryptionDecoder(encryption));
        }
    }

    public void onDisconnect() {
        if (disconnected) {
            return;
        }
        disconnected = true;
        player.onDisconnect();
    }

    public void disconnect() {
        if (disconnected) {
            return;
        }
        channel.close();
    }
}
