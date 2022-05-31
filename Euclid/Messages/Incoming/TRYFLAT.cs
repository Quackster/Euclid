using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;

namespace Euclid.Messages.Incoming
{
    class TRYFLAT : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            player.Send(new FLAT_LETIN());
        }
    }
}
