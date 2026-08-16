package euclid.game.navigator;

import euclid.game.ILoadable;
import euclid.storage.database.access.NavigatorDao;
import euclid.storage.database.data.NavigatorCategoryData;

import java.util.ArrayList;
import java.util.List;

public class NavigatorManager implements ILoadable {

    public static final NavigatorManager INSTANCE = new NavigatorManager();

    private List<NavigatorCategory> categories;

    private NavigatorManager() {
    }

    public static NavigatorManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void load() {
        categories = new ArrayList<>();
        List<NavigatorCategoryData> categoryDataList = NavigatorDao.getCategories();
        for (NavigatorCategoryData data : categoryDataList) {
            categories.add(new NavigatorCategory(data));
        }
    }

    public List<NavigatorCategory> getCategories() {
        return categories;
    }

    public List<NavigatorCategory> getCategories(int rank) {
        List<NavigatorCategory> result = new ArrayList<>();
        for (NavigatorCategory category : categories) {
            if (rank >= category.getData().getVisibleRank()) {
                result.add(category);
            }
        }
        return result;
    }
}
