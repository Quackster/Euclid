package euclid.messages.outgoing;

import euclid.messages.IMessageComposer;
import euclid.network.GameServer;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;
import euclid.storage.database.data.RoomData;

public class FLATCREATED extends IMessageComposer {

    private final RoomData roomData;

    public FLATCREATED(RoomData roomData) {
        this.roomData = roomData;
    }

    @Override
    public void write() {
        GameServer server = GameServer.getInstance();
        getData().add(new ArgumentEntry(roomData.getId()));
        getData().add(new DelimeterEntry(server.getPrivateServer().getIpAddress(), " "));
        getData().add(new DelimeterEntry(server.getPrivateServer().getPort(), " "));
        getData().add(new DelimeterEntry(roomData.getName(), " "));
    }
}
