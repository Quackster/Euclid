using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;
using Euclid.Storage.Database.Access;

namespace Euclid.Messages.Incoming
{
    class SEARCHBUSYFLATS : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            int page = 0;
            int maxResults = 11;

            if (request.GetArgumentAmount(",") > 0)
            {
                page = int.Parse(request.GetArgument(0, ",").Replace("/", ""));
                maxResults = int.Parse(request.GetArgument(1, ","));
            }

            player.Send(new BUSY_FLAT_RESULTS(RoomManager.Instance.ReplaceQueryRooms(RoomDao.GetPopularFlats(page / 11, maxResults))));
        }
    }
}
