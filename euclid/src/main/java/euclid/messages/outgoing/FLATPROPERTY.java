package euclid.messages.outgoing;

import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;

public class FLATPROPERTY extends IMessageComposer {

    private final String sprite;
    private final String customData;

    public FLATPROPERTY(String sprite, String customData) {
        this.sprite = sprite;
        this.customData = customData;
    }

    @Override
    public void write() {
        getData().add(new ArgumentEntry(sprite));
        getData().add(new DelimeterEntry(customData, "/"));
    }
}
