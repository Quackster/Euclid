package euclid.messages.incoming;

import euclid.Euclid;
import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.messages.outgoing.ENCRYPTION_OFF;
import euclid.messages.outgoing.ENCRYPTION_ON;
import euclid.messages.outgoing.SECRET_KEY;
import euclid.network.session.ConnectionMode;
import euclid.network.streams.Request;

public class VERSIONCHECK implements IMessageEvent {

    @Override
    public boolean authenticationRequired() {
        return false;
    }

    @Override
    public void handle(Player player, Request request) {
        if (player.getConnectionMode() == ConnectionMode.MAIN) {
            player.getConnection().initialiseEncryption();
        }

        if (Euclid.ENCRYPTION) {
            player.send(new ENCRYPTION_ON());
        } else {
            player.send(new ENCRYPTION_OFF());
        }

        player.send(new SECRET_KEY(player.getConnection().getPublicKey()));
    }
}
