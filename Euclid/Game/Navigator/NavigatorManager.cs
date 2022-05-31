using Euclid.Storage.Database.Access;
using System.Collections.Generic;
using System.Linq;

namespace Euclid.Game
{
    public class NavigatorManager : ILoadable
    {
        #region Fields

        public static readonly NavigatorManager Instance = new NavigatorManager();

        #endregion

        #region Properties

        public List<NavigatorCategory> Categories;

        #endregion

        #region Constructors

        public void Load()
        {
            Categories = NavigatorDao.GetCategories().Select(x => new NavigatorCategory(x)).ToList();
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Get applicable categories for rank
        /// </summary>
        public List<NavigatorCategory> GetCategories(int rank)
        {
            return Categories.Where(x => (rank >= x.Data.VisibleRank)).ToList();
        }

        #endregion
    }
}
