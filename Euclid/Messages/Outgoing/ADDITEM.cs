using Euclid.Game;
using Euclid.Network.Streams.Util;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Euclid.Messages.Outgoing
{
    class ADDITEM : IMessageComposer
    {
        private Item item;

        public ADDITEM(Item item)
        {
            this.item = item;
        }

        public override void Write()
        {
            this.item.Serialise(Data);
        }
    }
}
