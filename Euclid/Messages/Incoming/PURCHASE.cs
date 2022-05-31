using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;
using Euclid.Storage.Database.Access;
using Euclid.Storage.Database.Data;
using Euclid.Util.Extensions;
using System;

namespace Euclid.Messages.Incoming
{
    class PURCHASE : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            if (!CatalogueManager.Instance.OrderHistory.ContainsKey(player.Details.Id))
            {
                return;
            }

            var order = CatalogueManager.Instance.OrderHistory[player.Details.Id];
            CatalogueManager.Instance.OrderHistory.Remove(player.Details.Id);

            if (order == null)
            {
                return;
            }

            var itemData = new ItemData()
            {
                OwnerId = player.Details.Id,
                DefinitionId = order.Item.Definition.Data.Id,
                CustomData = order.CustomData
            };

            ItemDao.CreateItem(itemData);

            player.PrivateServer.Inventory.AddItem(new Item(itemData));
            player.Send(new Outgoing.ADDSTRIPITEM());
        }
    }
}
