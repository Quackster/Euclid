package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.item.Item;
import euclid.game.room.Room;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;
import euclid.util.extensions.StringExtensions;

public class PLACEITEMFROMSTRIP implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        if (player.getRoomEntity().getRoom() == null) {
            return;
        }

        Room room = player.getRoomEntity().getRoom();
        String[] placementData = request.getContent().split(" ");

        if (placementData.length < 1 || !StringExtensions.isNumeric(placementData[0])) {
            return;
        }

        int itemId = Integer.parseInt(placementData[0]);
        Item item = player.getInventory().getItem(itemId);

        if (item == null) {
            return;
        }

        if (room == null || !room.hasRights(player.getDetails().getId())) {
            return;
        }

        String[] wallPositionData = request.getContent().split("/")[0].split(" ");
        String wallPosition = wallPositionData[1] + " " + wallPositionData[2];

        room.getFurnitureManager().addItem(item, null, wallPosition, player);

        player.getInventory().removeItem(item);
    }
}
