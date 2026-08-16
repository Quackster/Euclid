package euclid.messages.outgoing;

import euclid.game.item.Item;
import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;

public class ACTIVEOBJECT_REMOVE extends IMessageComposer {

    private final Item item;

    public ACTIVEOBJECT_REMOVE(Item item) {
        this.item = item;
    }

    @Override
    public void write() {
        getData().add(new ArgumentEntry(item.getPaddedId()));
        getData().add(new DelimeterEntry(item.getData().getId(), ""));
    }
}
