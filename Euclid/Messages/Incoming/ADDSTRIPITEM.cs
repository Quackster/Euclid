using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;
using Euclid.Util.Extensions;
using Euclid.Util;

namespace Euclid.Messages.Incoming
{
    class ADDSTRIPITEM : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            if (player.RoomEntity.Room == null)
                return;

            Room room = player.RoomEntity.Room;
            var placementData = request.Content.Split(' ');

            if (!placementData[2].IsNumeric())
                return;

            int itemId = int.Parse(placementData[2]);
            Item item = room.ItemManager.GetItem(itemId);

            if (item == null)
                return;

            if (room == null || !room.IsOwner(player.Details.Id))
            {
                return;
            }

            room.FurnitureManager.RemoveItem(item, player);

            player.Inventory.AddItem(item);
            player.Inventory.TurnPage("last");
        }
    }
}
