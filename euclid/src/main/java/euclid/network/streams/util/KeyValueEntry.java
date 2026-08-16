package euclid.network.streams.util;

public final class KeyValueEntry {

    private final String key;
    private final String value;
    private final String delimiter;

    public KeyValueEntry(String key, Object value, Object delimiter) {
        this.key = key;
        this.value = value.toString();
        this.delimiter = delimiter.toString();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
