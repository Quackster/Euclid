package euclid.messages.outgoing;

import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;

public class ORDERINFO extends IMessageComposer {

    private final String id;
    private final int price;
    private final String customData;
    private final String name;

    public ORDERINFO(String id, int price, String customData, String name) {
        this.id = id;
        this.price = price;
        this.customData = customData;
        this.name = name;
    }

    @Override
    public void write() {
        getData().add(new ArgumentEntry(id));
        getData().add(new ArgumentEntry(price));
        getData().add(new ArgumentEntry(customData));
        getData().add(new ArgumentEntry(name));
    }
}
