using Euclid.Game;
using Euclid.Network.Streams;

namespace Euclid.Messages.Incoming
{
    class GOAWAY : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            if (player.RoomEntity.Room == null)
            {
                return;
            }

            if (!player.RoomEntity.WalkingAllowed)
            {
                return;
            }

            player.RoomEntity.BeingKicked = true;
            player.Disconnect();
        }
    }
}
