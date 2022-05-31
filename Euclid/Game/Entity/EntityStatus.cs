using Euclid.Storage.Database.Data;
using System.Collections.Concurrent;

namespace Euclid.Game
{
    public class EntityState
    {
        public int EntityId;
        public int InstanceId;
        public IEntityData Details;
        public EntityType EntityType;
        public Room Room;
        public Position Position;
        public ConcurrentDictionary<string, RoomUserStatus> Statuses;

        public EntityState(int entityId, int instanceId, IEntityData details, EntityType entityType, Room room, Position position, ConcurrentDictionary<string, RoomUserStatus> statuses)
        {
            this.EntityId = entityId;
            this.InstanceId = instanceId;
            this.Details = details;
            this.EntityType = entityType;
            this.Room = room;
            this.Position = position;
            this.Statuses = new ConcurrentDictionary<string, RoomUserStatus>(statuses);
        }
    }
}
