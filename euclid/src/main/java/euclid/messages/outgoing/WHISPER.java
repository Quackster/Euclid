package euclid.messages.outgoing;

import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;

public class WHISPER extends IMessageComposer {

    private final String name;
    private final String chatMsg;

    public WHISPER(String name, String chatMsg) {
        this.name = name;
        this.chatMsg = chatMsg;
    }

    @Override
    public void write() {
        getData().add(new ArgumentEntry(name));
        getData().add(new DelimeterEntry(chatMsg, " "));
    }
}
