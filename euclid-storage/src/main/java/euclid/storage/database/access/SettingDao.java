package euclid.storage.database.access;

import euclid.storage.database.SessionFactoryBuilder;
import euclid.storage.database.data.SettingsData;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SettingDao {

    public static Map<String, String> getSettings() {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<SettingsData> q = session.createQuery("FROM SettingsData", SettingsData.class);
            List<SettingsData> list = q.getResultList();
            Map<String, String> settings = new LinkedHashMap<>();
            for (SettingsData s : list) {
                settings.put(s.getKey(), s.getValue());
            }
            return settings;
        }
    }

    public static boolean hasSetting(String key) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<Long> q = session.createQuery(
                    "SELECT COUNT(s) FROM SettingsData s WHERE s.key = :key", Long.class);
            q.setParameter("key", key);
            return q.getSingleResult() > 0;
        }
    }

    public static void saveSetting(String key, String value) {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.persist(new SettingsData(key, value));
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }
}
