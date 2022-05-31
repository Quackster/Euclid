using Euclid.Storage.Database.Data;
using NHibernate.Criterion;
using NHibernate.Linq;
using System.Collections.Generic;
using System.Linq;

namespace Euclid.Storage.Database.Access
{
    public class RoomDao
    {
        public static List<RoomData> SearchRooms(string query, int roomLimit = 50)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                RoomData roomDataAlias = null;
                PlayerData playerDataAlias = null;

                /*
                var test = session.QueryOver<RoomData>(() => roomDataAlias)
                    .JoinQueryOver<PlayerData>(() => roomDataAlias.OwnerData, () => playerDataAlias)
                        .Where(() => playerDataAlias.Id == roomDataAlias.OwnerId)
                    .Where(
                        Restrictions.On(() => roomDataAlias.Name).IsInsensitiveLike(query, MatchMode.Anywhere) ||
                        Restrictions.On(() => playerDataAlias.Name).IsInsensitiveLike(query, MatchMode.Anywhere))
                    //.Select(Projections.Entity(() => playerDataAlias))
                    //.Select(x => x.OwnerData);
                    .List<PlayerData>() as List<PlayerData>;*/

                return session.QueryOver<RoomData>(() => roomDataAlias)
                    .JoinQueryOver<PlayerData>(() => roomDataAlias.OwnerData, () => playerDataAlias)
                        .Where(() => playerDataAlias.Id == roomDataAlias.OwnerId)
                    .Where(
                        Restrictions.On(() => roomDataAlias.Name).IsInsensitiveLike(query, MatchMode.Anywhere) ||
                        Restrictions.On(() => playerDataAlias.Name).IsInsensitiveLike(query, MatchMode.Anywhere))
                    .OrderBy(() => roomDataAlias.UsersNow).Desc
                    .OrderBy(() => roomDataAlias.Rating).Desc
                    .Take(roomLimit)
                    .List<RoomData>() as List<RoomData>;
            }
        }

        /// <summary>
        /// Delete room
        /// </summary>
        /// <param name="roomId"></param>
        public static void DeleteRoom(int roomId)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                session.Query<RoomData>().Where(x => x.Id == roomId).Delete();
            }
        }

        /// <summary>
        /// Get the list of users' own rooms
        /// </summary>
        public static List<RoomData> GetPopularFlats(int page, int resultsLimit = 11)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                RoomData roomDataAlias = null;
                PlayerData playerDataAlias = null;

                return session.QueryOver<RoomData>(() => roomDataAlias)
                    .JoinQueryOver<PlayerData>(() => roomDataAlias.OwnerData, () => playerDataAlias)
                        .Where(() => playerDataAlias.Id == roomDataAlias.OwnerId)
                    //.Where(() => roomDataAlias.CategoryId == categoryId)
                    .OrderBy(() => roomDataAlias.UsersNow).Desc
                    .Skip(page * resultsLimit)
                    //.OrderBy(() => roomDataAlias.Rating).Desc
                    .Take(resultsLimit)
                    .List() as List<RoomData>;
            }
        }
        /// <summary>
        /// Get the list of users' own rooms
        /// </summary>
        public static List<RoomData> GetPublicFlats(int categoryId)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                RoomData roomDataAlias = null;

                /*
                var test = session.QueryOver<RoomData>(() => roomDataAlias)
                    .JoinQueryOver<PlayerData>(() => roomDataAlias.OwnerData, () => playerDataAlias)
                        .Where(() => playerDataAlias.Id == roomDataAlias.OwnerId)
                    .Where(
                        Restrictions.On(() => roomDataAlias.Name).IsInsensitiveLike(query, MatchMode.Anywhere) ||
                        Restrictions.On(() => playerDataAlias.Name).IsInsensitiveLike(query, MatchMode.Anywhere))
                    //.Select(Projections.Entity(() => playerDataAlias))
                    //.Select(x => x.OwnerData);
                    .List<PlayerData>() as List<PlayerData>;*/

                return session.QueryOver<RoomData>(() => roomDataAlias)
                    .Where(() => roomDataAlias.OwnerId == 0)
                    .Where(() => roomDataAlias.CategoryId == categoryId)
                    .OrderBy(() => roomDataAlias.UsersNow).Desc
                    .OrderBy(() => roomDataAlias.Rating).Desc
                    .List() as List<RoomData>;
            }
        }

        public static List<RoomData> GetPublicFlats()
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                RoomData roomDataAlias = null;

                return session.QueryOver<RoomData>(() => roomDataAlias)
                    .Where(() => roomDataAlias.OwnerId == 0)
                    .OrderBy(() => roomDataAlias.UsersNow).Desc
                    .OrderBy(() => roomDataAlias.Rating).Desc
                    .List() as List<RoomData>;
            }
        }

        /// <summary>
        /// Get the list of users' own rooms
        /// </summary>
        public static List<RoomData> GetUserRooms(int userId)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                RoomData roomDataAlias = null;

                return session.QueryOver<RoomData>(() => roomDataAlias)
                    .Where(() => roomDataAlias.OwnerId == userId)
                    .OrderBy(() => roomDataAlias.UsersNow).Desc
                    .OrderBy(() => roomDataAlias.Rating).Desc
                    .List() as List<RoomData>;
            }
        }

        /// <summary>
        /// Count the rooms the user has.
        /// </summary>
        public static int CountUserRooms(int userId)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                return session.QueryOver<RoomData>().Where(x => x.OwnerId == userId).RowCount();
            }
        }

        /// <summary>
        /// Get the room model data
        /// </summary>
        /// <returns></returns>
        public static List<RoomModelData> GetModels()
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                return session.QueryOver<RoomModelData>().List() as List<RoomModelData>;
            }
        }

        /// <summary>
        /// Get data for room
        /// </summary>
        public static RoomData GetRoomData(int roomId)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                return session.QueryOver<RoomData>().Where(x => x.Id == roomId).Take(1).SingleOrDefault();
            }
        }

        /// <summary>
        /// Save room data
        /// </summary>
        public static void SaveRoom(RoomData data)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                using (var transaction = session.BeginTransaction())
                {
                    try
                    {
                        session.Update(data);
                        transaction.Commit();
                        session.Refresh(data);
                    }
                    catch
                    {
                        transaction.Rollback();
                    }
                }
            }
        }

        /// <summary>
        /// New room data
        /// </summary>
        public static void NewRoom(RoomData data)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                using (var transaction = session.BeginTransaction())
                {
                    try
                    {
                        session.Save(data);
                        transaction.Commit();
                    }
                    catch
                    {
                        transaction.Rollback();
                    }
                }
            }
        }

        /// <summary>
        /// Reset all visitors
        /// </summary>
        public static void ResetVisitorCounts()
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                session.Query<RoomData>().Where(x => x.UsersNow > 0 || x.UsersNow < 0).Update(x => new RoomData { UsersNow = 0 });
            }
        }

        /// <summary>
        /// Update users count
        /// </summary>
        public static void SetVisitorCount(int roomId, int visitorsNow)
        {
            using (var session = SessionFactoryBuilder.Instance.SessionFactory.OpenSession())
            {
                session.Query<RoomData>().Where(x => x.Id == roomId).Update(x => new RoomData { UsersNow = visitorsNow });
            }
        }
    }
}
