using Euclid.Network.Streams.Util;

namespace Euclid.Messages.Outgoing
{
    internal class WALLETBALANCE : IMessageComposer
    {
        private int credits;

        public WALLETBALANCE(int credits)
        {
            this.credits = credits;
        }

        public override void Write()
        {
            this.Data.Add(new ArgumentEntry(credits));
        }
    }
}