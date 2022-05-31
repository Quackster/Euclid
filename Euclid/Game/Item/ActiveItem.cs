using Euclid.Game.Entity.Humanoid;
using Euclid.Network.Streams.Util;
using Euclid.Storage.Database.Data;
using System;
using System.Collections.Generic;

namespace Euclid.Game
{
    public class ActiveItem : Item
    {
        public ActiveItem(ItemData data) : base(data)
        {
        }
    }
}
