using Euclid.Storage.Database.Data;
using NHibernate.Linq;
using System.Linq;

namespace Euclid.Storage.Database.Access
{
    public class UserDao
    {
        /// <summary>
        /// Login user with SSO ticket
        /// </summary>
        public static PlayerData Login(string username, string password)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                PlayerData playerDataAlias = null;

                var row = session.QueryOver(() => playerDataAlias)
                    .Where(() =>
                        (playerDataAlias.Name == username) &&
                        (playerDataAlias.Password == password))
                    .Take(1)
                .SingleOrDefault();

                if (row != null)
                    return row;
            }

            return null;
        }

        /// <summary>
        /// Save or update player data
        /// </summary>
        /// <param name="playerData">the player data to save</param>
        public static void SaveOrUpdate(PlayerData playerData)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                using (var transaction = session.BeginTransaction())
                {
                    try
                    {
                        session.SaveOrUpdate(playerData);
                        transaction.Commit();
                        session.Refresh(playerData);
                    }
                    catch
                    {
                        transaction.Rollback();
                    }
                }
            }
        }

        /// <summary>
        /// Get player by username
        /// </summary>
        public static PlayerData GetByName(string name)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                return session.QueryOver<PlayerData>().Where(x => x.Name == name).SingleOrDefault();
            }
        }

        /// <summary>
        /// Get player by id
        /// </summary>
        public static PlayerData GetById(int id)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                return session.QueryOver<PlayerData>().Where(x => x.Id == id).SingleOrDefault();
            }
        }

        /// <summary>
        /// Get player name by id
        /// </summary>
        public static string GetNameById(int id)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                return session.QueryOver<PlayerData>().Select(x => x.Name).Where(x => x.Id == id).SingleOrDefault<string>();
            }
        }

        /// <summary>
        /// Get player id by name
        /// </summary>
        public static int GetIdByName(string name)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                return session.QueryOver<PlayerData>().Select(x => x.Id).Where(x => x.Name == name).SingleOrDefault<int>();
            }
        }

        /// <summary>
        /// Update last online for the last online
        /// </summary>
        public static void SaveLastOnline(PlayerData playerData)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                session.Query<PlayerData>().Where(x => x.Id == playerData.Id).Update(x => new PlayerData { LastOnline = playerData.LastOnline });
            }
        }
    }
}
