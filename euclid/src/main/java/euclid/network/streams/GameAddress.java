package euclid.network.streams;

public final class GameAddress {

    private final String ipAddress;
    private final int port;
    private final int roomId;

    public GameAddress(String ipAddress, int port) {
        this(ipAddress, port, 0);
    }

    public GameAddress(String ipAddress, int port, int roomId) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.roomId = roomId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public int getRoomId() {
        return roomId;
    }
}
