using Euclid.Game;
using Euclid.Network.Streams.Util;
using System.Collections.Generic;

namespace Euclid.Messages.Outgoing
{
    public class ACTIVE_OBJECTS : IMessageComposer
    {
        private List<ActiveItem> items;

        public ACTIVE_OBJECTS(List<ActiveItem> items)
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