using Euclid.Game;
using Euclid.Network.Streams;

namespace Euclid.Messages.Incoming
{
    class GOTOFLAT : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            var roomId = int.Parse(request.GetArgument(1, "/"));
            var room = RoomManager.Instance.GetRoom(roomId);

            if (room == null)
            {
                return;
            }

            room.EntityManager.EnterRoom(player);
        }
    }
}
