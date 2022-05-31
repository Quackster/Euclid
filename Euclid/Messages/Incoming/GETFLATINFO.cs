using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;

namespace Euclid.Messages.Incoming
{
    class GETFLATINFO : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            var roomId = int.Parse(request.GetArgument(1, "/"));
            var room = RoomManager.Instance.GetRoom(roomId);

            if (room == null || !room.IsOwner(player.Details.Id))
            {
                return;
            }

            player.Send(new FLATINFO(room));
        }
    }
}
