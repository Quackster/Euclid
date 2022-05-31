using Euclid.Game;
using Euclid.Network.Streams.Util;
using Euclid.Storage.Database.Data;

namespace Euclid.Messages.Outgoing
{
    class FLATINFO : IMessageComposer
    {
        private Room room;

        public FLATINFO(Room room)
        {
            this.room = room;
        }

        public override void Write()
        {
            this.Data.Add(new KeyValueEntry("name", room.Data.Name, "="));
            this.Data.Add(new KeyValueEntry("password", room.Data.Password, "="));
            this.Data.Add(new KeyValueEntry("description", room.Data.Description, "="));
            this.Data.Add(new KeyValueEntry("showOwnerName", room.Data.ShowName ? "true" : "false", "="));
            this.Data.Add(new KeyValueEntry("allsuperuser", room.Data.SuperUsers ? "true" : "false", "="));

            if (room.Data.AccessType == RoomStatus.OPEN)
                Data.Add(new KeyValueEntry("doormode", "open", "="));

            if (room.Data.AccessType == RoomStatus.CLOSED)
                Data.Add(new KeyValueEntry("doormode", "closed", "="));

            if (room.Data.AccessType == RoomStatus.PASSWORD)
                Data.Add(new KeyValueEntry("doormode", "password", "="));
        }
    }
}
