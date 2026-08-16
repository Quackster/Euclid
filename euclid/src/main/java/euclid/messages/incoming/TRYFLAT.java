package euclid.messages.incoming;

import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.messages.outgoing.FLAT_LETIN;
import euclid.network.streams.Request;

public class TRYFLAT implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        player.send(new FLAT_LETIN());
    }
}
