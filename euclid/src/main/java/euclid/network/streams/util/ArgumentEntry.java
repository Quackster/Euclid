package euclid.network.streams.util;

public final class ArgumentEntry {

    private final String value;

    public ArgumentEntry() {
        this.value = "";
    }

    public ArgumentEntry(Object value) {
        this.value = (value != null ? value.toString() : "");
    }

    public String getValue() {
        return value;
    }
}
