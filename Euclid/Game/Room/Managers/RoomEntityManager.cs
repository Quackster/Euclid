using Euclid.Storage.Database.Access;
using System;
using System.Collections.Generic;
using System.Linq;
using Euclid.Util.Extensions;
using Euclid.Messages.Outgoing;
using Euclid.Game.Entity.Humanoid;

namespace Euclid.Game
{
    public class RoomEntityManager
    {
        #region Fields

        private Room room;
        private int instanceCounter;

        #endregion

        #region Constructors

        public RoomEntityManager(Room room)
        {
            this.room = room;
        }

        #endregion

        #region Private methods

        /// <summary>
        /// Generate instance ID for new entity that entered room
        /// </summary>
        private int GenerateInstanceId()
        {
            return instanceCounter++;
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Select entities where it's assignable by entity type
        /// </summary>
        public List<T> GetEntities<T>()
        {
            return room.Entities.Keys.Where(x => x.GetType().IsAssignableFrom(typeof(T)) 
                || x.GetType().GetInterfaces().Contains(typeof(T))
                || x.GetType().IsSubclassOf(typeof(T))
                ).Select(x => x).Cast<T>().ToList();
        }

        /// <summary>
        /// Enter room handler, used when user clicks room to enter
        /// </summary>
        public void EnterRoom(Humanoid entity, Position entryPosition = null)
        {
            var existingPlayer = room.EntityManager.GetEntities<Humanoid>().FirstOrDefault(x => x.EntityData.Name == entity.EntityData.Name);

            // Kick existing player from the room - if they exist,
            // used if a player re-enters room after being kicked and they walk back to door
            if (existingPlayer != null)
            {
                LeaveRoom(existingPlayer);
            }

            SilentlyEntityRoom(entity, entryPosition);

            if (!(entity is Player player))
                return;

            // player.Send(new RoomUrlComposer());
            // player.Send(new RoomReadyComposer(this.room.Data.Id, this.room.Model.Data.Model));

            if (!room.Data.IsPublicRoom)
            {
                if (room.IsOwner(player.Details.Id))
                {

                }
                else if (room.HasRights(player.Details.Id, false))
                {
                    
                }
            }
        }

        /// <summary>
        /// Silently enter room handler for every other entity type
        /// </summary>
        public void SilentlyEntityRoom(Humanoid entity, Position entryPosition = null)
        {
            if (entity.RoomEntity.Room != null)
                entity.RoomEntity.Room.EntityManager.LeaveRoom(entity);

            if (!RoomManager.Instance.HasRoom(room.Data.Id))
                RoomManager.Instance.AddRoom(room);

            entity.RoomEntity.Reset();
            entity.RoomEntity.Room = room;
            entity.RoomEntity.InstanceId = GenerateInstanceId();
            entity.RoomEntity.Position = (entryPosition ?? room.Model.Door);
            entity.RoomEntity.AuthenticateRoomId = -1;

            if (!room.IsActive)
                TryInitialise();

            if (entity is Player player)
            {
                room.Data.UsersNow++;
                RoomDao.SetVisitorCount(room.Data.Id, room.Data.UsersNow);

                player.Send(new OBJECTS(room.Model,room.EntityManager.GetEntities<PassiveItem>().Where(x => x.Definition.Data.IsFloorItem && x.Definition.Data.IsVisible).ToList()));
                player.Send(new ACTIVE_OBJECTS(room.EntityManager.GetEntities<ActiveItem>().Where(x => x.Definition.Data.IsFloorItem).ToList()));
                player.Send(new ITEMS(room.EntityManager.GetEntities<ActiveItem>().Where(x => x.Definition.Data.IsWallItem).ToList()));
                player.Send(new HEIGHTMAP(room.Model.Heightmap));

                if (room.Data.Wallpaper > 0)
                    player.Send(new FLATPROPERTY("wallpaper", room.Data.Wallpaper.ToString()));

                if (room.Data.Floor > 0)
                    player.Send(new FLATPROPERTY("floor", room.Data.Floor.ToString()));

                player.Send(new USERS(room.EntityManager.GetEntities<Humanoid>()));
                player.Send(new STATUS(room.EntityManager.GetEntities<Humanoid>()));

                if (room.IsOwner(player.Details.Id))
                {
                    player.RoomEntity.AddStatus("flatctrl", "1");
                    player.Send(new YOUAREOWNER());
                }
                else if (room.HasRights(player.Details.Id) || room.Data.SuperUsers)
                {
                    player.RoomEntity.AddStatus("flatctrl", "1");
                    player.Send(new YOUARECONTROLLER());
                }
            }

            // Add entity to room
            room.Entities.TryAdd(entity, entity.RoomEntity.InstanceId);

            // For 'Player' to show, see GetRoomEntryDataMessageComposer.cs line 21
            room.Send(new USERS(List.Create(entity)));

            // Mark entity for update
            entity.RoomEntity.NeedsUpdate = true;
        }

        /// <summary>
        /// Try initialize room to start
        /// </summary>
        private void TryInitialise()
        {
            if (room.IsActive)
                return;

            room.ItemManager.Load();
            room.TaskManager.Load();
            room.Mapping.Load();
            room.IsActive = true;
        }

        /// <summary>
        /// Leave room handler, called when user leaves room, clicks another room, re-enters room, and disconnects
        /// </summary>
        public void LeaveRoom(Humanoid entity, bool hotelView = false)
        {
            room.Entities.Remove(entity);
            room.Send(new LOGOUT(entity.EntityData.Name));

            // Remove entity from their current position
            var currentTile = entity.RoomEntity.Position.GetTile(room);

            if (currentTile != null)
                currentTile.RemoveEntity(entity);

            // Remove entity from their next position (if they were walking)
            var nextPosition = entity.RoomEntity.Next;

            if (nextPosition != null)
            {
                var nextTile = nextPosition.GetTile(room);

                if (nextTile != null)
                    nextTile.RemoveEntity(entity);
            }

            entity.RoomEntity.Reset();

            if (entity is Player player)
            {
                room.Data.UsersNow--;
                RoomDao.SetVisitorCount(room.Data.Id, room.Data.UsersNow);

                if (hotelView)
                {
                    if (player.ConnectionMode == Network.Session.ConnectionMode.PUBLIC ||
                        player.ConnectionMode == Network.Session.ConnectionMode.PRIVATE)
                    {
                        player.Connection.Disconnect();
                    }
                }
                
                //player.Messenger.SendStatus();
            }

            room.TryDispose();
        }

        #endregion
    }
}
