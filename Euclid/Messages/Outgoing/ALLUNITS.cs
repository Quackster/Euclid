using Euclid.Game;
using Euclid.Network.Streams.Util;
using System.Collections.Generic;

namespace Euclid.Messages.Outgoing
{
    internal class ALLUNITS : IMessageComposer
    {
        private List<Room> rooms;

        public ALLUNITS(List<Room> rooms)
        {
            this.rooms = rooms;
        }

        public override void Write()
        {
            foreach (var room in rooms)
            {
                Data.Add(new ArgumentEntry(room.Data.Name));
                Data.Add(new DelimeterEntry(room.Data.UsersNow, ","));
                Data.Add(new DelimeterEntry(room.Data.UsersMax, ","));
                Data.Add(new DelimeterEntry(room.Address.IpAddress, ","));
                Data.Add(new DelimeterEntry(room.Address.IpAddress, "/"));
                Data.Add(new DelimeterEntry(room.Address.Port, ","));
                Data.Add(new DelimeterEntry(room.Data.Name, ","));
                Data.Add(new DelimeterEntry(room.Data.Description, ","));
                Data.Add(new DelimeterEntry(room.Data.UsersNow, ","));
                Data.Add(new DelimeterEntry(room.Data.UsersMax, ","));
                Data.Add(new DelimeterEntry(room.Model.Data.Model, ","));
            }

            //this.m_Data.Add($"Main Lobby,0,40,127.0.0.1/127.0.0.1,{GameServer.Instance.MainPort + 1},Main Lobby" + (char)9 + "lobby,0,40,lobby_a" + (char)13);
            //this.m_Data.Add($"Median Lobby,0,40,127.0.0.1/127.0.0.1,{GameServer.Instance.MainPort + 1},Median Lobby" + (char)9 + "lobby,0,40,floorlobby_b" + (char)13);
        }
    }
}