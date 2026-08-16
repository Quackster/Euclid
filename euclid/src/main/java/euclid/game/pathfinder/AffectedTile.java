package euclid.game.pathfinder;

import euclid.game.item.Item;

import java.util.ArrayList;
import java.util.List;

public class AffectedTile {

    public static List<Position> getAffectedTiles(Item item) {
        return getAffectedTiles(
                item.getDefinition().getData().getLength(),
                item.getDefinition().getData().getWidth(),
                item.getPosition().getX(),
                item.getPosition().getY(),
                item.getPosition().getRotation());
    }

    public static List<Position> getAffectedTiles(Item item, int x, int y, int rotation) {
        return getAffectedTiles(
                item.getDefinition().getData().getLength(),
                item.getDefinition().getData().getWidth(),
                x, y, rotation);
    }

    public static List<Position> getAffectedTiles(int length, int width, int x, int y, int rotation) {
        List<Position> points = new ArrayList<>();

        if (length != width) {
            if (rotation == 0 || rotation == 4) {
                int l = length;
                length = width;
                width = l;
            }
        }

        for (int newX = x; newX < x + width; newX++) {
            for (int newY = y; newY < y + length; newY++) {
                Position pos = new Position(newX, newY);
                points.add(pos);
            }
        }

        return points;
    }
}
