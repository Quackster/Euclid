using Euclid.Game;
using Euclid.Network.Streams.Util;
using System.Collections.Generic;

namespace Euclid.Messages.Outgoing
{
    public class ITEMS : IMessageComposer
    {
        private List<ActiveItem> items;

        public ITEMS(List<ActiveItem> items)
        {
            this.items = items;
        }

        public override void Write()
        {
            foreach (var item in items)
            {
                item.Serialise(Data);
            }
        }
    }
}