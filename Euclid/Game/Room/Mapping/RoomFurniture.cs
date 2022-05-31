using System;
using Euclid.Messages.Outgoing;
using Euclid.Storage.Database.Access;

namespace Euclid.Game
{
    public class RoomFurniture
    {
        #region Fields

        private Room room;

        #endregion

        #region Constructors

        public RoomFurniture(Room room)
        {
            this.room = room;
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Add item to map handler
        /// </summary>
        internal void AddItem(Item item, Position position = null, string wallPosition = null, Player player = null)
        {
            item.Data.RoomId = room.Data.Id;

            if (item.Definition.Data.IsWallItem)
            {
                item.Data.WallPosition = wallPosition;
                room.Send(new ADDITEM(item));
            }
            else
            {
                RoomTile tile = position.GetTile(room);

                if (tile == null)
                    return;

                position.Z = tile.TileHeight;

                item.Data.X = position.X;
                item.Data.Y = position.Y;
                item.Data.Z = position.Z;
                item.Data.Rotation = position.Rotation;
                item.ApplyPosition();

                HandleItemAdjusted(item, false);

                room.Send(new ACTIVEOBJECT_ADD(item));
                room.Mapping.AddItem(item);
                item.UpdateEntities();    
            }

            room.ItemManager.AddItem(item);
            
            ItemDao.SaveItem(item.Data);
        }

        /// <summary>
        /// Move item handler
        /// </summary>
        internal void MoveItem(Item item, Position position = null, string wallPosition = null)
        {
            if (item.Definition.Data.IsWallItem)
            {
                item.Data.WallPosition = wallPosition;
            }
            else
            {
                bool isRotation = false;

                if (item.Position == new Position(position.X, position.Y) && item.Position.Rotation != position.Rotation)
                    isRotation = true;

                var oldTile = item.Position.GetTile(room);

                if (oldTile == null)
                    return;

                room.Mapping.RemoveItem(item);

                var newTile = position.GetTile(room);

                if (newTile == null)
                    return;

                position.Z = newTile.TileHeight;

                item.Data.X = position.X;
                item.Data.Y = position.Y;
                item.Data.Z = position.Z;
                item.Data.Rotation = position.Rotation;
                item.ApplyPosition();


                HandleItemAdjusted(item, isRotation);

                room.Send(new ACTIVEOBJECT_UPDATE(item));
                room.Mapping.AddItem(item);

                item.UpdateEntities(oldTile.Position);

            }
            
            ItemDao.SaveItem(item.Data);
        }

        private void HandleItemAdjusted(Item item, bool isRotation)
        {
            RoomTile tile = item.Position.GetTile(room);

            if (tile == null)
            {
                return;
            }

            if (!isRotation)
            {
                Item highestItem = tile.HighestItem;
                double tileHeight = tile.TileHeight;

                if (highestItem != null && highestItem.Data.Id == item.Data.Id)
                {
                    tileHeight -= highestItem.Height;

                    double defaultHeight = room.Model.TileHeights[item.Position.X, item.Position.Y];//this.room.getModel().getTileHeight(item.getPosition().getX(), item.getPosition().getY());

                    if (tileHeight < defaultHeight)
                        tileHeight = defaultHeight;
                }

                item.Position.Z = tileHeight;//.getPosition().setZ(tileHeight);
            }
        }


        /// <summary>
        /// Remove item handler
        /// </summary>
        public void RemoveItem(Item item, Player player)
        {
            room.Mapping.RemoveItem(item);

            if (item.Definition.Data.IsWallItem)
            {
                item.Data.WallPosition = string.Empty;
                room.Send(new REMOVEITEM(item));
            }
            else
            {
                item.UpdateEntities();

                item.Data.X = item.Position.X;
                item.Data.Y = item.Position.Y;
                item.Data.Z = item.Position.Z;
                item.Data.Rotation = item.Position.Rotation;
                item.ApplyPosition();

                room.Send(new ACTIVEOBJECT_REMOVE(item));
            }

            item.Data.RoomId = 0;

            room.ItemManager.RemoveItem(item);
            ItemDao.SaveItem(item.Data);
        }

        #endregion
    }
}
