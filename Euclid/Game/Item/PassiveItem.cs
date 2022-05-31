using Euclid.Game.Entity.Humanoid;
using Euclid.Network.Streams.Util;
using Euclid.Storage.Database.Data;
using System;
using System.Collections.Generic;

namespace Euclid.Game
{
    public class PassiveItem : Item
    {
        public override ItemDefinition Definition { get; set; }

        public PassiveItem(ItemData data) : base(data)
        {
        }
    }
}
