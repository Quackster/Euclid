using System;
using System.Net;
using DotNetty.Transport.Channels;
using Euclid.Game;
using Euclid.Messages;
using Euclid.Util.Encryption;

namespace Euclid.Network.Session
{
    public class ConnectionSession
    {
        #region Fields

        private bool m_Disconnected;

        #endregion

        #region Properties

        /// <summary>
        /// Gets the player channel.
        /// </summary>
        public IChannel Channel { get; private set; }

        /// <summary>
        /// Get the ip address of the player connected.
        /// </summary>
        public string IpAddress { get; set; }

        /// <summary>
        /// Get player instance
        /// </summary>
        public Player Player { get; private set; }

        /// <summary>
        /// Encryption instance
        /// </summary>
        public RC4 Encryption { get; private set; }
        
        /// <summary>
        /// Public key for client-side deciphering
        /// </summary>
        public string PublicKey { get; private set; }
        
        /// <summary>
        /// Locally connected port
        /// </summary>
        public int LocalPort { get; private set; }

        /// <summary>
        /// Connection state on whether it is on MAIN, PRIVATE, PUBLIC
        /// </summary>
        public ConnectionMode Mode { get; private set; }

        #endregion

        #region Constructors

        /// <summary>
        /// Constructor for player.
        /// </summary>
        /// <param name="channel">the channel</param>
        public ConnectionSession(IChannel channel)
        {
            Channel = channel;
            IpAddress = ((IPEndPoint)channel.LocalAddress).Address.ToString();
            LocalPort = ((IPEndPoint)channel.LocalAddress).Port;
            PublicKey = Guid.NewGuid().ToString().Replace("-", "").ToLower();

            if (LocalPort == GameServer.Instance.MainServer.Port)
                Mode = ConnectionMode.MAIN;

            if (LocalPort == GameServer.Instance.PrivateServer.Port)
                Mode = ConnectionMode.PRIVATE;

            if (GameServer.Instance.PublicServers.Exists(x => x.Port == LocalPort))
                Mode = ConnectionMode.PUBLIC;

            Player = new Player(this);
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Send message composer
        /// </summary>
        public void Send(IMessageComposer composer)
        {
            if (!composer.Composed)
            {
                composer.Composed = true;
                composer.Write();
            }

            try
            {
                Channel.WriteAndFlushAsync(composer);
            }
            catch { }
        }

        public void InitialiseEncryption()
        {
            if (Encryption != null)
            {
                return;
            }

            Encryption = new RC4();
            Encryption.SetKey(SecretKey.SecretDecode(PublicKey));

            if (Euclid.Encryption)
            {
                // If the connection mode is the main server, or there is no main server connection then we add it
                // if (Mode == ConnectionMode.MAIN || Player.MainServer == null)
                Channel.Pipeline.AddFirst("encryption", new EncryptionDecoder(this.Encryption));
            }
        }

        /// <summary>
        /// Disconnection handler
        /// </summary>
        public virtual void OnDisconnect()
        {
            if (m_Disconnected)
                return;

            m_Disconnected = true;
            Player.OnDisconnect();
        }

        /// <summary>
        /// Forcibly disconnect player
        /// </summary>
        public virtual void Disconnect()
        {
            if (m_Disconnected)
                return;

            this.Channel.CloseAsync();
        }

        #endregion

    }
}
