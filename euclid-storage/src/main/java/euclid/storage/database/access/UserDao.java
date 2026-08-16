package euclid.storage.database.access;

import euclid.storage.database.SessionFactoryBuilder;
import euclid.storage.database.data.PlayerData;
import org.hibernate.Session;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.time.LocalDateTime;

public class UserDao {

    public static PlayerData login(String username, String password) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<PlayerData> query = session.createQuery(
                    "FROM PlayerData p WHERE p.name = :name AND p.password = :password", PlayerData.class);
            query.setParameter("name", username);
            query.setParameter("password", password);
            query.setMaxResults(1);
            var results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        }
    }

    public static void saveOrUpdate(PlayerData playerData) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(playerData);
                transaction.commit();
                session.refresh(playerData);
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }

    public static PlayerData getByName(String name) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<PlayerData> query = session.createQuery(
                    "FROM PlayerData p WHERE p.name = :name", PlayerData.class);
            query.setParameter("name", name);
            var results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        }
    }

    public static PlayerData getById(int id) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            return session.find(PlayerData.class, id);
        }
    }

    public static String getNameById(int id) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<String> query = session.createQuery(
                    "SELECT p.name FROM PlayerData p WHERE p.id = :id", String.class);
            query.setParameter("id", id);
            var results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        }
    }

    public static int getIdByName(String name) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<Integer> query = session.createQuery(
                    "SELECT p.id FROM PlayerData p WHERE p.name = :name", Integer.class);
            query.setParameter("name", name);
            var results = query.getResultList();
            return results.isEmpty() ? 0 : results.get(0);
        }
    }

    public static void saveLastOnline(PlayerData playerData) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            try {
                MutationQuery query = session.createMutationQuery(
                        "UPDATE PlayerData p SET p.lastOnline = :lastOnline WHERE p.id = :id");
                query.setParameter("lastOnline", playerData.getLastOnline());
                query.setParameter("id", playerData.getId());
                query.executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }
}
