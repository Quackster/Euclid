package euclid.network.codec;

import euclid.network.streams.Request;
import euclid.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class NetworkDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> output) {
        if (buffer.readableBytes() < 5) {
            return;
        }

        byte[] lengthBytes = new byte[4];
        buffer.readBytes(lengthBytes);
        int length = Integer.parseInt(new String(lengthBytes, StringUtil.getEncoding()).trim());

        byte[] message = new byte[length];
        buffer.readBytes(message);
        String content = new String(message, StringUtil.getEncoding());

        String header;
        String request;

        if (content.contains(" ")) {
            header = content.split(" ", 2)[0];
            request = content.substring(header.length() + 1);
        } else {
            header = content;
            request = "";
        }

        output.add(new Request(header, request));
    }
}
