package euclid.game.pathfinder;

import euclid.game.entity.Humanoid;
import euclid.game.room.Room;
import euclid.game.room.RoomTile;
import euclid.game.room.TileState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Position {

    private int x;
    private int y;
    private double z;
    private int bodyRotation;
    private int headRotation;

    public Position() {
        this(0, 0, 0);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Position(int x, int y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(int x, int y, double z, int headRotation, int bodyRotation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.headRotation = headRotation;
        this.bodyRotation = bodyRotation;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getRotation() {
        return bodyRotation;
    }

    public void setRotation(int rotation) {
        this.bodyRotation = rotation;
        this.headRotation = rotation;
    }

    public int getBodyRotation() {
        return bodyRotation;
    }

    public void setBodyRotation(int bodyRotation) {
        this.bodyRotation = bodyRotation;
    }

    public int getHeadRotation() {
        return headRotation;
    }

    public void setHeadRotation(int headRotation) {
        this.headRotation = headRotation;
    }

    public RoomTile getTile(Room room) {
        if (room == null || room.getModel() == null || !room.getModel().isTile(this))
            return null;

        if (room.getModel().getTileStates()[this.x][this.y] == TileState.CLOSED)
            return null;

        return room.getMapping().getTiles()[this.x][this.y];
    }

    public boolean touches(Position position) {
        return getDistanceSquared(position) <= 2;
    }

    public Position add(Position other) {
        return new Position(other.getX() + x, other.getY() + y, other.getZ() + z);
    }

    public Position subtract(Position other) {
        return new Position(other.getX() - x, other.getY() - y, other.getZ() - z);
    }

    public Position closestTile(Room room, Position other, Humanoid entity) {
        List<Position> potentialTiles = new ArrayList<>();

        for (int i = 0; i < Pathfinder.MOVE_POINTS.length; i++)
            potentialTiles.add(copy().add(Pathfinder.MOVE_POINTS[i]));

        potentialTiles.removeIf(pos -> !RoomTile.isValidTile(room, entity, pos, false));

        if (!potentialTiles.isEmpty()) {
            potentialTiles.sort(Comparator.comparingInt(pos -> pos.getDistanceSquared(other)));
            return potentialTiles.get(0);
        }

        return null;
    }

    public int getDistanceSquared(Position point) {
        int dx = x - point.getX();
        int dy = y - point.getY();
        return (int) Math.sqrt((dx * dx) + (dy * dy));
    }

    public Position getSquareInFront() {
        Position square = copy();

        if (bodyRotation == 0) {
            square.y--;
        } else if (bodyRotation == 1) {
            square.x++;
            square.y--;
        } else if (bodyRotation == 2) {
            square.x++;
        } else if (bodyRotation == 3) {
            square.x++;
            square.y++;
        } else if (bodyRotation == 4) {
            square.y++;
        } else if (bodyRotation == 5) {
            square.x--;
            square.y++;
        } else if (bodyRotation == 6) {
            square.x--;
        } else if (bodyRotation == 7) {
            square.x--;
            square.y--;
        }

        return square;
    }

    public Position getSquareBehind() {
        Position square = copy();

        if (bodyRotation == 0) {
            square.y++;
        } else if (bodyRotation == 1) {
            square.x--;
            square.y++;
        } else if (bodyRotation == 2) {
            square.x--;
        } else if (bodyRotation == 3) {
            square.x--;
            square.y--;
        } else if (bodyRotation == 4) {
            square.y--;
        } else if (bodyRotation == 5) {
            square.x++;
            square.y--;
        } else if (bodyRotation == 6) {
            square.x++;
        } else if (bodyRotation == 7) {
            square.x++;
            square.y++;
        }

        return square;
    }

    public Position getSquareRight() {
        Position square = copy();

        if (bodyRotation == 0) {
            square.x++;
        } else if (bodyRotation == 1) {
            square.x++;
            square.y++;
        } else if (bodyRotation == 2) {
            square.y++;
        } else if (bodyRotation == 3) {
            square.x--;
            square.y++;
        } else if (bodyRotation == 4) {
            square.x--;
        } else if (bodyRotation == 5) {
            square.x--;
            square.y--;
        } else if (bodyRotation == 6) {
            square.y--;
        } else if (bodyRotation == 7) {
            square.x++;
            square.y--;
        }

        return square;
    }

    public Position getSquareLeft() {
        Position square = copy();

        if (bodyRotation == 0) {
            square.x--;
        } else if (bodyRotation == 1) {
            square.x--;
            square.y--;
        } else if (bodyRotation == 2) {
            square.y--;
        } else if (bodyRotation == 3) {
            square.x++;
            square.y--;
        } else if (bodyRotation == 4) {
            square.x++;
        } else if (bodyRotation == 5) {
            square.x++;
            square.y++;
        } else if (bodyRotation == 6) {
            square.y++;
        } else if (bodyRotation == 7) {
            square.x--;
            square.y++;
        }

        return square;
    }

    public Position copy() {
        return new Position(x, y, z, headRotation, bodyRotation);
    }

    public Position plus(Position other) {
        return copy().add(other);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position other = (Position) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return String.format("[X: %d, Y: %d, Z: %f, HeadRotation: %d, BodyRotation: %d]", x, y, z, headRotation, bodyRotation);
    }
}
