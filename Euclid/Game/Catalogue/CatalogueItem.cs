using Euclid.Game.Entity.Humanoid;
using Euclid.Network.Streams.Util;
using Euclid.Storage.Database.Data;
using System;
using System.Collections.Generic;

namespace Euclid.Game
{
    public class CatalogueItem : IEntity
    {
        #region Properties

        public CatalogueData Data { get; }
        public virtual ItemDefinition Definition => ItemManager.Instance.GetDefinition(Data.DefinitionId);
       
        #endregion

        #region Constructors

        public CatalogueItem(CatalogueData data)
        {
            Data = data;
        }

        #endregion

    }
}
