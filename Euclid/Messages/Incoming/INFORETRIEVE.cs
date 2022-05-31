using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;
using Euclid.Storage.Database.Access;

namespace Euclid.Messages.Incoming
{
    class INFORETRIEVE : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            string username = request.GetArgument(0);
            string password = request.GetArgument(1);

            var playerData = UserDao.Login(username, password);

            if (playerData == null)
            {
                return;
            }

            player.Send(new USEROBJECT(playerData));
        }
    }
}
