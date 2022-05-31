using Euclid.Game;
using Euclid.Network.Streams;
using Euclid.Storage.Database.Access;
using Euclid.Storage.Database.Data;

namespace Euclid.Messages.Incoming
{
    class UPDATEFLAT : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            var roomId = int.Parse(request.GetArgument(1, "/"));
            var room = RoomManager.Instance.GetRoom(roomId);

            if (room == null || !room.IsOwner(player.Details.Id))
            {
                return;
            }

            string name = request.GetArgument(2, "/");
            string accessType = request.GetArgument(3, "/");
            bool showOwnerName = request.GetArgument(4, "/") == "1";

            // Name length check
            if (name.Length < 2)
                name = room.Data.Name;

            var state = RoomStatus.OPEN;

            if (accessType == "closed")
                state = RoomStatus.CLOSED;

            if (accessType == "password")
                state = RoomStatus.PASSWORD;

            room.Data.Name = name;
            room.Data.AccessType = state;
            room.Data.ShowName = showOwnerName;

            RoomDao.SaveRoom(room.Data);
        }
    }
}