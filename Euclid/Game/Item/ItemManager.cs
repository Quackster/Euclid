using Euclid.Storage.Database.Access;
using Euclid.Storage.Database.Data;
using System.Collections.Generic;
using System.Linq;

namespace Euclid.Game
{
    public class ItemManager : ILoadable
    {
        #region Fields

        public static readonly ItemManager Instance = new ItemManager();

        #endregion

        #region Properties

        public Dictionary<int, ItemDefinition> Definitions { get; set; }

        #endregion

        #region Constructors

        public void Load()
        {
            Definitions = ItemDao.GetDefinitions().Select(x => new ItemDefinition(x)).ToDictionary(x => x.Data.Id, x => x);
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Safe method to try and get definition
        /// </summary>
        public ItemDefinition GetDefinition(int definitionId)
        {
            Definitions.TryGetValue(definitionId, out var definition);
            return definition;
        }

        /// <summary>
        /// Try and resolve an item inside a room or persons inventory
        /// </summary>
        /// <param name="itemId">the item GUID</param>
        /// <returns>the tiem</returns>
        public Item ResolveItem(int itemId, ItemData itemData = null)
        {
            if (itemData == null)
                itemData = ItemDao.GetItem(itemId);

            if (itemData == null)
                return null;

            var room = RoomManager.Instance.GetRoom(itemData.RoomId);

            if (room == null)
            {
                var player = PlayerManager.Instance.GetPlayerById(itemData.RoomId);

                if (player != null)
                {
                    return null;// player.Inventory.GetItem(itemData.Id.ToString());
                }
            }
            else
            {
                if (room.EntityManager.GetEntities<Item>().Count == 0)
                    room.ItemManager.Load();

                return room.ItemManager.GetItem(itemData.Id);
            }


            return null;
        }

        #endregion
    }
}
