package euclid.network.codec;

import euclid.Euclid;
import euclid.messages.IMessageComposer;
import euclid.messages.MessageHandler;
import euclid.network.GameNetworkHandler;
import euclid.network.session.ConnectionSession;
import euclid.network.streams.Response;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;
import euclid.network.streams.util.KeyValueEntry;
import euclid.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class NetworkEncoder extends MessageToMessageEncoder<IMessageComposer> {

    @Override
    @SuppressWarnings("unchecked")
    protected void encode(ChannelHandlerContext ctx, IMessageComposer composer, List<Object> output) {
        ConnectionSession connection = (ConnectionSession) ctx.channel().attr(GameNetworkHandler.CONNECTION_KEY).get();

        try {
            String header = MessageHandler.getInstance().getComposerId(composer);

            if (header == null) {
                connection.getPlayer().getLog().error("No header found for composer class {}", composer.getClass().getSimpleName());
                return;
            }

            ByteBuf buffer = Unpooled.buffer();
            Response response = new Response(header, buffer);

            for (Object objectData : composer.getData()) {
                if (objectData instanceof String || objectData instanceof Integer || objectData instanceof Long || objectData instanceof Boolean) {
                    response.write(objectData);
                }

                if (objectData instanceof ArgumentEntry entry) {
                    response.write((char) 13);
                    response.write(entry.getValue());
                }

                if (objectData instanceof KeyValueEntry kve) {
                    response.write((char) 13);
                    response.write(kve.getKey());
                    response.write(kve.getDelimiter());
                    response.write(kve.getValue());
                }

                if (objectData instanceof DelimeterEntry tab) {
                    response.write(tab.getDelimiter());
                    response.write(tab.getValue());
                }
            }

            buffer.writeBytes(StringUtil.getEncoding().encode("#"));
            buffer.writeBytes(StringUtil.getEncoding().encode("#"));

            if (connection != null) {
                connection.getPlayer().getLog().debug("SENT {}: {} / {}", composer.getClass().getSimpleName(), response.getHeader(), response.getMessageBody());
            }

            output.add(buffer);
        } catch (Exception ex) {
            connection.getPlayer().getLog().error("Error occurred: ", ex);
        }
    }
}
