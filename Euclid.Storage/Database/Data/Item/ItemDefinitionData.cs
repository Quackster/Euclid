using FluentNHibernate.Mapping;

namespace Euclid.Storage.Database.Data
{
    class ItemDefinitionMapping : ClassMap<ItemDefinitionData>
    {
        public ItemDefinitionMapping()
        {
            Table("item_definitions");
            Id(x => x.Id, "id");
            Map(x => x.Sprite, "sprite");
            Map(x => x.Colour, "colour");
            Map(x => x.Length, "length");
            Map(x => x.Width, "width");
            Map(x => x.Height, "height");
            Map(x => x.DataClass, "data_class");
            Map(x => x.IsFloorItem, "is_floor_item");
            Map(x => x.IsWallItem, "is_wall_item");
            Map(x => x.IsStackable, "is_stackable");
            Map(x => x.IsBed, "is_bed");
            Map(x => x.IsChair, "is_chair");
            Map(x => x.IsWalkable, "is_walkable");
            Map(x => x.IsDecoration, "is_decoration");
            Map(x => x.IsTeleporter, "is_teleporter");
            Map(x => x.IsPostIt, "is_post_it");
            Map(x => x.RequiresTouchingForInteraction, "requires_touching_for_interaction");
            Map(x => x.RequiresRightsForInteraction, "requires_rights_for_interaction");
            Map(x => x.Name, "name");
            Map(x => x.Description, "description");
        }
    }

    public class ItemDefinitionData
    {
        public virtual int Id { get; set; }
        public virtual string Sprite { get; set; }
        public virtual string Colour { get; set; }
        public virtual int Length { get; set; }
        public virtual int Width { get; set; }
        public virtual int Height { get; set; }
        public virtual string DataClass { get; set; }
        public virtual bool IsVisible { get; set; }
        public virtual bool IsFloorItem { get; set; }
        public virtual bool IsWallItem { get; set; }
        public virtual bool IsStackable { get; set; }
        public virtual bool IsBed { get; set; }
        public virtual bool IsChair { get; set; }
        public virtual bool IsWalkable { get; set; }
        public virtual bool IsDecoration { get; set; }
        public virtual bool IsTeleporter { get; set; }
        public virtual bool IsPostIt { get; set; }
        public virtual bool RequiresTouchingForInteraction { get; set; }
        public virtual bool RequiresRightsForInteraction { get; set; }
        public virtual string Name { get; set; }
        public virtual string Description { get; set; }
    }
}
