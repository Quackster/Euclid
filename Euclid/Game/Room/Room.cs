using Euclid.Messages;
using Euclid.Network;
using Euclid.Network.Streams;
using Euclid.Storage.Database.Data;
using Euclid.Util;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;

namespace Euclid.Game
{
    public class Room
    {
        #region Properties

        public RoomData Data { get; }
        public RoomEntityManager EntityManager { get; }
        public RoomTaskManager TaskManager { get; }
        public RoomItemManager ItemManager { get; }
        public RoomFurniture FurnitureManager { get; set; }
        public RoomMapping Mapping { get; set; }
        public RoomModel Model => RoomManager.Instance.RoomModels.FirstOrDefault(x => x.Data.Id == Data.ModelId);
        public ConcurrentDictionary<IEntity, int> Entities { get; }
        public bool IsActive { get; set; }
        public GameAddress Address;

        #endregion

        #region Constructors

        public Room(RoomData data)
        {
            Data = data;
            Entities = new ConcurrentDictionary<IEntity, int>();
            EntityManager = new RoomEntityManager(this);
            Mapping = new RoomMapping(this);
            TaskManager = new RoomTaskManager(this);
            ItemManager = new RoomItemManager(this);
            FurnitureManager = new RoomFurniture(this);

            if (data.IsPrivateRoom)
                Address = GameServer.Instance.PrivateServer;

            if (data.IsPublicRoom)
                Address = new GameAddress(ServerConfig.Instance.GetString("server", "public", "ip", Data.Id.ToString()), ServerConfig.Instance.GetInt("server", "public", "port", Data.Id.ToString()));
        }

        #endregion

        #region Static methods

        /// <summary>
        /// Wrap the retrieved database data with a room instance >:)
        /// </summary>
        public static Room Wrap(RoomData roomData)
        {
            return new Room(roomData);
        }

        #endregion

        #region Private methods

        /// <summary>
        /// Get if the user has rights
        /// </summary>
        public bool HasRights(int userId, bool checkOwner = true)
        {
            if (checkOwner)
                if (Data.OwnerId == userId)
                    return true;

            var player = PlayerManager.Instance.GetPlayerById(userId);

            if (player != null)
            {
                if (player.UserGroup.HasPermission("room.rights"))
                    return true;
            }

            return false;
        }

        /// <summary>
        /// Get if the user is owner
        /// </summary>
        public bool IsOwner(int userId)
        {
            if (Data.OwnerId == userId)
                return true;

            var player = PlayerManager.Instance.GetPlayerById(userId);

            if (player != null)
            {
                if (player.UserGroup.HasPermission("room.owner"))
                    return true;
            }

            return false;
        }


        /// <summary>
        /// Try and dispose, only if it has 0 players active.
        /// </summary>
        public void TryDispose()
        {
            var playerList = EntityManager.GetEntities<Player>();

            if (playerList.Any())
                return;

            TaskManager.StopTasks();
            RoomManager.Instance.RemoveRoom(Data.Id);

            IsActive = false;
        }

        /// <summary>
        /// Send packet to entire player list in room
        /// </summary>
        public void Send(IMessageComposer composer, List<Player> specificUsers = null)
        {
            if (specificUsers == null)
                specificUsers = EntityManager.GetEntities<Player>();

            foreach (var player in specificUsers)
                player.Send(composer);
        }

        /// <summary>
        /// Forward entity to room
        /// </summary>
        /// <param name="entity"></param>
        public void Forward(IEntity entity)
        {
            if (!(entity is Player))
            {
                return;
            }

            var player = (Player)entity;
            
      
        }

        #endregion
    }
}
