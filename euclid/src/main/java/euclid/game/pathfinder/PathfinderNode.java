package euclid.game.pathfinder;

public class PathfinderNode implements Comparable<PathfinderNode> {

    private Position position;
    private PathfinderNode next;
    private int cost = Integer.MAX_VALUE;
    private boolean inOpen = false;
    private boolean inClosed = false;

    public PathfinderNode(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public PathfinderNode getNext() {
        return next;
    }

    public void setNext(PathfinderNode next) {
        this.next = next;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isInOpen() {
        return inOpen;
    }

    public void setInOpen(boolean inOpen) {
        this.inOpen = inOpen;
    }

    public boolean isInClosed() {
        return inClosed;
    }

    public void setInClosed(boolean inClosed) {
        this.inClosed = inClosed;
    }

    public boolean equals(PathfinderNode breadcrumb) {
        return breadcrumb.getPosition().equals(this.position);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PathfinderNode) && ((PathfinderNode) obj).getPosition().equals(this.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    @Override
    public int compareTo(PathfinderNode other) {
        return Integer.compare(this.cost, other.cost);
    }
}
