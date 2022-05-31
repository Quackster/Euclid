using DotNetty.Buffers;
using DotNetty.Codecs;
using DotNetty.Transport.Channels;
using Euclid.Util;
using Euclid.Util.Encryption;
using System.Collections.Generic;

namespace Euclid.Network
{
    internal class EncryptionDecoder : ByteToMessageDecoder
    {
        private RC4 rc4;

        public EncryptionDecoder(RC4 rc4)
        {
            this.rc4 = rc4;
        }

        protected override void Decode(IChannelHandlerContext ctx, IByteBuffer buffer, List<object> output)
        {
            byte[] payload = new byte[buffer.ReadableBytes];
            buffer.ReadBytes(payload);

            var messagePayload = StringUtil.GetEncoding().GetString(payload);
            var decodedPayload = StringUtil.GetEncoding().GetBytes(this.rc4.Decipher(messagePayload));

            var result = Unpooled.Buffer();
            result.WriteBytes(decodedPayload);
            output.Add(result);
        }
    }
}