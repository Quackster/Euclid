package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.item.Item;
import euclid.game.pathfinder.Position;
import euclid.game.room.Room;
import euclid.messages.IMessageEvent;
import euclid.messages.outgoing.ACTIVEOBJECT_UPDATE;
import euclid.network.streams.Request;

public class MOVESTUFF implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        int itemId = Integer.parseInt(request.getArgument(0));

        if (player.getRoomEntity().getRoom() == null) {
            return;
        }

        Room room = player.getRoomEntity().getRoom();

        if (room == null) {
            return;
        }

        Item item = room.getItemManager().getItem(itemId);

        if (item == null || item.getData().getOwnerId() != player.getDetails().getId()) {
            return;
        }

        int x = Integer.parseInt(request.getArgument(1));
        int y = Integer.parseInt(request.getArgument(2));
        int rotation = Integer.parseInt(request.getArgument(3));

        Position oldPosition = item.getPosition().copy();

        if ((oldPosition.getX() == x &&
                oldPosition.getY() == y &&
                oldPosition.getRotation() == rotation) || !item.isValidMove(item, room, x, y, rotation)) {
            if (!new Position(x, y).equals(item.getPosition())) {
                player.send(new ACTIVEOBJECT_UPDATE(item));
                return;
            }
        }

        if (rotation % 2 != 0 || rotation < 0 || rotation > 6) {
            rotation = 0;
        }

        room.getFurnitureManager().moveItem(item, new Position(x, y, rotation));
    }
}
