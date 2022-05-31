using Euclid.Game;
using Euclid.Network.Streams;

namespace Euclid.Messages.Incoming
{
    class FINDUSER : IMessageEvent
    {
        #region Properties

        public bool AuthenticationRequired => false;

        #endregion

        public void Handle(Player player, Request request)
        {

        }
    }
}
