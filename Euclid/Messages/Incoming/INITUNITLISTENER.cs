using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;
using Euclid.Storage.Database.Access;

namespace Euclid.Messages.Incoming
{
    class INITUNITLISTENER : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            player.Send(new ALLUNITS(RoomManager.Instance.ReplaceQueryRooms(RoomDao.GetPublicFlats())));
        }
    }
}
