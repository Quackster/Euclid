package euclid.messages.outgoing;

import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;

public class SECRET_KEY extends IMessageComposer {

    private final String publicKey;

    public SECRET_KEY(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public void write() {
        getData().add(new ArgumentEntry(publicKey));
    }
}
