using Euclid.Storage.Database.Data;

namespace Euclid.Game.Entity.Humanoid
{
    public class Humanoid : IEntity
    {
        public virtual IEntityData EntityData { get; private set; }
        public virtual EntityType EntityType { get; private set; }
        public virtual RoomEntity RoomEntity { get; set; }
    }
}
