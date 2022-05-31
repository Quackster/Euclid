using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;
using Euclid.Storage.Database.Access;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Euclid.Messages.Incoming
{
    class FLATPROPERTYBYITEM : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            var room = player.RoomEntity.Room;

            if (room == null || !room.IsOwner(player.Details.Id))
            {
                return;
            }

            var item = player.Inventory.GetItem(int.Parse(request.GetArgument(2, "/")));

            if (item == null || !item.Definition.Data.IsDecoration)
            {
                return;
            }
            switch (item.Definition.Data.Sprite)
            {
                case "floor":
                    room.Data.Floor = int.Parse(item.Data.CustomData);
                    break;
                case "wallpaper":
                    room.Data.Wallpaper = int.Parse(item.Data.CustomData);
                    break;
            }

            player.Inventory.RemoveItem(item);
            player.Inventory.TurnPage("update");

            RoomDao.SaveRoom(room.Data);
            ItemDao.DeleteItem(item.Data);

            player.Send(new FLATPROPERTY(item.Definition.Data.Sprite, item.Data.CustomData));

        }
    }
}
