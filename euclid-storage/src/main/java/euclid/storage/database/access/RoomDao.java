package euclid.storage.database.access;

import euclid.storage.database.SessionFactoryBuilder;
import euclid.storage.database.data.PlayerData;
import euclid.storage.database.data.RoomData;
import euclid.storage.database.data.RoomModelData;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.util.List;

public class RoomDao {

    public static List<RoomData> searchRooms(String query, int roomLimit) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<RoomData> q = session.createQuery(
                    "FROM RoomData r JOIN r.ownerData o WHERE o.id = r.ownerId " +
                    "AND (LOWER(r.name) LIKE :query OR LOWER(o.name) LIKE :query) " +
                    "ORDER BY r.usersNow DESC, r.rating DESC", RoomData.class);
            q.setParameter("query", "%" + query + "%");
            q.setMaxResults(roomLimit);
            return q.getResultList();
        }
    }

    public static void deleteRoom(int roomId) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            try {
                MutationQuery query = session.createMutationQuery(
                        "DELETE FROM RoomData r WHERE r.id = :id");
                query.setParameter("id", roomId);
                query.executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }

    public static List<RoomData> getPopularFlats(int page, int resultsLimit) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<RoomData> q = session.createQuery(
                    "FROM RoomData r LEFT JOIN FETCH r.ownerData o WHERE o.id = r.ownerId " +
                    "ORDER BY r.usersNow DESC", RoomData.class);
            q.setFirstResult(page * resultsLimit);
            q.setMaxResults(resultsLimit);
            return q.getResultList();
        }
    }

    public static List<RoomData> getPublicFlats(int categoryId) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<RoomData> q = session.createQuery(
                    "FROM RoomData r WHERE r.ownerId = 0 AND r.categoryId = :categoryId " +
                    "ORDER BY r.usersNow DESC, r.rating DESC", RoomData.class);
            q.setParameter("categoryId", categoryId);
            return q.getResultList();
        }
    }

    public static List<RoomData> getPublicFlats() {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<RoomData> q = session.createQuery(
                    "FROM RoomData r WHERE r.ownerId = 0 " +
                    "ORDER BY r.usersNow DESC, r.rating DESC", RoomData.class);
            return q.getResultList();
        }
    }

    public static List<RoomData> getUserRooms(int userId) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<RoomData> q = session.createQuery(
                    "FROM RoomData r WHERE r.ownerId = :userId " +
                    "ORDER BY r.usersNow DESC, r.rating DESC", RoomData.class);
            q.setParameter("userId", userId);
            return q.getResultList();
        }
    }

    public static int countUserRooms(int userId) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<Long> q = session.createQuery(
                    "SELECT COUNT(r) FROM RoomData r WHERE r.ownerId = :userId", Long.class);
            q.setParameter("userId", userId);
            return q.getSingleResult().intValue();
        }
    }

    public static List<RoomModelData> getModels() {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<RoomModelData> q = session.createQuery("FROM RoomModelData", RoomModelData.class);
            return q.getResultList();
        }
    }

    public static RoomData getRoomData(int roomId) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            return session.find(RoomData.class, roomId);
        }
    }

    public static void saveRoom(RoomData data) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.update(data);
                transaction.commit();
                session.refresh(data);
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }

    public static void newRoom(RoomData data) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.persist(data);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }

    public static void resetVisitorCounts() {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                MutationQuery query = session.createMutationQuery(
                        "UPDATE RoomData r SET r.usersNow = 0 WHERE r.usersNow > 0 OR r.usersNow < 0");
                query.executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    public static void setVisitorCount(int roomId, int visitorsNow) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                MutationQuery query = session.createMutationQuery(
                        "UPDATE RoomData r SET r.usersNow = :count WHERE r.id = :id");
                query.setParameter("count", visitorsNow);
                query.setParameter("id", roomId);
                query.executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }
}
