using Euclid.Game;
using Euclid.Network.Session;
using Euclid.Network.Streams;

namespace Euclid.Messages.Incoming
{
    class CLIENTIP : IMessageEvent
    {
        #region Properties

        public bool AuthenticationRequired => false;

        #endregion

        public void Handle(Player player, Request request)
        {
            if (player.ConnectionMode == ConnectionMode.PUBLIC ||
                player.ConnectionMode == ConnectionMode.PRIVATE)
                player.Connection.InitialiseEncryption();
        }
    }
}
