using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;
using Euclid.Util.Extensions;
using Euclid.Util;

namespace Euclid.Messages.Incoming
{
    class APPROVENAME : IMessageEvent
    {
        #region Properties

        public bool AuthenticationRequired => false;

        #endregion

        public void Handle(Player player, Request request)
        {
            if (!(request.GetArgumentAmount() > 0))
            {
                return;
            }

            string name = request.GetArgument(0).FilterInput();

            if (RegisterUtil.IsValidName(name))
            {
                player.Send(new NAME_APPROVED());
            }
            else
            {
                player.Send(new NAME_UNACCEPTABLE());
            }
        }
    }
}
