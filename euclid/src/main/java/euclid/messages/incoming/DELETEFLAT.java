package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.room.Room;
import euclid.game.room.RoomManager;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;
import euclid.storage.database.access.RoomDao;

public class DELETEFLAT implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        int roomId = Integer.parseInt(request.getArgument(1, "/"));
        Room room = RoomManager.getInstance().getRoom(roomId);

        if (room == null || !room.isOwner(player.getDetails().getId())) {
            return;
        }

        RoomDao.deleteRoom(room.getData().getId());
    }
}
