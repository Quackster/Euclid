package euclid.storage.database.data;

public enum RoomStatus {
    OPEN(0),
    CLOSED(1),
    PASSWORD(2);

    private final int value;

    RoomStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RoomStatus toStatusEnum(int roomAccess) {
        return switch (roomAccess) {
            case 1 -> CLOSED;
            case 2 -> PASSWORD;
            default -> OPEN;
        };
    }
}
