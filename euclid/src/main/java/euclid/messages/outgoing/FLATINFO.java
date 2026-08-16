package euclid.messages.outgoing;

import euclid.game.room.Room;
import euclid.messages.IMessageComposer;
import euclid.network.streams.util.KeyValueEntry;
import euclid.storage.database.data.RoomStatus;

public class FLATINFO extends IMessageComposer {

    private final Room room;

    public FLATINFO(Room room) {
        this.room = room;
    }

    @Override
    public void write() {
        getData().add(new KeyValueEntry("name", room.getData().getName(), "="));
        getData().add(new KeyValueEntry("password", room.getData().getPassword(), "="));
        getData().add(new KeyValueEntry("description", room.getData().getDescription(), "="));
        getData().add(new KeyValueEntry("showOwnerName", room.getData().isShowName() ? "true" : "false", "="));
        getData().add(new KeyValueEntry("allsuperuser", room.getData().isSuperUsers() ? "true" : "false", "="));

        if (room.getData().getAccessType() == RoomStatus.OPEN) {
            getData().add(new KeyValueEntry("doormode", "open", "="));
        }
        if (room.getData().getAccessType() == RoomStatus.CLOSED) {
            getData().add(new KeyValueEntry("doormode", "closed", "="));
        }
        if (room.getData().getAccessType() == RoomStatus.PASSWORD) {
            getData().add(new KeyValueEntry("doormode", "password", "="));
        }
    }
}
