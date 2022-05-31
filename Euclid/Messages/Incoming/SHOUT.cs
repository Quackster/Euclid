using Euclid.Game;
using Euclid.Network.Streams;
using Euclid.Util.Extensions;

namespace Euclid.Messages.Incoming
{
    class SHOUT : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            if (!player.Authenticated)
            {
                return;
            }

            if (player.RoomEntity.Room == null)
            {
                return;
            }

            string message = request.Content.FilterInput();
            player.RoomEntity.Talk(ChatMessageType.SHOUT, message);
        }
    }
}
