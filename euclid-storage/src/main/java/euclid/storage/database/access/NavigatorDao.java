package euclid.storage.database.access;

import euclid.storage.database.SessionFactoryBuilder;
import euclid.storage.database.data.NavigatorCategoryData;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class NavigatorDao {

    public static List<NavigatorCategoryData> getCategories() {
        try (Session session = SessionFactoryBuilder.getInstance().getSessionFactory().openSession()) {
            Query<NavigatorCategoryData> q = session.createQuery("FROM NavigatorCategoryData", NavigatorCategoryData.class);
            return q.getResultList();
        }
    }
}
