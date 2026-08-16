package euclid.game.room;

import euclid.game.ILoadable;
import euclid.game.item.ActiveItem;
import euclid.game.item.Item;
import euclid.game.item.ItemDefinition;
import euclid.game.item.PassiveItem;
import euclid.storage.database.access.ItemDao;
import euclid.storage.database.data.ItemData;
import euclid.storage.database.data.ItemDefinitionData;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class RoomItemManager implements ILoadable {

    private final Room room;

    public RoomItemManager(Room room) {
        this.room = room;
    }

    @Override
    public void load() {
        if (room.getData().isPrivateRoom()) {
            List<ItemData> items = ItemDao.getRoomItems(room.getData().getId());
            for (ItemData itemData : items) {
                Item item = new ActiveItem(itemData);
                room.getEntities().putIfAbsent(item, itemData.getId());
            }
        }

        if (room.getData().isPublicRoom()) {
            Path furniturePath = Path.of("tools", "public_rooms", room.getModel().getData().getModel() + ".furniture");

            if (!FileSystems.isExisting(furniturePath))
                return;

            try {
                List<String> lines = java.nio.file.Files.readAllLines(furniturePath);
                for (String line : lines) {
                    if (line.length() < 6)
                        continue;

                    String[] passiveData = line.split(" ");
                    boolean isChair = passiveData[1].contains("chair");
                    boolean isVisible = !passiveData[1].contains("none");

                    ItemData itemData = new ItemData();
                    itemData.setCustomData(passiveData[0]);
                    itemData.setX(Integer.parseInt(passiveData[2]));
                    itemData.setY(Integer.parseInt(passiveData[3]));
                    itemData.setZ(Integer.parseInt(passiveData[4]));

                    PassiveItem passiveItem = new PassiveItem(itemData);

                    ItemDefinitionData defData = new ItemDefinitionData();
                    defData.setSprite(passiveData[1]);
                    defData.setLength(1);
                    defData.setWidth(1);
                    defData.setChair(isChair);
                    defData.setFloorItem(true);
                    defData.setWalkable(isChair);
                    defData.setHeight(isChair ? 1 : 0);
                    defData.setVisible(isVisible);

                    passiveItem.setDefinition(new ItemDefinition(defData));

                    if (passiveData.length > 6) {
                        passiveItem.getDefinition().getData().setLength(Integer.parseInt(passiveData[5]));
                        passiveItem.getDefinition().getData().setWidth(Integer.parseInt(passiveData[6]));
                    } else {
                        passiveItem.getPosition().setRotation(Integer.parseInt(passiveData[5]));
                    }

                    room.getEntities().putIfAbsent(passiveItem, 0);
                }
            } catch (Exception e) {
                // file not found or parse error
            }
        }
    }

    private static class FileSystems {
        static boolean isExisting(Path path) {
            return java.nio.file.Files.exists(path);
        }
    }

    public Item getItem(int itemId) {
        for (Item item : room.getEntityManager().getEntities(Item.class)) {
            if (item.getData().getId() == itemId)
                return item;
        }
        return null;
    }

    public void addItem(Item item) {
        room.getEntities().putIfAbsent(item, item.getData().getId());
    }

    public void removeItem(Item item) {
        room.getEntities().remove(item);
    }
}
