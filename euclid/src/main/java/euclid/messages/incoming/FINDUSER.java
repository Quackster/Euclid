package euclid.messages.incoming;

import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;

public class FINDUSER implements IMessageEvent {

    @Override
    public boolean authenticationRequired() {
        return false;
    }

    @Override
    public void handle(Player player, Request request) {
    }
}
