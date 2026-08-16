package euclid.messages.incoming;

import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.network.GameServer;
import euclid.network.session.ConnectionMode;
import euclid.network.streams.GameAddress;
import euclid.network.streams.Request;
import euclid.game.room.Room;
import euclid.game.room.RoomManager;

import java.util.List;

public class LOGIN implements IMessageEvent {

    @Override
    public boolean authenticationRequired() {
        return false;
    }

    @Override
    public void handle(Player player, Request request) {
        if (player.isAuthenticated()) {
            return;
        }

        String username = request.getArgument(0);
        String password = request.getArgument(1);

        player.tryLogin(username, password);

        if (player.getConnectionMode() == ConnectionMode.PUBLIC) {
            GameServer server = GameServer.getInstance();
            List<GameAddress> publicServers = server.getPublicServers();
            GameAddress publicServer = null;
            for (GameAddress addr : publicServers) {
                if (addr.getPort() == player.getConnection().getLocalPort()) {
                    publicServer = addr;
                    break;
                }
            }

            if (publicServer == null) {
                return;
            }

            Room room = RoomManager.getInstance().getRoom(publicServer.getRoomId());
            if (room == null) {
                return;
            }

            room.getEntityManager().enterRoom(player);
        }
    }
}
