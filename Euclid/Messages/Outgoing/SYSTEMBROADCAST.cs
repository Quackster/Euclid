using Euclid.Network.Streams.Util;

namespace Euclid.Messages.Outgoing
{
    class SYSTEMBROADCAST : IMessageComposer
    {
        private string message;

        public SYSTEMBROADCAST(string message)
        {
            this.message = message;
        }

        public override void Write()
        {
            this.Data.Add(new ArgumentEntry(this.message));
        }
    }
}
