using Euclid.Network;
using Euclid.Network.Streams.Util;
using Euclid.Storage.Database.Data;

namespace Euclid.Messages.Outgoing
{
    class FLATCREATED : IMessageComposer
    {
        private RoomData roomData;

        public FLATCREATED(RoomData roomData)
        {
            this.roomData = roomData;
        }

        public override void Write()
        {
            Data.Add(new ArgumentEntry(roomData.Id));
            Data.Add(new DelimeterEntry(GameServer.Instance.PrivateServer.IpAddress, " "));
            Data.Add(new DelimeterEntry(GameServer.Instance.PrivateServer.Port, " "));
            Data.Add(new DelimeterEntry(roomData.Name, " "));
        }

    }
}
