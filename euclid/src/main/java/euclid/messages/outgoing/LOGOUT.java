package euclid.messages.outgoing;

import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;

public class LOGOUT extends IMessageComposer {

    private final String name;

    public LOGOUT(String name) {
        this.name = name;
    }

    @Override
    public void write() {
        getData().add(new ArgumentEntry(name));
    }
}
