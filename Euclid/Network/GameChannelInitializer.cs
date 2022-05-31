using DotNetty.Transport.Channels;
using Euclid.Network.Codec;

namespace Euclid.Network
{
    internal class GameChannelInitializer : ChannelInitializer<IChannel>
    {
        protected override void InitChannel(IChannel channel)
        {
            IChannelPipeline pipeline = channel.Pipeline;
            pipeline.AddLast("gameEncoder", new NetworkEncoder());
            pipeline.AddLast("gameDecoder", new NetworkDecoder());
            pipeline.AddLast("clientHandler", new GameNetworkHandler());
        }
    }
}