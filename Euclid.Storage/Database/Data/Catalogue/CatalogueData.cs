using FluentNHibernate.Mapping;
using System;

namespace Euclid.Storage.Database.Data
{
    class CatalogueMapping : ClassMap<CatalogueData>
    {
        public CatalogueMapping()
        {
            Table("catalogue");
            Id(x => x.Id, "id");
            Map(x => x.Section, "section");
            Map(x => x.Price, "price");
            Map(x => x.DefinitionId, "definition_id");
        }
    }

    public class CatalogueData
    {
        public virtual string Id { get; set; }
        public virtual string Section { get; set; }
        public virtual int Price { get; set; }
        public virtual int DefinitionId { get; set; }
    }

}
