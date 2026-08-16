package euclid.game.room;

public class RoomUserStatus {

    private final String key;
    private final String value;

    public RoomUserStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
