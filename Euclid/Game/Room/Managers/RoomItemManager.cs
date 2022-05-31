using Euclid.Storage.Database.Access;
using Euclid.Storage.Database.Data;
using Euclid.Util.Extensions;
using System;
using System.Collections.Concurrent;
using System.IO;
using System.Linq;

namespace Euclid.Game
{
    public class RoomItemManager : ILoadable
    {
        #region Fields

        private Room room;

        #endregion

        #region Constructors

        public RoomItemManager(Room room)
        {
            this.room = room;
        }

        #endregion

        #region Public methods
        
        public void Load()
        {
            if (room.Data.IsPrivateRoom)
            {
                foreach (var itemData in ItemDao.GetRoomItems(room.Data.Id))
                {
                    Item item = new ActiveItem(itemData);
                    room.Entities.TryAdd(item, item.Data.Id);
                }
            }

            if (room.Data.IsPublicRoom)
            {
                var furnitureMap = Path.Combine("tools", "public_rooms", $"{room.Model.Data.Model}.furniture");

                if (!File.Exists(furnitureMap))
                    return;

                foreach (var line in File.ReadAllLines(furnitureMap))
                {
                    if (line.Length < 6)
                        continue;

                    var passiveData = line.Split(' ');
                    var isChair = passiveData[1].Contains("chair");
                    var isVisible = !passiveData[1].Contains("none");

                    var passiveItem = new PassiveItem(new ItemData
                    {
                        CustomData = passiveData[0],
                        X = int.Parse(passiveData[2]),
                        Y = int.Parse(passiveData[3]),
                        Z = int.Parse(passiveData[4])
                    });

                    passiveItem.Definition = new ItemDefinition(new ItemDefinitionData
                    {
                        Sprite = passiveData[1],
                        Length = 1,
                        Width = 1,
                        IsChair = isChair,
                        IsFloorItem = true,
                        IsWalkable = isChair,
                        Height = isChair ? 1 : 0,
                        IsVisible = isVisible
                    });

                    if (passiveData.Length > 6)
                    {
                        passiveItem.Definition.Data.Length = int.Parse(passiveData[5]);
                        passiveItem.Definition.Data.Width = int.Parse(passiveData[6]);
                    }
                    else
                    {
                        passiveItem.Position.Rotation = int.Parse(passiveData[5]);
                    }

                    room.Entities.TryAdd(passiveItem, 0);
                }
            }
        }

        #endregion

        #region Public methods

        /// <summary> 
        /// Retrieve item from room item list by id
        /// </summary>
        public Item GetItem(int itemId)
        {
            return room.EntityManager.GetEntities<Item>().FirstOrDefault(x => x.Data.Id == itemId);
        }

        /// <summary>
        /// Add item to manager
        /// </summary>
        /// <param name="item"></param>
        public void AddItem(Item item)
        {
            room.Entities.TryAdd(item, item.Data.Id);
        }

        /// <summary>
        /// Remove item from manager
        /// </summary>
        /// <param name="item"></param>
        public void RemoveItem(Item item)
        {
            room.Entities.Remove(item);
        }

        #endregion
    }
}
