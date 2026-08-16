package euclid.game.room;

import euclid.game.entity.Humanoid;
import euclid.game.item.Item;
import euclid.game.pathfinder.AffectedTile;
import euclid.game.pathfinder.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RoomTile {

    private final Room room;
    private final Position position;
    private double tileHeight;
    private final double defaultHeight;
    private double walkingHeight;
    private final TileState tileState;
    private final ConcurrentHashMap<Integer, Humanoid> entities;
    private final ConcurrentHashMap<Integer, Item> furniture;
    private Item highestItem;

    public RoomTile(Room room, Position position, double tileHeight, TileState tileState) {
        this.room = room;
        this.position = position;
        this.tileHeight = tileHeight;
        this.defaultHeight = tileHeight;
        this.tileState = tileState;
        this.walkingHeight = tileHeight;
        this.entities = new ConcurrentHashMap<>();
        this.furniture = new ConcurrentHashMap<>();
    }

    public boolean isHeightDrop(RoomTile otherTile) {
        return this.walkingHeight > otherTile.walkingHeight;
    }

    public boolean isHeightUpwards(RoomTile otherTile) {
        return this.walkingHeight < otherTile.walkingHeight;
    }

    public static boolean isValidTile(Room room, Humanoid entity, Position position, boolean lastStep) {
        if (room == null || entity == null || position == null)
            return false;

        RoomTile tile = position.getTile(room);

        if (tile == null || tile.getTileState() == TileState.CLOSED)
            return false;

        if (entity.getRoomEntity().getPosition() != null && entity.getRoomEntity().getPosition().equals(position))
            return true;

        boolean hasOtherEntity = false;
        for (Humanoid e : tile.getEntities().values()) {
            if (e != entity) {
                hasOtherEntity = true;
                break;
            }
        }
        if (hasOtherEntity)
            return false;

        if (tile.getHighestItem() != null) {
            if (!tile.getHighestItem().isWalkable(position))
                return false;
        }

        return true;
    }

    public static boolean isValidTile(Room room, Humanoid entity, Position position) {
        return isValidTile(room, entity, position, false);
    }

    public void addEntity(Humanoid entity) {
        if (entity == null)
            return;
        entities.putIfAbsent(entity.getRoomEntity().getInstanceId(), entity);
    }

    public double getWalkingHeight() {
        double height = tileHeight;

        if (highestItem != null) {
            if (highestItem.getDefinition().getData().isChair()
                    || highestItem.getDefinition().getData().isBed()) {
                height -= highestItem.getDefinition().getData().getHeight();
            }
        }

        return height;
    }

    public void removeEntity(Humanoid entity) {
        if (entity == null)
            return;
        entities.remove(entity.getRoomEntity().getInstanceId());
    }

    private void resetHighestItem() {
        highestItem = null;
        tileHeight = defaultHeight;

        for (Item item : furniture.values()) {
            if (item == null)
                continue;

            double height = item.getHeight();

            if (height < tileHeight)
                continue;

            highestItem = item;
            tileHeight = height;
        }
    }

    public void addItem(Item item) {
        if (item == null)
            return;

        furniture.putIfAbsent(item.getData().getId(), item);

        if (item.getHeight() < tileHeight)
            return;

        resetHighestItem();
    }

    public void removeItem(Item item) {
        if (item == null)
            return;

        furniture.remove(item.getData().getId());

        if (highestItem == null || item.getData().getId() != highestItem.getData().getId())
            return;

        resetHighestItem();
    }

    public List<Item> getTileItems() {
        List<Item> items = new ArrayList<>(furniture.values());
        items.sort(Comparator.comparingDouble(i -> i.getPosition().getZ()));
        return items;
    }

    public Position getPosition() {
        return position;
    }

    public double getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(double tileHeight) {
        this.tileHeight = tileHeight;
    }

    public double getDefaultHeight() {
        return defaultHeight;
    }

    public double getWalkingHeightValue() {
        return walkingHeight;
    }

    public void setWalkingHeight(double walkingHeight) {
        this.walkingHeight = walkingHeight;
    }

    public TileState getTileState() {
        return tileState;
    }

    public ConcurrentHashMap<Integer, Humanoid> getEntities() {
        return entities;
    }

    public ConcurrentHashMap<Integer, Item> getFurniture() {
        return furniture;
    }

    public Item getHighestItem() {
        return highestItem;
    }

    public void setHighestItem(Item highestItem) {
        this.highestItem = highestItem;
    }
}
