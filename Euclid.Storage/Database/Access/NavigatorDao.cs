using Euclid.Storage.Database.Data;
using System.Collections.Generic;

namespace Euclid.Storage.Database.Access
{
    public class NavigatorDao
    {
        /// <summary>
        /// Get list of room categories
        /// </summary>
        public static List<NavigatorCategoryData> GetCategories()
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                return session.QueryOver<NavigatorCategoryData>().List() as List<NavigatorCategoryData>;
            }
        }
    }
}
