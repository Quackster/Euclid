package euclid.network.streams;

import euclid.util.StringUtil;
import euclid.util.specialised.WireEncoding;
import io.netty.buffer.ByteBuf;

public class Response {

    private final String header;
    private final ByteBuf buffer;

    public Response(String header, ByteBuf buffer) {
        this.header = header;
        this.buffer = buffer;

        buffer.writeBytes(StringUtil.getEncoding().encode("#"));
        buffer.writeBytes(StringUtil.getEncoding().encode(" "));
        buffer.writeBytes(StringUtil.getEncoding().encode(header));
    }

    public String getHeader() {
        return header;
    }

    public ByteBuf getBuffer() {
        return buffer;
    }

    public String getMessageBody() {
        String consoleText = buffer.toString(StringUtil.getEncoding());
        for (int i = 0; i < 14; i++) {
            consoleText = consoleText.replace(String.valueOf((char) i), "[" + i + "]");
        }
        return consoleText;
    }

    public void write(Object obj) {
        buffer.writeBytes(StringUtil.getEncoding().encode(obj.toString()));
    }

    public void writeInt(int obj) {
        buffer.writeBytes(WireEncoding.encodeInt32(obj));
    }

    public void writeBool(boolean obj) {
        writeInt(obj ? 1 : 0);
    }
}
