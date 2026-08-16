package euclid.messages.incoming;

import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.messages.outgoing.ALLUNITS;
import euclid.network.streams.Request;
import euclid.storage.database.access.RoomDao;
import euclid.game.room.RoomManager;

public class INITUNITLISTENER implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        player.send(new ALLUNITS(RoomManager.getInstance().replaceQueryRooms(RoomDao.getPublicFlats())));
    }
}
