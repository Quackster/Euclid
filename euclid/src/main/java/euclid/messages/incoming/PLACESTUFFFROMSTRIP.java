package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.item.Item;
import euclid.game.pathfinder.Position;
import euclid.game.room.Room;
import euclid.messages.IMessageEvent;
import euclid.messages.outgoing.ACTIVEOBJECT_UPDATE;
import euclid.network.streams.Request;
import euclid.util.extensions.StringExtensions;

public class PLACESTUFFFROMSTRIP implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        if (player.getRoomEntity().getRoom() == null) {
            return;
        }

        Room room = player.getRoomEntity().getRoom();
        String[] placementData = request.getContent().split(" ");

        if (placementData.length < 4 || !StringExtensions.isNumeric(placementData[0])) {
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

        int x = (int) Double.parseDouble(placementData[1]);
        int y = (int) Double.parseDouble(placementData[2]);
        int rotation = (int) Double.parseDouble(placementData[3]);

        if (rotation % 2 != 0 || rotation < 0 || rotation > 6) {
            rotation = 0;
        }

        Position position = new Position(x, y);
        position.setRotation(0);

        if (!item.isValidMove(item, room, position.getX(), position.getY(), position.getRotation())) {
            player.send(new ACTIVEOBJECT_UPDATE(item));
            return;
        }

        room.getFurnitureManager().addItem(item, position, null, player);
        player.getInventory().removeItem(item);
    }
}
