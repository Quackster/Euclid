package euclid.messages.outgoing;

import euclid.game.room.Room;
import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;

import java.util.List;

public class ALLUNITS extends IMessageComposer {

    private final List<Room> rooms;

    public ALLUNITS(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public void write() {
        for (Room room : rooms) {
            getData().add(new ArgumentEntry(room.getData().getName()));
            getData().add(new DelimeterEntry(room.getData().getUsersNow(), ","));
            getData().add(new DelimeterEntry(room.getData().getUsersMax(), ","));
            getData().add(new DelimeterEntry(room.getAddress().getIpAddress(), ","));
            getData().add(new DelimeterEntry(room.getAddress().getIpAddress(), "/"));
            getData().add(new DelimeterEntry(room.getAddress().getPort(), ","));
            getData().add(new DelimeterEntry(room.getData().getName(), ","));
            getData().add(new DelimeterEntry(room.getData().getDescription(), ","));
            getData().add(new DelimeterEntry(room.getData().getUsersNow(), ","));
            getData().add(new DelimeterEntry(room.getData().getUsersMax(), ","));
            getData().add(new DelimeterEntry(room.getModel().getData().getModel(), ","));
        }
    }
}
