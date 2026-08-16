package euclid.game.item;

import euclid.game.entity.IEntity;
import euclid.game.pathfinder.AffectedTile;
import euclid.game.pathfinder.Position;
import euclid.game.room.Room;
import euclid.game.room.RoomManager;
import euclid.game.room.RoomTile;
import euclid.game.room.TileState;
import euclid.game.entity.Humanoid;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;
import euclid.storage.database.data.ItemData;

import java.util.ArrayList;
import java.util.List;

public class Item implements IEntity {

    private final ItemData data;
    private ItemDefinition definition;
    private Position position;

    public Item(ItemData data) {
        this.data = data;
        this.position = new Position(data.getX(), data.getY(), data.getZ(), data.getRotation(), data.getRotation());
    }

    public ItemData getData() {
        return data;
    }

    public ItemDefinition getDefinition() {
        if (definition != null)
            return definition;
        return ItemManager.getInstance().getDefinition(data.getDefinitionId());
    }

    public void setDefinition(ItemDefinition definition) {
        this.definition = definition;
    }

    public Room getRoom() {
        return RoomManager.getInstance().getRoom(data.getRoomId());
    }

    public RoomTile getCurrentTile() {
        return position != null ? (position.getTile(getRoom()) != null ? position.getTile(getRoom()) : null) : null;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getPaddedId() {
        String id = String.valueOf(data.getId());
        int spriteLen = getSprite().length();
        if (id.length() < spriteLen) {
            id = "0".repeat(spriteLen - id.length()) + id;
        }
        return id;
    }

    public String getSprite() {
        String sprite = getDefinition().getData().getSprite();
        return sprite.contains("*") ? sprite.split("\\*")[0] : sprite;
    }

    public void serialise(List<Object> data) {
        if (getDefinition().getData().isFloorItem()) {
            data.add(new ArgumentEntry(getPaddedId()));
            data.add(new DelimeterEntry(this.data.getId(), ""));
            data.add(new DelimeterEntry(getSprite(), ","));
            data.add(new DelimeterEntry(position.getX(), " "));
            data.add(new DelimeterEntry(position.getY(), " "));
            data.add(new DelimeterEntry(getDefinition().getData().getLength(), " "));
            data.add(new DelimeterEntry(getDefinition().getData().getWidth(), " "));
            data.add(new DelimeterEntry(position.getRotation(), " "));
            data.add(new DelimeterEntry(position.getZ(), " "));
            data.add(new DelimeterEntry(getDefinition().getData().getColour(), " "));
            data.add(new DelimeterEntry(getDefinition().getData().getName(), "/"));
            data.add(new DelimeterEntry(getDefinition().getData().getDescription(), "/"));
            data.add(new DelimeterEntry(getDefinition().getData().getDataClass(), "/"));
            data.add(new DelimeterEntry(this.data.getCustomData(), "/"));
        } else {
            data.add(new ArgumentEntry(this.data.getId()));
            data.add(new DelimeterEntry(getSprite(), ";"));
            data.add(new DelimeterEntry(null, ";"));
            data.add(new DelimeterEntry(this.data.getWallPosition(), ";"));
            data.add(new ArgumentEntry(this.data.getCustomData()));
            data.add("\\");
        }
    }

    public void updateEntities(Position position) {
        List<Humanoid> entities = new ArrayList<>();

        for (Position affectedPosition : AffectedTile.getAffectedTiles(this)) {
            RoomTile tile = affectedPosition.getTile(getRoom());
            if (tile == null)
                continue;
            entities.addAll(tile.getEntities().values());
        }

        if (position != null) {
            for (Position affectedPosition : AffectedTile.getAffectedTiles(this, position.getX(), position.getY(), position.getRotation())) {
                RoomTile tile = affectedPosition.getTile(getRoom());
                if (tile == null)
                    continue;
                entities.addAll(tile.getEntities().values());
            }
        }

        for (Humanoid entity : entities)
            entity.getRoomEntity().interactItem();
    }

    public void updateEntities() {
        updateEntities(null);
    }

    public boolean isWalkable(Position position) {
        if (getDefinition().getData().isWalkable())
            return true;
        if (getDefinition().getData().isChair())
            return true;
        if (getDefinition().getData().isBed())
            return true;
        return false;
    }

    public double getHeight() {
        return getDefinition().getData().getHeight() + position.getZ() + 0.001;
    }

    public void applyPosition() {
        position = new Position(data.getX(), data.getY(), data.getZ(), data.getRotation(), data.getRotation());
    }

    public boolean isValidMove(Item item, Room room, int x, int y, int rotation) {
        RoomTile tile = new Position(x, y).getTile(room);

        if (tile == null || room == null)
            return false;

        boolean isRotation = (item.getPosition().getRotation() != rotation) && (new Position(x, y).equals(item.getPosition()));

        if (isRotation) {
            if (item.getDefinition().getData().getLength() <= 1 && item.getDefinition().getData().getWidth() <= 1) {
                return true;
            }
        }

        for (Position pos : AffectedTile.getAffectedTiles(this, x, y, rotation)) {
            tile = pos.getTile(room);

            if (tile == null || !room.getModel().isTile(pos))
                return false;

            if (room.getModel().getTileStates()[pos.getX()][pos.getY()] == TileState.CLOSED)
                return false;

            if (!isRotation) {
                if (tile.getEntities().size() > 0)
                    return false;
            }

            Item highestItem = tile.getHighestItem();

            if (highestItem != null && highestItem.getData().getId() != item.getData().getId()) {
                if (!canItemsMerge(item, highestItem, new Position(x, y)))
                    return false;
            }

            for (Item tileItem : tile.getFurniture().values()) {
                if (tileItem.getData().getId() == item.getData().getId())
                    continue;
                if (!canItemsMerge(item, tileItem, new Position(x, y)))
                    return false;
            }
        }

        return true;
    }

    private boolean canItemsMerge(Item item, Item tileItem, Position targetTile) {
        if (!tileItem.getDefinition().getData().isStackable())
            return false;
        if (tileItem.getDefinition().getData().isChair())
            return false;
        if (tileItem.getDefinition().getData().isBed())
            return false;
        return true;
    }
}
