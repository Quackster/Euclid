using System;
using System.Collections.Generic;
using DotNetty.Buffers;
using DotNetty.Codecs;
using DotNetty.Transport.Channels;
using Euclid.Messages;
using Euclid.Network.Session;
using Euclid.Network.Streams;
using Euclid.Network.Streams.Util;
using Euclid.Util;

namespace Euclid.Network.Codec
{
    internal class NetworkEncoder : MessageToMessageEncoder<IMessageComposer>
    {
        protected override void Encode(IChannelHandlerContext ctx, IMessageComposer composer, List<object> output)
        {
            ConnectionSession connection = ctx.Channel.GetAttribute(GameNetworkHandler.CONNECTION_KEY).Get();

            try
            {
                string header = MessageHandler.Instance.GetComposerId(composer);

                if (header == null)
                {
                    connection.Player.Log.Error($"No header found for composer class {composer.GetType().Name}");
                    return;
                }

                var buffer = Unpooled.Buffer();
                var response = new Response(header, buffer);

                foreach (var objectData in composer.Data)
                {
                    if (objectData is string || objectData is int || objectData is uint || objectData is bool)
                        response.Write(objectData.ToString());

                    if (objectData is ArgumentEntry entry)
                    {
                        response.Write((char)13);
                        response.Write(entry.Value);
                    }

                    if (objectData is KeyValueEntry kve)
                    {
                        response.Write((char)13);
                        response.Write(kve.Key);
                        response.Write(kve.Delimiter);
                        response.Write(kve.Value);
                    }

                    if (objectData is DelimeterEntry tab)
                    {
                        response.Write(tab.Delimiter.ToString());
                        response.Write(tab.Value);
                    }
                }

                buffer.WriteBytes(StringUtil.GetEncoding().GetBytes("#"));
                buffer.WriteBytes(StringUtil.GetEncoding().GetBytes("#"));

                if (connection != null)
                    connection.Player.Log.Debug($"SENT {composer.GetType().Name}: " + response.Header + " / " + response.MessageBody);

                output.Add(buffer);
            }
            catch (Exception ex)
            {
                connection.Player.Log.Error("Error occurred: ", ex);
            }
        }
    }
}