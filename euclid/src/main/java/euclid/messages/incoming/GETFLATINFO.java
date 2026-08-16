package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.room.Room;
import euclid.game.room.RoomManager;
import euclid.messages.IMessageEvent;
import euclid.messages.outgoing.FLATINFO;
import euclid.network.streams.Request;

public class GETFLATINFO implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        int roomId = Integer.parseInt(request.getArgument(1, "/"));
        Room room = RoomManager.getInstance().getRoom(roomId);

        if (room == null || !room.isOwner(player.getDetails().getId())) {
            return;
        }

        player.send(new FLATINFO(room));
    }
}
