using Euclid.Game;
using Euclid.Network.Streams;
using Euclid.Storage.Database.Access;

namespace Euclid.Messages.Incoming
{
    class DELETEFLAT : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            var roomId = int.Parse(request.GetArgument(1, "/"));
            var room = RoomManager.Instance.GetRoom(roomId);

            if (room == null || !room.IsOwner(player.Details.Id))
            {
                return;
            }

            RoomDao.DeleteRoom(room.Data.Id);
        }
    }
}