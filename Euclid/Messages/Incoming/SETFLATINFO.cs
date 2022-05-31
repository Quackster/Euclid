using Euclid.Game;
using Euclid.Network.Streams;
using Euclid.Storage.Database.Access;

namespace Euclid.Messages.Incoming
{
    class SETFLATINFO : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            var roomId = int.Parse(request.GetArgument(1, "/"));
            var content = request.Content.Substring(string.Concat("/", roomId, "/").Length);

            var flatValues = request.GetKeyValues(content);

            if (!flatValues.ContainsKey("description") ||
                !flatValues.ContainsKey("password") ||
                !flatValues.ContainsKey("allsuperuser"))
            {
                return;
            }

            var room = RoomManager.Instance.GetRoom(roomId);

            if (room == null || !room.IsOwner(player.Details.Id))
            {
                return;
            }

            room.Data.Description = flatValues["description"];
            room.Data.Password = flatValues["password"];
            room.Data.SuperUsers = flatValues["allsuperuser"] == "1";

            RoomDao.SaveRoom(room.Data);
        }
    }
}
