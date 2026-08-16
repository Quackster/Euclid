package euclid.storage.database.access;

import euclid.storage.database.SessionFactoryBuilder;
import euclid.storage.database.data.ItemData;
import euclid.storage.database.data.ItemDefinitionData;
import org.hibernate.Session;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.util.List;

public class ItemDao {

    public static List<ItemDefinitionData> getDefinitions() {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<ItemDefinitionData> q = session.createQuery("FROM ItemDefinitionData", ItemDefinitionData.class);
            return q.getResultList();
        }
    }

    public static List<ItemData> getUserItems(int userId) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<ItemData> q = session.createQuery(
                    "FROM ItemData i WHERE i.ownerId = :userId AND i.roomId = 0", ItemData.class);
            q.setParameter("userId", userId);
            return q.getResultList();
        }
    }

    public static List<ItemData> getRoomItems(int roomId) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<ItemData> q = session.createQuery(
                    "FROM ItemData i WHERE i.roomId = :roomId", ItemData.class);
            q.setParameter("roomId", roomId);
            return q.getResultList();
        }
    }

    public static ItemData getItem(int itemId) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            return session.find(ItemData.class, itemId);
        }
    }

    public static void saveDefinition(ItemDefinitionData itemDefinition) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.update(itemDefinition);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }

    public static void saveItem(ItemData itemData) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.update(itemData);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }

    public static void createItems(List<ItemData> items) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            try {
                for (ItemData item : items) {
                    session.persist(item);
                }
                transaction.commit();
                for (ItemData item : items) {
                    session.refresh(item);
                }
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }

    public static void createItem(ItemData item) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.persist(item);
                transaction.commit();
                session.refresh(item);
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }

    public static void deleteItem(ItemData item) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            try {
                MutationQuery query = session.createMutationQuery(
                        "DELETE FROM ItemData i WHERE i.id = :id");
                query.setParameter("id", item.getId());
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
