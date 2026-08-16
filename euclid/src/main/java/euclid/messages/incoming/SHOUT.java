package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.util.ChatMessageType;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;
import euclid.util.extensions.StringExtensions;

public class SHOUT implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        if (!player.isAuthenticated()) {
            return;
        }

        if (player.getRoomEntity().getRoom() == null) {
            return;
        }

        String message = StringExtensions.filterInput(request.getContent());
        player.getRoomEntity().talk(ChatMessageType.SHOUT, message);
    }
}
