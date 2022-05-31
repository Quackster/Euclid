using Euclid.Game.Entity.Humanoid;
using Euclid.Network.Streams.Util;
using Euclid.Storage.Database.Data;
using System;
using System.Collections.Generic;

namespace Euclid.Game
{
    public class CatalogueOrder : IEntity
    {
        #region Properties

        public CatalogueItem Item { get; private set; }
        public string CustomData { get; private set; }

        #endregion

        #region Constructors

        public CatalogueOrder(CatalogueItem item, string customData)
        {
            Item = item;
            CustomData = customData;
        }

        #endregion

    }
}
