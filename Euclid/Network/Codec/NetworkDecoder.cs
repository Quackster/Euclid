using DotNetty.Buffers;
using DotNetty.Codecs;
using DotNetty.Transport.Channels;
using Euclid.Network.Streams;
using Euclid.Util;
using System.Collections.Generic;

namespace Euclid.Network
{
    internal class NetworkDecoder : ByteToMessageDecoder
    {
        protected override void Decode(IChannelHandlerContext ctx, IByteBuffer buffer, List<object> output)
        {
            if (buffer.ReadableBytes < 5)
            {
                // If the incoming data is less than 6 bytes, it's junk.
                return;
            }

            byte[] message_length = buffer.ReadBytes(4).Array;
            int.TryParse(StringUtil.GetEncoding().GetString(message_length).Trim(), out int length);

            byte[] message = buffer.ReadBytes(length).Array;
            string content = StringUtil.GetEncoding().GetString(message);

            string request = string.Empty;
            string header;

            if (content.Contains(" "))
            {
                header = content.Split(" ")[0];
                request = content.Substring(header.Length + 1);
            }
            else
            {
                header = content;
            }

            output.Add(new Request(header, request));
        }
    }
}