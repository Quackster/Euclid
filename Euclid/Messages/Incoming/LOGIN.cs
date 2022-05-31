using Euclid.Game;
using Euclid.Network;
using Euclid.Network.Session;
using Euclid.Network.Streams;
using System.Linq;

namespace Euclid.Messages.Incoming
{
    class LOGIN : IMessageEvent
    {
        #region Properties

        public bool AuthenticationRequired => false;

        #endregion

        public void Handle(Player player, Request request)
        {
            if (player.Authenticated)
            {
                return;
            }

            string username = request.GetArgument(0);
            string password = request.GetArgument(1);

            player.TryLogin(username, password);

            // User is entering a public room
            if (player.ConnectionMode == ConnectionMode.PUBLIC)
            {
                var publicServer = GameServer.Instance.PublicServers.FirstOrDefault(x => x.Port == player.Connection.LocalPort);

                if (publicServer == null)
                {
                    return;
                }

                var room = RoomManager.Instance.GetRoom(publicServer.RoomId);

                if (room == null)
                {
                    return;
                }

                room.EntityManager.EnterRoom(player);
            }
        }
    }
}

