package euclid.messages.outgoing;

import euclid.game.item.Item;
import euclid.game.room.RoomModel;
import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;

import java.util.List;

public class OBJECTS extends IMessageComposer {

    private final RoomModel model;
    private final List<euclid.game.item.PassiveItem> passiveItems;

    public OBJECTS(RoomModel model, List<euclid.game.item.PassiveItem> passiveItems) {
        this.model = model;
        this.passiveItems = passiveItems;
    }

    @Override
    public void write() {
        getData().add(new DelimeterEntry("WORLD", " "));
        getData().add(new DelimeterEntry("0", " "));
        getData().add(new DelimeterEntry(model.getData().getModel(), " "));

        for (euclid.game.item.PassiveItem item : passiveItems) {
            getData().add(new ArgumentEntry(item.getData().getCustomData()));
            getData().add(new DelimeterEntry(item.getDefinition().getData().getSprite(), " "));
            getData().add(new DelimeterEntry(item.getPosition().getX(), " "));
            getData().add(new DelimeterEntry(item.getPosition().getY(), " "));
            getData().add(new DelimeterEntry(item.getPosition().getZ(), " "));

            if (item.getDefinition().getData().getLength() > 1 ||
                    item.getDefinition().getData().getWidth() > 1) {
                getData().add(new DelimeterEntry(item.getDefinition().getData().getLength(), " "));
                getData().add(new DelimeterEntry(item.getDefinition().getData().getWidth(), " "));
            } else {
                getData().add(new DelimeterEntry(item.getPosition().getRotation(), " "));
            }
        }
    }
}
