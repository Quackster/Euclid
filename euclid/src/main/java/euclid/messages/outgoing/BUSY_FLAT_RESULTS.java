package euclid.messages.outgoing;

import euclid.game.room.Room;
import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;
import euclid.storage.database.data.RoomStatus;

import java.util.List;

public class BUSY_FLAT_RESULTS extends IMessageComposer {

    private final List<Room> rooms;

    public BUSY_FLAT_RESULTS(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public void write() {
        for (Room room : rooms) {
            getData().add(new ArgumentEntry(room.getData().getId()));
            getData().add(new DelimeterEntry(room.getData().getName(), "/"));

            if (room.getData().isShowName()) {
                getData().add(new DelimeterEntry(room.getData().getOwnerData() != null ? room.getData().getOwnerData().getName() : "", "/"));
            } else {
                getData().add(new DelimeterEntry("-", "/"));
            }

            if (room.getData().getAccessType() == RoomStatus.OPEN) {
                getData().add(new DelimeterEntry("open", "/"));
            }
            if (room.getData().getAccessType() == RoomStatus.CLOSED) {
                getData().add(new DelimeterEntry("closed", "/"));
            }
            if (room.getData().getAccessType() == RoomStatus.PASSWORD) {
                getData().add(new DelimeterEntry("password", "/"));
            }

            getData().add(new DelimeterEntry("", "/"));
            getData().add(new DelimeterEntry("Floor1", "/"));
            getData().add(new DelimeterEntry(room.getAddress().getIpAddress(), "/"));
            getData().add(new DelimeterEntry(room.getAddress().getIpAddress(), "/"));
            getData().add(new DelimeterEntry(room.getAddress().getPort(), "/"));
            getData().add(new DelimeterEntry(room.getData().getUsersNow(), "/"));
            getData().add(new DelimeterEntry("null", "/"));
            getData().add(new DelimeterEntry(room.getData().getDescription(), "/"));
        }
    }
}
