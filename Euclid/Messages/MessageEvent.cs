using Euclid.Game;
using Euclid.Network.Streams;

namespace Euclid.Messages
{
    public interface IMessageEvent
    {
        #region Properties

        public bool AuthenticationRequired { get { return true; } set { AuthenticationRequired = value; } }

        #endregion

        void Handle(Player player, Request request);


    }
}