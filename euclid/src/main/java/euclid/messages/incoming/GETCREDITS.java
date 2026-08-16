package euclid.messages.incoming;

import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.messages.outgoing.WALLETBALANCE;
import euclid.network.streams.Request;

public class GETCREDITS implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        player.send(new WALLETBALANCE(999999));
    }
}
