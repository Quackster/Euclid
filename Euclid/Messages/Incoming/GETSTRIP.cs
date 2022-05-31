using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;
using Euclid.Util.Extensions;

namespace Euclid.Messages.Incoming
{
    class GETSTRIP : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            player.Inventory.TurnPage(request.Content);
        }
    }
}
