package euclid.messages.outgoing;

import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;

public class WALLETBALANCE extends IMessageComposer {

    private final int credits;

    public WALLETBALANCE(int credits) {
        this.credits = credits;
    }

    @Override
    public void write() {
        getData().add(new ArgumentEntry(credits));
    }
}
