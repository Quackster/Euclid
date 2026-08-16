package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.item.Item;
import euclid.game.room.Room;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;
import euclid.util.extensions.StringExtensions;

public class ADDSTRIPITEM implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        if (player.getRoomEntity().getRoom() == null) {
            return;
        }

        Room room = player.getRoomEntity().getRoom();
        String[] placementData = request.getContent().split(" ");

        if (placementData.length < 3 || !StringExtensions.isNumeric(placementData[2])) {
            return;
        }

        int itemId = Integer.parseInt(placementData[2]);
        Item item = room.getItemManager().getItem(itemId);

        if (item == null) {
            return;
        }

        if (room == null || !room.isOwner(player.getDetails().getId())) {
            return;
        }

        room.getFurnitureManager().removeItem(item, player);

        player.getInventory().addItem(item);
        player.getInventory().turnPage("last");
    }
}
