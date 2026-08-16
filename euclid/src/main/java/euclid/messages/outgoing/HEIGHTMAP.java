package euclid.messages.outgoing;

import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;

public class HEIGHTMAP extends IMessageComposer {

    private final String heightmap;

    public HEIGHTMAP(String heightmap) {
        this.heightmap = heightmap;
    }

    @Override
    public void write() {
        getData().add(new ArgumentEntry(heightmap));
    }
}
