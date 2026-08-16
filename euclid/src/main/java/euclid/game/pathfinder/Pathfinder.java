package euclid.game.pathfinder;

import euclid.game.entity.Humanoid;
import euclid.game.room.Room;
import euclid.game.room.RoomTile;

import java.util.ArrayList;
import java.util.List;

public class Pathfinder {

    private static final boolean NO_DIAG = false;
    private static final double MAX_DROP_HEIGHT = 3.0;
    private static final double MAX_LIFT_HEIGHT = 1.5;

    public static Position[] MOVE_POINTS = {
            new Position(-1, -1),
            new Position(0, -1),
            new Position(1, -1),
            new Position(1, 0),
            new Position(1, 1),
            new Position(0, 1),
            new Position(-1, 1),
            new Position(-1, 0)
    };

    private static final Position[] NO_DIAG_MOVE_POINTS = {
            new Position(0, -1),
            new Position(1, 0),
            new Position(0, 1),
            new Position(-1, 0)
    };

    public static List<Position> findPath(Humanoid entity, Room room, Position start, Position end) {
        List<Position> path = new ArrayList<>();

        PathfinderNode nodes = findPathReversed(entity, room, end, start);

        if (nodes != null) {
            while (nodes.getNext() != null) {
                path.add(nodes.getNext().getPosition());
                nodes = nodes.getNext();
            }
        }

        return path;
    }

    private static PathfinderNode findPathReversed(Humanoid entity, Room room, Position start, Position end) {
        MinHeap<PathfinderNode> openList = new MinHeap<>(256);

        PathfinderNode[][] map = new PathfinderNode[room.getModel().getMapSizeX()][room.getModel().getMapSizeY()];

        PathfinderNode current = new PathfinderNode(start);
        current.setCost(0);

        PathfinderNode finish = new PathfinderNode(end);
        map[current.getPosition().getX()][current.getPosition().getY()] = current;
        openList.add(current);

        while (openList.getCount() > 0) {
            current = openList.extractFirst();
            current.setInClosed(true);

            Position[] movePoints = NO_DIAG ? NO_DIAG_MOVE_POINTS : MOVE_POINTS;

            for (Position movePoint : movePoints) {
                Position tmp = new Position(
                        current.getPosition().getX() + movePoint.getX(),
                        current.getPosition().getY() + movePoint.getY()
                );

                boolean isFinalMove = (tmp.getX() == end.getX() && tmp.getY() == end.getY());

                if (isValidStep(room, entity, new Position(current.getPosition().getX(), current.getPosition().getY()), tmp, isFinalMove)) {
                    if (tmp.getX() < 0 || tmp.getX() >= map.length || tmp.getY() < 0 || tmp.getY() >= map[0].length)
                        continue;

                    PathfinderNode node;
                    if (map[tmp.getX()][tmp.getY()] == null) {
                        node = new PathfinderNode(tmp);
                        map[tmp.getX()][tmp.getY()] = node;
                    } else {
                        node = map[tmp.getX()][tmp.getY()];
                    }

                    if (!node.isInClosed()) {
                        int diff = 0;

                        if (current.getPosition().getX() != node.getPosition().getX()) {
                            diff += 2;
                        }

                        if (current.getPosition().getY() != node.getPosition().getY()) {
                            diff += 2;
                        }

                        int cost = current.getCost() + diff + node.getPosition().getDistanceSquared(end);

                        if (cost < node.getCost()) {
                            node.setCost(cost);
                            node.setNext(current);
                        }

                        if (!node.isInOpen()) {
                            if (node.equals(finish)) {
                                node.setNext(current);
                                return node;
                            }

                            node.setInOpen(true);
                            openList.add(node);
                        }
                    }
                }
            }
        }

        return null;
    }

    public static boolean isValidStep(Room room, Humanoid entity, Position from, Position to, boolean isFinalMove) {
        if (!RoomTile.isValidTile(room, entity, to))
            return false;

        if (!RoomTile.isValidTile(room, entity, from))
            return false;

        RoomTile fromTile = from.getTile(room);
        RoomTile toTile = to.getTile(room);

        if (fromTile == null || toTile == null)
            return false;

        double oldHeight = fromTile.getWalkingHeight();
        double newHeight = toTile.getWalkingHeight();

        if (toTile.isHeightUpwards(fromTile)) {
            if (Math.abs(newHeight - oldHeight) > MAX_LIFT_HEIGHT) {
                return false;
            }
        }

        if (toTile.isHeightDrop(fromTile)) {
            if (Math.abs(oldHeight - newHeight) > MAX_DROP_HEIGHT) {
                return false;
            }
        }

        return true;
    }
}
