package euclid.messages.outgoing;

import euclid.game.item.Item;
import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;

public class ACTIVEOBJECT_ADD extends IMessageComposer {

    private final Item item;

    public ACTIVEOBJECT_ADD(Item item) {
        this.item = item;
    }

    @Override
    public void write() {
        item.serialise(getData());
    }
}
