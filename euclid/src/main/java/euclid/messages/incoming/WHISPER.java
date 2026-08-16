package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.entity.Humanoid;
import euclid.game.util.ChatMessageType;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;
import euclid.util.extensions.StringExtensions;

import java.util.List;

public class WHISPER implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        if (!player.isAuthenticated()) {
            return;
        }

        if (player.getRoomEntity().getRoom() == null) {
            return;
        }

        String name = request.getArgument(0, " ");
        String message = StringExtensions.filterInput(request.getContent().substring(name.length() + 1));

        List<Humanoid> entities = player.getRoomEntity().getRoom().getEntityManager().getEntities(Humanoid.class);
        Humanoid target = name.length() > 0 ? entities.stream()
                .filter(x -> x.getEntityData().getName().equals(name))
                .findFirst()
                .orElse(null) : null;

        if (target == null) {
            return;
        }

        if (target instanceof Player targetPlayer) {
            player.getRoomEntity().talk(ChatMessageType.WHISPER, message, List.of(targetPlayer));
        }
    }
}
