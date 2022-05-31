using Euclid.Game;
using Euclid.Network.Streams.Util;
using System.Collections.Generic;

namespace Euclid.Messages.Outgoing
{
    public class ACTIVEOBJECT_REMOVE : IMessageComposer
    {
        private Item item;

        public ACTIVEOBJECT_REMOVE(Item item)
        {
            this.item = item;
        }

        public override void Write()
        {
            Data.Add(new ArgumentEntry(this.item.PaddedId));
            Data.Add(new DelimeterEntry(this.item.Data.Id, string.Empty));
        }
    }
}