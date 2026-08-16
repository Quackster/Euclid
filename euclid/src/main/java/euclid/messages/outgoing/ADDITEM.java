package euclid.messages.outgoing;

import euclid.game.item.Item;
import euclid.messages.IMessageComposer;

public class ADDITEM extends IMessageComposer {

    private final Item item;

    public ADDITEM(Item item) {
        this.item = item;
    }

    @Override
    public void write() {
        item.serialise(getData());
    }
}
