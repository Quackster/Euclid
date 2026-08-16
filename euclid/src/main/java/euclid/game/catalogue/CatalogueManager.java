package euclid.game.catalogue;

import euclid.game.ILoadable;
import euclid.storage.database.access.CatalogueDao;
import euclid.storage.database.data.CatalogueData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CatalogueManager implements ILoadable {

    public static final CatalogueManager INSTANCE = new CatalogueManager();

    private List<CatalogueItem> offers;
    private ConcurrentHashMap<Integer, CatalogueOrder> orderHistory;

    private CatalogueManager() {
    }

    public static CatalogueManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void load() {
        offers = new ArrayList<>();
        List<CatalogueData> catalogueOffers = CatalogueDao.getCatalogueOffers();
        for (CatalogueData data : catalogueOffers) {
            offers.add(new CatalogueItem(data));
        }
        orderHistory = new ConcurrentHashMap<>();
    }

    public List<CatalogueItem> getOffers() {
        return offers;
    }

    public ConcurrentHashMap<Integer, CatalogueOrder> getOrderHistory() {
        return orderHistory;
    }
}
