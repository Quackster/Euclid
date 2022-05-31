using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;
using Euclid.Util.Extensions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Euclid.Messages.Incoming
{
    class PLACESTUFFFROMSTRIP : IMessageEvent
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


            int x = (int)double.Parse(placementData[1]);
            int y = (int)double.Parse(placementData[2]);
            int rotation = (int)double.Parse(placementData[3]);

            if (rotation % 2 != 0 || rotation < 0 || rotation > 6)
                rotation = 0;

            var position = new Position();
            position.X = x;
            position.Y = y;
            position.Rotation = 0;///rotation;

            if (!item.IsValidMove(item, room, position.X, position.Y, position.Rotation))
            {
                player.Send(new ACTIVEOBJECT_UPDATE(item));
                return;
            }

            room.FurnitureManager.AddItem(item, position, player: player);
            player.Inventory.RemoveItem(item);
        }
    }
}
