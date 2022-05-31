using Euclid.Storage.Database.Data;
using NHibernate.Linq;
using System.Collections.Generic;
using System.Linq;

namespace Euclid.Storage.Database.Access
{
    public class CatalogueDao
    {
        /// <summary>
        /// Get list of all definition data
        /// </summary>
        public static List<CatalogueData> GetCatalogueOffers()
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                return session.QueryOver<CatalogueData>().List() as List<CatalogueData>;
            }
        }

    }
}
