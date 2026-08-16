package euclid.messages.incoming;

import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;

public class GETSTRIP implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        player.getInventory().turnPage(request.getContent());
    }
}
