package euclid.messages.outgoing;

import euclid.game.item.Item;
import euclid.messages.IMessageComposer;

public class ACTIVEOBJECT_UPDATE extends IMessageComposer {

    private final Item item;

    public ACTIVEOBJECT_UPDATE(Item item) {
        this.item = item;
    }

    @Override
    public void write() {
        item.serialise(getData());
    }
}
