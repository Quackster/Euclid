package euclid.game.room;

import euclid.game.Player;
import euclid.game.item.Item;
import euclid.game.pathfinder.Position;
import euclid.messages.outgoing.ACTIVEOBJECT_ADD;
import euclid.messages.outgoing.ACTIVEOBJECT_REMOVE;
import euclid.messages.outgoing.ACTIVEOBJECT_UPDATE;
import euclid.messages.outgoing.ADDITEM;
import euclid.messages.outgoing.REMOVEITEM;
import euclid.storage.database.access.ItemDao;

public class RoomFurniture {

    private final Room room;

    public RoomFurniture(Room room) {
        this.room = room;
    }

    public void addItem(Item item, Position position, String wallPosition, Player player) {
        item.getData().setRoomId(room.getData().getId());

        if (item.getDefinition().getData().isWallItem()) {
            item.getData().setWallPosition(wallPosition);
            room.send(new ADDITEM(item));
        } else {
            RoomTile tile = position.getTile(room);

            if (tile == null)
                return;

            position.setZ(tile.getTileHeight());

            item.getData().setX(position.getX());
            item.getData().setY(position.getY());
            item.getData().setZ(position.getZ());
            item.getData().setRotation(position.getRotation());
            item.applyPosition();

            handleItemAdjusted(item, false);

            room.send(new ACTIVEOBJECT_ADD(item));
            room.getMapping().addItem(item);
            item.updateEntities();
        }

        room.getItemManager().addItem(item);

        ItemDao.saveItem(item.getData());
    }

    public void addItem(Item item) {
        addItem(item, null, null, null);
    }

    public void moveItem(Item item, Position position, String wallPosition) {
        if (item.getDefinition().getData().isWallItem()) {
            item.getData().setWallPosition(wallPosition);
        } else {
            boolean isRotation = false;

            if (item.getPosition().equals(new Position(position.getX(), position.getY()))
                    && item.getPosition().getRotation() != position.getRotation())
                isRotation = true;

            RoomTile oldTile = item.getPosition().getTile(room);
            if (oldTile == null)
                return;

            room.getMapping().removeItem(item);

            RoomTile newTile = position.getTile(room);
            if (newTile == null)
                return;

            position.setZ(newTile.getTileHeight());

            item.getData().setX(position.getX());
            item.getData().setY(position.getY());
            item.getData().setZ(position.getZ());
            item.getData().setRotation(position.getRotation());
            item.applyPosition();

            handleItemAdjusted(item, isRotation);

            room.send(new ACTIVEOBJECT_UPDATE(item));
            room.getMapping().addItem(item);

            item.updateEntities(oldTile.getPosition());
        }

        ItemDao.saveItem(item.getData());
    }

    public void moveItem(Item item, Position position) {
        moveItem(item, position, null);
    }

    private void handleItemAdjusted(Item item, boolean isRotation) {
        RoomTile tile = item.getPosition().getTile(room);

        if (tile == null) {
            return;
        }

        if (!isRotation) {
            Item highestItem = tile.getHighestItem();
            double tileHeight = tile.getTileHeight();

            if (highestItem != null && highestItem.getData().getId() == item.getData().getId()) {
                tileHeight -= highestItem.getHeight();

                double defaultHeight = room.getModel().getTileHeights()[item.getPosition().getX()][item.getPosition().getY()];

                if (tileHeight < defaultHeight)
                    tileHeight = defaultHeight;
            }

            item.getPosition().setZ(tileHeight);
        }
    }

    public void removeItem(Item item, Player player) {
        room.getMapping().removeItem(item);

        if (item.getDefinition().getData().isWallItem()) {
            item.getData().setWallPosition("");
            room.send(new REMOVEITEM(item));
        } else {
            item.updateEntities();

            item.getData().setX(item.getPosition().getX());
            item.getData().setY(item.getPosition().getY());
            item.getData().setZ(item.getPosition().getZ());
            item.getData().setRotation(item.getPosition().getRotation());
            item.applyPosition();

            room.send(new ACTIVEOBJECT_REMOVE(item));
        }

        item.getData().setRoomId(0);

        room.getItemManager().removeItem(item);
        ItemDao.saveItem(item.getData());
    }
}
