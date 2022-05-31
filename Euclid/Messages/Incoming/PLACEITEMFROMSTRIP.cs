using Euclid.Game;
using Euclid.Network.Streams;
using Euclid.Util.Extensions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Euclid.Messages.Incoming
{
    class PLACEITEMFROMSTRIP : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            if (player.RoomEntity.Room == null)
                return;

            Room room = player.RoomEntity.Room;
            var placementData = request.Content.Split(' ');

            if (!placementData[0].IsNumeric())
                return;

            int itemId = int.Parse(placementData[0]);
            Item item = player.Inventory.GetItem(itemId);

            if (item == null)
                return;

            if (room == null || !room.HasRights(player.Details.Id))
            {
                return;
            }


            string[] wallPositionData = request.Content.Split("/")[0].Split(" ");
            string wallPosition = $"{wallPositionData[1]} {wallPositionData[2]}";

            room.FurnitureManager.AddItem(item, wallPosition: wallPosition, player: player);

            player.Inventory.RemoveItem(item);
        }
    }
}
