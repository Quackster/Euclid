using Euclid.Storage.Database.Access;
using Euclid.Storage.Database.Data;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;

namespace Euclid.Game
{
    public class CatalogueManager : ILoadable
    {
        #region Fields

        public static readonly CatalogueManager Instance = new CatalogueManager();

        #endregion

        #region Properties

        public List<CatalogueItem> Offers { get; private set; }
        public ConcurrentDictionary<int, CatalogueOrder> OrderHistory { get; private set; }

        #endregion

        #region Constructors

        public void Load()
        {
            Offers = CatalogueDao.GetCatalogueOffers().Select(x => new CatalogueItem(x)).ToList();
            OrderHistory = new ConcurrentDictionary<int, CatalogueOrder>();
        }

        #endregion

        #region Public methods


        #endregion
    }
}
