package euclid.storage.database.access;

import euclid.storage.database.SessionFactoryBuilder;
import euclid.storage.database.data.CatalogueData;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CatalogueDao {

    public static List<CatalogueData> getCatalogueOffers() {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<CatalogueData> q = session.createQuery("FROM CatalogueData", CatalogueData.class);
            return q.getResultList();
        }
    }
}
