using Euclid.Util;
using log4net;
using log4net.Config;
using System;
using System.IO;
using System.Reflection;
using Euclid.Storage.Database;
using Euclid.Network;
using Euclid.Storage.Database.Access;
using Euclid.Game;
using Euclid.Messages;
using System.Threading;
using System.Linq;
using Euclid.Network.Streams;
using System.Collections.Generic;

namespace Euclid
{
    class Euclid
    {
        #region Fields

        private static readonly ILog log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        public static readonly bool Encryption = true;

        #endregion

        #region Properties

        /// <summary>
        /// Get the logger instance.
        /// </summary>
        public static ILog Logger
        {
            get { return log; }
        }

        /// <summary>
        /// Get the official release supported
        /// </summary>
        public static string ClientVersion
        {
            get { return "RELEASE63-201302211227-193109692"; }
        }

        #endregion

        static void Main(string[] args)
        {
            var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
            XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));

            Console.Title = "Euclid - Habbo Hotel Emulation";

            log.Info("Booting Euclid - Written by Quackster");
            log.Info("Emulation of Habbo Hotel 2007 Shockwave client");

            /*
            var encrypt_rc4 = new RC4();
            var key = encrypt_rc4.CreateKey();
            encrypt_rc4.SetKey(key);
            var encipher = encrypt_rc4.Encipher("test");

            var decrypt_rc4 = new RC4();
            decrypt_rc4.SetKey(key);
            var decipher = decrypt_rc4.Decipher(encipher);
            Console.WriteLine(encipher + " == " + decipher);
            */

            /*
            var rc4 = new RC4();
            rc4.SetKey(1337);
            Console.WriteLine(rc4.Decipher("6055992B"));
            // Console.WriteLine(rc4.Encipher("test"));
            */

            try
            {
                tryDatabaseConnection();
                tryGameData();
                tryCreateServer();

                log.Info("Server has started!");

                while (true) Thread.Sleep(100);
            }
            catch (Exception ex)
            {
                log.Error(ex);
            }

#if DEBUG
            Console.Read();
#endif

        }

        #region Private methods

        /// <summary>
        /// Test database connection
        /// </summary>
        private static void tryDatabaseConnection()
        {
            log.Info("Attempting to connect to database");
            SessionFactoryBuilder.Instance.InitialiseSessionFactory(ServerConfig.Instance.ConnectionString);
            log.Info("Connection successful!");
        }

        /// <summary>
        /// Load game data
        /// </summary>
        private static void tryGameData()
        {
            RoomDao.ResetVisitorCounts();

            PermissionsManager.Instance.Load();
            ValueManager.Instance.Load();
            ItemManager.Instance.Load();
            CatalogueManager.Instance.Load();
            RoomManager.Instance.Load();
            NavigatorManager.Instance.Load();
            MessageHandler.Instance.Load();
            PluginManager.Instance.Load();        
        }

        /// <summary>
        /// Try and create server
        /// </summary>
        private static void tryCreateServer()
        {
            GameServer.Logger.Info("Starting server");

            var roomIds = ServerConfig.Instance.ConfigValues
                .Where(x => x.Key.Contains("server/public/port/"))
                .Select(x => x.Key.Replace("server/public/port/", ""))
                .ToList();

            var gameAddresses = new List<GameAddress>();
            
            foreach (var roomId in roomIds)
            {
                gameAddresses.Add(new GameAddress(ServerConfig.Instance.GetString("server", "public", "ip", roomId), ServerConfig.Instance.GetInt("server", "public", "port", roomId), int.Parse(roomId)));
            }

            GameServer.CreateServer(
                new GameAddress(ServerConfig.Instance.GetString("server", "main", "ip"), ServerConfig.Instance.GetInt("server", "main", "port")),
                new GameAddress(ServerConfig.Instance.GetString("server", "private", "ip"), ServerConfig.Instance.GetInt("server", "private", "port")),
                gameAddresses
            );

            GameServer.Instance.InitialiseServer();

            foreach (var gameAddress in gameAddresses)
            {
                var roomData = RoomDao.GetRoomData(gameAddress.RoomId);

                if (roomData == null)
                    continue;

                GameServer.Logger.Info($"[{roomData.Name}] is listening on port: {gameAddress.IpAddress}:{gameAddress.Port}!");
            }

            GameServer.Logger.Info($"Private server is now listening on port: {GameServer.Instance.PrivateServer.IpAddress}:{GameServer.Instance.PrivateServer.Port}!");
            GameServer.Logger.Info($"Main server is now listening on port: {GameServer.Instance.MainServer.IpAddress}:{GameServer.Instance.MainServer.Port}!");
        }

        public class Game
        {
        }

        #endregion
    }
}
