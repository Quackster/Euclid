package euclid.messages.outgoing;

import euclid.game.item.ActiveItem;
import euclid.messages.IMessageComposer;

import java.util.List;

public class ACTIVE_OBJECTS extends IMessageComposer {

    private final List<ActiveItem> items;

    public ACTIVE_OBJECTS(List<ActiveItem> items) {
        this.items = items;
    }

    @Override
    public void write() {
        for (ActiveItem item : items) {
            item.serialise(getData());
        }
    }
}
