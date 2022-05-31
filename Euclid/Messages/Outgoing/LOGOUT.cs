using Euclid.Network.Streams.Util;

namespace Euclid.Messages.Outgoing
{
    class LOGOUT : IMessageComposer
    {
        private string name;

        public LOGOUT(string name)
        {
            this.name = name;
        }

        public override void Write()
        {
            Data.Add(new ArgumentEntry(this.name));
        }
    }
}
