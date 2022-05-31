using Euclid.Messages.Outgoing;
using Euclid.Storage.Database.Access;
using Euclid.Util.Extensions;
using System;
using System.Collections.Concurrent;
using System.Linq;

namespace Euclid.Game
{
    public class Inventory : ILoadable
    {
        #region Fields

        private Player player;
        public static readonly int HAND_SIZE = 6;

        #endregion

        #region Properties

        public ConcurrentDictionary<int, Item> Items { get; private set; }
        public int CurrentPage { get; private set; }

        #endregion

        #region Constructors

        public Inventory(Player player)
        {
            this.player = player;
            this.Items = new ConcurrentDictionary<int, Item>();
        }

        public void Load()
        {
            this.Items = new ConcurrentDictionary<int, Item>(ItemDao.GetUserItems(player.Details.Id).Select(x => new Item(x)).ToDictionary(x => x.Data.Id, x => x));
            this.CurrentPage = 0;
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Handle inventory page mode
        /// </summary>
        public void TurnPage(string mode)
        {
            int inventoryPages = Items.CountPages(HAND_SIZE) - 1;

            switch (mode)
            {
                case "last":
                    CurrentPage = inventoryPages;
                    break;
                case "new":
                    CurrentPage = 0;
                    break;
                case "next":
                    CurrentPage++;
                    break;
            }

            if (CurrentPage > inventoryPages)
                CurrentPage = 0;

            player.Send(new STRIPINFO(
                player.Inventory.CurrentPage,
                player.Inventory.Items.Count,
                player.Inventory.Items.Values.GetPage(player.Inventory.CurrentPage, Inventory.HAND_SIZE)));
        }

        /// <summary>
        /// Retrieve item from inventory
        /// </summary>
        public Item GetItem(int itemId)
        {
            if (Items.TryGetValue(itemId, out var item))
                return item;

            return null;
        }

        /// <summary>
        /// Add item to inventory
        /// </summary>
        public void AddItem(Item item)
        {
            this.Items.TryAdd(item.Data.Id, item);
        }

        /// <summary>
        /// Remove item from inventory
        /// </summary>
        public void RemoveItem(Item item)
        {
            Items.Remove(item.Data.Id);
        }

        #endregion
    }
}
