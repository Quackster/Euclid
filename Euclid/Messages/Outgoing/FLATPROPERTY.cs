using Euclid.Network.Streams.Util;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Euclid.Messages.Outgoing
{
    class FLATPROPERTY : IMessageComposer
    {
        private string sprite;
        private string customData;

        public FLATPROPERTY(string sprite, string customData)
        {
            this.sprite = sprite;
            this.customData = customData;
        }

        public override void Write()
        {
            this.Data.Add(new ArgumentEntry(this.sprite));
            this.Data.Add(new DelimeterEntry(this.customData, "/"));
        }
    }
}
