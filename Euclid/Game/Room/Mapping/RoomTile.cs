﻿using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using Euclid.Game.Entity.Humanoid;
using Euclid.Util.Extensions;

namespace Euclid.Game
{
    public class RoomTile
    {
        #region Fields

        private Room _room;

        #endregion

        #region Properties

        public Position Position { get; }
        public double TileHeight { get; set; }
        public double DefaultHeight { get; }
        public double WalkingHeight { get; set; }
        public TileState TileState { get; }
        public ConcurrentDictionary<int, Humanoid> Entities { get; }
        public ConcurrentDictionary<int, Item> Furniture { get; }
        public Item HighestItem { get; set; }

        #endregion

        public RoomTile(Room room, Position position, double tileHeight, TileState tileState)
        {
            _room = room;
            Position = position;
            TileHeight = tileHeight;
            DefaultHeight = tileHeight;
            TileState = tileState;
            WalkingHeight = tileHeight;
            Entities = new ConcurrentDictionary<int, Humanoid>();
            Furniture = new ConcurrentDictionary<int, Item>();
        }

        public bool isHeightDrop(RoomTile otherTile)
        {
            return this.WalkingHeight > otherTile.WalkingHeight;
        }

        public bool isHeightUpwards(RoomTile otherTile)
        {
            return this.WalkingHeight < otherTile.WalkingHeight;
        }

        /// <summary>
        /// Get if the tile is valid for this entity
        /// </summary>
        public static bool IsValidTile(Room room, Humanoid entity, Position position, bool lastStep = false)
        {
            if (room == null || entity == null || position == null)
                return false;

            var tile = position.GetTile(room);

            if (tile == null || tile.TileState == TileState.CLOSED)
                return false;
            
            // Return true always... otherwise the user will be stuck
            if (entity.RoomEntity.Position == position)
                return true;
            
            // If the room doesn't allow walking through users OR it's the last step, then check if a user that IS NOT you is on the tile
            if (tile.Entities.Count(x => x.Value != entity) > 0)
                return false;

            if (tile.HighestItem != null)
            {
                if (!tile.HighestItem.IsWalkable(position))
                    return false;
            }

            return true;
        }

        /// <summary>
        /// Add entity to the tile
        /// </summary>
        public void AddEntity(Humanoid entity)
        {
            if (entity == null)
                return;

            Entities.TryAdd(entity.RoomEntity.InstanceId, entity);
        }

        public double GetWalkingHeight()
        {
            double height = TileHeight;
            
            if (HighestItem != null)
            {
                if (HighestItem.Definition.Data.IsChair ||
                    HighestItem.Definition.Data.IsBed)
                {
                    height -= HighestItem.Definition.Data.Height;
                }
            }

            return height;
        }

        /// <summary>
        /// Remove entity from tile
        /// </summary>
        public void RemoveEntity(Humanoid entity)
        {
            if (entity == null)
                return;

            Entities.Remove(entity.RoomEntity.InstanceId);
        }

        /// <summary>
        /// Reset highest item, called after an item in the tile has been changed
        /// </summary>
        private void ResetHighestItem()
        {
            HighestItem = null;
            TileHeight = DefaultHeight;

            foreach (var item in Furniture.Values)
            {
                if (item == null)
                    continue;

                double height = item.Height;

                // TODO: Ignore stack helpers

                if (height < TileHeight)
                    continue;

                HighestItem = item;
                TileHeight = height;
            }
        }

        /// <summary>
        /// Add item to tile map
        /// </summary>
        /// <param name="item"></param>
        public void AddItem(Item item)
        {
            if (item == null)
                return;

            Furniture.TryAdd(item.Data.Id, item);

            if (item.Height < TileHeight) // TODO: Stack helper?
                return;

            ResetHighestItem();
        }

        /// <summary>
        /// Remove item from tile map
        /// </summary>
        /// <param name="item"></param>
        public void RemoveItem(Item item)
        {
            if (item == null)
                return;

            Furniture.Remove(item.Data.Id);

            if (HighestItem == null || item.Data.Id != HighestItem.Data.Id) // TODO: Stack helper?
                return;

            ResetHighestItem();
        }

        /// <summary>
        /// Get the list of items on this tile.
        /// </summary>
        /// <returns></returns>
        public List<Item> GetTileItems()
        {
            var items = new List<Item>(Furniture.Values);
            return items.OrderBy(x => x.Position.Z).ToList();
        }
    }
}
