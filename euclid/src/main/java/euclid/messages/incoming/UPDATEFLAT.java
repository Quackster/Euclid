package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.room.Room;
import euclid.game.room.RoomManager;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;
import euclid.storage.database.access.RoomDao;
import euclid.storage.database.data.RoomStatus;

public class UPDATEFLAT implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        int roomId = Integer.parseInt(request.getArgument(1, "/"));
        Room room = RoomManager.getInstance().getRoom(roomId);

        if (room == null || !room.isOwner(player.getDetails().getId())) {
            return;
        }

        String name = request.getArgument(2, "/");
        String accessType = request.getArgument(3, "/");
        boolean showOwnerName = request.getArgument(4, "/").equals("1");

        if (name.length() < 2) {
            name = room.getData().getName();
        }

        RoomStatus state = RoomStatus.OPEN;

        if (accessType.equals("closed")) {
            state = RoomStatus.CLOSED;
        }

        if (accessType.equals("password")) {
            state = RoomStatus.PASSWORD;
        }

        room.getData().setName(name);
        room.getData().setAccessType(state);
        room.getData().setShowName(showOwnerName);

        RoomDao.saveRoom(room.getData());
    }
}
