using FluentNHibernate.Mapping;

namespace Euclid.Storage.Database.Data
{
    class NavigatorCategoryMapping : ClassMap<NavigatorCategoryData>
    {
        public NavigatorCategoryMapping()
        {
            Table("room_category");
            Id(x => x.Id, "id");
            Map(x => x.OrderId, "order_id");
            Map(x => x.ParentId, "parent_id");
            Map(x => x.Name, "name");
            Map(x => x.IsNode, "is_node");
            Map(x => x.VisibleRank, "visible_rank");
            Map(x => x.IsPublicSpaces, "is_public_spaces");
            Map(x => x.IsTradingAllowed, "is_trading_allowed");
        }
    }

    public class NavigatorCategoryData
    {
        public virtual int Id { get; set; }
        public virtual int OrderId { get; set; }
        public virtual int ParentId { get; set; }
        public virtual string Name { get; set; }
        public virtual int VisibleRank { get; set; }
        public virtual bool IsNode { get; set; }
        public virtual bool IsPublicSpaces { get; set; }
        public virtual bool IsTradingAllowed { get; set; }
    }
}
