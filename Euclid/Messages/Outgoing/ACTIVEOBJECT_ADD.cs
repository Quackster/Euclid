using Euclid.Game;
using Euclid.Network.Streams.Util;
using System.Collections.Generic;

namespace Euclid.Messages.Outgoing
{
    public class ACTIVEOBJECT_ADD : IMessageComposer
    {
        private Item item;

        public ACTIVEOBJECT_ADD(Item item)
        {
            this.item = item;
        }

        public override void Write()
        {
            this.item.Serialise(Data);
        }
    }
}