package euclid.messages.outgoing;

import euclid.game.item.Item;
import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;

import java.util.List;

public class STRIPINFO extends IMessageComposer {

    private final int stripSlotId;
    private final int totalItems;
    private final List<Item> items;

    public STRIPINFO(int stripSlotId, int totalItems, List<Item> items) {
        this.stripSlotId = stripSlotId;
        this.totalItems = totalItems;
        this.items = items;
    }

    @Override
    public void write() {
        int slotId = stripSlotId;
        for (Item item : items) {
            getData().add(new ArgumentEntry("SI"));
            getData().add(new DelimeterEntry(item.getData().getId(), ";"));
            getData().add(new DelimeterEntry(slotId, ";"));

            if (item.getDefinition().getData().isFloorItem()) {
                getData().add(new DelimeterEntry("S", ";"));
            }

            if (item.getDefinition().getData().isWallItem()) {
                getData().add(new DelimeterEntry("I", ";"));
            }

            getData().add(new DelimeterEntry(item.getData().getId(), ";"));
            getData().add(new DelimeterEntry(item.getDefinition().getData().getSprite(), ";"));
            getData().add(new DelimeterEntry(item.getDefinition().getData().getName(), ";"));

            if (item.getDefinition().getData().isFloorItem()) {
                getData().add(new DelimeterEntry(item.getData().getCustomData(), ";"));
                getData().add(new DelimeterEntry(item.getDefinition().getData().getLength(), ";"));
                getData().add(new DelimeterEntry(item.getDefinition().getData().getWidth(), ";"));
                getData().add(new DelimeterEntry(item.getDefinition().getData().getColour(), ";"));
            }

            if (item.getDefinition().getData().isWallItem()) {
                getData().add(new DelimeterEntry(item.getData().getCustomData(), ";"));
                getData().add(new DelimeterEntry(item.getDefinition().getData().getName(), ";"));
            }

            getData().add(new DelimeterEntry("", "/"));
            slotId++;
        }

        getData().add(new ArgumentEntry(totalItems));
    }
}
