using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;

namespace Euclid.Messages.Incoming
{
    class GETCREDITS : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            player.Send(new WALLETBALANCE(999999));
        }
    }
}

