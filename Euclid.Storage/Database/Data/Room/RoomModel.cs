

using FluentNHibernate.Mapping;

namespace Euclid.Storage.Database.Data
{
    public class RoomModelMapping : ClassMap<RoomModelData>
    {
        public RoomModelMapping()
        {
            Table("room_model");
            Id(x => x.Id, "id");
            Map(x => x.Model, "model");
            Map(x => x.DoorX, "door_x");
            Map(x => x.DoorY, "door_y");
            Map(x => x.DoorZ, "door_z");
            Map(x => x.DoorDirection, "door_dir");
            Map(x => x.Heightmap, "heightmap");
            Map(x => x.IsClubOnly, "is_club_only");
        }
    }

    public class RoomModelData
    {
        public virtual int Id { get; set; }
        public virtual string Model { get; set; }
        public virtual int DoorX { get; set; }
        public virtual int DoorY { get; set; }
        public virtual int DoorZ { get; set; }
        public virtual int DoorDirection { get; set; }
        public virtual string Heightmap { get; set; }
        public virtual bool IsClubOnly { get; set; }
    }
}
