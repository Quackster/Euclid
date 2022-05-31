using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Session;
using Euclid.Network.Streams;

namespace Euclid.Messages.Incoming
{
    class VERSIONCHECK : IMessageEvent
    {
        #region Properties

        public bool AuthenticationRequired => false;

        #endregion

        public void Handle(Player player, Request request)
        {
            if (player.ConnectionMode == ConnectionMode.MAIN)
                player.Connection.InitialiseEncryption();

            if (Euclid.Encryption)
                player.Send(new ENCRYPTION_ON());
            else
                player.Send(new ENCRYPTION_OFF());

            player.Send(new SECRET_KEY(player.Connection.PublicKey));
        }
    }
}
