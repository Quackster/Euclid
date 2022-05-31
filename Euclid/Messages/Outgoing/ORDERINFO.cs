using Euclid.Network.Streams.Util;

namespace Euclid.Messages.Outgoing
{
    class ORDERINFO : IMessageComposer
    {
        private string id;
        private int price;
        private string customData;
        private string name;

        public ORDERINFO(string id, int price, string customData, string name)
        {
            this.id = id;
            this.price = price;
            this.customData = customData;
            this.name = name;
        }

        public override void Write()
        {
            this.Data.Add(new ArgumentEntry(id));
            this.Data.Add(new ArgumentEntry(price));
            this.Data.Add(new ArgumentEntry(customData));
            this.Data.Add(new ArgumentEntry(name));
        }
    }
}
