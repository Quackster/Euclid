package euclid.game.item;

import euclid.game.ILoadable;
import euclid.game.room.Room;
import euclid.game.room.RoomManager;
import euclid.storage.database.access.ItemDao;
import euclid.storage.database.data.ItemData;
import euclid.storage.database.data.ItemDefinitionData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager implements ILoadable {

    public static final ItemManager INSTANCE = new ItemManager();

    private Map<Integer, ItemDefinition> definitions;

    private ItemManager() {
    }

    public static ItemManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void load() {
        definitions = new HashMap<>();
        List<ItemDefinitionData> data = ItemDao.getDefinitions();
        for (ItemDefinitionData defData : data) {
            ItemDefinition def = new ItemDefinition(defData);
            definitions.put(defData.getId(), def);
        }
    }

    public Map<Integer, ItemDefinition> getDefinitions() {
        return definitions;
    }

    public ItemDefinition getDefinition(int definitionId) {
        return definitions.get(definitionId);
    }

    public Item resolveItem(int itemId, ItemData itemData) {
        if (itemData == null)
            itemData = ItemDao.getItem(itemId);

        if (itemData == null)
            return null;

        Room room = RoomManager.getInstance().getRoom(itemData.getRoomId());

        if (room == null) {
            return null;
        } else {
            if (room.getEntityManager().getEntities(Item.class).isEmpty())
                room.getItemManager().load();
            return room.getItemManager().getItem(itemData.getId());
        }
    }

    public Item resolveItem(int itemId) {
        return resolveItem(itemId, null);
    }
}
