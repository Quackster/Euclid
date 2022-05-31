using DotNetty.Buffers;
using DotNetty.Transport.Bootstrapping;
using DotNetty.Transport.Channels;
using DotNetty.Transport.Channels.Sockets;
using Euclid.Network.Streams;
using log4net;
using System;
using System.Collections.Generic;
using System.Net;
using System.Reflection;

namespace Euclid.Network
{
    public class GameServer
    {
        #region Fields

        private static readonly ILog m_Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private static GameServer m_GameServer;// = new GameServer();

        private MultithreadEventLoopGroup m_BossGroup;
        private MultithreadEventLoopGroup m_WorkerGroup;

        #endregion

        #region Properties

        /// <summary>
        /// Invoke the singleton instance
        /// </summary>
        public static GameServer Instance
        {
            get
            {
                return m_GameServer;
            }
        }

        /// <summary>
        /// Get the logger
        /// </summary>
        public static ILog Logger
        {
            get
            {
                return m_Log;
            }
        }

        /// <summary>
        /// Get the main server details
        /// </summary>
        public GameAddress MainServer { get; }

        /// <summary>
        /// Get the private server details
        /// </summary>
        public GameAddress PrivateServer { get; }

        /// <summary>
        /// Get the public servers details
        /// </summary>
        public List<GameAddress> PublicServers { get; }

        #endregion

        #region Constructors

        /// <summary>
        /// GameServer constructor
        /// </summary>
        public GameServer(GameAddress mainServer, GameAddress privateServer, List<GameAddress> publicServerAddresses)
        {
            this.m_BossGroup = new MultithreadEventLoopGroup(1);
            this.m_WorkerGroup = new MultithreadEventLoopGroup(10);

            this.MainServer = mainServer;
            this.PrivateServer = privateServer;
            this.PublicServers = publicServerAddresses;
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Initialise the game server by given pot
        /// </summary>
        /// <param name="port">the game port</param>
        public void InitialiseServer()
        {
            try
            {
                ServerBootstrap bootstrap = new ServerBootstrap()
                    .Group(m_BossGroup, m_WorkerGroup)
                    .Channel<TcpServerSocketChannel>()
                    .ChildHandler(new GameChannelInitializer())
                    .ChildOption(ChannelOption.TcpNodelay, true)
                    .ChildOption(ChannelOption.SoKeepalive, true)
                    .ChildOption(ChannelOption.SoReuseaddr, true)
                    .ChildOption(ChannelOption.SoRcvbuf, 1024)
                    .ChildOption(ChannelOption.RcvbufAllocator, new FixedRecvByteBufAllocator(1024))
                    .ChildOption(ChannelOption.Allocator, UnpooledByteBufferAllocator.Default);

                bootstrap.BindAsync(IPAddress.Parse(this.MainServer.IpAddress), this.MainServer.Port);
                bootstrap.BindAsync(IPAddress.Parse(this.PrivateServer.IpAddress), this.PrivateServer.Port);

                foreach (var gameAddress in PublicServers)
                {
                    bootstrap.BindAsync(IPAddress.Parse(gameAddress.IpAddress), gameAddress.Port);
                }
            }
            catch (Exception e)
            {
                m_Log.Error($"Failed to setup network listener... {e}");
            }
        }

        #endregion

        #region Static methods

        public static void CreateServer(GameAddress mainServer, GameAddress privateServer, List<GameAddress> publicServerAddresses)
        {
            m_GameServer = new GameServer(mainServer, privateServer, publicServerAddresses);
        }

        #endregion
    }
}
