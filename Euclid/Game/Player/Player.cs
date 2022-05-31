using Euclid.Game.Entity.Humanoid;
using Euclid.Messages;
using Euclid.Messages.Outgoing;
using Euclid.Network.Session;
using Euclid.Storage.Database.Access;
using Euclid.Storage.Database.Data;
using log4net;
using System;
using System.Linq;
using System.Reflection;

namespace Euclid.Game
{
    public class Player : Humanoid
    {
        #region Fields

        private ILog log = LogManager.GetLogger(typeof(Player));
        private PlayerData playerData;

        #endregion

        #region Interface properties

        /// <summary>
        /// Get entity data
        /// </summary>
        public override IEntityData EntityData => (IEntityData)playerData;

        /// <summary>
        /// Get entity type
        /// </summary>
        public override EntityType EntityType => EntityType.PLAYER;

        /// <summary>
        /// Get room entity
        /// </summary>
        public override RoomEntity RoomEntity { get; set; }

        #endregion

        #region Properties

        /// <summary>
        /// Get the connection session
        /// </summary>
        public ConnectionSession Connection { get; private set; }


        public ConnectionMode ConnectionMode => Connection.Mode;

        /// <summary>
        /// Get the logging
        /// </summary>
        public ILog Log => log;

        /// <summary>
        /// Get entity data
        /// </summary>
        public PlayerData Details => playerData;

        /// <summary>
        /// Get inventory instance for user
        /// </summary>
        public Inventory Inventory { get; private set; }

        /// <summary>
        /// Whether the player has logged in or not
        /// </summary>
        public bool Authenticated { get; private set; }

        /// <summary>
        /// The time when player connected
        /// </summary>
        public DateTime AuthenticationTime { get; private set; }

        /// <summary>
        /// Get user group
        /// </summary>
        public UserGroup UserGroup => PermissionsManager.Instance.Ranks[Details.Rank];

        /// <summary>
        /// Get whether disconnect was handled
        /// </summary>
        public bool Disconnected { get; private set; }

        /// <summary>
        /// Get the player by the main server connection
        /// </summary>
        public Player MainServer => PlayerManager.Instance.Players.FirstOrDefault(x => x.Details.Id == Details.Id && x.ConnectionMode == ConnectionMode.MAIN);

        /// <summary>
        /// Get the player by the private server connection
        /// </summary>
        public Player PrivateServer => PlayerManager.Instance.Players.FirstOrDefault(x => x.Details.Id == Details.Id && x.ConnectionMode == ConnectionMode.PRIVATE);

        /// <summary>
        /// Get the player by the public server connection
        /// </summary>
        public Player PublicServer => PlayerManager.Instance.Players.FirstOrDefault(x => x.Details.Id == Details.Id && x.ConnectionMode == ConnectionMode.PUBLIC);

        #endregion

        #region Constructors

        /// <summary>
        /// Constructor for player.
        /// </summary>
        /// <param name="channel">the channel</param>
        public Player(ConnectionSession connectionSession)
        {
            Connection = connectionSession;
            playerData = new PlayerData();
            RoomEntity = new RoomPlayer(this);

            if (ConnectionMode == ConnectionMode.MAIN)
                log = LogManager.GetLogger(Assembly.GetExecutingAssembly(), $"[Main] Connection {connectionSession.Channel.Id}");

            if (ConnectionMode == ConnectionMode.PRIVATE)
                log = LogManager.GetLogger(Assembly.GetExecutingAssembly(), $"[Private] Connection {connectionSession.Channel.Id}");

            if (ConnectionMode == ConnectionMode.PUBLIC)
                log = LogManager.GetLogger(Assembly.GetExecutingAssembly(), $"[Public] Connection {connectionSession.Channel.Id}");
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Login handler
        /// </summary>
        /// <param name="ssoTicket">the sso ticket</param>
        /// <returns></returns>
        public bool TryLogin(string username, string password)
        {
            playerData = UserDao.Login(username, password);

            if (playerData == null)
            {
                Send(new SYSTEMBROADCAST("Your username or password was incorrect."));
                return false;
            }

            if (ConnectionMode == ConnectionMode.MAIN)
                log = LogManager.GetLogger(Assembly.GetExecutingAssembly(), $"[Main] Player {playerData.Name}");

            if (ConnectionMode == ConnectionMode.PRIVATE)
                log = LogManager.GetLogger(Assembly.GetExecutingAssembly(), $"[Private] Player {playerData.Name}");

            if (ConnectionMode == ConnectionMode.PUBLIC)
                log = LogManager.GetLogger(Assembly.GetExecutingAssembly(), $"[Public] Player {playerData.Name}");

            log.Debug($"Player {playerData.Name} has logged in");

            playerData.LastOnline = DateTime.Now;
            
            Inventory = new Inventory(this);

            // Don't bother loading inventory on anything but the private connection
            if (this.ConnectionMode == ConnectionMode.PRIVATE)
            {
                Inventory.Load();
            }
            UserDao.SaveLastOnline(playerData);
            PlayerManager.Instance.AddPlayer(this);

            Authenticated = true;
            AuthenticationTime = DateTime.Now;

            return true;
        }

        /// <summary>
        /// Send message composer
        /// </summary>
        public void Send(IMessageComposer composer)
        {
            Connection.Send(composer);
        }

        /// <summary>
        /// Disconnection handler
        /// </summary>
        public void OnDisconnect()
        {
            if (!Authenticated)
                return;

            PlayerManager.Instance.RemovePlayer(this);

            playerData.LastOnline = DateTime.Now;
            UserDao.SaveLastOnline(playerData);

            if (RoomEntity.Room != null)
            {
                if (RoomEntity.BeingKicked)
                {
                    // Kick instead of leave room
                    RoomEntity.kick(false);
                }
                else
                {
                    RoomEntity.Room.EntityManager.LeaveRoom(this);
                }
            }

            this.Disconnected = true;

            /*long timeInSeconds = (long)(DateTime.Now - AuthenticationTime).TotalSeconds;
            settings.OnlineTime += timeInSeconds;*/
           
        }

        public bool Disconnect() {

            Connection.Disconnect();
            return true;
        }

        #endregion
    }
}
