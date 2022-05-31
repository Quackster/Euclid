using Euclid.Network.Streams.Util;

namespace Euclid.Messages.Outgoing
{
    class HEIGHTMAP : IMessageComposer
    {
        private string heightmap;

        public HEIGHTMAP(string heightmap)
        {
            this.heightmap = heightmap;
        }

        public override void Write()
        {
            Data.Add(new ArgumentEntry(this.heightmap));
        }
    }
}
