package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.room.Room;
import euclid.game.room.RoomManager;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;
import euclid.storage.database.access.RoomDao;

import java.util.Map;

public class SETFLATINFO implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        int roomId = Integer.parseInt(request.getArgument(1, "/"));
        String content = request.getContent().substring(String.valueOf("/" + roomId + "/").length());

        Map<String, String> flatValues = request.getKeyValues(content);

        if (!flatValues.containsKey("description") ||
                !flatValues.containsKey("password") ||
                !flatValues.containsKey("allsuperuser")) {
            return;
        }

        Room room = RoomManager.getInstance().getRoom(roomId);

        if (room == null || !room.isOwner(player.getDetails().getId())) {
            return;
        }

        room.getData().setDescription(flatValues.get("description"));
        room.getData().setPassword(flatValues.get("password"));
        room.getData().setSuperUsers(flatValues.get("allsuperuser").equals("1"));

        RoomDao.saveRoom(room.getData());
    }
}
