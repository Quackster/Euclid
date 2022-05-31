using Euclid.Game;
using Euclid.Network.Streams.Util;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Euclid.Messages.Outgoing
{
    class REMOVEITEM : IMessageComposer
    {
        private Item item;

        public REMOVEITEM(Item item)
        {
            this.item = item;
        }

        public override void Write()
        {
            Data.Add(new ArgumentEntry(this.item.Data.Id));
        }
    }
}
