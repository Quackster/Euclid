package euclid.messages.incoming;

import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;

public class Move implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        if (!player.getRoomEntity().isWalkingAllowed()) {
            return;
        }

        int x = Integer.parseInt(request.getArgument(0));
        int y = Integer.parseInt(request.getArgument(1));

        player.getRoomEntity().move(x, y);
    }
}
