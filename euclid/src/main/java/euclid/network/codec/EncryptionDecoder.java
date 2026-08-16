package euclid.network.codec;

import euclid.util.StringUtil;
import euclid.util.encryption.RC4;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class EncryptionDecoder extends ByteToMessageDecoder {

    private final RC4 rc4;

    public EncryptionDecoder(RC4 rc4) {
        this.rc4 = rc4;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> output) {
        byte[] payload = new byte[buffer.readableBytes()];
        buffer.readBytes(payload);

        String messagePayload = new String(payload, StringUtil.getEncoding());
        byte[] decodedPayload = rc4.decipher(messagePayload).getBytes(StringUtil.getEncoding());

        ByteBuf result = Unpooled.buffer();
        result.writeBytes(decodedPayload);
        output.add(result);
    }
}
