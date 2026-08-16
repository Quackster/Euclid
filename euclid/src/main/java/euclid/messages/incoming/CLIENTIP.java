package euclid.messages.incoming;

import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.network.session.ConnectionMode;
import euclid.network.streams.Request;

public class CLIENTIP implements IMessageEvent {

    @Override
    public boolean authenticationRequired() {
        return false;
    }

    @Override
    public void handle(Player player, Request request) {
        if (player.getConnectionMode() == ConnectionMode.PUBLIC ||
                player.getConnectionMode() == ConnectionMode.PRIVATE) {
            player.getConnection().initialiseEncryption();
        }
    }
}
