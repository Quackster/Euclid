package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.item.Item;
import euclid.game.room.Room;
import euclid.messages.IMessageEvent;
import euclid.messages.outgoing.FLATPROPERTY;
import euclid.network.streams.Request;
import euclid.storage.database.access.ItemDao;
import euclid.storage.database.access.RoomDao;

public class FLATPROPERTYBYITEM implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        Room room = player.getRoomEntity().getRoom();

        if (room == null || !room.isOwner(player.getDetails().getId())) {
            return;
        }

        Item item = player.getInventory().getItem(Integer.parseInt(request.getArgument(2, "/")));

        if (item == null || !item.getDefinition().getData().isDecoration()) {
            return;
        }

        switch (item.getDefinition().getData().getSprite()) {
            case "floor":
                room.getData().setFloor(Integer.parseInt(item.getData().getCustomData()));
                break;
            case "wallpaper":
                room.getData().setWallpaper(Integer.parseInt(item.getData().getCustomData()));
                break;
        }

        player.getInventory().removeItem(item);
        player.getInventory().turnPage("update");

        RoomDao.saveRoom(room.getData());
        ItemDao.deleteItem(item.getData());

        player.send(new FLATPROPERTY(item.getDefinition().getData().getSprite(), item.getData().getCustomData()));
    }
}
