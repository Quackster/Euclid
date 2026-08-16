package euclid.messages.outgoing;

import euclid.game.item.Item;
import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;

public class REMOVEITEM extends IMessageComposer {

    private final Item item;

    public REMOVEITEM(Item item) {
        this.item = item;
    }

    @Override
    public void write() {
        getData().add(new ArgumentEntry(item.getData().getId()));
    }
}
