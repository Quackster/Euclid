package euclid.messages.incoming;

import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;

public class GOAWAY implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        if (player.getRoomEntity().getRoom() == null) {
            return;
        }

        if (!player.getRoomEntity().isWalkingAllowed()) {
            return;
        }

        player.getRoomEntity().setBeingKicked(true);
        player.disconnect();
    }
}
