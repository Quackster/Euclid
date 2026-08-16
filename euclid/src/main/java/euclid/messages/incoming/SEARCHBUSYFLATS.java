package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.room.RoomManager;
import euclid.messages.IMessageEvent;
import euclid.messages.outgoing.BUSY_FLAT_RESULTS;
import euclid.network.streams.Request;
import euclid.storage.database.access.RoomDao;

public class SEARCHBUSYFLATS implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        int page = 0;
        int maxResults = 11;

        if (request.getArgumentAmount(",") > 0) {
            page = Integer.parseInt(request.getArgument(0, ",").replace("/", ""));
            maxResults = Integer.parseInt(request.getArgument(1, ","));
        }

        player.send(new BUSY_FLAT_RESULTS(RoomManager.getInstance().replaceQueryRooms(
                RoomDao.getPopularFlats(page / 11, maxResults))));
    }
}
