package euclid.game.room;

import euclid.game.ILoadable;
import euclid.game.item.Item;
import euclid.game.pathfinder.AffectedTile;
import euclid.game.pathfinder.Position;

public class RoomMapping implements ILoadable {

    private final Room room;
    private final RoomModel model;
    private RoomTile[][] tiles;

    public RoomMapping(Room room) {
        this.room = room;
        this.model = room.getModel();
    }

    @Override
    public void load() {
        tiles = new RoomTile[model.getMapSizeX()][model.getMapSizeY()];

        for (int y = 0; y < model.getMapSizeY(); y++) {
            for (int x = 0; x < model.getMapSizeX(); x++) {
                tiles[x][y] = new RoomTile(
                        room,
                        new Position(x, y),
                        model.getTileHeights()[x][y],
                        model.getTileStates()[x][y]
                );
            }
        }

        for (Item item : room.getEntityManager().getEntities(Item.class)) {
            if (item.getDefinition().getData().isWallItem())
                continue;

            for (Position position : AffectedTile.getAffectedTiles(item)) {
                RoomTile tile = position.getTile(room);
                if (tile == null)
                    continue;
                tile.addItem(item);
            }
        }
    }

    public void addItem(Item item) {
        for (Position affectedPosition : AffectedTile.getAffectedTiles(item)) {
            RoomTile roomTile = affectedPosition.getTile(room);
            if (roomTile == null)
                continue;
            roomTile.addItem(item);
        }
    }

    public void removeItem(Item item) {
        for (Position affectedPosition : AffectedTile.getAffectedTiles(item)) {
            RoomTile roomTile = affectedPosition.getTile(room);
            if (roomTile == null)
                continue;
            roomTile.removeItem(item);
        }
    }

    public RoomTile[][] getTiles() {
        return tiles;
    }
}
