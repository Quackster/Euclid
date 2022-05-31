using Euclid.Network.Streams.Util;

namespace Euclid.Messages.Outgoing
{
    class SECRET_KEY : IMessageComposer
    {
        private string publicKey;

        public SECRET_KEY(string publicKey)
        {
            this.publicKey = publicKey;
        }

        public override void Write()
        {
            this.Data.Add(new ArgumentEntry(this.publicKey));
        }
    }
}
