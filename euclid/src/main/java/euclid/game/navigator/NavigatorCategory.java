package euclid.game.navigator;

import euclid.game.room.Room;
import euclid.game.room.RoomManager;
import euclid.storage.database.data.NavigatorCategoryData;

public class NavigatorCategory {

    private final NavigatorCategoryData categoryData;

    public NavigatorCategory(NavigatorCategoryData categoryData) {
        this.categoryData = categoryData;
    }

    public NavigatorCategoryData getData() {
        return categoryData;
    }

    public int getCurrentVisitors() {
        int currentVisitors = 0;

        for (Room room : RoomManager.getInstance().getRooms().values()) {
            if (room.getData().getCategoryId() == this.categoryData.getId()) {
                currentVisitors += room.getData().getUsersNow();
            }
        }

        return currentVisitors;
    }

    public int getMaxVisitors() {
        int maxVisitors = 0;

        for (Room room : RoomManager.getInstance().getRooms().values()) {
            if (room.getData().getCategoryId() == this.categoryData.getId()) {
                maxVisitors += room.getData().getUsersMax();
            }
        }

        return maxVisitors;
    }
}
