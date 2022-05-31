using Euclid.Storage.Database.Access;
using Euclid.Storage.Database.Data;
using Euclid.Util.Extensions;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;

namespace Euclid.Game
{
    public class PlayerManager
    {
        #region Fields

        public static readonly PlayerManager Instance = new PlayerManager();

        #endregion

        #region Properties

        /// <summary>
        /// Get dictionary of players with names as keys
        /// </summary>
        public ConcurrentDictionary<Player, int> AuthenticatedPlayers { get; private set; }

        /// <summary>
        /// Get the list of online players
        /// </summary>
        public List<Player> Players
        {
            get => AuthenticatedPlayers.Keys.ToList();
        }

        #endregion

        #region Constructors

        public PlayerManager()
        {
            AuthenticatedPlayers = new ConcurrentDictionary<Player, int>();
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Add the player
        /// </summary>
        /// <param name="player">remove the player</param>
        public void AddPlayer(Player player)
        {
            AuthenticatedPlayers.TryAdd(player, player.EntityData.Id);
        }

        /// <summary>
        /// Add the player
        /// </summary>
        /// <param name="player">remove the player</param>
        public void RemovePlayer(Player player)
        {
            AuthenticatedPlayers.Remove(player);
        }

        /// <summary>
        /// Get the player by username
        /// </summary>
        /// <param name="username">the player username</param>
        /// <returns></returns>
        public Player GetPlayerByName(string username)
        {
            return AuthenticatedPlayers.Keys.FirstOrDefault(x => x.Details.Name == username);
        }

        /// <summary>
        /// Get the player by id
        /// </summary>
        /// <param name="id">the player username</param>
        /// <returns></returns>
        public Player GetPlayerById(int id)
        {
            return AuthenticatedPlayers.Keys.FirstOrDefault(x => x.Details.Id == id);
        }

        /// <summary>
        /// Get data by id
        /// </summary>
        public PlayerData GetDataById(int userId)
        {
            var player = GetPlayerById(userId);

            if (player != null)
                return player.Details;

            return UserDao.GetById(userId);
        }

        /// <summary>
        /// Get data by name
        /// </summary>
        public PlayerData GetDataByName(int userId)
        {
            var player = GetPlayerById(userId);

            if (player != null)
                return player.Details;

            return UserDao.GetById(userId);
        }

        /// <summary>
        /// Get data by name
        /// </summary>
        public string GetName(int userId)
        {
            var player = GetPlayerById(userId);

            if (player != null)
                return player.Details.Name;

            return UserDao.GetNameById(userId);
        }

        #endregion
    }
}
