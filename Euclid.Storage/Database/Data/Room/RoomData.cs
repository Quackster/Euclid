
using FluentNHibernate.Mapping;

namespace Euclid.Storage.Database.Data
{
    public class RoomMapping : ClassMap<RoomData>
    {
        public RoomMapping()
        {
            Table("room");
            Id(x => x.Id, "id");
            Map(x => x.OwnerId, "owner_id");
            Map(x => x.Name, "name");
            Map(x => x.Description, "description");
            Map(x => x.CategoryId, "category_id").Generated.Insert();
            Map(x => x.UsersNow, "visitors_now").Generated.Insert();
            Map(x => x.UsersMax, "visitors_max").Generated.Insert();
            Map(x => x.AccessType, "access_type").Generated.Insert().CustomType<RoomStatus>();
            Map(x => x.Password, "password").Generated.Insert();
            Map(x => x.ModelId, "model_id");
            Map(x => x.CCTs, "ccts").Generated.Insert();
            Map(x => x.Wallpaper, "wallpaper").Generated.Insert();
            Map(x => x.Floor, "floor").Generated.Insert();
            Map(x => x.Rating, "rating").Generated.Insert();
            Map(x => x.ShowName, "show_name").Generated.Insert();
            Map(x => x.SuperUsers, "super_users").Generated.Insert();

            References(x => x.OwnerData, "owner_id")
                .ReadOnly()
                .Cascade.None()
                .NotFound.Ignore();

            References(x => x.Category, "category_id")
                .ReadOnly()
                .Cascade.None()
                .NotFound.Ignore();
        }
    }

    public class RoomData
    {
        public virtual int Id { get; set; }
        public virtual int OwnerId { get; set; }
        public virtual string Name { get; set; }
        public virtual string Description { get; set; }
        public virtual int CategoryId { get; set; }
        public virtual RoomStatus AccessType { get; set; }
        public virtual string Password { get; set; }
        public virtual int ModelId { get; set; }
        public virtual string CCTs { get; set; }
        public virtual int Wallpaper { get; set; }
        public virtual int Floor { get; set; }
        public virtual string Landscape { get; set; }
        public virtual PlayerData OwnerData { get; set; }
        public virtual NavigatorCategoryData Category { get; set; }
        public virtual int Rating { get; set; }
        public virtual bool IsPrivateRoom => OwnerId > 0;
        public virtual bool IsPublicRoom => OwnerId == 0;
        public virtual int UsersNow { get; set; }
        public virtual int UsersMax { get; set; }
        public virtual bool ShowName { get; set; }
        public virtual bool SuperUsers { get; set; }

        public static RoomStatus ToStatusEnum(int roomAccess)
        {
            switch (roomAccess)
            {
                case 1:
                    return RoomStatus.CLOSED;
                case 2:
                    return RoomStatus.PASSWORD;
                default:
                    return RoomStatus.OPEN;

            }
        }
    }

    public enum RoomStatus
    {
        OPEN = 0,
        CLOSED = 1,
        PASSWORD = 2
    }
}
