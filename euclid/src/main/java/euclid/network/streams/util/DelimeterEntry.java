package euclid.network.streams.util;

public final class DelimeterEntry {

    private final String value;
    private final String delimiter;

    public DelimeterEntry(Object value, Object delimeter) {
        this.value = (value == null ? "" : value.toString());
        this.delimiter = (delimeter == null ? "" : delimeter.toString());
    }

    public String getValue() {
        return value;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
