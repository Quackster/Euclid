using Euclid.Game.Entity.Humanoid;
using Euclid.Network.Streams.Util;
using Euclid.Storage.Database.Data;
using System;
using System.Collections.Generic;

namespace Euclid.Game
{
    public class Item : IEntity
    {
        #region Properties

        public ItemData Data { get; }
        public virtual ItemDefinition Definition { get { return ItemManager.Instance.GetDefinition(Data.DefinitionId); } set { Definition = value; } }
        public Room Room => RoomManager.Instance.GetRoom(Data.RoomId);
        public RoomTile CurrentTile => Position != null ? (Position.GetTile(Room) ?? null) : null;
        public Position Position { get; set; }
        public string PaddedId => Convert.ToString(Data.Id).PadLeft(Sprite.Length, '0');
        public string Sprite => Definition.Data.Sprite.Contains("*") ? Definition.Data.Sprite.Split("*")[0] : Definition.Data.Sprite;
        #endregion

        #region Constructors

        public Item(ItemData data)
        {
            Data = data;
            Position = new Position(data.X, data.Y, data.Z, data.Rotation, data.Rotation);
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Serialise item for rooms
        /// </summary>
        public void Serialise(List<object> data)
        {
            if (Definition.Data.IsFloorItem)
            {
                data.Add(new ArgumentEntry(this.PaddedId));
                data.Add(new DelimeterEntry(this.Data.Id, string.Empty));
                data.Add(new DelimeterEntry(this.Sprite, ","));
                data.Add(new DelimeterEntry(this.Position.X, " "));
                data.Add(new DelimeterEntry(this.Position.Y, " "));
                data.Add(new DelimeterEntry(this.Definition.Data.Length, " "));
                data.Add(new DelimeterEntry(this.Definition.Data.Width, " "));
                data.Add(new DelimeterEntry(this.Position.Rotation, " "));
                data.Add(new DelimeterEntry(this.Position.Z, " "));
                data.Add(new DelimeterEntry(this.Definition.Data.Colour, " "));
                data.Add(new DelimeterEntry(this.Definition.Data.Name, "/"));
                data.Add(new DelimeterEntry(this.Definition.Data.Description, "/"));
                data.Add(new DelimeterEntry(this.Definition.Data.DataClass, "/"));
                data.Add(new DelimeterEntry(this.Data.CustomData, "/"));
            }
            else
            {
                // 6;poster;Alex;leftwall 7.1250,1.3333,7125[13]56\
                data.Add(new ArgumentEntry(this.Data.Id));
                data.Add(new DelimeterEntry(this.Sprite, ";"));
                data.Add(new DelimeterEntry(null, ";"));
                data.Add(new DelimeterEntry(this.Data.WallPosition, ";"));
                data.Add(new ArgumentEntry(this.Data.CustomData));
                data.Add(@"\");
            }
        }

        public void UpdateEntities(Position position = null)
        {
            List<Humanoid> entities = new List<Humanoid>();

            foreach (Position affectedPositon in AffectedTile.GetAffectedTiles(this))
            {
                var tile = affectedPositon.GetTile(Room);

                if (tile == null)
                    continue;

                entities.AddRange(tile.Entities.Values);
            }

            if (position != null)
            {
                foreach (Position affectedPositon in AffectedTile.GetAffectedTiles(this, position.X, position.Y, position.Rotation))
                {
                    var tile = affectedPositon.GetTile(Room);

                    if (tile == null)
                        continue;

                    entities.AddRange(tile.Entities.Values);
                }
            }

            foreach (var entity in entities)
                entity.RoomEntity.InteractItem();
        }

        /// <summary>
        /// Get whether the item is walkable
        /// </summary>
        public bool IsWalkable(Position position)
        {
            if (Definition.Data.IsWalkable)
                return true;

            if (Definition.Data.IsChair)
                return true;

            if (Definition.Data.IsBed)
                return true;

            return false;
        }

        /// <summary>
        /// Get total height for furni, which is floor z axis, plus height of furni
        /// </summary>
        public double Height => Definition.Data.Height + Position.Z + 0.001;

        /// <summary>
        /// Override the position from the data variable, used to save to the database.
        /// </summary>
        public void ApplyPosition()
        {
            Position = new Position(Data.X, Data.Y, Data.Z, Data.Rotation, Data.Rotation);
        }

        /// <summary>
        ///  Check if the move is valid before moving an item.
        /// </summary>
        public bool IsValidMove(Item item, Room room, int x, int y, int rotation)
        {
            RoomTile tile = new Position(x, y).GetTile(room);

            if (tile == null || room == null)
                return false;

            bool isRotation = (item.Position.Rotation != rotation) && (new Position(x, y) == item.Position); ;

            if (isRotation)
            {
                if (item.Definition.Data.Length <= 1 && item.Definition.Data.Width <= 1)
                {
                    return true;
                }
            }

            foreach (Position position in AffectedTile.GetAffectedTiles(this, x, y, rotation))
            {
                tile = position.GetTile(room);

                if (tile == null || !room.Model.IsTile(position))
                    return false;

                if (room.Model.TileStates[position.X, position.Y] == TileState.CLOSED)
                    return false;

                if (!isRotation/* && item.Definition.InteractorType != InteractorType.BED && item.Definition.InteractorType != InteractorType.CHAIR*/)
                {
                    if (tile.Entities.Count > 0)
                        return false;
                }

                Item highestItem = tile.HighestItem;

                if (highestItem != null && highestItem.Data.Id != item.Data.Id)
                {
                    if (!CanItemsMerge(item, highestItem, new Position(x, y)))
                        return false;

                }

                foreach (Item tileItem in tile.Furniture.Values)
                {
                    if (tileItem.Data.Id == item.Data.Id)
                        continue;

                    if (!CanItemsMerge(item, tileItem, new Position(x, y)))
                        return false;
                }
            }


            return true;
        }

        /// <summary>
        /// Get if placing an item on top of another item is allowed.
        /// </summary>
        private bool CanItemsMerge(Item item, Item tileItem, Position targetTile)
        {
            if (!tileItem.Definition.Data.IsStackable)
                return false;

            if (tileItem.Definition.Data.IsChair)
                return false;

            if (tileItem.Definition.Data.IsBed)
                return false;

            return true;
        }

        #endregion
    }
}
