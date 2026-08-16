package euclid.game.pathfinder;

public class Rotation {

    public static int calculateDirection(Position from, Position to) {
        return calculateDirection(from.getX(), from.getY(), to.getX(), to.getY());
    }

    public static int calculateDirection(int x, int y, int toX, int toY) {
        if (x == toX) {
            if (y < toY) return 4;
            else return 0;
        } else if (x > toX) {
            if (y == toY) return 6;
            else if (y < toY) return 5;
            else return 7;
        } else {
            if (y == toY) return 2;
            else if (y < toY) return 3;
            else return 1;
        }
    }
}
