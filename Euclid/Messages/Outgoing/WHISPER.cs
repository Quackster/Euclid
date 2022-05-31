using Euclid.Network.Streams.Util;

namespace Euclid.Messages.Outgoing
{
    class WHISPER : IMessageComposer
    {
        private string name;
        private string chatMsg;

        public WHISPER(string name, string chatMsg)
        {
            this.name = name;
            this.chatMsg = chatMsg;
        }

        public override void Write()
        {
            this.Data.Add(new ArgumentEntry(this.name));
            this.Data.Add(new DelimeterEntry(this.chatMsg, ' '));
        }
    }
}
