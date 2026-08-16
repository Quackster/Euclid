package euclid.messages.outgoing;

import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;

public class SYSTEMBROADCAST extends IMessageComposer {

    private final String message;

    public SYSTEMBROADCAST(String message) {
        this.message = message;
    }

    @Override
    public void write() {
        getData().add(new ArgumentEntry(message));
    }
}
