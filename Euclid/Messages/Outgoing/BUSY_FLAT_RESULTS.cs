using Euclid.Game;
using Euclid.Network.Streams.Util;
using Euclid.Storage.Database.Data;
using System.Collections.Generic;

namespace Euclid.Messages.Outgoing
{
    class BUSY_FLAT_RESULTS : IMessageComposer
    {
        private List<Room> rooms;

        public BUSY_FLAT_RESULTS(List<Room> rooms)
        {
            this.rooms = rooms;
        }

        public override void Write()
        {
            foreach (var room in rooms)
            {
                Data.Add(new ArgumentEntry(room.Data.Id));
                Data.Add(new DelimeterEntry(room.Data.Name, "/"));

                if (room.Data.ShowName)
                    Data.Add(new DelimeterEntry(room.Data.OwnerData.Name, "/"));
                else
                    Data.Add(new DelimeterEntry("-", "/"));

                if (room.Data.AccessType == RoomStatus.OPEN)
                    Data.Add(new DelimeterEntry("open", "/"));

                if (room.Data.AccessType == RoomStatus.CLOSED)
                    Data.Add(new DelimeterEntry("closed", "/"));

                if (room.Data.AccessType == RoomStatus.PASSWORD)
                    Data.Add(new DelimeterEntry("password", "/"));

                Data.Add(new DelimeterEntry("", "/"));
                Data.Add(new DelimeterEntry("Floor1", "/"));
                Data.Add(new DelimeterEntry(room.Address.IpAddress, "/"));
                Data.Add(new DelimeterEntry(room.Address.IpAddress, "/"));
                Data.Add(new DelimeterEntry(room.Address.Port, "/"));
                Data.Add(new DelimeterEntry(room.Data.UsersNow, "/"));
                Data.Add(new DelimeterEntry("null", "/"));
                Data.Add(new DelimeterEntry(room.Data.Description, "/"));
            }
        }
    }
}