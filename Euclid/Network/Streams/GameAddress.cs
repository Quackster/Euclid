namespace Euclid.Network.Streams
{
    public class GameAddress
    {
        #region Fields

        public string IpAddress { get; }
        public int Port { get; }
        public int RoomId { get; }

        #endregion

        #region Constructors

        public GameAddress(string ipAddress, int port, int roomId = 0)
        {
            IpAddress = ipAddress;
            Port = port;
            RoomId = roomId;
        }

        #endregion
    }
}
