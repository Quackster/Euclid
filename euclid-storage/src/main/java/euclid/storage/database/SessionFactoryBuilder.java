package euclid.storage.database;

import euclid.storage.database.data.CatalogueData;
import euclid.storage.database.data.ItemData;
import euclid.storage.database.data.ItemDefinitionData;
import euclid.storage.database.data.NavigatorCategoryData;
import euclid.storage.database.data.PlayerData;
import euclid.storage.database.data.RoomData;
import euclid.storage.database.data.RoomModelData;
import euclid.storage.database.data.SettingsData;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

public class SessionFactoryBuilder {

    private static final SessionFactoryBuilder INSTANCE = new SessionFactoryBuilder();

    private SessionFactory sessionFactory;

    public static SessionFactoryBuilder getInstance() {
        return INSTANCE;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void initialiseSessionFactory(String connectionString) {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory(connectionString);
        }
    }

    private SessionFactory buildSessionFactory(String connectionString) {
        Configuration config = new Configuration();
        config.setProperty(AvailableSettings.JAKARTA_JDBC_URL, connectionString);
        config.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQLDialect");
        config.setProperty(AvailableSettings.SHOW_SQL, "false");
        config.addAnnotatedClass(PlayerData.class);
        config.addAnnotatedClass(RoomData.class);
        config.addAnnotatedClass(RoomModelData.class);
        config.addAnnotatedClass(ItemData.class);
        config.addAnnotatedClass(ItemDefinitionData.class);
        config.addAnnotatedClass(CatalogueData.class);
        config.addAnnotatedClass(NavigatorCategoryData.class);
        config.addAnnotatedClass(SettingsData.class);
        return config.buildSessionFactory();
    }
}
